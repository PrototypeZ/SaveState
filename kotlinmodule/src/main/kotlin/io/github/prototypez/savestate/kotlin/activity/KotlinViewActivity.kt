package io.github.prototypez.savestate.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import io.github.prototypez.savestate.kotlin.R


class KotlinViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_kotlin)
    }
}
