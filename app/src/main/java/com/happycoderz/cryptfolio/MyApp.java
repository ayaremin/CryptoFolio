package com.happycoderz.cryptfolio;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.session.Configuration;
import co.chatsdk.firebase.FirebaseModule;
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule;
import co.chatsdk.firebase.push.FirebasePushModule;
import co.chatsdk.ui.manager.UserInterfaceModule;
import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.JobManager;
import com.facebook.appevents.AppEventsLogger;
import com.happycoderz.cryptfolio.jobs.CoinIntervalJob;
import com.happycoderz.cryptfolio.jobs.CoinJobCreator;
import com.happycoderz.cryptfolio.utils.RealmMigrations;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import org.solovyev.android.checkout.Billing;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by EminAyar on 27.11.2017.
 */

public class MyApp extends MultiDexApplication {

  private static MyApp sInstance;
  private final Billing mBilling = new Billing(this, new Billing.DefaultConfiguration() {
    @Override
    public String getPublicKey() {
      return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwQoDe1IRhgtdm571lRSG/4rXSr+3QqAkhqVYLPqYuMfMlLjA7+k+jn9BhwCoJ0vz05BuT6CJuepFgb7ckFSGG6HTkguTuxKvQnu8cB55A8yCVPya8SE8A3pZ76sOs3MTUkCW2wxP5j3vojU023ahMzz5wUmAbxtzpa5TthAPW7YMVhFqsoHb0s033L1nkkQQY9fG+tj4JNqP8bI+gs46o4RqYh7RxTf+xc1FeTZi1ZOJh+tOZwcFsM910C9QA+Ely/8AbqNiQtPsS9J2rnlvmZQkeiMuVjBUpK7hFtpPXwYGakcSqKRCX8l3J642iQ8VB6ezsmeeF5HjPBVs72SgiQIDAQAB";
    }
  });

  @Override public void onCreate() {
    super.onCreate();
    sInstance = this;
    Fabric.with(this, new Crashlytics());
    AppEventsLogger.activateApp(this);

    JobManager.create(this).addJobCreator(new CoinJobCreator());
    CoinIntervalJob.schedulePeriodic();

    Realm.init(this);

    final RealmConfiguration configuration = new RealmConfiguration.Builder().name("sample"
        + ".realm").schemaVersion(3).migration(new RealmMigrations()).build();
    Realm.setDefaultConfiguration(configuration);
    Realm.getInstance(configuration);

    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .setFontAttrId(R.attr.fontPath)
        .build());

    Context context = getApplicationContext();

    // Create a new configuration
    Configuration.Builder builder = new Configuration.Builder(context);

    // Perform any configuration steps (optional)
    builder.firebaseRootPath("prod");
    builder.facebookLoginEnabled(false);
    builder.twitterLoginEnabled(false);
    // Initialize the Chat SDK
    ChatSDK.initialize(builder.build());
    UserInterfaceModule.activate(context);

    // Activate the Firebase module
    FirebaseModule.activate();
    FirebasePushModule.activateForFirebase();
    // File storage is needed for profile image upload and image messages
    FirebaseFileStorageModule.activate();
  }

  public Billing getBilling() {
    return mBilling;
  }

  public static MyApp get() {
    return sInstance;
  }

  @Override
  protected void attachBaseContext (Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }
}