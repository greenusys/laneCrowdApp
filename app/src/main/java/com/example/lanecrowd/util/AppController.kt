package com.example.lanecrowd.util

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.lanecrowd.modal.repository.UserRepository
import com.example.lanecrowd.retrofit.ApiInterface
import com.example.lanecrowd.retrofit.ConnectivityReceiver
import com.example.lanecrowd.retrofit.ConnectivityReceiver.ConnectivityReceiverListener
import com.example.lanecrowd.retrofit.GetLastIdCallback
import com.example.lanecrowd.view_modal.factory.ViewModelFactoryC
import okhttp3.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

import java.io.IOException
import java.util.concurrent.TimeUnit

class AppController : Application(), KodeinAware {


    //kodein declaration
    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppController))


        bind() from singleton { NetworkConnectionInterceptor(instance())}
        bind() from singleton { ApiInterface(instance()) }
        bind() from singleton { UserRepository(instance()) }
        bind() from provider { ViewModelFactoryC(instance()) }


    }



    var okHttpClient: OkHttpClient? = null
        private set

    override fun onCreate() {
        super.onCreate()
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        instance = this
    }

    fun setConnectivityListener(listener: ConnectivityReceiverListener?) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

    @Throws(IOException::class)
    fun GetTest(Url: String?, idCallback: GetLastIdCallback) {
        val request = Request.Builder()
            .url(Url)
            .get()
            .build()
        val calls = okHttpClient!!.newCall(request)
        calls.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                backgroundThreadShortToast(
                    applicationContext,
                    e.message
                )
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                idCallback.lastId(response.body()!!.string())
            }
        })
    }

    @Throws(IOException::class)
    fun PostTest(
        Url: String?,
        requestBody: RequestBody?,
        idCallback: GetLastIdCallback
    ) {
        val request = Request.Builder()
            .url(Url)
            .post(requestBody)
            .build()
        okHttpClient!!.newBuilder()
            .connectTimeout(40000, TimeUnit.MILLISECONDS)
            .build()
        val calls = okHttpClient!!.newCall(request)
        calls.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                backgroundThreadShortToast(
                    applicationContext,
                    e.message
                )
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                idCallback.lastId(response.body()!!.string())
            }
        })
    }

    @Throws(IOException::class)
    fun POST(url: String, body: RequestBody?): String {
        println("URL_FEED: $url")
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val response = okHttpClient!!.newCall(request).execute()
        return response.body()!!.string()
    }

    companion object {
        @get:Synchronized
        var instance: AppController? = null
            private set

        fun backgroundThreadShortToast(
            context: Context?,
            msg: String?
        ) {
            if (context != null && msg != null) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


}