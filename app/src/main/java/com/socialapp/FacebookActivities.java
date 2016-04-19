package com.socialapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vanga.sureshkumar on 4/19/2016.
 */
public class FacebookActivities extends Activity implements View.OnClickListener{
    private Button viewProfile_btn, viewPosts_btn, viewgroup_btn;
    private TextView username_tv;
    private String TAG = "FacebookActivities ";
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fbactivity);

        progress=new ProgressDialog(FacebookActivities.this);
        progress.setMessage(getResources().getString(R.string.please_wait_facebooklogin));
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        //Initializing the Facebook sdk....
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //Initializing the UI
        setUpView();

        //Get the profileinfo and render it to the UI
        getProfileInformation();

    }

    /*
         Method: GetprofileInformation
         Desc: This method gets the Facebook app profile,Fname,Lastname based on the query we pass
         Params: None
     */
    private void getProfileInformation() {
        GraphRequest graphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, response.getRawResponse());

                        JSONObject res = response.getJSONObject();
                        if(res!=null){
                            Log.e(TAG, "=-=--Res--=-="+res.toString());
                            try {
                                String email = res.getString("email");
                                String firstname = res.getString("first_name");
                                String lastname = res.getString("last_name");
                                String gender = res.getString("gender");

                                username_tv.setText("FirstName: " + firstname + "\n" + "LastName:" + lastname + "\n" + "Gender:" + gender+"\n"+"Email Id:"+email);
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,gender,last_name,link,locale,name,timezone,updated_time,verified,age_range,friends");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    /*
        Method: setupView
        Desc: This method Initialize the resource UI elements..
        Params: None
    */
    private void setUpView() {
        username_tv = (TextView)findViewById(R.id.username);
        viewProfile_btn = (Button)findViewById(R.id.viewprofile);
        viewPosts_btn = (Button)findViewById(R.id.lastfivepost);
        viewgroup_btn = (Button)findViewById(R.id.viewgroups);

        /*viewProfile_btn.setOnClickListener(this);
        viewPosts_btn.setOnClickListener(this);
        viewgroup_btn.setOnClickListener(this);*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.viewprofile:

                break;
            case R.id.lastfivepost:

                break;
            case R.id.viewgroups:
                break;
        }
    }
}
