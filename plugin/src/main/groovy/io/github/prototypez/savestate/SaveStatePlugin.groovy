package io.github.prototypez.savestate

import org.gradle.api.Plugin
import org.gradle.api.Project

class SaveStatePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        if (project.plugins.hasPlugin("com.android.application")
                || project.plugins.hasPlugin("com.android.library")) {

            if (project.plugins.hasPlugin("kotlin-android")) {
                project.dependencies {
                    implementation 'io.github.prototypez:save-state-core:0.1'
                    kapt 'io.github.prototypez:save-state-processor:0.2'
                }
            } else {
                project.dependencies {
                    implementation 'io.github.prototypez:save-state-core:0.1'
                    annotationProcessor 'io.github.prototypez:save-state-processor:0.2'
                }
            }

            if (project.plugins.hasPlugin("com.android.application")) {
                project.extensions.create('saveState', SaveState)
                project.android.registerTransform(new SaveStateTransform(project))
            }
        }
    }
}
