package com.example.lanecrowd.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.SelectedModal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.CommentAdapter
import com.example.lanecrowd.adapter.Show_Selected_File_Adapter

class Add_Post_Activity : AppCompatActivity() {


    var rv_addFiles: RecyclerView? = null
    var adapter: Show_Selected_File_Adapter? = null

    var comment_list = ArrayList<SelectedModal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__post_)

        initViews()
    }



    private fun initViews() {




        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())
        comment_list.add(SelectedModal())


        rv_addFiles = findViewById<RecyclerView>(R.id.rv_addFiles)
        adapter = Show_Selected_File_Adapter(comment_list,applicationContext)
        rv_addFiles!!.adapter = adapter
        rv_addFiles!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.HORIZONTAL, false)
        adapter!!.notifyDataSetChanged()

    }



    fun back_activity(view: View) {
        onBackPressed()
    }

    fun choose_Status(view: View) {

        startActivity(Intent(applicationContext,Choose_Status_Activity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    }


}
