package com.example.lanecrowd.view_modalimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport com.example.lanecrowd.modal.repository.UserRepositoryimport com.example.lanecrowd.retrofit.ApiClientimport com.example.lanecrowd.retrofit.ApiInterfaceimport com.google.gson.JsonObjectimport io.reactivex.android.schedulers.AndroidSchedulersimport io.reactivex.disposables.CompositeDisposableimport io.reactivex.observers.DisposableSingleObserverimport io.reactivex.schedulers.Schedulersimport retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseclass CommentVM(val repo:UserRepository) : ViewModel() {    var fetchCommentRes: MutableLiveData<JsonObject>? = null    var insertCommentRes: MutableLiveData<JsonObject>? = null    private val compositeDisposable = CompositeDisposable()    fun fetchCommentVM(post_id: String, offset: String): MutableLiveData<JsonObject> {        fetchCommentRes = MutableLiveData()        fechCommentAPI(post_id, offset)        return fetchCommentRes as MutableLiveData<JsonObject>    }    fun addCommentVM(post_id: String,comment: String, comment_by: String): MutableLiveData<JsonObject> {            insertCommentRes = MutableLiveData()         addCommentAPI(post_id,comment_by,comment)        return insertCommentRes as MutableLiveData<JsonObject>    }    fun deleteCommentVM(comment_id: String): MutableLiveData<JsonObject> {        fetchCommentRes = MutableLiveData()        deleteCommentAPI(comment_id)        return fetchCommentRes as MutableLiveData<JsonObject>    }    fun editCommentVM(userID:String,comment:String,comment_id: String): MutableLiveData<JsonObject> {        fetchCommentRes = MutableLiveData()        editCommentAPI(userID,comment,comment_id)        return fetchCommentRes as MutableLiveData<JsonObject>    }    fun fechCommentAPI(post_id: String, offset: String) {        println("fechComment" + post_id  + " " + offset)        compositeDisposable.add(            repo.fetchCommentRepo(post_id, offset)                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribeWith(object : DisposableSingleObserver<JsonObject?>() {                    override fun onSuccess(response: JsonObject) {                        println("fetch_Comment" + response)                        if (response.has("code") && response.get("code").toString().equals("1")) {                            println("if_kaif")                            fetchCommentRes!!.value = response                        } else {                            println("ifelse_kaif")                            fetchCommentRes!!.value = null                        }                    }                    override fun onError(e: Throwable) {                        println("Errorrrr" + e.message)                        fetchCommentRes?.value = null                    }                })        )    }    fun addCommentAPI(post_id: String, commented_by_: String, comment: String) {        println("addCommentAPI" + post_id + "  " + commented_by_ + " " + comment)        compositeDisposable.add(            repo.addCommentRepo( post_id, commented_by_, comment)                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribeWith(object : DisposableSingleObserver<JsonObject?>() {                    override fun onSuccess(response: JsonObject) {                        println("fetch_Story_res" + response)                        if (response.has("code") && response.get("code").toString().equals("1")) {                            println("if_kaif")                            insertCommentRes!!.value = response                        } else {                            println("ifelse_kaif")                            insertCommentRes!!.value = null                        }                    }                    override fun onError(e: Throwable) {                        println("Errorrrr" + e.message)                        insertCommentRes?.value = null                    }                })        )    }    fun deleteCommentAPI(c_id: String) {        println("deleteCommentAPI" + c_id )        compositeDisposable.add(            repo.deleteCommentRepo(c_id)                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribeWith(object : DisposableSingleObserver<JsonObject?>() {                    override fun onSuccess(response: JsonObject) {                        println("deleteCommentAPI" + response)                    }                    override fun onError(e: Throwable) {                        println("Errorrrr" + e.message)                    }                })        )    }    fun editCommentAPI(userId:String,comment:String,c_id: String) {        println("editCommentAPI" + c_id )        compositeDisposable.add(            repo.editCommentRepo(userId,comment,c_id)                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribeWith(object : DisposableSingleObserver<JsonObject?>() {                    override fun onSuccess(response: JsonObject) {                        println("editCommentAPI" + response)                    }                    override fun onError(e: Throwable) {                        println("Errorrrr" + e.message)                    }                })        )    }    override fun onCleared() {        super.onCleared()        println("cleared")        compositeDisposable.clear()    }}