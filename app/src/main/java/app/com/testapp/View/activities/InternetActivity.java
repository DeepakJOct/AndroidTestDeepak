package app.com.testapp.View.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import app.com.testapp.Background.ConnectivityChangeReceiver;
import app.com.testapp.Model.utils.CommonUtils;
import app.com.testapp.R;

public class InternetActivity extends AppCompatActivity {

    private ConnectivityChangeReceiver receiver = new ConnectivityChangeReceiver();
    boolean isConnected;
    Handler h;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);/*
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                isConnected = CommonUtils.checkInternetConnection(InternetActivity.this);
                Log.d("InternetAct::", "isConneted->" + isConnected);

                if(isConnected) {
                    finish();
                }
            }
        };
        timer = new Timer();
        timer.schedule(t, 0, 1500);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
