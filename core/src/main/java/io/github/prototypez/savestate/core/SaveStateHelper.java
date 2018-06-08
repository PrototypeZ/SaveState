package io.github.prototypez.savestate.core;


import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SaveStateHelper {

    public static <T> Type gsonType() {
        return new TypeToken<T>(){}.getType();
    }
}
