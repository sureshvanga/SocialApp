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
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import adapter.ActionBarTitleSetter;

public class GroupsFragment extends Fragment {
    Typeface typeface;
    private ListView listViewgroups;
    private Context con;
    private String TAG = "FriendsFragment";
    ProgressDialog progress;
    int grouplistcount;

    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.facebook_groups, container, false);
        con = this.getActivity();
        typeface = Typeface.createFromAsset(con.getAssets(), "fonts/Ubuntu-Light.ttf");
        listViewgroups = (ListView) rootView.findViewById(R.id.grouplistView);
        progress = new ProgressDialog(con);
        progress.setMessage(getResources().getString(R.string.please_wait_facebooklogin));
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        FacebookSdk.sdkInitialize(con);
        AppEventsLogger.activateApp(con);
        progress.show();
        getFacebookGroups();
        // Inflate the layout for this fragment
        return rootView;
    }

    private void getFacebookGroups() {
        Bundle param = new Bundle();
        param.putString("fields", "name");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/groups",
                param,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject res = response.getJSONObject();

                        if (res != null) {
                            progress.dismiss();
                            Log.e(TAG, "=-GROUP=-=-" + res.toString());

                            JSONArray data = response.getJSONObject().optJSONArray("data");

                            Log.e(TAG, "=-data length=-=-" + data.length());
                            ArrayList<String> groups_array = new ArrayList<String>();
                            grouplistcount = data.length();

                            groups_array.clear();
                            try {
                                if (data.length() != 0) {
                                    for (int l = 0; l < data.length(); l++) {
                                        JSONObject story_obj = data.getJSONObject(l);
                                        Log.e(TAG, "=-=--groups--=-=" + story_obj.getString("name"));
                                        groups_array.add(story_obj.getString("name"));
                                        ((ActionBarTitleSetter) getActivity()).setTitle(getString(R.string.title_groups) + "-" + grouplistcount);

                                    }
                                    ArrayAdapter adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1, groups_array);
                                    listViewgroups.setAdapter(adapter);

                                } else {
                                    AlertDialog("You don't have a required permission.");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                AlertDialog("You don't have a required permission.");
                            }


                        } else {
                            AlertDialog("You don't have a required  permission.");

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


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
}
