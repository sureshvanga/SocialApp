package com.socialapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vanga.sureshkumar on 4/18/2016.
 */
public class HomeActivity extends Activity {
    private  ListView listView;
    private String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_friendslist);

        setUpview();

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata"); // receiving data from  FacebookActivities.java

        Log.e(TAG, "===-jsondata-=-=--" + jsondata);


        JSONArray friendslist;
        ArrayList<String> friends = new ArrayList<String>();
        try {
            friendslist = new JSONArray(jsondata);
            for (int l = 0; l < friendslist.length(); l++) {
                friends.add(friendslist.getJSONObject(l).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friends);
        listView.setAdapter(adapter);


    }

    private void setUpview() {
         listView = (ListView) findViewById(R.id.listView);

    }
}
