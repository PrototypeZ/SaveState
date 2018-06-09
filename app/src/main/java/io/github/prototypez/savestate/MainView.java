package io.github.prototypez.savestate;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Map;

import io.github.prototypez.savestate.core.annotation.AutoRestore;

public class MainView extends View {

    @AutoRestore
    int count;

    @AutoRestore
    List<Map<String, Object>> response;


    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Nullable
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState =  super.onSaveInstanceState();
//        return MainViewState.onSaveInstanceState(this, superState);
//    }

//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        state = MainViewState.onRestoreInstanceState(this, state);
//        super.onRestoreInstanceState(state);
//    }
}
