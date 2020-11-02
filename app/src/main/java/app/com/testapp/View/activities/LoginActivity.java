package app.com.testapp.View.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.com.testapp.Background.ConnectivityChangeReceiver;
import app.com.testapp.Model.utils.CommonUtils;
import app.com.testapp.Model.utils.Constants;
import app.com.testapp.Model.utils.Prefs;
import app.com.testapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.logo)
    ImageView logo;

    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.btn_fb_login)
    LoginButton loginButton;

    @BindView(R.id.btn_gplus_signin)
    SignInButton signInButton;

    @BindView(R.id.mb_signin_fb)
    Button btnFacebook;

    @BindView(R.id.mb_signin_google)
    Button btnGoogle;

    private static final int RC_SIGN_IN = 430;
    public static GoogleApiClient mGoogleApiClient;
    public CallbackManager callbackManager;
    public String id, name, email;
    private boolean isApiClientConnected;
    private GoogleSignInClient mGoogleSignInClient;
    private ConnectivityChangeReceiver connectivityChangeReceiver;

    static LoginActivity newLoginActivityInstance;
    String accessToken;
    private boolean isFromOnActivityResult;

    public static LoginActivity getLoginInstance() {
        return newLoginActivityInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Prefs.initPrefs(LoginActivity.this);
        setContentView(R.layout.activity_login);
        newLoginActivityInstance = this;
        connectivityChangeReceiver = new ConnectivityChangeReceiver();
        ButterKnife.bind(this);
        setClickListeners();
        init();
        registerNetworkBroadcastForNoughat();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.packagename",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
        }
        catch (NoSuchAlgorithmException e) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!Prefs.getBoolean(Constants.IS_FIRST_TIME, true)) {
            if (isLoggedInWithFacebook() && !isFromOnActivityResult) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                getOptionalPendingResult();
            }

        } else {
            Prefs.putBoolean(Constants.IS_FIRST_TIME, false);
        }


    }

    @Override
    public void onClick(View view) {
        if (view == btnFacebook) {
            if (CommonUtils.checkInternetConnection(LoginActivity.this)) {
                loginButton.performClick();
                loginWithFacebook();
            } else {
                CommonUtils.showNoInternet(LoginActivity.this);
            }
        } else if (view == btnGoogle) {
            if (CommonUtils.checkInternetConnection(LoginActivity.this)) {
                signInButton.performClick();
                loginWithGoogle();
            } else {
                CommonUtils.showNoInternet(LoginActivity.this);
            }
        }
    }


    private void init() {
        callbackManager = CallbackManager.Factory.create();
        initGooglePlusSettings();
    }

    private void setClickListeners() {
        btnFacebook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
    }

    private boolean isLoggedInWithFacebook() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    private void loginWithFacebook() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken().getToken();
                Log.i("AccessToken ::", accessToken);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity",
                                        response.toString());
                                try {
                                    id = object.getString("id");
                                    name = object.getString("name");
                                    URL profilePic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                                    Log.i("profilePic",
                                            profilePic + "");
                                    Bundle profileBundle = new Bundle();
                                    profileBundle.putString("profile_pic", profilePic + "");
                                    profileBundle.putString("profile_name", name);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                            .putExtras(profileBundle)
                                            .putExtra(Constants.IS_FROM_GPLUS, false)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle params = new Bundle();
                params.putString("fields", "id, name, email");
                request.setParameters(params);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                CommonUtils.showToast(LoginActivity.this, "Signin Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                CommonUtils.showToast(LoginActivity.this, "Error: " + error.getMessage());
            }
        });
    }

    public void getOptionalPendingResult() {
        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (optionalPendingResult.isDone()) {
            Log.d("LoginActivity", "Got cached sign-in");
            GoogleSignInResult result = optionalPendingResult.get();
            handleGooglePlusSignInResult(result);
            Log.d("goingtohome::", "going to home from optionalPendingResult" + "");
        } else {
            CommonUtils.showProgress(LoginActivity.this, "Checking Credentials..", false);
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    CommonUtils.hideProgress();
                    handleGooglePlusSignInResult(result);
                    Log.d("goingtohome::", "going to home from optionalPendingResult" + "else");
                }
            });
        }
    }

    private void loginWithGoogle() {
        googlePlusSignIn();
    }

    private void initGooglePlusSettings() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_oauth_client_id))
                .requestEmail()
                .requestProfile()
                .requestServerAuthCode(getString(R.string.google_oauth_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Plus.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        isApiClientConnected = true;
                        Log.d("isApiClientConnected::", isApiClientConnected + "");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
        signInButton.setScopes(googleSignInOptions.getScopeArray());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("LoginActivity", "ConnectionFailed:" + connectionResult);
    }

    private void googlePlusSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void googlePlusSignOut() {
        if (mGoogleApiClient != null) {
            Log.d("Signout::", "Signout is happening");
            /*mGoogleApiClient.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    mGoogleApiClient.disconnect();
                    Log.d("Signout::", "Signout is over");

                }
            });*/
            /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            //Update UI here or go back to login page.
                            mGoogleApiClient.disconnect();
                        }
                    }
            );*/
            mGoogleSignInClient.signOut().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mGoogleApiClient.disconnect();
                }
            });

        }


    }

    private void handleGooglePlusSignInResult(GoogleSignInResult result) {
        Log.d("gplusSignInResult::", result.isSuccess() + "");
        if (result.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            String personName = googleSignInAccount.getDisplayName();
            String personPhotoUrl = googleSignInAccount.getPhotoUrl().toString();
            String email = googleSignInAccount.getEmail();
            String familyName = googleSignInAccount.getFamilyName();
            Bundle profileBundle = new Bundle();
            profileBundle.putString("person_name", personName);
            profileBundle.putString("person_photo_url", personPhotoUrl);
            profileBundle.putString("email", email);
            profileBundle.putString("family_name", familyName);
            Log.e("gPlusSignInDetails::", "Name: " + personName + ", email: " + email + ", Image: " + personPhotoUrl + ", Family Name: " + familyName);
            if (mGoogleApiClient.isConnected()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class)
                        .putExtras(profileBundle)
                        .putExtra(Constants.IS_FROM_GPLUS, true)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else {
            if (result.getStatus().isCanceled()) {
                CommonUtils.showToast(LoginActivity.this, "Login Cancelled.");
            } else if (result.getStatus().isInterrupted()) {
                CommonUtils.showToast(LoginActivity.this, result.getStatus().getStatusMessage() + "");
            } else {
                //CommonUtils.showToast(LoginActivity.this, result.getStatus().getStatusMessage() + "");
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGooglePlusSignInResult(googleSignInResult);
            Log.d("goingtohome::", "going to home from onActivityResult" + "");
            int statusCode = googleSignInResult.getStatus().getStatusCode();
            Log.d("onActivityResult::", statusCode + "");
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            //to prevent going to home from on resume twice
            isFromOnActivityResult = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("googleApiClientOnSt::", mGoogleApiClient.isConnected() + "");
    }

    private void registerNetworkBroadcastForNoughat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(connectivityChangeReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            Log.d("googleApiClientOnStop::", mGoogleApiClient.isConnected() + "");
        }
    }

    @Override
    public void onDestroy() {
        unregisterNetworkChanges();
        super.onDestroy();
    }

    public void facebookSignOut() {
        LoginManager.getInstance().logOut();
    }

}
