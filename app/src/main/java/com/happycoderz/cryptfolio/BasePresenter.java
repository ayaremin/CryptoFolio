package com.happycoderz.cryptfolio;

import android.content.Context;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EminAyar on 17.10.2017.
 */

public class BasePresenter<View extends MvpView> extends MvpPresenter<View> {

  public Context context;
  public Retrofit retrofit, retrofitGraphApi, retrofitICOApi;
  public CacheHelper cacheHelper;

  public void start() {
    cacheHelper = CacheHelper.getInstance(context);
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setLenient();
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient.Builder b = new OkHttpClient.Builder();
    b.readTimeout(40000, TimeUnit.MILLISECONDS);
    b.writeTimeout(60000, TimeUnit.MILLISECONDS);
    b.addInterceptor(interceptor);
    b.addInterceptor(new Interceptor() {
      @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder =
            original.newBuilder().header("Content-Type", "application/json");
        Request request = requestBuilder.build();
        return chain.proceed(request);
      }
    });

    OkHttpClient okHttpClient = b.build();

    retrofit = new Retrofit.Builder().addConverterFactory(
        GsonConverterFactory.create(gsonBuilder.create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .build();

    retrofitGraphApi = new Retrofit.Builder().addConverterFactory(
        GsonConverterFactory.create(gsonBuilder.create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BuildConfig.GRAPH_BASE_URL)
        .client(okHttpClient)
        .build();

    retrofitICOApi = new Retrofit.Builder().addConverterFactory(
        GsonConverterFactory.create(gsonBuilder.create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BuildConfig.ICO_BASE_URL)
        .client(okHttpClient)
        .build();
  }

  public void setContext(Context ctx) {
    this.context = ctx;
  }
}
