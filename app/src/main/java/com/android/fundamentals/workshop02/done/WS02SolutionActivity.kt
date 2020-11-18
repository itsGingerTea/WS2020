package com.android.fundamentals.workshop02.done

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.fundamentals.R
import com.android.fundamentals.workshop02.WS02RootFragment
import com.android.fundamentals.workshop02.WS02SecondFragment

class WS02SolutionActivity : AppCompatActivity(), WS02RootFragment.TransactionsFragmentClicks {

    private val rootFragment =
        WS02RootFragment().apply { setClickListener(this@WS02SolutionActivity) }
    private var count: Int = 0
    private var addBackStack: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ws02_ws03)

        supportFragmentManager.beginTransaction()
            .apply {
                add(R.id.persistent_container, rootFragment)
                commit()
            }
    }

    override fun addToBackStack(value: Boolean) {
        addBackStack = value
    }

    override fun addRedFragment() {
        count++
        supportFragmentManager.beginTransaction()
            .apply {
                add(R.id.fragments_container, WS02SecondFragment.newInstance(count, R.color.red))
                if (addBackStack) addToBackStack(null)
                commit()
            }
    }

    override fun addBlueFragment() {
        count++
        supportFragmentManager.beginTransaction()
            .apply {
                add(R.id.fragments_container, WS02SecondFragment.newInstance(count, R.color.blue))
                if (addBackStack) addToBackStack(null)
                commit()
            }
    }

    override fun removeLast() {
        if (supportFragmentManager.fragments.size > 1) {
            val lastFragment = supportFragmentManager.fragments.last()
            supportFragmentManager.beginTransaction()
                .apply {
                    remove(lastFragment)
                    if (addBackStack) addToBackStack(null)
                    commit()
                }
        }
    }

    override fun replaceFragment() {
        count++
        supportFragmentManager.beginTransaction()
            .apply {
                replace(
                    R.id.fragments_container,
                    WS02SecondFragment.newInstance(count, R.color.green)
                )
                if (addBackStack) addToBackStack(null)
                commit()
            }
    }

}