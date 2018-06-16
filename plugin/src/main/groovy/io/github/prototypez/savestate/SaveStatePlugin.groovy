package io.github.prototypez.savestate

import org.gradle.api.Plugin
import org.gradle.api.Project

class SaveStatePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.dependencies {
            implementation 'io.github.prototypez:save-state-core:0.1'
            annotationProcessor 'io.github.prototypez:save-state-processor:0.1.1'
//            implementation project(':core')
//            annotationProcessor project(':processor')
        }

        if (!project.android.getTransforms().any { it -> it instanceof SaveStateTransform }) {
            project.extensions.create('saveState', SaveState)
            project.android.registerTransform(new SaveStateTransform(project))
        }
    }
}
