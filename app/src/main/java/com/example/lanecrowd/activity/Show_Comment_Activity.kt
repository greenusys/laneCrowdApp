package com.example.lanecrowd.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lanecrowd.R
import com.example.lanecrowd.adapter.CommentAdapter
import com.example.lanecrowd.modal.CommentModel
import com.example.lanecrowd.modal.PowerMenuUtils
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem

class Show_Comment_Activity : AppCompatActivity() {

    var firstTime: Boolean=false
    private var hamburgerMenu: PowerMenu? = null
    private var vibe: Vibrator? = null

    var comment_rv: RecyclerView? = null
    var adapter: CommentAdapter? = null

    var comment_list = ArrayList<CommentModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show__comment_)


        initViews()


    }

    @SuppressLint("WrongConstant")
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


        //power
        hamburgerMenu = PowerMenuUtils.getCommentMenu(this, this, onHamburgerItemClickListener, onHamburgerMenuDismissedListener)

        vibe = this@Show_Comment_Activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        comment_rv = findViewById<RecyclerView>(R.id.comment_rv)
        adapter = CommentAdapter(this, comment_list)
        comment_rv!!.adapter = adapter
        comment_rv!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        adapter!!.notifyDataSetChanged()

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
    private fun showVibration() {

        vibe!!.vibrate(80);
    }


    fun back_activity(view: View) {
        onBackPressed()
    }

}
