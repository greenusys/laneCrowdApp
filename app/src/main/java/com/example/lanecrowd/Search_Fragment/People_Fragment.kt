package com.example.lanecrowd.Home_Fragment

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Search_Modal
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Home_Post_Adapter
import com.example.lanecrowd.adapter.Search_Adapter
import com.example.lanecrowd.adapter.Story_Adapter


class People_Fragment : Fragment() {



    var people_rv: RecyclerView? = null

    var searchAdapter: Search_Adapter? = null

    var search_post_list = ArrayList<Search_Modal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false)
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





        people_rv = view.findViewById(R.id.people_rv) as RecyclerView



        searchAdapter = Search_Adapter(search_post_list, view.context)
        people_rv!!.setLayoutManager(GridLayoutManager(context, 3))

        people_rv!!.adapter = searchAdapter
        searchAdapter!!.notifyDataSetChanged()


    }



}
