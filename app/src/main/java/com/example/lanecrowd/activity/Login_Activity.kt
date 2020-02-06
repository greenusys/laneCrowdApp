package com.example.lanecrowd.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lanecrowd.R


class Login_Activity : AppCompatActivity(), View.OnClickListener {


    var login_sign_up: TextView? = null
    var register_signin: TextView? = null
    var forget_password: TextView? = null


    var register_layout: LinearLayout? = null
    var login_layout: LinearLayout? = null


    var login_sign_inbtn: TextView? = null
    var register_sign_upbtn: TextView? = null

    var loginEmail: EditText? = null
    var loginPassword: EditText? = null

    var reg_name: EditText? = null
    var reg_email: EditText? = null
    var reg_password: EditText? = null
    var reg_re_password: EditText? = null
    internal var showPassword: Boolean? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)


        login_sign_up = findViewById(R.id.login_sign_up)
        register_signin = findViewById(R.id.register_signin)


        forget_password = findViewById(R.id.forget_password)
        register_layout = findViewById(R.id.register_layout)
        login_layout = findViewById(R.id.login_layout)


        //login and register button
        login_sign_inbtn = findViewById(R.id.login_sign_in)
        register_sign_upbtn = findViewById(R.id.register_sign_up)


        //fields of sign in
        loginEmail = findViewById(R.id.email)
        loginPassword = findViewById(R.id.password)


        //fields of register
        reg_name = findViewById(R.id.reg_name)
        reg_email = findViewById(R.id.reg_email)
        reg_password = findViewById(R.id.reg_password)
        reg_re_password = findViewById(R.id.reg_re_password)


        //set listener
        login_sign_inbtn!!.setOnClickListener(this)
        register_sign_upbtn!!.setOnClickListener(this)
        forget_password!!.setOnClickListener(this)
        register_signin!!.setOnClickListener(this)



       visiblePassword(loginPassword!!)
       visiblePassword(reg_password!!)
       visiblePassword(reg_re_password!!)



    }

    private fun visiblePassword(view: EditText) {

        view!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= view!!.getRight() - loginPassword!!.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {
                    if (showPassword!!) {
                        view!!.setTransformationMethod(PasswordTransformationMethod())
                        view!!.setSelected(false)
                    } else {
                        view!!.setTransformationMethod(HideReturnsTransformationMethod())
                        view!!.setSelected(true)
                    }
                    showPassword = !showPassword!!
                    return@OnTouchListener true
                }
            }
            false
        })


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
            R.id.forget_password ->
                startActivity(Intent(applicationContext, Forget_Pwd_Activity::class.java))
            R.id.login_sign_in ->
                 startActivity(Intent(applicationContext,HomeActivity::class.java))
               // checkLoginValidation()

            R.id.register_sign_up ->
                checkRegisterValidation()
        }
    }

    private fun checkLoginValidation() {
        if (!validateLoginEmail())
            return

        if (!validateLoginPassword())
            return


        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
    }

    private fun checkRegisterValidation() {
        if (!validateName())
            return
        if (!validateEmail())
            return
        if (!validatePassword())
            return
        if (!validateComfirmPassword())
            return

        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
    }

    private fun validateName(): Boolean {
        if (reg_name!!.text!!.toString() == "") {
            reg_name!!.error = "Please Enter Name"
            requestFocusCursor(reg_name!!)
            return false
        } else if (reg_name!!.text!!.toString().length < 3) {
            reg_name!!.error = "Please Enter Valid Name"
            requestFocusCursor(reg_name!!)
            return false
        } else
            reg_name!!.error = null

        return true
    }


    private fun validateLoginEmail(): Boolean {
        if (loginEmail!!.text!!.toString() == "") {
            loginEmail!!.error = "Please Enter Email Address"
            requestFocusCursor(loginEmail!!)
            return false
        }
        else if (loginEmail!!.text.toString().length<=15) {
            loginEmail!!.error = "Please Enter Valid Email Address"
            requestFocusCursor(loginEmail!!)
            return false
        }
        else if (!isValidEmail(loginEmail!!.text!!.toString())) {
            loginEmail!!.error = "Please Enter Valid Email Address"
            requestFocusCursor(loginEmail!!)
            return false
        } else
            loginEmail!!.error = null

        return true
    }


    private fun validateLoginPassword(): Boolean {
        if (loginPassword!!.text!!.toString() == "") {
            loginPassword!!.error = "Please Enter Password"
            requestFocusCursor(loginPassword!!)
            return false
        } else if (loginPassword!!.text!!.toString().length < 5) {
            loginPassword!!.error = "Please Enter Valid Password"
            requestFocusCursor(loginPassword!!)
            return false
        } else
            loginPassword!!.error = null

        return true
    }


private fun validateEmail(): Boolean {
        if (reg_email!!.text!!.toString() == "") {
            reg_email!!.error = "Please Enter Email Address"
            requestFocusCursor(reg_email!!)
            return false
        } else if (reg_email!!.text.toString().length<=12) {
            reg_email!!.error = "Please Enter Valid Email Address"
            requestFocusCursor(reg_email!!)
            return false
        }else if (!isValidEmail(reg_email!!.text!!.toString())) {
            reg_email!!.error = "Please Enter Valid Email Address"
            requestFocusCursor(reg_email!!)
            return false
        } else
            reg_email!!.error = null

        return true
    }


    private fun validatePassword(): Boolean {
        if (reg_password!!.text!!.toString() == "") {
            reg_password!!.error = "Please Enter Password"
            requestFocusCursor(reg_password!!)
            return false
        } else if (reg_password!!.text!!.toString().length < 5) {
            reg_password!!.error = "Please Enter Atleast 5 Digit Password"
            requestFocusCursor(reg_password!!)
            return false
        } else
            reg_password!!.error = null

        return true
    }

    private fun validateComfirmPassword(): Boolean {
        if (reg_re_password!!.text!!.toString() == "") {
            reg_re_password!!.error = "Please Enter Comfirm Password"
            requestFocusCursor(reg_re_password!!)
            return false
        } else if (!isSamePassword(reg_password!!.text!!.toString(), reg_re_password!!.text!!.toString())) {
            reg_re_password!!.error = "Comfirm Password Do Not Matched"
            requestFocusCursor(reg_re_password!!)
            return false
        } else
            reg_re_password!!.error = null

        return true
    }

    internal fun isSamePassword(password: String, comfirm_pass: String): Boolean {
        return password == comfirm_pass

    }


    internal fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun requestFocusCursor(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun showSignInForm() {

        login_layout!!.requestLayout()

        login_layout!!.visibility = View.VISIBLE
        register_layout!!.visibility = View.GONE

        val translate = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_left_to_right)
        login_layout!!.startAnimation(translate)

        val clockwise = AnimationUtils.loadAnimation(applicationContext, R.anim.left_to_right)
        register_signin!!.startAnimation(clockwise)

    }

    private fun showRegisterForm() {
        register_layout!!.requestLayout()

        login_layout!!.visibility = View.GONE
        register_layout!!.visibility = View.VISIBLE

        val translate = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_right_to_left)
        register_layout!!.startAnimation(translate)

        val clockwise = AnimationUtils.loadAnimation(applicationContext, R.anim.left_to_right)
        login_sign_up!!.startAnimation(clockwise)

    }

    private fun hideSoftKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        try {


            if (imm.isAcceptingText) {
                // verify if the soft keyboard is open
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
        } catch (e: Exception) {

        }
    }

    fun onClick_Customer(v: View) {


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