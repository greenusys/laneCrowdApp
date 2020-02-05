package com.example.lanecrowd.Home_Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Friend_Req_Modal
import com.example.lancrowd.activity.modal.Recent_Chat_Modal

import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Friend_Request_Adapter
import com.example.lanecrowd.adapter.Recent_Chat_Adapter


class Friend_Request_Fragment : Fragment() {


    var friend_req_rv: RecyclerView? = null

    var friend_req_Adapter: Friend_Request_Adapter? = null

    var friend_req_list = ArrayList<Friend_Req_Modal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friendrequest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {


        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())
        friend_req_list.add(Friend_Req_Modal())





        friend_req_rv = view.findViewById(R.id.friend_req_rv)



        friend_req_Adapter = Friend_Request_Adapter(friend_req_list, view.context)
        friend_req_rv!!.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
        friend_req_rv!!.adapter = friend_req_Adapter
        friend_req_Adapter!!.notifyDataSetChanged()



    }
}
