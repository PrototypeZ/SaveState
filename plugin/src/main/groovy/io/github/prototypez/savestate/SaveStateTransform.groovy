package io.github.prototypez.savestate

import com.android.build.api.transform.*
import com.google.common.collect.Sets
import groovy.io.FileType
import io.github.prototypez.savestate.util.Compressor
import io.github.prototypez.savestate.util.Decompression
import javassist.ClassPath
import javassist.ClassPool
import javassist.CtClass
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.lang.reflect.Constructor

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
        if (mProject.plugins.hasPlugin("com.android.application")) {
            return Sets.immutableEnumSet(
                    QualifiedContent.Scope.PROJECT,
                    QualifiedContent.Scope.SUB_PROJECTS,
                    QualifiedContent.Scope.EXTERNAL_LIBRARIES)
        } else if (mProject.plugins.hasPlugin("com.android.library")) {
            return Sets.immutableEnumSet(
                    QualifiedContent.Scope.PROJECT)
        } else {
            return Collections.emptySet()
        }
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        ClassPool classPool = ClassPool.getDefault()

        def classPath = []

        classPool.appendClassPath(mProject.android.bootClasspath[0].toString())

        Class jarClassPathClazz = Class.forName("javassist.JarClassPath")
        Constructor constructor = jarClassPathClazz.getDeclaredConstructor(String.class)
        constructor.setAccessible(true)

        transformInvocation.inputs.each { input ->

            def subProjectInputs = []

            input.jarInputs.each { jarInput ->
//                mProject.logger.warn("jar input:" + jarInput.file.getAbsolutePath())
                ClassPath clazzPath = (ClassPath) constructor.newInstance(jarInput.file.absolutePath)
                classPath.add(clazzPath)
                classPool.appendClassPath(clazzPath)

                def jarName = jarInput.name
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
//                mProject.logger.warn("jar name:" + jarName)
                if (jarName.startsWith(":")) {
                    // handle it later, after classpath set
                    subProjectInputs.add(jarInput)
                } else {
                    def dest = transformInvocation.outputProvider.getContentLocation(jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
//                    mProject.logger.warn("jar output path:" + dest.getAbsolutePath())
                    FileUtils.copyFile(jarInput.file, dest)
                }
            }

            // Handle library project jar here
            subProjectInputs.each { jarInput ->

                def jarName = jarInput.name
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }

                if (jarName.startsWith(":")) {
                    // sub project
                    File unzipDir = new File(
                            jarInput.file.getParent(),
                            jarName.replace(":", "") + "_unzip")
                    if (unzipDir.exists()) {
                        unzipDir.delete()
                    }
                    unzipDir.mkdirs()
                    Decompression.uncompress(jarInput.file, unzipDir)

                    File repackageFolder = new File(
                            jarInput.file.getParent(),
                            jarName.replace(":", "") + "_repackage"
                    )

                    FileUtils.copyDirectory(unzipDir, repackageFolder)

                    unzipDir.eachFileRecurse(FileType.FILES) { File it ->
                        if (it.name.endsWith(".class")) {
                            checkAndTransformClass(classPool, it, repackageFolder)
                        }
                    }

                    // re-package the folder to jar
                    def dest = transformInvocation.outputProvider.getContentLocation(
                            jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)

                    Compressor zc = new Compressor(dest.getAbsolutePath())
                    zc.compress(repackageFolder.getAbsolutePath())
                }
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
                        boolean handled = checkAndTransformClass(classPool, it, outDir)
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

        // release File Handlers in ClassPool
        classPath.each { it ->
            classPool.removeClassPath(it)
        }
    }


    boolean checkAndTransformClass(ClassPool classPool, File file, File dest) {

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