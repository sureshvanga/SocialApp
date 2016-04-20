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
 * Created by vanga.sureshkumar on 4/19/2016.
 */
public class LastFivePosts extends Activity {
    private String TAG = "LastFivePosts";
    private ListView listViewstores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fbposts);

        setUpview();

        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("jsonarray");// receiving data from  FacebookActivities.java


        ArrayList<String> stories_array = new  ArrayList<String>();
        stories_array.clear();
        try {
            JSONArray postarray_obj = new JSONArray(jsonArray);
            System.out.println(postarray_obj.toString(2));
            for (int l = 0; l < postarray_obj.length(); l++) {
                JSONObject story_obj = postarray_obj.getJSONObject(l);
                Log.e(TAG, "=-=--stories--=-=" + story_obj.getString("story"));
                stories_array.add(story_obj.getString("story"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stories_array);
        listViewstores.setAdapter(adapter);

    }

    /*
           Method: setupView
           Desc: This method Initialize the resource UI elements..
           Params: None
       */
    private void setUpview() {
        listViewstores = (ListView) findViewById(R.id.postlistView);
    }
}
