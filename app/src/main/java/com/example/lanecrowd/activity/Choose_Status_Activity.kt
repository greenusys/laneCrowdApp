package com.example.lanecrowd.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Choose_Status_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Choose_Status_Adapter

class Choose_Status_Activity : AppCompatActivity() {

    var main_rel: RelativeLayout? = null
    var status_rv: RecyclerView? = null
    var adapter: Choose_Status_Adapter? = null

    var comment_list = ArrayList<Choose_Status_Modal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose__status_)

        initViews()
    }

    private fun initViews() {




        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())
        comment_list.add(Choose_Status_Modal())


        main_rel = findViewById<RelativeLayout>(R.id.main_rel)
        status_rv = findViewById<RecyclerView>(R.id.status_rv)
        adapter = Choose_Status_Adapter(comment_list,this)
        status_rv!!.adapter = adapter
        status_rv!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.HORIZONTAL, false)
        adapter!!.notifyDataSetChanged()

    }

    fun back_activity(view: View) {

        onBackPressed()
    }

    fun setTheme() {

        main_rel!!.setBackgroundResource(R.drawable.test)

    }


}
