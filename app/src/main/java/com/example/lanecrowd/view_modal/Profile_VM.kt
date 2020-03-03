package com.example.lanecrowd.view_modal


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lanecrowd.retrofit.ApiClient
import com.example.lanecrowd.retrofit.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Profile_VM : ViewModel() {
    var loginlist: MutableLiveData<RegisterResModal>? = null
    var reglist: MutableLiveData<RegisterResModal>? = null
    var verifyList: MutableLiveData<RegisterResModal>? = null

    lateinit var apiInterface: ApiInterface

    fun changeProfileCoverPic(
        from: String,
        files: MultipartBody.Part,
        userId: RequestBody,
        android: RequestBody
    ): MutableLiveData<RegisterResModal> {

        apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        loginlist = MutableLiveData()
        callChangeProfileCoverPicAPI(from, files, userId, android)

        return loginlist as MutableLiveData<RegisterResModal>

    }


    fun callChangeProfileCoverPicAPI(
        from: String,
        files: MultipartBody.Part,
        userId: RequestBody,
        android: RequestBody
    ) {
        var call: retrofit2.Call<RegisterResModal?>? = null

        if (from.equals("profile"))
            call = apiInterface.changeProfilePic(files, userId, android)
        else
            call = apiInterface.changeCoverePic(files, userId, android)


        call!!.enqueue(object : Callback<RegisterResModal?> {
            override fun onResponse(
                call: Call<RegisterResModal?>,
                response: Response<RegisterResModal?>
            ) {

                try {
                    var a: RegisterResModal? = response.body()

                    println("callChangeProfilePicAPIres" + a!!.msg)


                    if (response.isSuccessful) {


                        loginlist!!.value = response.body()

                    } else
                        loginlist?.value = null


                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            override fun onFailure(call: Call<RegisterResModal?>, t: Throwable) {
                println("Failed" + t.message)
                loginlist?.value = null
                call.cancel()

            }


        })


    }
}
