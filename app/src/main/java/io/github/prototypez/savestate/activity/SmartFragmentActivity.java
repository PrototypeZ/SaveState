package io.github.prototypez.savestate.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.prototypez.savestate.R;
import io.github.prototypez.savestate.SmartFragment;

public class SmartFragmentActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_smart);
        if (savedInstanceState == null) {
            SmartFragment fragment = new SmartFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, SmartFragment.class.getCanonicalName())
                    .show(fragment)
                    .commit();
        }
    }
}
