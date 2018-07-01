package io.github.prototypez.savestate

import io.github.prototypez.savestate.core.annotation.AutoRestore
import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.CtNewMethod
import javassist.Modifier
import org.gradle.api.Project

class FragmentSaveStateTransform {

    CtClass ctClass
    ClassPool classPool
    Project project

    FragmentSaveStateTransform(CtClass ctClass, ClassPool classPool, Project project) {
        this.ctClass = ctClass
        this.classPool = classPool
        this.project = project
    }

    void handleFragmentSaveState() {

        CtClass bundleCtClass = classPool.get("android.os.Bundle")


        CtMethod saveCtMethod = ctClass.declaredMethods.find
                { it.name == "onSaveInstanceState" && it.parameterTypes == [bundleCtClass] as CtClass[] }
        CtMethod restoreCtMethod = ctClass.declaredMethods.find
                { it.name == "onActivityCreated" && it.parameterTypes == [bundleCtClass] as CtClass[] }

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
                    // 原来的 Fragment 没有 saveInstanceState 方法
                    saveCtMethod = CtNewMethod.make(generateFragmentSaveMethod(
                            ctClass.name + Constant.GENERATED_FILE_SUFFIX), ctClass)
                    ctClass.addMethod(saveCtMethod)
                } else {
                    // 原来的 Fragment 有 saveInstanceState 方法
                    saveCtMethod.insertBefore(
                            "${ctClass.name}${Constant.GENERATED_FILE_SUFFIX}.onSaveInstanceState(this, \$1);")
                }

                if (restoreCtMethod == null) {
                    // 原来的 Fragment 没有 onActivityCreated 方法
                    restoreCtMethod = CtNewMethod.make(generateFragmentRestoreMethod(
                            ctClass.name + Constant.GENERATED_FILE_SUFFIX), ctClass)
                    ctClass.addMethod(restoreCtMethod)
                } else {
                    // 原来的 Fragment 有 onActivityCreated 方法
                    restoreCtMethod.insertBefore(
                            "if ($Constant.ENABLE_SAVE_STATE && \$1 != null) ${ctClass.name}${Constant.GENERATED_FILE_SUFFIX}.onRestoreInstanceState(this, \$1);")
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


    // Fragment onActivityCreated 不存在的情况下创建 onActivityCreated 方法
    String generateFragmentRestoreMethod(String delegatedName) {
        return "public void onActivityCreated(Bundle savedInstanceState) {\n" +
                "if ($Constant.ENABLE_SAVE_STATE && savedInstanceState != null) ${delegatedName}.onRestoreInstanceState(this, savedInstanceState);" + "\n" +
                "super.onActivityCreated(savedInstanceState);\n" +
                "}"
    }

    // Fragment onSaveInstanceState 不存在的情况下创建 onSaveInstanceState
    String generateFragmentSaveMethod(String delegatedName) {
        return "public void onSaveInstanceState(Bundle outState) {\n" +
                "${delegatedName}.onSaveInstanceState(this, outState);" + "\n" +
                "super.onSaveInstanceState(outState);\n" +
                "}"
    }
}
