package io.github.prototypez.savestate.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.github.prototypez.savestate.R
import io.github.prototypez.savestate.core.annotation.AutoRestore
import kotlinx.android.synthetic.main.activity_smart_kotlin.*;

class SmartKotlinActivity : AppCompatActivity() {

    @AutoRestore
    var a: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_kotlin)
        text.text = a.toString()

        assign_value.setOnClickListener {
            a = 100
            text.text = a.toString()
        }
    }
}
