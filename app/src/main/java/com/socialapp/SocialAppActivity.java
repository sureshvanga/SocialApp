package com.socialapp;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SocialAppActivity extends AppCompatActivity {

    private String TAG = "SocialAppActivity";
    //For facebook
    private CallbackManager callbackManager;

    private Button facebook_button;
    ProgressDialog progress;
    private String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

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
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    facebook_id = profile.getId();
                    f_name = profile.getFirstName();
                    m_name = profile.getMiddleName();
                    l_name = profile.getLastName();
                    full_name = profile.getName();

                    profile_image = profile.getProfilePictureUri(400, 400).toString();

                    Log.e(TAG, "Fb f_name,  l_name=-=-=-=-" + f_name + "\n" + l_name);

                    Intent i = new Intent(SocialAppActivity.this, FacebookActivities.class);
                    progress.dismiss();
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

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

    //for facebook callback result.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}


