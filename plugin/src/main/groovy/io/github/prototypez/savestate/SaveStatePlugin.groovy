package io.github.prototypez.savestate

import org.gradle.api.Plugin
import org.gradle.api.Project

class SaveStatePlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {

        project.extensions.create('saveState', SaveState)
        project.android.registerTransform(new SaveStateTransform(project))
    }
}
