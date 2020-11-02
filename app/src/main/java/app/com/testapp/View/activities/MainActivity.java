package app.com.testapp.View.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import app.com.testapp.Model.utils.CommonUtils;
import app.com.testapp.Model.utils.Constants;
import app.com.testapp.R;
import app.com.testapp.View.fragments.ImageFragment;
import app.com.testapp.View.fragments.NotificationFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ll_notif)
    LinearLayout llNotif;

    @BindView(R.id.ll_images)
    LinearLayout llImages;

    @BindView(R.id.ll_json)
    LinearLayout llJson;

    String personName, email, photoUrl, familyName;
    boolean isFromGplus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        isFromGplus = getIntent().getBooleanExtra(Constants.IS_FROM_GPLUS, false);
        if (isFromGplus) {
            Bundle profileBundle = getIntent().getExtras();
            if (profileBundle != null) {
                personName = profileBundle.getString("person_name");
                photoUrl = profileBundle.getString("person_photo_url");
                email = profileBundle.getString("email");
                familyName = profileBundle.getString("family_name");
                Log.e("gPlusSignInDetails::", "Name: " + personName + ", email: " + email + ", Image: " + photoUrl + ", Family Name: " + familyName);
            }
        } else {
            Bundle profileBundle = getIntent().getExtras();
            if (profileBundle != null) {
                personName = profileBundle.getString("profile_name");
                photoUrl = profileBundle.getString("profile_pic");
                Log.e("facebookSigniin::", "Name: " + personName + ", Image: " + photoUrl);
            }
        }
        Log.d("googleApiClientMA::", LoginActivity.mGoogleApiClient.isConnected() + "");
        setClickListeners();


    }


    private void setClickListeners() {
        llNotif.setOnClickListener(this);
        llImages.setOnClickListener(this);
        llJson.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == llNotif) {
            Intent viewTaskIntent = new Intent(MainActivity.this, ViewTaskActivity.class);
            viewTaskIntent.putExtra(Constants.FRAGMENT_CODE, 0001);
            startActivity(viewTaskIntent);
        } else if (view == llImages) {
            Intent viewTaskIntent = new Intent(MainActivity.this, ViewTaskActivity.class);
            viewTaskIntent.putExtra(Constants.FRAGMENT_CODE, 0002);
            startActivity(viewTaskIntent);
        } else if (view == llJson) {
            Intent viewTaskIntent = new Intent(MainActivity.this, ViewTaskActivity.class);
            viewTaskIntent.putExtra(Constants.FRAGMENT_CODE, 0003);
            startActivity(viewTaskIntent);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.exit) {
            signOutDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }

    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Message");
        builder.setMessage("You are about to close the app. Click 'Yes' to exit.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //signout
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    private void signOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Message");
        builder.setMessage("Do you really want to signout??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //signout
                if (isFromGplus) {
                    googlePlusSignOut();
                } else {
                    facebookSignOut();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    private void facebookSignOut() {
        if (CommonUtils.checkInternetConnection(MainActivity.this)) {
            LoginActivity.getLoginInstance().facebookSignOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            CommonUtils.showNoInternet(MainActivity.this);
        }


    }

    private void googlePlusSignOut() {
        if (CommonUtils.checkInternetConnection(MainActivity.this)) {
            LoginActivity.getLoginInstance().googlePlusSignOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            CommonUtils.showNoInternet(MainActivity.this);
        }
    }

}
