package app.com.testapp;

import android.app.Application;
import android.content.Context;

import app.com.testapp.Background.ConnectivityChangeReceiver;
import app.com.testapp.Model.utils.Prefs;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MyApplication.context = getApplicationContext();
        Prefs.initPrefs(this);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    /*public void setConnectivityListener(ConnectivityChangeReceiver.ConnectivityReceiverListener listener) {
        ConnectivityChangeReceiver.connectivityReceiverListener = listener;
    }*/
}
