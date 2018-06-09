package io.github.prototypez.savestate.processor;

import com.squareup.javapoet.ClassName;

public class Constant {

    static final ClassName BUNDLE_CLASS = ClassName.get("android.os", "Bundle");
    static final ClassName PARCELABLE_CLASS = ClassName.get("android.os", "Parcelable");

    static final String KEY_SERIALIZER = "serializer";
    static final String SERIALIZER_GSON = "gson";
    static final String SERIALIZER_FASTJSON = "fastjson";

    static final String CLASS_ACTIVITY = "android.app.Activity";
    static final String CLASS_FRAGMENT_ACTIVITY = "android.support.v4.app.FragmentActivity";
    static final String CLASS_V4_FRAGMENT = "android.support.v4.app.Fragment";
    static final String CLASS_FRAGMENT = "android.app.Fragment";
    static final String CLASS_VIEW = "android.view.View";

    static final String SUPER_STATE = "io.github.prototypez.savastate.super_state";

}
