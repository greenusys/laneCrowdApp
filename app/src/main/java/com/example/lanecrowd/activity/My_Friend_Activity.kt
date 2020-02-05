package com.example.lanecrowd.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lancrowd.activity.modal.My_Friends_Modal
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.My_Friend_List_Adapter
import com.example.lanecrowd.modal.PowerMenuUtils
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem


class My_Friend_Activity : AppCompatActivity(), View.OnClickListener, SearchView.OnQueryTextListener {

    var firstTime: Boolean=false

    private var hamburgerMenu: PowerMenu? = null
    private var vibe: Vibrator? = null

    var all_friends: TextView? = null
    var family_friends: TextView? = null
    var recent_friends: TextView? = null
    var favourite_friends: TextView? = null


    var friend_rv: RecyclerView? = null
    var adapter: My_Friend_List_Adapter? = null
    var friend_list = ArrayList<My_Friends_Modal>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my__friend_)

        initViews()

        set_All_Friend_Adapter("all")

    }


    @SuppressLint("WrongConstant")
    private fun set_All_Friend_Adapter(value: String) {


        changeTextColors(value)



        friend_rv!!.recycledViewPool.clear()
        friend_list.clear()

        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())
        friend_list.add(My_Friends_Modal())


        adapter = My_Friend_List_Adapter(friend_list, this)
        friend_rv!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        friend_rv!!.adapter = adapter
        adapter!!.notifyDataSetChanged()

        println("list_size"+friend_list.size)

    }

    private fun changeTextColors(value: String) {

        if (value.equals("all")) {
            all_friends!!.setBackgroundColor(getResources().getColor(R.color.box_9))
            recent_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))
            favourite_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))
            family_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))


        } else if (value.equals("recent")) {


            all_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))
            recent_friends!!.setBackgroundColor(getResources().getColor(R.color.box_9))
            favourite_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))
            family_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))


        } else if (value.equals("family")) {


            all_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))
            recent_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))
            favourite_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))
            family_friends!!.setBackgroundColor(getResources().getColor(R.color.box_9))


        } else if (value.equals("fav")) {


            all_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))
            recent_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))
            favourite_friends!!.setBackgroundColor(getResources().getColor(R.color.box_9))
            family_friends!!.setBackgroundColor(getResources().getColor(R.color.light_grey2))


        }

    }


    //power option menu
    private val onHamburgerItemClickListener =
            OnMenuItemClickListener<PowerMenuItem> { position, item ->
                // hamburgerMenu!!.selectedPosition = position

                if(!firstTime) {
/*
                    if (item.title.equals(getString(R.string.rate_me)))
                        rateMe()
                    else if (item.title.equals(getString(R.string.share)))
                        share()
                    else if (item.title.equals(getString(R.string.about)))
                        gotoABoutActivity()*/
                }


            }

    private val onHamburgerMenuDismissedListener = {
        Log.d("Test", "onDismissed hamburger menu") }



    fun showMenu(frndMenu: ImageView) {
        showVibration()

        if (hamburgerMenu!!.isShowing) {
            hamburgerMenu!!.dismiss()
            return
        }
        hamburgerMenu!!.showAsDropDown(frndMenu)


    }


    private fun initViews() {


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //power menu
        hamburgerMenu = PowerMenuUtils.getFriendOptionMenu(this, this, onHamburgerItemClickListener, onHamburgerMenuDismissedListener)

        vibe = this@My_Friend_Activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        all_friends = findViewById(R.id.all_friends)
        family_friends = findViewById(R.id.family_friends)
        recent_friends = findViewById(R.id.recent_friends)
        favourite_friends = findViewById(R.id.favourite_friends)

        friend_rv = findViewById<RecyclerView>(R.id.friend_rv)


        all_friends!!.setOnClickListener(this)
        family_friends!!.setOnClickListener(this)
        recent_friends!!.setOnClickListener(this)
        favourite_friends!!.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search_menu, menu)

        var searchItem = menu.findItem(R.id.action_search)
        var searchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.maxWidth = Integer.MAX_VALUE

        searchView.queryHint="Search Friends"
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.all_friends) {
            showVibration()
            set_All_Friend_Adapter("all")
        } else if (v.id == R.id.family_friends) {
            showVibration()
            set_All_Friend_Adapter("family")
        } else if (v.id == R.id.recent_friends) {
            showVibration()
            set_All_Friend_Adapter("recent")
        } else if (v.id == R.id.favourite_friends) {
            showVibration()
            set_All_Friend_Adapter("fav")
        }
    }

    private fun showVibration() {

        vibe!!.vibrate(80);
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //this is used for back button
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
