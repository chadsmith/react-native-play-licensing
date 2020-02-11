package com.github.chadsmith.playlicensing;

import android.content.Context;
import android.provider.Settings.Secure;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;

public class PlayLicensingModule extends ReactContextBaseJavaModule {

  public static final String REACT_CLASS = "PlayLicensing";


  @Override
  public String getName() {
    return REACT_CLASS;
  }

  public PlayLicensingModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @ReactMethod
  public void verify(String base64PublicKey, String salt, final Promise promise) {
    Context context = getReactApplicationContext();

    // Try to use more data here. ANDROID_ID is a single point of attack.
    String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

    final LicenseChecker checker = new LicenseChecker(
            context, new ServerManagedPolicy(context,
            new AESObfuscator(salt.getBytes(), context.getPackageName(), deviceId)),
            base64PublicKey);
    checker.checkAccess(new LicenseCheckerCallback() {
      @Override
      public void allow(int reason) {
          promise.resolve(true);
          checker.onDestroy();
      }

      @Override
      public void dontAllow(int reason) {
        if(reason == Policy.RETRY) {
            promise.resolve(null);
        }
        else {
            promise.resolve(false);
        }
        checker.onDestroy();
      }

      @Override
      public void applicationError(int errorCode) {
          promise.reject("code: " + errorCode, "license check failed");
          checker.onDestroy();
      }
    });
  }

}
