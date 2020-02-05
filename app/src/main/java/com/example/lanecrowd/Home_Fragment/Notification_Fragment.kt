package com.example.lanecrowd.Home_Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Friend_Req_Modal
import com.example.lancrowd.activity.modal.Notification_Modal

import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Friend_Request_Adapter
import com.example.lanecrowd.adapter.Notification_Adapter


class Notification_Fragment : Fragment() {


    var notification_rv: RecyclerView? = null

    var notification_Adapter: Notification_Adapter? = null

    var notification_list = ArrayList<Notification_Modal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {


        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
        notification_list.add(Notification_Modal())
     





        notification_rv = view.findViewById(R.id.notification_rv)



        notification_Adapter = Notification_Adapter(notification_list, view.context)
        notification_rv!!.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
        notification_rv!!.adapter = notification_Adapter
        notification_Adapter!!.notifyDataSetChanged()



    }
}
