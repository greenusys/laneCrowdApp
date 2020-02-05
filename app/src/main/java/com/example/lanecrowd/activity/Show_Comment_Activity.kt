package com.example.lanecrowd.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.CommentAdapter
import com.example.lanecrowd.modal.CommentModel

class Show_Comment_Activity : AppCompatActivity() {


    var comment_rv: RecyclerView? = null
    var adapter: CommentAdapter? = null

    var comment_list = ArrayList<CommentModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show__comment_)


        initViews()


    }

    private fun initViews() {




        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())
        comment_list.add(CommentModel())


        comment_rv = findViewById<RecyclerView>(R.id.comment_rv)
        adapter = CommentAdapter(applicationContext, comment_list)
        comment_rv!!.adapter = adapter
        comment_rv!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        adapter!!.notifyDataSetChanged()

    }

    fun back_activity(view: View) {
        onBackPressed()
    }

}
