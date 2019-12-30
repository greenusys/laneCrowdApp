package com.example.lanecrowd.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.lanecrowd.R
import kotlinx.android.synthetic.main.login_layout.*
import kotlinx.android.synthetic.main.register_layout.*
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.login_main.*
import org.w3c.dom.Text


class Login_Activity : AppCompatActivity(), View.OnClickListener {



     var login_sign_up:TextView?=null
     var register_signin:TextView?=null


     var register_layout:LinearLayout?=null
     var login_layout:LinearLayout?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)


        login_sign_up=findViewById(R.id.login_sign_up)
        register_signin=findViewById(R.id.register_signin)


        register_layout=findViewById(R.id.register_layout)
        login_layout=findViewById(R.id.login_layout)




    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_sign_up -> {
                hideSoftKeyBoard()
                showRegisterForm()
            }
            R.id.register_signin -> {
                hideSoftKeyBoard()
                showSignInForm()
            }
        }
    }


    private fun showSignInForm() {

        login_layout!!.requestLayout()

        login_layout!!.visibility=View.VISIBLE
        register_layout!!.visibility=View.GONE

        val translate = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_left_to_right)
        login_layout!!.startAnimation(translate)

        val clockwise = AnimationUtils.loadAnimation(applicationContext, R.anim.left_to_right)
        register_signin!!.startAnimation(clockwise)

    }

    private fun showRegisterForm() {
        register_layout!!.requestLayout()

        login_layout!!.visibility=View.GONE
        register_layout!!.visibility=View.VISIBLE

        val translate = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_left_to_right)
        register_layout!!.startAnimation(translate)

        val clockwise = AnimationUtils.loadAnimation(applicationContext, R.anim.left_to_right)
        login_sign_up!!.startAnimation(clockwise)

    }
    private fun hideSoftKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        try {


            if (imm!!.isAcceptingText()) {
                // verify if the soft keyboard is open
                imm!!.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
        } catch (e: Exception) {

        }
    }

    fun onClick_Customer(v: View) {

        println("Kkk")

        when (v.id) {
            R.id.login_sign_up -> {
                hideSoftKeyBoard()
                showRegisterForm()
            }
            R.id.register_signin -> {
                hideSoftKeyBoard()
                showSignInForm()
            }
        }
    }
}