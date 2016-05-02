package com.socialapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Iterator;

/*
  Created by V.Suresh
  Dated 05-02-2016

 */
public class HomeFragment extends Fragment {
    Typeface typeface;
    Context con;
    TextView username_tv, lastname_tv, gender_tv, emailid_tv, education_tv, username_title, lastname_title, gender_title, emailid_title, educationtitle;
    private String TAG = "HomeFragment";
    ProgressDialog progress;
    private JSONArray postarray_obj;
    ArrayList<String> stories_array = new ArrayList<String>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        con = this.getActivity();
        typeface = Typeface.createFromAsset(con.getAssets(), "fonts/Ubuntu-Light.ttf");

        progress = new ProgressDialog(con);
        progress.setMessage(getResources().getString(R.string.please_wait_facebooklogin));
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        FacebookSdk.sdkInitialize(con);
        AppEventsLogger.activateApp(con);

        //Initializing the UI
        username_title = (TextView) rootView.findViewById(R.id.username_title);
        lastname_title = (TextView) rootView.findViewById(R.id.lastname_title);
        gender_title = (TextView) rootView.findViewById(R.id.gender_title);
        emailid_title = (TextView) rootView.findViewById(R.id.emailid_title);
        educationtitle = (TextView) rootView.findViewById(R.id.educationtitle);
        username_tv = (TextView) rootView.findViewById(R.id.username);
        lastname_tv = (TextView) rootView.findViewById(R.id.lastname);
        gender_tv = (TextView) rootView.findViewById(R.id.gender);
        emailid_tv = (TextView) rootView.findViewById(R.id.emailid);
        education_tv = (TextView) rootView.findViewById(R.id.education);
        username_title.setTypeface(typeface);
        lastname_title.setTypeface(typeface);
        gender_title.setTypeface(typeface);
        emailid_title.setTypeface(typeface);
        educationtitle.setTypeface(typeface);
        username_tv.setTypeface(typeface);
        lastname_tv.setTypeface(typeface);
        gender_tv.setTypeface(typeface);
        emailid_tv.setTypeface(typeface);
        education_tv.setTypeface(typeface);
        progress.show();
        getProfileInformation();
        // Inflate the layout for this fragment
        return rootView;
    }

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
                        if (res != null) {
                            Log.e(TAG, "=-=--Res--=-=" + res.toString());
                            try {
                                String email = res.getString("email");
                                String firstname = res.getString("first_name");
                                String lastname = res.getString("last_name");
                                String gender = res.getString("gender");
                                username_tv.setText(firstname);
                                lastname_tv.setText(lastname);
                                gender_tv.setText(gender);
                                emailid_tv.setText(email);
                                progress.dismiss();
                                JSONArray data = response.getJSONObject().optJSONArray("education");
                                Log.e(TAG, "=-=--data--=-=" + data.length());
                                ArrayList<String> education_array = new ArrayList<String>();
                                education_array.clear();
                                if (data != null) {
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
                                    StringBuilder builder = new StringBuilder();
                                    for (String details : education_array) {
                                        builder.append(details + "\n");
                                    }
                                    education_tv.setText(builder.toString());
                                }


                                stories_array = new ArrayList<String>();
                                stories_array.clear();
                                postarray_obj = new JSONArray();
                                if (data != null) {
                                    JSONObject posts_obj = res.getJSONObject("posts");

                                    if (posts_obj != null) {
                                        postarray_obj = posts_obj.getJSONArray("data");

                                        for (int l = 0; l < postarray_obj.length(); l++) {
                                            JSONObject story_obj = postarray_obj.getJSONObject(l);

                                            Iterator iterator = story_obj.keys();
                                            while (iterator.hasNext()) {
                                                String key = (String) iterator.next();

                                                if (key.contains("message")) {
                                                    Log.e(TAG, "=-=--key values--=-=" + story_obj.getString("message"));
                                                    stories_array.add(story_obj.getString("message"));
                                                }


                                            }

                                        }
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,gender,last_name,locale,name,age_range,friends, education, posts,groups");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
