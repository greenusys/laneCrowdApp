package com.example.lanecrowd.activity

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.chaos.view.PinView
import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lanecrowd.R
import com.example.lanecrowd.view_modal.LoginRegUserVM
import com.example.lanecrowd.view_modal.factory.ViewModelFactoryC
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class Verify_OTP_Activity : AppCompatActivity(), KodeinAware {


    var pinView: PinView? = null
    var verfiOtp: Button? = null
    lateinit var viewmodel: LoginRegUserVM
    var loading_anim: LottieAnimationView? = null
    var successOTP: LottieAnimationView? = null

    var response: RegisterResModal? = null
    var mainlayout: LinearLayout? = null


    override val kodein by kodein()
    //get factory depencey from outside using kodein framework
    private val factory: ViewModelFactoryC by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify__otp_)



        try {
            initViews()
        } catch (e: Exception) {

        }


    }

    private fun initViews() {


        response = intent.getSerializableExtra("result") as RegisterResModal


        viewmodel = ViewModelProviders.of(this,factory).get(LoginRegUserVM::class.java)

        pinView = findViewById(R.id.pinView_verify)
        verfiOtp = findViewById(R.id.verify_otp)
        loading_anim = findViewById(R.id.loading_anim)
        successOTP = findViewById(R.id.successOTP)
        mainlayout = findViewById(R.id.mainlayout)



        RxView.clicks(findViewById(R.id.verify_otp)).throttleFirst(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Consumer<Any> {
                    override fun accept(o: Any) {
                        print("sallu")

                        checkValidation()

                    }
                })


        val d2 = RxTextView.textChanges(pinView!!)
                .filter { s -> s.toString().length > 3 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    println("sallu_2")

                    hideKeyboardFrom(applicationContext!!, findViewById(R.id.root))


                }



    }

    private fun checkValidation() {

        if (pinView!!.text.toString().equals(""))
            showSnackBar("Please Enter OTP")
        else {
            if (!isNetworkAvailable(applicationContext!!))
                showSnackBar("Please Check Your Internet Connection")
            else {
                hideKeyboardFrom(applicationContext!!, findViewById(R.id.root))
                visibleAnimation(true)
                callVerifyOTPAPI()
            }

        }


    }

    private fun visibleAnimation(value: Boolean) {


        if (value) {
            loading_anim!!.visibility = View.VISIBLE
            loading_anim!!.playAnimation()
        } else {
            loading_anim!!.visibility = View.GONE
            loading_anim!!.pauseAnimation()
        }

    }


    private fun callVerifyOTPAPI() {


        println("pinview"+pinView!!.text.toString())
        println("response!!.otp"+response!!.otp)
        println("response!!.email_phone"+response!!.email_phone)
        println("response!!.password"+response!!.password)
        println("response!!.name"+response!!.name)
        println("response!!.dob"+response!!.date_of_birth)
        println("pinview"+response!!.gender.trim())


        try {
            viewmodel.verifyOTPUSER(pinView!!.text.toString(),
                    response!!.otp,
                    response!!.email_phone,
                    response!!.password,
                    response!!.name,
                    response!!.date_of_birth,
                    response!!.gender.trim()
            ).observe(this, Observer { resultPi ->

                visibleAnimation(false)

                if(resultPi!=null) {
                    if (resultPi.status.equals("1")) {
                        gotoLoginActivity()
                    } else {
                        showSnackBar("OTP does not matched")
                    }
                }
                else
                    showSnackBar("Please try again later")

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun gotoLoginActivity() {

        visiblesuccessOTPANimation(true)

        successOTP!!.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.e("Animation:", "start")
            }

            override fun onAnimationEnd(animation: Animator) {


            }

            override fun onAnimationCancel(animation: Animator) {
                Log.e("Animation:", "cancel")
            }

            override fun onAnimationRepeat(animation: Animator) {
                visiblesuccessOTPANimation(false)
gotoLoginActivity2()
            }
        })

    }

    private fun gotoLoginActivity2() {

        startActivity(Intent(applicationContext, Login_Activity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))

    }

    private fun visiblesuccessOTPANimation(value: Boolean) {



            if (value) {

                mainlayout!!.visibility = View.GONE
                successOTP!!.visibility = View.VISIBLE
                successOTP!!.playAnimation()
            } else {
                successOTP!!.visibility = View.GONE
                successOTP!!.pauseAnimation()
            }


    }


    fun hideKeyboardFrom(context: Context, view: View) {

        try {

            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {

        }


    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    private fun showSnackBar(msg: String) {

        val snackbar = Snackbar.make(findViewById(R.id.root), msg, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.red)
        )
        snackbar.setTextColor(ContextCompat.getColor(applicationContext, R.color.white)
        )
        snackbar.show()

    }


    fun back_activity(view: View) {
        gotoLoginActivity2()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        gotoLoginActivity2()
    }
}
