package io.github.prototypez.savestate.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.util.SizeF;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import io.github.prototypez.savestate.R;
import io.github.prototypez.savestate.core.annotation.AutoRestore;
import io.github.prototypez.savestate.databinding.ActivityMainBinding;
import io.github.prototypez.savestate.entity.User;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.smartActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, SmartActivity.class);
            startActivity(intent);
        });

        mBinding.smartFragment.setOnClickListener(v -> {
            Intent intent = new Intent(this, SmartFragmentActivity.class);
            startActivity(intent);
        });

        mBinding.smartView.setOnClickListener(v -> {
            Intent intent = new Intent(this, SmartViewActivity.class);
            startActivity(intent);
        });
    }
}
