package com.happycoderz.cryptfolio.jobs;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import co.chatsdk.ui.main.MainActivity;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.happycoderz.cryptfolio.BuildConfig;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.api.RestService;
import com.happycoderz.cryptfolio.dashboard.DashboardActivity;
import com.happycoderz.cryptfolio.models.Alert;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Portfolio;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoinIntervalJob extends Job {

  static final String TAG = "show_notification_job_tag";

  @NonNull @Override protected Result onRunJob(Params params) {
    Portfolio portfolio = CacheHelper.getInstance(getContext()).getPortfolio();

    Realm realm = Realm.getDefaultInstance();

    final RealmResults<Alert> alerts = realm.where(Alert.class).findAll();

    if (alerts.isEmpty()) {
      schedulePeriodic();
      return Result.SUCCESS;
    }

    retrofit().create(RestService.class)
        .getCoins(portfolio.getCurrency())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .unsubscribeOn(Schedulers.io())
        .subscribe(new Observer<List<Coin>>() {
          @Override public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

          }

          @Override public void onNext(@io.reactivex.annotations.NonNull List<Coin> coins) {
            Realm realm = Realm.getDefaultInstance();
            for (Coin coin : coins) {
              if (!realm.where(Alert.class)
                  .equalTo("symbol", coin.getSymbol())
                  .equalTo("stopped", false)
                  .findAll()
                  .isEmpty()) {
                checkCoin(coin);
              }
            }
          }

          @Override public void onError(@io.reactivex.annotations.NonNull Throwable e) {

          }

          @Override public void onComplete() {

          }
        });

    schedulePeriodic();
    return Result.SUCCESS;
  }

  private void checkCoin(Coin coin) {
    Realm realm = Realm.getDefaultInstance();
    final RealmResults<Alert> alerts = realm.where(Alert.class).equalTo("symbol", coin.getSymbol()).equalTo("stopped", false)
        .findAll();

    for (final Alert alert : alerts) {
      if (coin.getPriceUSDAsDouble() <= alert.getLessThan()
          || coin.getPriceUSDAsDouble() >= alert.getGreatThan()) {
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
            new Intent(getContext(), DashboardActivity.class), 0);

        Answers.getInstance()
            .logContentView(new ContentViewEvent().putContentName("Notification")
                .putContentType("Notification")
                .putCustomAttribute("coinName", coin.getSymbol()));

        Notification notification = new NotificationCompat.Builder(getContext()).setContentTitle(
            getContext().getString(R.string.label_price_notification_title, coin.getName()))
            .setContentText(
                getContext().getString(R.string.label_price_notification_desc, coin.getPriceUSD()))
            .setAutoCancel(true)
            .setContentIntent(pi)
            .setLargeIcon(
                BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setShowWhen(true)
            .setColor(ContextCompat.getColor(getContext(), R.color.purple))
            .setLocalOnly(true)
            .build();

        NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), notification);

        realm.executeTransaction(new Realm.Transaction() {
          @Override public void execute(Realm realm) {
            alert.setStopped(true);
          }
        });
      }
    }
  }

  public static void schedulePeriodic() {
    new JobRequest.Builder(CoinIntervalJob.TAG).setExact(TimeUnit.MINUTES.toMillis(5))
        .setUpdateCurrent(true)
        .setPersisted(true)
        .build()
        .schedule();
  }

  public class CoinService extends IntentService {

    public CoinService(String name) {
      super(name);
    }

    @Override protected void onHandleIntent(@Nullable Intent intent) {

    }
  }

  private Retrofit retrofit() {
    Retrofit retrofit;
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setLenient();
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
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

    return retrofit;
  }
}