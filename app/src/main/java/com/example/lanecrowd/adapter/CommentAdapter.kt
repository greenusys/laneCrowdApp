package com.example.lanecrowd.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lanecrowd.R
import com.example.lanecrowd.modal.CommentModel
import java.util.*

class CommentAdapter(var context: Context,  var commentModels: ArrayList<CommentModel>) : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return commentModels.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {

    }




}