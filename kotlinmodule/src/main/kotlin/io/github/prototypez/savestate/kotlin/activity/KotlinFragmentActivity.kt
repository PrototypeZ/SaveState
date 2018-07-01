package io.github.prototypez.savestate.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import io.github.prototypez.savestate.kotlin.KotlinFragment
import io.github.prototypez.savestate.kotlin.R


class KotlinFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_kotlin)
        if (savedInstanceState == null) {
            val fragment = KotlinFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment, KotlinFragment::class.java.canonicalName)
                    .show(fragment)
                    .commit()
        }
    }
}
