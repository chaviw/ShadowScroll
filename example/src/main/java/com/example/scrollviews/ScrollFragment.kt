package com.example.scrollviews

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by chavi on 1/27/17.
 */
class ScrollFragment() : Fragment() {
    companion object {
        const val LAYOUT_ID_EXTRA = "layoutId"

        fun createInstance(@LayoutRes layoutId: Int): ScrollFragment {
            val bundle = Bundle()
            bundle.putInt(LAYOUT_ID_EXTRA, layoutId)
            val fragment = ScrollFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val layoutId = arguments?.getInt(LAYOUT_ID_EXTRA) ?: R.layout.fragment_vertical_scroll
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById(R.id.backButton)?.setOnClickListener {
            activity?.onBackPressed()
        }

        view.findViewById(R.id.closeButton)?.setOnClickListener {
            activity?.finish()
        }
    }
}