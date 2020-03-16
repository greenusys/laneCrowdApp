package com.example.lanecrowd.view_modal


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lanecrowd.modal.repository.UserRepository
import com.example.lanecrowd.retrofit.ApiClient
import com.example.lanecrowd.retrofit.ApiInterface
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRegUserVM(val repo:UserRepository) : ViewModel() {
    var loginlist: MutableLiveData<RegisterResModal>? = null
    var reglist: MutableLiveData<RegisterResModal>? = null
    var verifyList: MutableLiveData<RegisterResModal>? = null
    private val compositeDisposable = CompositeDisposable()


    fun loginUser(email: String, password: String): MutableLiveData<RegisterResModal> {

        loginlist = MutableLiveData()
        callLoginUserAPI(email,password)

        return loginlist as MutableLiveData<RegisterResModal>

    }

    fun registerUser(name: String, email: String, password: String, dob: String, gender: String): MutableLiveData<RegisterResModal> {

        reglist = MutableLiveData()
        callRegisetUserAPI(name,email,password,dob,gender)

        return reglist as MutableLiveData<RegisterResModal>

    }


 fun verifyOTPUSER(otp_input: String, otp: String, email_phone: String, password: String, name: String,dob: String, gender: String): MutableLiveData<RegisterResModal> {

          verifyList = MutableLiveData()
       callVerifyOTPAPI(otp_input, otp, email_phone, password, name,dob,gender)

        return verifyList as MutableLiveData<RegisterResModal>

    }


    fun callLoginUserAPI(email: String, password: String) {



        compositeDisposable.add(
            repo.loginRepo(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RegisterResModal?>() {
                    override fun onSuccess(response: RegisterResModal) {


                        println("callLoginUserAPI" + response.msg+""+response.status)

                            loginlist!!.value = response


                    }

                    override fun onError(e: Throwable) {
                        println("Errorrrr" + e.message)
                        loginlist?.value = null
                    }
                })
        )


    } fun callRegisetUserAPI(
        name: String,
        email: String,
        password: String,
        dob: String,
        gender: String
    ) {

        compositeDisposable.add(
            repo.registerRepo(name,email,password,dob,gender)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RegisterResModal?>() {
                    override fun onSuccess(response: RegisterResModal) {


                        println("fetch_Story_res" + response)


                        if (response.status.equals("1")) {
                            reglist!!.value = response
                        } else {
                            reglist!!.value = null
                        }

                    }

                    override fun onError(e: Throwable) {
                        println("Errorrrr" + e.message)
                        reglist?.value = null
                    }
                })
        )


    }


    fun callVerifyOTPAPI(
        otp_input: String,
        otp: String,
        email_phone: String,
        password: String,
        name: String,
        dob: String,
        gender: String
    ) {


        compositeDisposable.add(
            repo.verfifyOTPRepo(otp_input,otp,email_phone,password,name,dob, gender)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RegisterResModal?>() {
                    override fun onSuccess(response: RegisterResModal) {


                        println("fetch_Story_res" + response.msg)
                        println("fetch_Story_res_code" + response.status)


                        if (response.status.equals("1")) {
                            verifyList!!.value = response
                        } else {
                            verifyList!!.value = null
                        }

                    }

                    override fun onError(e: Throwable) {
                        println("Errorrrr" + e.message)
                        verifyList?.value = null
                    }
                })
        )


    }

    override fun onCleared() {
        super.onCleared()
        println("cleared")
        compositeDisposable.clear()
    }


}
