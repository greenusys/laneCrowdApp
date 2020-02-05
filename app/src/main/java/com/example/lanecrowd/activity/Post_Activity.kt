package com.example.lanecrowd.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Notification_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Notification_Adapter

class Post_Activity : AppCompatActivity() {


    var post_activity__rv: RecyclerView? = null
    var toolbar: Toolbar? = null

    var notification_Adapter: Notification_Adapter? = null

    var notification_list = ArrayList<Notification_Modal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_)
        
        initViews()
    }

    private fun initViews() {


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


        post_activity__rv =findViewById(R.id.post_activity__rv)
        toolbar =findViewById(R.id.toolbar)





        notification_Adapter = Notification_Adapter(notification_list, applicationContext)
        post_activity__rv!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        post_activity__rv!!.adapter = notification_Adapter
        notification_Adapter!!.notifyDataSetChanged()



    }

    fun gotoBack(view: View) {
        onBackPressed()
    }


}
