package io.github.prototypez.savestate.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeHelper implements ParameterizedType{

    private Type rawType;
    private Type ownerType;
    private Type[] actualTypeArguments;

    public ParameterizedTypeHelper(Type rawType, Type ownerType, Type... actualTypeArguments) {
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
        this.ownerType = ownerType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }
}
