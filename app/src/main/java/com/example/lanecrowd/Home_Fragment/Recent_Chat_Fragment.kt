package com.example.lancrowd.activity.home_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.lanecrowd.R

/*import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.lancrowd.R
import com.example.lancrowd.activity.adapter.Home_Post_Adapter
import com.example.lancrowd.activity.adapter.Recent_Chat_Adapter
import com.example.lancrowd.activity.adapter.Story_Adapter
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Recent_Chat_Modal
import com.example.lancrowd.activity.modal.Story_Modal*/


class Recent_Chat_Fragment : Fragment() {

  /*  var recent_chat_rv:RecyclerView?=null

    var recent_chat_adapter:Recent_Chat_Adapter?=null

    var recent_chat_list= ArrayList<Recent_Chat_Modal>()
*/
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
      //  initViews(view)
    }
  /*  private fun initViews(view: View) {



        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())
        recent_chat_list.add(Recent_Chat_Modal())




        recent_chat_rv=view.findViewById(R.id.recent_chat_rv)



        recent_chat_adapter= Recent_Chat_Adapter(recent_chat_list,view.context)
        recent_chat_rv!!.layoutManager= LinearLayoutManager(view.context, LinearLayout.VERTICAL,false)
        recent_chat_rv!!.adapter=recent_chat_adapter
        recent_chat_adapter!!.notifyDataSetChanged()


        println("Home_Fragment")

    }*/
}
