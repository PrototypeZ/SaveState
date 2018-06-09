package io.github.prototypez.savestate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;
import java.util.Map;

import io.github.prototypez.savestate.core.annotation.AutoRestore;

public class MainFragment extends Fragment {

    @AutoRestore
    int count;

    @AutoRestore
    List<Map<String, Object>> response;


//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
}
