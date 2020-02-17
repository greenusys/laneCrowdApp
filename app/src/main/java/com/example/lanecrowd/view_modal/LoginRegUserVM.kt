package com.example.lanecrowd.view_modal


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lanecrowd.retrofit.ApiClient
import com.example.lanecrowd.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRegUserVM : ViewModel() {
    var loginlist: MutableLiveData<RegisterResModal>? = null
    var reglist: MutableLiveData<RegisterResModal>? = null
    var verifyList: MutableLiveData<RegisterResModal>? = null

    lateinit var apiInterface: ApiInterface

    fun loginUser(email: String, password: String): MutableLiveData<RegisterResModal> {

        apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        loginlist = MutableLiveData()
        callLoginUserAPI(email,password)

        return loginlist as MutableLiveData<RegisterResModal>

    }

    fun registerUser(name: String, email: String, password: String): MutableLiveData<RegisterResModal> {

        apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        reglist = MutableLiveData()
        callRegisetUserAPI(name,email,password)

        return reglist as MutableLiveData<RegisterResModal>

    }


 fun verifyOTPUSER(otp_input: String, otp: String, email_phone: String, password: String, name: String): MutableLiveData<RegisterResModal> {

        apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
     verifyList = MutableLiveData()
     callVerifyOTPAPI(otp_input, otp, email_phone, password, name)

        return verifyList as MutableLiveData<RegisterResModal>

    }


    fun callLoginUserAPI(email: String, password: String) {
        val call = apiInterface.loginAPI(email,password)
        call.enqueue(object : Callback<RegisterResModal> {
            override fun onResponse(call: Call<RegisterResModal>, response: Response<RegisterResModal>) {

                try {

                    println("login_res"+response.body())


                    if (response.isSuccessful) {

                        loginlist!!.value=response.body()

                    }
                    else
                        loginlist?.value = null


                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            override fun onFailure(call: Call<RegisterResModal>, t: Throwable) {
                println("Failed" + t.message)
                loginlist?.value = null
                call.cancel()

            }
        })


    } fun callRegisetUserAPI(name: String, email: String, password: String) {
        val call = apiInterface.registerUser(name,email,password)
        call.enqueue(object : Callback<RegisterResModal> {
            override fun onResponse(call: Call<RegisterResModal>, response: Response<RegisterResModal>) {

                try {

                    println("mmm"+response.body())


                    if (response.isSuccessful) {

                        reglist!!.value=response.body()

                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            override fun onFailure(call: Call<RegisterResModal>, t: Throwable) {
                println("Failed" + t.message)
                reglist?.value = null
                call.cancel()

            }
        })


    }


    fun callVerifyOTPAPI(otp_input: String, otp: String, email_phone: String, password: String, name: String) {
        val call = apiInterface.verifyOTP(otp_input,otp,email_phone,password,name)
        call.enqueue(object : Callback<RegisterResModal> {
            override fun onResponse(call: Call<RegisterResModal>, response: Response<RegisterResModal>) {

                try {

                    println("verifyList"+response.body())


                    if (response.isSuccessful) {

                        verifyList!!.value=response.body()

                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            override fun onFailure(call: Call<RegisterResModal>, t: Throwable) {
                println("Failed" + t.message)
                verifyList?.value = null
                call.cancel()

            }
        })


    }


}
