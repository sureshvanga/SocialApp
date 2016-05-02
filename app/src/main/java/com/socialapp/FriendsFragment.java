package com.socialapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import adapter.ActionBarTitleSetter;


public class FriendsFragment extends Fragment {

    private ListView listView;
    private Context con;
    private String TAG = "FriendsFragment";
    ProgressDialog progress;
    int gfriendslistcount;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        con = this.getActivity();

        listView = (ListView) rootView.findViewById(R.id.listView);

        progress = new ProgressDialog(con);
        progress.setMessage(getResources().getString(R.string.please_wait_facebooklogin));
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        FacebookSdk.sdkInitialize(con);
        AppEventsLogger.activateApp(con);
        progress.show();

        getFacebookFriends();
        // Inflate the layout for this fragment
        return rootView;
    }

    private void getFacebookFriends() {
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

                        JSONArray friendslist = response.getJSONObject().optJSONArray("data");
                        Log.e(TAG, "=-=--data length--=-=" + friendslist.length());
                        gfriendslistcount = friendslist.length();
                        ((ActionBarTitleSetter) getActivity()).setTitle(getString(R.string.title_friends) + "-" + gfriendslistcount);
                        progress.dismiss();
                        ArrayList<String> friends = new ArrayList<String>();
                        try {
                            if (friendslist.length() > 0 || friendslist != null) {
                                for (int l = 0; l < friendslist.length(); l++) {
                                    friends.add(friendslist.getJSONObject(l).getString("name"));
                                }
                                Collections.sort(friends, String.CASE_INSENSITIVE_ORDER);
                                ArrayAdapter adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1, friends);

                                listView.setAdapter(adapter);


                            } else {
                                AlertDialog("You don't have a required permission.");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            AlertDialog("You don't have a required permission.");
                        }

                    }
                }
        ).executeAsync();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void AlertDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                con).create();

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

}
