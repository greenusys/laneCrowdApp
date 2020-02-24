package com.example.lanecrowd.Home_Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.lancrowd.activity.modal.Home_Post_Modal
import com.example.lancrowd.activity.modal.Story_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.Home_Post_Adapter
import com.example.lanecrowd.adapter.Story_Adapter
import com.example.lanecrowd.modal.PowerMenuUtils
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.FetchPostVm
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.JsonObject
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import org.json.JSONArray
import org.json.JSONObject


class Home_Post_Fragment :Fragment(), SearchView.OnQueryTextListener {




    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    private var isLoading = false
    internal var firstTime = true
    internal var counting:Int = 0
    internal var visibleThreshold:Int = 10


    internal var postId:String=""
    internal var postposition:Int = -1

    private var selPostfMenu: PowerMenu? = null
    private var otherPostMenu: PowerMenu? = null
    private var swipe: SwipeRefreshLayout? = null
    private var vibe: Vibrator? = null

    var home_post_rv: RecyclerView?=null
      var story_rv: RecyclerView?=null

      var homePostAdapter:Home_Post_Adapter?=null
      var storyAdapter:Story_Adapter?=null

      var home_post_list= ArrayList<Home_Post_Modal>()
      var story_list= ArrayList<Story_Modal>()
    lateinit var viewmodel: FetchPostVm
    var layoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home__post_, container, false)


        story_list.add(Story_Modal())
        story_list.add(Story_Modal())
        story_list.add(Story_Modal())
        story_list.add(Story_Modal())
        story_list.add(Story_Modal())
        story_list.add(Story_Modal())


        viewmodel = ViewModelProvider(this).get(FetchPostVm::class.java)


        //power menu
        selPostfMenu = PowerMenuUtils.getSelfPostMenu(context!!, this, onHamburgerItemClickListener, onHamburgerMenuDismissedListener)
        otherPostMenu = PowerMenuUtils.getOtherPostMenu(context!!, this, onHamburgerItemClickListener, onHamburgerMenuDismissedListener)

        vibe = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        home_post_rv = view.findViewById(R.id.home_post_rv) as RecyclerView
        story_rv = view.findViewById(R.id.story_rv) as RecyclerView
        swipe = view.findViewById(R.id.swipe) as SwipeRefreshLayout




        swipe!!.setDistanceToTriggerSync(100)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            swipe!!.setProgressViewOffset(false, 0, 300)
        }

        layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
       // homePostAdapter = Home_Post_Adapter(home_post_list, context!!,this@Home_Post_Fragment)
        home_post_rv!!.layoutManager=layoutManager
       // home_post_rv!!.adapter = homePostAdapter
        // homePostAdapter!!.notifyDataSetChanged()





        storyAdapter = Story_Adapter(story_list, view.context)
        story_rv!!.layoutManager = LinearLayoutManager(view.context, LinearLayout.HORIZONTAL, false)
        story_rv!!.adapter = storyAdapter
        storyAdapter!!.notifyDataSetChanged()

        swipe!!.setRefreshing(true)

        swiperefresh_Listener()
         fetchPost(counting.toString())
        //  initScrollListener()



        return  view
    }


    private fun initScrollListener() {

        home_post_rv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)



            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager!!.itemCount
                val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                println("total_itemCount$totalItemCount")
                println("lastVisibleItem$lastVisibleItem")
                println("john_3")
