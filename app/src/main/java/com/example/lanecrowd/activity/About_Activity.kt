package com.example.lanecrowd.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.lanecrowd.R
import com.xw.repo.VectorCompatTextView
import kotlinx.android.synthetic.main.activity_about_.*
import org.json.JSONObject


class About_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_)
    }


    fun add_work(view: View) {

       /* var id=101
        var image=CircleImageView(this)
        image.id=id
        image.setImageResource(R.drawable.work_icon2)
        image.setColorFilter(ContextCompat.getColor(applicationContext,R.color.bgBottomNavigation))


        val tv_dynamic = TextView(this)
        tv_dynamic.textSize = 22f
        tv_dynamic.text = "Android Developer at Greenusys Technology"
        tv_dynamic.setPadding(10,0,0,5)
*/




      /*  <com.xw.repo.VectorCompatTextView
        android:padding="10dp"
        android:drawablePadding="10dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Android Developer at Greenusys Technolggy"
        style="@style/TextAppearance.AppCompat.Headline"
        app:drawableWidth="60dp"
        app:drawableHeight="60dp"
        android:textSize="22dp"
        android:gravity="center_vertical"
        android:drawableTint="@color/colorPrimary"
        app:drawableLeftCompat="@drawable/work_icon2"/>
*/


        var textView=VectorCompatTextView(this)
        textView.setText("Android Developer at Greenusys Technolggy")
        //textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.work_icon2, 0, 0, 0);

        val img: Drawable = this@About_Activity.applicationContext.getResources().getDrawable(R.drawable.work_icon2)
        img.setBounds(0, 0, img.intrinsicWidth * textView.getMeasuredHeight() / img.intrinsicHeight, textView.getMeasuredHeight())
        textView.setCompoundDrawables(img, null, null, null);


        // var first =  RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //var second =  RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);



        //second.addRule(RelativeLayout.RIGHT_OF, id);


       // work_layout!!.addView(image,first)
        work_layout!!.addView(textView)







    }

    fun back_activity(view: View) {
        onBackPressed()
    }

}
