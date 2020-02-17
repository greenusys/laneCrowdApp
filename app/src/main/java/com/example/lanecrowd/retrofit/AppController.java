package com.example.lanecrowd.retrofit;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;



import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppController extends Application
{
    private OkHttpClient okHttpClient;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        okHttpClient = new OkHttpClient.Builder().
                connectTimeout(100, TimeUnit.SECONDS).
                writeTimeout(100, TimeUnit.SECONDS).
                readTimeout(100, TimeUnit.SECONDS).
                build();
        mInstance = this;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static synchronized AppController getInstance()
    {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener)
    {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void GetTest(String Url, final GetLastIdCallback idCallback) throws IOException {
        final Request request = new Request.Builder()
                .url(Url)
                .get()
                .build();

        Call calls = getOkHttpClient().newCall(request);
        calls.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                backgroundThreadShortToast(getApplicationContext(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                idCallback.lastId(response.body().string());
            }
        });

    }

    public void PostTest(String Url, RequestBody requestBody, final GetLastIdCallback idCallback) throws IOException {
        Request request = new Request.Builder()
                .url(Url)
                .post(requestBody)
                .build();

        okHttpClient.newBuilder()
                .connectTimeout(40000, TimeUnit.MILLISECONDS)
                .build();
        Call calls = getOkHttpClient().newCall(request);

        calls.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                backgroundThreadShortToast(getApplicationContext(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                idCallback.lastId(response.body().string());
            }
        });


    }

    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




    public String POST(String url, RequestBody body) throws IOException
    {
        System.out.println("URL_FEED: "+url);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = getOkHttpClient().newCall(request).execute();
        return response.body().string();
    }


}