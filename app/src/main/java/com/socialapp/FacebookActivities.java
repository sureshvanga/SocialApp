package com.socialapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vanga.sureshkumar on 4/19/2016.
 */
public class FacebookActivities extends Activity implements View.OnClickListener{
    private Button viewProfile_btn, viewPosts_btn, viewgroup_btn;
    private TextView username_tv, education_tv;
    private String TAG = "FacebookActivities ";
    ProgressDialog progress;
    private JSONArray postarray_obj;
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

                                username_tv.setText("FirstName: " + firstname + "\n" + "LastName:" + lastname + "\n" + "Gender:" + gender + "\n" + "Email Id:" + email);
                                JSONArray data = response.getJSONObject().optJSONArray("education");
                                Log.e(TAG, "=-=--data--=-=" + data.length());


                                ArrayList<String> education_array = new  ArrayList<String>();
                                education_array.clear();
                                for (int k = 0; k < data.length(); k++) {
                                    try {
                                        JSONObject edu_obj = data.getJSONObject(k);

                                        JSONObject school_obj = edu_obj.getJSONObject("school");
                                        Log.e(TAG, "=-=--school names--=-=" + school_obj.getString("name"));
                                        education_array.add(school_obj.getString("name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                ArrayList<String> stories_array = new  ArrayList<String>();
                                stories_array.clear();
                                postarray_obj = new JSONArray();

                                JSONObject posts_obj = res.getJSONObject("posts");
                                 postarray_obj = posts_obj.getJSONArray("data");

                                for (int l = 0; l < postarray_obj.length(); l++) {
                                    JSONObject story_obj = postarray_obj.getJSONObject(l);
                                    Log.e(TAG, "=-=--stories--=-=" + story_obj.getString("story"));
                                    stories_array.add(story_obj.getString("story"));
                                }




                                StringBuilder builder = new StringBuilder();
                                for (String details : education_array) {
                                    builder.append(details + "\n");
                                }

                                education_tv.setText(builder.toString());


                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,gender,last_name,link,locale,name,timezone,updated_time,verified,age_range,friends, education, posts,groups");
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
        education_tv = (TextView)findViewById(R.id.education);
        viewProfile_btn = (Button)findViewById(R.id.viewprofile);
        viewPosts_btn = (Button)findViewById(R.id.lastfivepost);
        viewgroup_btn = (Button)findViewById(R.id.viewgroups);

       viewProfile_btn.setOnClickListener(this);
        viewPosts_btn.setOnClickListener(this);
        viewgroup_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewprofile:
                progress.show();

                final ArrayList<String> fndslist_array = new  ArrayList<String>();
                fndslist_array.clear();

                Bundle params = new Bundle();
                params.putString("limit", "100");
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/invitable_friends",
                        params,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                        /* handle the result */
                                JSONObject res = response.getJSONObject();
                                Log.e(TAG, "=-=--Res--=-=" + res.toString());

                                JSONArray data = response.getJSONObject().optJSONArray("data");
                                Log.e(TAG, "=-=--data length--=-=" + data.length());

                                for (int i = 0; i < data.length(); i++) {
                                    try {
                                        JSONObject fnd_obj = data.getJSONObject(i);
                                        Log.e(TAG, "=-=--Friends names--=-=" + fnd_obj.getString("name"));
                                        fndslist_array.add(fnd_obj.getString("name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                                /* make the Intent call */
                                Intent i = new Intent(FacebookActivities.this, HomeActivity.class);
                                i.putExtra("jsondata", data.toString());
                                progress.dismiss();
                                startActivity(i);


                            }
                        }
                ).executeAsync();


                break;
            case R.id.lastfivepost:
                /* make the Intent call */
                Intent in = new Intent(FacebookActivities.this, LastFivePosts.class);
                in.putExtra("jsonarray", postarray_obj.toString());
                startActivity(in);
                break;
            case R.id.viewgroups:

                break;
        }
    }


}
