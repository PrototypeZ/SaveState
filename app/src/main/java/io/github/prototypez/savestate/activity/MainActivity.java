package io.github.prototypez.savestate.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.prototypez.savestate.R;
import io.github.prototypez.savestate.core.annotation.AutoRestore;
import io.github.prototypez.savestate.databinding.ActivityMainBinding;
import io.github.prototypez.savestate.kotlin.activity.KotlinActivity;
import io.github.prototypez.savestate.kotlin.activity.KotlinFragmentActivity;
import io.github.prototypez.savestate.kotlin.activity.KotlinViewActivity;
import io.github.prototypez.savestate.plain.activity.JavaActivity;
import io.github.prototypez.savestate.plain.activity.JavaFragmentActivity;
import io.github.prototypez.savestate.plain.activity.JavaViewActivity;

public class MainActivity extends Activity {

    ActivityMainBinding mBinding;

    @AutoRestore
    String lastClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.javaActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, JavaActivity.class);
            startActivity(intent);
            this.lastClick = "javaActivity";
            showLastClick();
        });

        mBinding.javaFragment.setOnClickListener(v -> {
            Intent intent = new Intent(this, JavaFragmentActivity.class);
            startActivity(intent);
            this.lastClick = "javaFragment";
            showLastClick();
        });

        mBinding.javaView.setOnClickListener(v -> {
            Intent intent = new Intent(this, JavaViewActivity.class);
            startActivity(intent);
            this.lastClick = "javaView";
            showLastClick();
        });

        mBinding.kotlinActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, KotlinActivity.class);
            startActivity(intent);
            this.lastClick = "kotlinActivity";
            showLastClick();
        });

        mBinding.kotlinFragment.setOnClickListener(v -> {
            Intent intent = new Intent(this, KotlinFragmentActivity.class);
            startActivity(intent);
            this.lastClick = "kotlinFragment";
            showLastClick();
        });

        mBinding.kotlinView.setOnClickListener(v -> {
            Intent intent = new Intent(this, KotlinViewActivity.class);
            startActivity(intent);
            this.lastClick = "kotlinView";
            showLastClick();
        });

        mBinding.smartKotlinActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, SmartKotlinActivity.class);
            startActivity(intent);
            this.lastClick = "kotlinActivityInApp";
            showLastClick();
        });

        showLastClick();
    }


    private void showLastClick() {
        mBinding.history.setText(String.format("Last click: %s", this.lastClick));
    }
}
