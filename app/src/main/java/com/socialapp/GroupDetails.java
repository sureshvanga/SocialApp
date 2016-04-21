package com.socialapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vanga.sureshkumar on 4/21/2016.
 */
public class GroupDetails extends Activity {
    private ListView listViewgroups;
    private String TAG = "GroupDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_groups);

        setUpview();


        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("jsonarray");// receiving data from  FacebookActivities.java


        ArrayList<String> groups_array = new  ArrayList<String>();
        groups_array.clear();
        try {
            JSONArray postarray_obj = new JSONArray(jsonArray);
            System.out.println(postarray_obj.toString(2));
            for (int l = 0; l < postarray_obj.length(); l++) {
                JSONObject story_obj = postarray_obj.getJSONObject(l);
                Log.e(TAG, "=-=--groups--=-=" + story_obj.getString("name"));
                groups_array.add(story_obj.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groups_array);
        listViewgroups.setAdapter(adapter);
    }

    private void setUpview() {
        listViewgroups = (ListView) findViewById(R.id.grouplistView);
    }
}
