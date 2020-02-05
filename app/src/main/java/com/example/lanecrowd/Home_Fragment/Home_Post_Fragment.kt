package com.example.lanecrowd.Home_Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Home_Post_Adapter
import com.example.lanecrowd.adapter.Story_Adapter


class Home_Post_Fragment : Fragment(), SearchView.OnQueryTextListener {



      var home_post_rv: RecyclerView?=null
      var story_rv: RecyclerView?=null

      var homePostAdapter:Home_Post_Adapter?=null
      var storyAdapter:Story_Adapter?=null

      var home_post_list= ArrayList<Home_Post_Modal>()
      var story_list= ArrayList<Story_Modal>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home__post_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

    }

    @SuppressLint("WrongConstant")
    private fun initViews(view: View) {


        home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())
        home_post_list.add(Home_Post_Modal())


        story_list.add(Story_Modal())
        story_list.add(Story_Modal())
        story_list.add(Story_Modal())
        story_list.add(Story_Modal())
        story_list.add(Story_Modal())
        story_list.add(Story_Modal())


        home_post_rv = view.findViewById(R.id.home_post_rv) as RecyclerView
        story_rv = view.findViewById(R.id.story_rv) as RecyclerView



        homePostAdapter = Home_Post_Adapter(home_post_list, view.context)
        home_post_rv!!.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
        home_post_rv!!.adapter = homePostAdapter
        homePostAdapter!!.notifyDataSetChanged()


        storyAdapter = Story_Adapter(story_list, view.context)
        story_rv!!.layoutManager = LinearLayoutManager(view.context, LinearLayout.HORIZONTAL, false)
        story_rv!!.adapter = storyAdapter
        storyAdapter!!.notifyDataSetChanged()



    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(this)

    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}