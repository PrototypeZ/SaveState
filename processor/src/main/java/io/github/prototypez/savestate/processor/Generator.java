package io.github.prototypez.savestate.processor;

import com.squareup.javapoet.JavaFile;

public interface Generator {
    JavaFile createSaveStateSourceFile();
}
