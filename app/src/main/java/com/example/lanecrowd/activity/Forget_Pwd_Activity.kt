package com.example.lanecrowd.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.chaos.view.PinView
import com.example.lanecrowd.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Forget_Pwd_Activity : AppCompatActivity(), View.OnClickListener {


    var resetpwdBtn:Button?=null
    var pinView:PinView?=null
    var otptext:TextView?=null
    var emailtxt:TextInputEditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget__pwd_)


        initViews()
    }

    private fun initViews() {

        resetpwdBtn=findViewById(R.id.resetpwdBtn)
        pinView=findViewById(R.id.pinView)
        emailtxt=findViewById(R.id.emailtxt)
        otptext=findViewById(R.id.otptext)


        resetpwdBtn!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        if(v!!.id==R.id.resetpwdBtn)
        {
            emailtxt!!.visibility=View.GONE
            pinView!!.visibility=View.VISIBLE
            otptext!!.visibility=View.VISIBLE

        }
    }

    fun back_activity(view: View) {
        onBackPressed()
    }
}
