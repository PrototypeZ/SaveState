package io.github.prototypez.savestate.plain.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.prototypez.savestate.plain.JavaFragment;
import io.github.prototypez.savestate.plain.R;


public class JavaFragmentActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_java);
        if (savedInstanceState == null) {
            JavaFragment fragment = new JavaFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, JavaFragment.class.getCanonicalName())
                    .show(fragment)
                    .commit();
        }
    }
}
