package io.github.prototypez.savestate

import io.github.prototypez.savestate.core.annotation.AutoRestore
import javassist.*
import org.gradle.api.Project

class ViewSaveStateTransform {

    CtClass ctClass
    ClassPool classPool
    Project project

    ViewSaveStateTransform(CtClass ctClass, ClassPool classPool, Project project) {
        this.ctClass = ctClass
        this.classPool = classPool
        this.project = project
    }

    void handleViewSaveState() {

        CtClass bundleCtClass = classPool.get("android.os.Bundle")
        CtClass parcelableCtClass = classPool.get("android.os.Parcelable")


        CtMethod saveCtMethod = ctClass.declaredMethods.find
                { it.name == "onSaveInstanceState" && it.parameterTypes.size() == 0}
        CtMethod restoreCtMethod = ctClass.declaredMethods.find
                { it.name == "onRestoreInstanceState" && it.parameterTypes == [parcelableCtClass] as CtClass[] }

        CtField enableCtField = ctClass.declaredFields.find
                { it.name == Constant.ENABLE_SAVE_STATE && it.getType().name == "boolean" }

        def list = []
        ctClass.declaredFields.each { field ->
            if (field.getAnnotation(AutoRestore.class) != null) {
                list.add(field)
            }
        }
        if (list.size() == 0) {
            if (enableCtField != null) {
                // 原来有需要自动恢复的变量，现在没有了
                ctClass.removeField(enableCtField)
                ctClass.addField(generateEnabledField(ctClass),
                        CtField.Initializer.constant(false))
            }
        } else {
            if (enableCtField == null) {
                // 原来没有需要自动恢复的变量，现在出现了需要自动恢复的变量

                ctClass.addField(generateEnabledField(ctClass),
                        CtField.Initializer.constant(true))


                if (saveCtMethod == null) {
                    // 原来的 View 没有 saveInstanceState 方法
                    saveCtMethod = CtNewMethod.make(generateViewSaveMethod(
                            ctClass.name + Constant.GENERATED_FILE_SUFFIX), ctClass)
                    ctClass.addMethod(saveCtMethod)
                }

                if (restoreCtMethod == null) {
                    // 原来的 View 没有 onRestoreInstanceState 方法
                    restoreCtMethod = CtNewMethod.make(generateViewRestoreMethod(
                            ctClass.name + Constant.GENERATED_FILE_SUFFIX), ctClass)
                    ctClass.addMethod(restoreCtMethod)
                }
            }
        }
    }


    CtField generateEnabledField(CtClass ctClass) {
        CtField ctField = new CtField(
                classPool.get("boolean"), Constant.ENABLE_SAVE_STATE, ctClass)
        ctField.setModifiers(Modifier.PRIVATE | Modifier.STATIC)
        return ctField
    }


    // View onRestoreInstanceState 不存在的情况下创建 onRestoreInstanceState 方法
    String generateViewRestoreMethod(String delegatedName) {
        return "protected void onRestoreInstanceState(Parcelable state) {\n" +
                "if ($Constant.ENABLE_SAVE_STATE) state = ${delegatedName}.onRestoreInstanceState(this, state);" + "\n" +
                "super.onRestoreInstanceState(state);\n" +
                "}"
    }

    // View onSaveInstanceState 不存在的情况下创建 onSaveInstanceState
    String generateViewSaveMethod(String delegatedName) {
        return "public Parcelable onSaveInstanceState() {\n" +
                "Parcelable superState = super.onSaveInstanceState();\n" +
                "return ${delegatedName}.onSaveInstanceState(this, superState);" + "\n" +
                "}"
    }
}
