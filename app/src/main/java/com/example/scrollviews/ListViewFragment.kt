package com.example.scrollviews

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

/**
 * Created by chavi on 5/5/17.
 */
class ListViewFragment: Fragment() {
    private val items = Array(50, {i -> i.toString()})

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_with_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById(R.id.backButton)?.setOnClickListener {
            activity?.onBackPressed()
        }

        view.findViewById(R.id.closeButton)?.setOnClickListener {
            activity?.finish()
        }

        val listView = view.findViewById(R.id.list_view) as ListView
        listView.adapter = ArrayAdapter<String> (context, android.R.layout.simple_list_item_1, items)
    }
}