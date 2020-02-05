package com.example.lancrowd.activity.home_fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Search_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Search_Adapter


class Tag_Fragment : Fragment() {

    var tag_rv: RecyclerView? = null

    var searchAdapter: Search_Adapter? = null

    var search_post_list = ArrayList<Search_Modal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view: View) {


        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())
        search_post_list.add(Search_Modal())





        tag_rv = view.findViewById(R.id.tag_rv) as RecyclerView



        searchAdapter = Search_Adapter(search_post_list, view.context)
        tag_rv!!.layoutManager = GridLayoutManager(context, 3)

        tag_rv!!.adapter = searchAdapter
        searchAdapter!!.notifyDataSetChanged()


    }



}
