package com.android.academy.fundamentals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.academy.fundamentals.workshop_2.WS02FirstCoroutineTaskFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(R.id.fragment_container_view, WS02FirstCoroutineTaskFragment())
                .commit()
        }
    }
}