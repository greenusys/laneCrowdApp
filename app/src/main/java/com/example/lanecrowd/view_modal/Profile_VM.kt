package com.example.lanecrowd.view_modal


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lancrowd.activity.modal.RegisterResModal
import com.example.lanecrowd.modal.repository.UserRepository
import com.example.lanecrowd.retrofit.ApiClient
import com.example.lanecrowd.retrofit.ApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Profile_VM(val repo: UserRepository) : ViewModel() {
    var changepiclist: MutableLiveData<RegisterResModal>? = null
    private val compositeDisposable = CompositeDisposable()


    fun changeProfileCoverPic(from: String, files: MultipartBody.Part, userId: RequestBody, android: RequestBody): MutableLiveData<RegisterResModal> {

        changepiclist = MutableLiveData()
        callChangeProfileCoverPicAPI(from, files, userId, android)

        return changepiclist as MutableLiveData<RegisterResModal>

    }


    fun callChangeProfileCoverPicAPI(from: String, files: MultipartBody.Part, userId: RequestBody, android: RequestBody) {

        compositeDisposable.add(
            repo.changePic(from,files, userId, android)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RegisterResModal?>() {
                    override fun onSuccess(response: RegisterResModal) {


                        println("callChangeProfilePicAPIres" + response)


                        if (response.status.equals("1")) {
                            changepiclist!!.value = response
                        } else {
                            changepiclist!!.value = null
                        }

                    }

                    override fun onError(e: Throwable) {
                        changepiclist?.value = null
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
