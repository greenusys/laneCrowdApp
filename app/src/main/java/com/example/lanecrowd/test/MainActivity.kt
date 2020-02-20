package com.example.lanecrowd.test

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lanecrowd.R
import java.util.*

class MainActivity : AppCompatActivity() {
    var getDatalist: ArrayList<Home_Post_Modal?> = ArrayList()
    var postfiles: ArrayList<String> = ArrayList()
    private var mrecyclerView: RecyclerView? = null
    var mAdapter: KaifAdapter? = null
    private var random: Random? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test5)

        mrecyclerView = findViewById<View>(R.id.mRecyclerview) as RecyclerView
        mrecyclerView!!.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        mAdapter = KaifAdapter(this@MainActivity, getDatalist!!, mrecyclerView!!)
        mrecyclerView!!.adapter = mAdapter
        // set RecyclerView on item click listner
        mAdapter!!.setOnItemListener(object :
            KaifAdapter.OnItemClickListener {
            override fun onItemClick(homelist: Home_Post_Modal) {

            }
        })


      loadData()


        //set load more listener for the RecyclerView adapter
        mAdapter!!.setOnLoadMoreListener(object :
            KaifAdapter.OnLoadMoreListener {
            override fun onLoadMore() {

                    getDatalist!!.add(null)
                    mAdapter!!.notifyItemInserted(getDatalist!!.size - 1)
                    Handler().postDelayed({
                        getDatalist!!.removeAt(getDatalist!!.size - 1)
                        mAdapter!!.notifyItemRemoved(getDatalist!!.size)
                        //Generating more data
                        val index = getDatalist!!.size
                        val end = index + 20

                        loadData()

                        mAdapter!!.notifyDataSetChanged()
                        mAdapter!!.setLoaded()
                    }, 2000)

            }
        })
    }

    private fun loadData() {

        for (i in 0 until 10)
            getDatalist.add(Home_Post_Modal("","","",
                postfiles,"","","","",
                "","",true,true))



    }

    private fun phoneNumberGenerating(): String {
        val low = 100000000
        val high = 999999999
        val randomNumber = random!!.nextInt(high - low) + low
        return "0$randomNumber"
    }
}