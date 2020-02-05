package com.example.lanecrowd.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Chatting_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Chatting_Adapter

class Chat_Activity : AppCompatActivity() {


    var chat_rv: RecyclerView? = null

    var chatting_Adapter: Chatting_Adapter? = null

    var chatting_list = ArrayList<Chatting_Modal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_)

        initViews()


    }


    private fun initViews() {


        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())
        chatting_list.add(Chatting_Modal())






        chat_rv = findViewById(R.id.chat_rv)



        chatting_Adapter = Chatting_Adapter(chatting_list, applicationContext)
        chat_rv!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        chat_rv!!.adapter = chatting_Adapter
        chatting_Adapter!!.notifyDataSetChanged()


    }

    fun back_activity(view: View) {}

    override fun onBackPressed() {
        super.onBackPressed()
    }


}
