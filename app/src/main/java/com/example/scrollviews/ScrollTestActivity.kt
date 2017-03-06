package com.example.scrollviews

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by chavi on 1/24/17.
 */

class ScrollTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_test)

        val currFrag = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currFrag == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MenuFragment())
                    .commitAllowingStateLoss()
        }
    }

    fun showFragmentWithLayout(@LayoutRes layoutId: Int) {
        showFragment(ScrollFragment.createInstance(layoutId))
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }
}
