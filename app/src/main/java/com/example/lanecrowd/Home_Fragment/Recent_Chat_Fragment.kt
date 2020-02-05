package com.example.lancrowd.activity.home_fragments

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Recent_Chat_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Recent_Chat_Adapter




class Recent_Chat_Fragment : Fragment(), SearchView.OnQueryTextListener {

    var recent_chat_rv: RecyclerView? = null

    var recent_chat_adapter: Recent_Chat_Adapter? = null

    var recent_chat_list = ArrayList<Recent_Chat_Modal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {


        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())




        recent_chat_rv = view.findViewById(R.id.recent_chat_rv)



        recent_chat_adapter = Recent_Chat_Adapter(recent_chat_list, view.context)
        recent_chat_rv!!.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
        recent_chat_rv!!.adapter = recent_chat_adapter
        recent_chat_adapter!!.notifyDataSetChanged()


        println("Home_Fragment")

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
