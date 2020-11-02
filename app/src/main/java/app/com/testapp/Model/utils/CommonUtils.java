package app.com.testapp.Model.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;

import app.com.testapp.R;
import app.com.testapp.View.activities.LoginActivity;

public class CommonUtils {

    public static ProgressDialog dialog;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showProgress(Context context, String message, boolean cancellable) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(cancellable);
        dialog.show();
    }

    public static void hideProgress() {
        if(dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    public static void showSnackBar(final Context mContext, String text) {
        final Snackbar snackbar = Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params.setMargins(0, getActionBarHeight(mContext), 0, 0);
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));
        snackbar.setActionTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));
        snackbar.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    public static void showNoInternet(final Context mContext) {
        final Snackbar snackbar = Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), "NO INTERNET CONNECTION", Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params.setMargins(0, getActionBarHeight(mContext), 0, 0);
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));
        snackbar.setActionTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));
        snackbar.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    public static int getActionBarHeight(Context context) {
        int actionBarHeight;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,
                true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, context.getResources().getDisplayMetrics());
        } else {
            actionBarHeight = (int) context.getResources().getDimension(R.dimen._40sdp);
        }
        return actionBarHeight;
    }

    public static boolean checkInternetConnection(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

}
