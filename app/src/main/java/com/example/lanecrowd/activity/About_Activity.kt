package com.example.lanecrowd.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.lanecrowd.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_about_.*


class About_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_)
    }


    fun add_work(view: View) {

        var id=101
        var image=CircleImageView(this)
        image.id=id
        image.setImageResource(R.drawable.work_icon2)
        image.setColorFilter(ContextCompat.getColor(applicationContext,R.color.bgBottomNavigation))


        val tv_dynamic = TextView(this)
        tv_dynamic.textSize = 22f
        tv_dynamic.text = "Android Developer at Greenusys Technology"
        tv_dynamic.setPadding(10,0,0,5)




        var first =  RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        var second =  RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        second.addRule(RelativeLayout.RIGHT_OF, id);


        work_layout!!.addView(image,first)
        work_layout!!.addView(tv_dynamic,second)











    }

    fun back_activity(view: View) {
        onBackPressed()
    }

}
