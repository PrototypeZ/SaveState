package io.github.prototypez.savestate

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.google.common.collect.Sets
import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.Project
import org.apache.commons.io.FileUtils

class SaveStateTransform extends Transform {

    Project mProject

    SaveStateTransform(Project project) {
        mProject = project
    }

    @Override
    String getName() {
        return "saveState"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return Collections.singleton(QualifiedContent.DefaultContentType.CLASSES);
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
//        return new HashSet<QualifiedContent.Scope>(Arrays.asList(
//                QualifiedContent.Scope.PROJECT
//        ))


        return Sets.immutableEnumSet(
                QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.SUB_PROJECTS,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES)
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        classPool.appendClassPath(mProject.android.bootClasspath[0].toString())

        transformInvocation.inputs.each { input ->

            input.jarInputs.each { jarInput ->
//                mProject.logger.warn("jar input:" + jarInput.file.getAbsolutePath())
                classPool.appendClassPath(jarInput.file.absolutePath)

                def jarName = jarInput.name
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }

                def dest = transformInvocation.outputProvider.getContentLocation(jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
//                mProject.logger.warn("jar output path:" + dest.getAbsolutePath())
                FileUtils.copyFile(jarInput.file, dest)
            }

            input.directoryInputs.each { dirInput ->
                def outDir = transformInvocation.outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                classPool.appendClassPath(dirInput.file.absolutePath)
                // dirInput.file is like "build/intermediates/classes/debug"
                int pathBitLen = dirInput.file.toString().length()

                def callback = { File it ->
                    def path = "${it.toString().substring(pathBitLen)}"
                    if (it.isDirectory()) {
                        new File(outDir, path).mkdirs()
                    } else {
                        boolean handled = checkAndTransformClass(it, outDir)
                        if (!handled) {
                            // copy the file to output location
                            new File(outDir, path).bytes = it.bytes
                        }
                    }
                }

                if (dirInput.changedFiles == null || dirInput.changedFiles.isEmpty()) {
                    dirInput.file.traverse(callback)
                } else {
                    dirInput.changedFiles.keySet().each(callback)
                }
            }
        }
    }

    ClassPool classPool = ClassPool.getDefault()


    boolean checkAndTransformClass(File file, File dest) {

        CtClass fragmentActivityCtClass = classPool.get("android.support.v4.app.FragmentActivity")
        CtClass activityCtClass = classPool.get("android.app.Activity")
        CtClass v4FragmentCtClass = classPool.get("android.support.v4.app.Fragment")
        CtClass fragmentCtClass = classPool.get("android.app.Fragment")
        CtClass viewCtClass = classPool.get("android.view.View")

        classPool.importPackage("android.os")
        classPool.importPackage("android.util")

        CtClass ctClass = classPool.makeClass(new FileInputStream(file))
        // Support Activity and AppCompatActivity
        boolean handled
        if (ctClass.subclassOf(activityCtClass) || ctClass.subclassOf(fragmentActivityCtClass)) {
//            mProject.logger.warn("save-state checking activity class:" + ctClass.getName())
            ActivitySaveStateTransform transform = new ActivitySaveStateTransform(mProject, ctClass, classPool)
            transform.handleActivitySaveState()
            handled = true
        } else if (ctClass.subclassOf(fragmentCtClass) || ctClass.subclassOf(v4FragmentCtClass)) {
//            mProject.logger.warn("save-state checking fragment class:" + ctClass.getName())
            FragmentSaveStateTransform transform = new FragmentSaveStateTransform(ctClass, classPool, mProject)
            transform.handleFragmentSaveState()
            handled = true
        } else if (ctClass.subclassOf(viewCtClass)) {
//            mProject.logger.warn("save-state checking view class:" + ctClass.getName())
            ViewSaveStateTransform transform = new ViewSaveStateTransform(ctClass, classPool, mProject)
            transform.handleViewSaveState()
            handled = true
        }
        if (handled) {
            ctClass.writeFile(dest.absolutePath)
            ctClass.detach()
        }
        return handled
    }
}