package com.example.scrollviews

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by chavi on 1/27/17.
 */
class MenuFragment: Fragment() {
    var scrollTestActivity: ScrollTestActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById(R.id.vertical_scroll).setOnClickListener {
            scrollTestActivity?.showFragmentWithLayout(R.layout.fragment_vertical_scroll)
        }

        view.findViewById(R.id.horizontal_scroll).setOnClickListener {
            scrollTestActivity?.showFragmentWithLayout(R.layout.fragment_horizontal_scroll)
        }

        view.findViewById(R.id.vertical_scroll_with_color).setOnClickListener {
            scrollTestActivity?.showFragmentWithLayout(R.layout.fragment_vertical_scroll_with_color)
        }

        view.findViewById(R.id.vertical_scroll_with_drawable).setOnClickListener {
            scrollTestActivity?.showFragmentWithLayout(R.layout.fragment_vertical_scroll_with_bg)
        }

        view.findViewById(R.id.vertical_scroll_with_index).setOnClickListener {
            scrollTestActivity?.showFragmentWithLayout(R.layout.fragment_vertical_scroll_index)
        }
    }

    override fun onDetach() {
        super.onDetach()

        scrollTestActivity = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        scrollTestActivity = activity as ScrollTestActivity
    }

}