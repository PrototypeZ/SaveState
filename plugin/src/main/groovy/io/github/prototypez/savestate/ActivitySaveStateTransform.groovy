package io.github.prototypez.savestate

import io.github.prototypez.savestate.core.annotation.AutoRestore
import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.CtNewMethod
import javassist.Modifier
import org.gradle.api.Project


class ActivitySaveStateTransform {



    CtClass ctClass
    ClassPool classPool
    Project project


    ActivitySaveStateTransform(Project project, CtClass ctClass, ClassPool classPool) {
        this.ctClass = ctClass
        this.classPool = classPool
        this.project = project
    }

    void handleActivitySaveState() {

        CtClass bundleCtClass = classPool.get("android.os.Bundle")


        CtMethod saveCtMethod = ctClass.declaredMethods.find
                { it.name == "onSaveInstanceState" && it.parameterTypes == [bundleCtClass] as CtClass[] }
        CtMethod restoreCtMethod = ctClass.declaredMethods.find
                { it.name == "onCreate" && it.parameterTypes == [bundleCtClass] as CtClass[] }

        CtField enableCtField = ctClass.declaredFields.find
                { it.name == Constant.ENABLE_SAVE_STATE && it.getType().name == "boolean" }

        def list = []
        ctClass.declaredFields.each { field ->
            if (field.getAnnotation(AutoRestore.class) != null) {
//                project.logger.info("field ${field.name} is AutoRestore annotated!")
                list.add(field)
            }
        }
        if (list.size() == 0) {
            if (enableCtField != null) {
                // 原来有需要自动恢复的变量，现在没有了
                ctClass.removeField(enableCtField)
                ctClass.addField(generateEnabledField(ctClass),
                        CtField.Initializer.constant(false))
//                project.logger.info("Last state: some, current state: none.")
            }
        } else {
            if (enableCtField == null) {
                // 原来没有需要自动恢复的变量，现在出现了需要自动恢复的变量

                ctClass.addField(generateEnabledField(ctClass),
                        CtField.Initializer.constant(true))


                if (saveCtMethod == null) {
                    // 原来的 Activity 没有 saveInstanceState 方法
                    saveCtMethod = CtNewMethod.make(generateActivitySaveMethod(
                            ctClass.name + Constant.GENERATED_FILE_SUFFIX), ctClass)
                    ctClass.addMethod(saveCtMethod)
                } else {
                    // 原来的 Activity 有 saveInstanceState 方法
                    saveCtMethod.insertBefore(
                            "${ctClass.name}${Constant.GENERATED_FILE_SUFFIX}.onSaveInstanceState(this, \$1);")
                }

                if (restoreCtMethod == null) {
                    // 原来的 Activity 没有 onCreate 方法
                    restoreCtMethod = CtNewMethod.make(generateActivityRestoreMethod(
                            ctClass.name + Constant.GENERATED_FILE_SUFFIX), ctClass)
                    ctClass.addMethod(restoreCtMethod)
                } else {
                    // 原来的 Activity 有 onCreate 方法
                    restoreCtMethod.insertBefore(
                            "if ($Constant.ENABLE_SAVE_STATE && \$1 != null) ${ctClass.name}${Constant.GENERATED_FILE_SUFFIX}.onRestoreInstanceState(this, \$1);")
                }
//                project.logger.info("Last state: none, current state: some")
            }
        }
    }

    CtField generateEnabledField(CtClass ctClass) {
        CtField ctField = new CtField(
                classPool.get("boolean"), Constant.ENABLE_SAVE_STATE, ctClass)
        ctField.setModifiers(Modifier.PRIVATE | Modifier.STATIC)
        return ctField
    }

    // Activity onCreate 不存在的情况下创建 onCreate 方法
    String generateActivityRestoreMethod(String delegatedName) {
        return "protected void onCreate(Bundle savedInstanceState) {\n" +
                "if ($Constant.ENABLE_SAVE_STATE && savedInstanceState != null) ${delegatedName}.onRestoreInstanceState(this, savedInstanceState);" + "\n" +
                "super.onCreate(savedInstanceState);\n" +
                "}"
    }

    // Activity onSaveInstanceState 不存在的情况下创建 onSaveInstanceState
    String generateActivitySaveMethod(String delegatedName) {
        return "protected void onSaveInstanceState(Bundle outState) {\n" +
                "${delegatedName}.onSaveInstanceState(this, outState);" + "\n" +
                "super.onSaveInstanceState(outState);\n" +
                "}"
    }
}
