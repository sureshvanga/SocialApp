package com.socialapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import android.content.IntentSender.SendIntentException;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;

import model.LoginResponseValues;
/*
  Created by V.Suresh
  Dated 05-02-2016

 */
public class SocialAppActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ConnectionCallbacks, View.OnClickListener {

    private String TAG = "SocialAppActivity";
    Typeface typeface;
    TextView log;
    //For facebook
    private CallbackManager callbackManager;
    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SocialAppActivity sc = this;
    private Button facebook_button;
    ProgressDialog progress;
    private String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    private AccessToken accessToken;
    public static String _accessToken = "";
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    SignInButton signIn_btn;
    private boolean is_signInBtn_clicked;
    private int request_code;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private String personName, personPhotoUrl, email, dob, tagleline, aboutme;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buidNewGoogleApiClient();
        setContentView(R.layout.content_main);
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Ubuntu-Light.ttf");
        custimizeSignBtn();
        log = (TextView) findViewById(R.id.log);
        log.setTypeface(typeface);

        facebook_button = (Button) findViewById(R.id.btn_facebbook);

        progress = new ProgressDialog(SocialAppActivity.this);
        progress.setMessage(getResources().getString(R.string.please_wait_facebooklogin));
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        //Initializing the Facebook sdk....
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        //register callback object for facebook result
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progress.show();
                Intent i = new Intent(SocialAppActivity.this, MainActivity.class);
                LoginResponseValues.getInstance().setIsGmail(0);
                progress.dismiss();
                startActivity(i);

            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                // App code
                Log.v("LoginActivity", error.getCause().toString());
            }
        });

        //facebook button click
        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SocialAppActivity.this, Arrays.asList("public_profile", "user_friends", "email"));

            }
        });


    }


    /*Customize sign-in button. The sign-in button can be displayed in
  multiple sizes and color schemes. It can also be contextually
  rendered based on the requested scopes. For example. a red button may
  be displayed when Google+ scopes are requested, but a white button
  may be displayed when only basic profile is requested. Try adding the
  Plus.SCOPE_PLUS_LOGIN scope to see the  difference.
*/
    private void custimizeSignBtn() {

        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn_btn.setSize(SignInButton.SIZE_STANDARD);
        signIn_btn.setScopes(new Scope[]{Plus.SCOPE_PLUS_LOGIN});

        signIn_btn.setOnClickListener(this);

    }

    protected void onStart() {
        super.onStart();
        google_api_client.connect();
    }

    protected void onStop() {
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    protected void onResume() {
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }

    /*
   create and  initialize GoogleApiClient object to use Google Plus Api.
   While initializing the GoogleApiClient object, request the Plus.SCOPE_PLUS_LOGIN scope.
   */
    private void buidNewGoogleApiClient() {
        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    //for facebook callback result.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (resultCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                progress.dismiss();

            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout
        getProfileInfo();

    }

    @Override
    public void onConnectionSuspended(int i) {
        google_api_client.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(this, result.getErrorCode(), request_code).show();
            return;
        }

        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }
    }

    /**
     * Sign-in into the Google + account
     */
    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            progress.show();
            resolveSignInError();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                gPlusSignIn();
                break;

        }
    }


     /*
      Method to resolve any signin errors
     */

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            } catch (Exception e1) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    /*
      Sign-out from Google+ account
     */

    public void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();


            Intent i = new Intent(SocialAppActivity.this, MainActivity.class);
            LoginResponseValues.getInstance().setIsGmail(1);
            LoginResponseValues.getInstance().setName(personName);
            LoginResponseValues.getInstance().setEmailId(email);
            LoginResponseValues.getInstance().setDob(dob);
            LoginResponseValues.getInstance().setToggleLine(tagleline);
            LoginResponseValues.getInstance().setAboutme(aboutme);
            LoginResponseValues.getInstance().setPhotoUrl(personPhotoUrl);
            startActivity(i);
        }
    }

    /*
     Revoking access from Google+ account
     */

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d(TAG, "User access revoked!");
                            buidNewGoogleApiClient();
                            google_api_client.connect();
                        }

                    });
        }
    }

    private void setPersonalInfo(Person currentPerson) {

        personName = currentPerson.getDisplayName();
        personPhotoUrl = currentPerson.getImage().getUrl();
        email = Plus.AccountApi.getAccountName(google_api_client);


        dob = currentPerson.getBirthday();
        tagleline = currentPerson.getTagline();
        aboutme = currentPerson.getAboutMe();

        progress.dismiss();
        gPlusSignOut();

    }

    /*
     get user's information name, email, profile pic,Date of birth,tag line and about me
     */

    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                setPersonalInfo(currentPerson);

            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        //Exit When Back and Set no History
        finish();
        System.exit(0);

    }
}


