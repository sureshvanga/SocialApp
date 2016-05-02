package com.socialapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Iterator;

import adapter.ActionBarTitleSetter;


public class PostsFragment extends Fragment {
    int postslistcount;
    private ListView listViewstores;
    private Context con;
    private String TAG = "PostsFragment";
    ProgressDialog progress;
    private JSONArray postarray_obj;
    ArrayList<String> stories_array = new ArrayList<String>();

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        con = this.getActivity();
        listViewstores = (ListView) rootView.findViewById(R.id.postlistView);
        progress = new ProgressDialog(con);
        progress.setMessage(getResources().getString(R.string.please_wait_facebooklogin));
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        FacebookSdk.sdkInitialize(con);
        AppEventsLogger.activateApp(con);
        progress.show();
        getPostsFromFacebook();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void getPostsFromFacebook() {

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
                            progress.dismiss();
                            try {
                                stories_array = new ArrayList<String>();
                                stories_array.clear();
                                postarray_obj = new JSONArray();
                                JSONObject posts_obj = res.getJSONObject("posts");

                                if (posts_obj != null) {
                                    postarray_obj = posts_obj.getJSONArray("data");

                                    for (int l = 0; l < postarray_obj.length(); l++) {
                                        JSONObject story_obj = postarray_obj.getJSONObject(l);
                                        postslistcount = story_obj.length();
                                      //  ((ActionBarTitleSetter) getActivity()).setTitle(getString(R.string.title_messages) + "-" + postslistcount);
                                        Iterator iterator = story_obj.keys();
                                        while (iterator.hasNext()) {
                                            String key = (String) iterator.next();

                                            if (key.contains("message")) {
                                                Log.e(TAG, "=-=--key values--=-=" + story_obj.getString("message"));
                                                stories_array.add(story_obj.getString("message"));
                                            }


                                        }

                                    }
                                    ArrayAdapter adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1, stories_array);

                                    listViewstores.setAdapter(adapter);
                                } else {
                                    AlertDialog("You don't have facebook POST permission.");
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                AlertDialog("You don't have facebook POST permission.");
                            }

                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "locale,name,posts");
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
