package com.webcraft.ZagazigApp.utilities;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
//import com.crashlytics.android.Crashlytics;
import com.onesignal.OneSignal;
import com.webcraft.ZagazigApp.activities.SectorActivity;
import com.webcraft.ZagazigApp.helper.MyPreferenceManager;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

//import io.fabric.sdk.android.Fabric;

/**
 * Created by Mahmoud on 2/25/2016.
 */
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;
    private MyPreferenceManager pref;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        Fabric.with(this, new Crashlytics());
//        OneSignal.startInit(this).init();
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .setAutoPromptLocation(true)
                .init();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }
        return pref;
    }
    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        /**
         * Callback to implement in your app to handle when a notification is opened from the Android status bar or
         * a new one comes in while the app is running.
         * This method is located in this Application class as an example, you may have any class you wish implement NotificationOpenedHandler and define this method.
         *
         * @param message        The message string the user seen/should see in the Android status bar.
         * @param additionalData The additionalData key value pair section you entered in on onesignal.com.
         * @param isActive       Was the app in the foreground when the notification was received.
         */
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            String additionalMessage = "";

//            Toast.makeText(getApplicationContext(),"Noti opened",Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(getBaseContext(),SectorActivity.class) ;
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("type", 2);
            getApplicationContext().startActivity(intent);

            try {
                if (additionalData != null) {
                    if (additionalData.has("actionSelected")) {
                        additionalMessage += "Pressed ButtonID: " + additionalData.getString("actionSelected");
                    }

                    additionalMessage = message + "\nFull additionalData:\n" + additionalData.toString();
                }
                Log.i("OneSignalExample", "message:\n" + message + "\nadditionalMessage:\n" + additionalMessage);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
