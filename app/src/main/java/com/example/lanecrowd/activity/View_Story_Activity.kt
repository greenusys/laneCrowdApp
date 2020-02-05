package com.example.lanecrowd.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lanecrowd.R

class View_Story_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view__story_)
    }

    fun back(view: View) {
        onBackPressed()
    }
}