/*
                if (lastVisibleItem <= totalItemCount - 1 && !isLoading && !firstTime)
                {
                    isLoading = true
                    if (home_post_list.size == 0)
                        counting = 0


                    println("load_more_called")
                    fetchPost(counting.toString())
                }*/



                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    isLoading = true
                    println("load_more_called")
                    fetchPost(counting.toString())

                }

            }
        })




    }




    private fun swiperefresh_Listener() {

        swipe!!.setOnRefreshListener(OnRefreshListener {
            try {
                ClearForRefreshData()
                counting = 0
                fetchPost(counting.toString())
            } catch (e: IndexOutOfBoundsException) {
                println("on_Refresh_Exception_Found_$e")
            }
        })

    }

    private fun ClearForRefreshData() {

        home_post_list.clear()
        home_post_rv!!.recycledViewPool.clear()
        notifiyAdapter()


    }

    private fun setRefreshingfalse() {

            swipe!!.isRefreshing=false

    }


    private fun fetchPost(offsettt:String) {


        try {

            viewmodel.fetchPostvm(URL.userId,offsettt).observe(viewLifecycleOwner, Observer { resultPi ->

                println("fetch_post" + resultPi)



                storepostDataTOModal(resultPi)


            })


        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }



    }

    private fun storepostDataTOModal(resultPi: JsonObject?) {


        updateAdapterForMultipleData()


        var main = JSONObject(resultPi.toString())



        var isMylike =false
        var isImage =false


        if(main.getString("code").equals("1"))
        {


            val data:JSONArray=main.getJSONArray("data")

            println("data_size"+data.length())

            for (i in 0 until data.length()) {

                var item=data.getJSONObject(i)

                var postList:ArrayList<String> = ArrayList()


                try {

               if(!item.getString("post_files").equals("null")) {
                   postList = item.getString("post_files").split(",") as ArrayList<String>

                   isImage = item.getString("post_files").contains(".jpg") || item.getString("post_files").contains(".jpeg") || item.getString("post_files").contains(".png")

               }  else {
                   isImage=false
                   postList.add("empty")
               }

                }catch (e:Exception)
                {
                    isImage=false
                    postList.add(item.getString("post_files"))

                }

                    //for likes
                val like:JSONArray=item.getJSONArray("likes_data")
               if(like.length()>0) {

                   println("like_kaif"+like)

                   var likeitem = like.getJSONObject(0)

                   isMylike = likeitem.getString("like_or_dislike").equals("1")

               }
                else
                   isMylike=false






                //store data to list
                home_post_list.add(
                    Home_Post_Modal(
                        item.getString("post_id"),
                        item.getString("user_id"),
                        item.getString("post"),
                        postList,
                        item.getString("posted_by"),
                        item.getString("profile_pic"),
                        item.getString("posted_on"),
                        item.getString("total_likes"),
                        item.getString("total_share"),
                        item.getString("total_comments"),
                        isImage,
                        isMylike))





            }

            counting++
            isLoading = false

        }

        //for no post available
        else
        {
            isLoading = true

        }


        if (firstTime) {
            println("firstime"+home_post_list.size)
            firstTime = false
           // showFooter.add(true)

            homePostAdapter = Home_Post_Adapter(home_post_list, context!!,this@Home_Post_Fragment)
            home_post_rv!!.setAdapter(homePostAdapter)
            notifiyAdapter()

        } else {
            println("second_time")

            notifiyAdapter()
        }







    }

    private fun updateAdapterForMultipleData() {


        println("kaifff"+homePostAdapter)
        if (swipe!!.isRefreshing()) {
            println("Refreshing;......")
            if (activity != null) {
                activity!!.runOnUiThread {
                    if (homePostAdapter != null) {
                        home_post_rv!!.getRecycledViewPool().clear()
                        homePostAdapter!!.notifyDataSetChanged()
                        swipe!!.setRefreshing(false)
                    }
                    home_post_list.clear()
                    swipe!!.setRefreshing(false)
                }
            }
        }


    }

    private fun notifiyAdapter() {


        homePostAdapter!!.notifyDataSetChanged()

    }


    //power option menu
    private val onHamburgerItemClickListener =
            OnMenuItemClickListener<PowerMenuItem> { position, item ->
                // hamburgerMenu!!.selectedPosition = position

                if(!firstTime) {


                    println("menu_clcked")



                    if (item.title.equals(getString(R.string.delete)))
                       askToDeleteSelfPost(postId)


/*

                    if (item.title.equals(getString(R.string.rate_me)))
                        rateMe()
                    else if (item.title.equals(getString(R.string.share)))
                        share()
                    else if (item.title.equals(getString(R.string.about)))
                        gotoABoutActivity()*/
                }


            }

    private fun askToDeleteSelfPost(postId: String) {


        MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
            .setTitle("LaneCrowd")
            .setMessage("Are you sure want to delete post?")
            .setNegativeButton("No") { dialogInterface, i ->
            }
            .setPositiveButton("Yes") { dialogInterface, i ->
                println("show_psoitio_2"+postposition)


                viewmodel.deletePosetAPI(postId)
                homePostAdapter!!.notifyItemRemoved(postposition)

            }
            .show()
    }

    private val onHamburgerMenuDismissedListener = {
        Log.d("Test", "onDismissed hamburger menu") }



    fun showMenu(position:Int,postId:String,menu: ImageView, value: Boolean) {
        showVibration()


        println("show_psoitio"+position)


        this.postId=postId
        this.postposition=position

        if (selPostfMenu!!.isShowing) {
            selPostfMenu!!.dismiss()
            return
        }

        if (otherPostMenu!!.isShowing) {
            otherPostMenu!!.dismiss()
            return
        }


        println("menu_check"+value)

        if(value)
        selPostfMenu!!.showAsDropDown(menu)
        else
        otherPostMenu!!.showAsDropDown(menu)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(this)

    }

     fun showVibration() {

        vibe!!.vibrate(80);
    }

    fun likeDislike(postId: String, userId: String) {


        viewmodel.likeDislikeAPI(postId,userId)

    }



}