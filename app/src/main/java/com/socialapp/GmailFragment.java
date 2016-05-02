package com.socialapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import java.io.InputStream;
import java.util.ArrayList;
import model.LoginResponseValues;


public class GmailFragment extends Fragment {
    Typeface typeface;
    Context con;
    private TextView username_tv, lastname_tv, gender_tv, emailid_tv, education_tv;
    private String TAG = "HomeFragment";
    ProgressDialog progress;
    private JSONArray postarray_obj;
    ArrayList<String> stories_array = new ArrayList<String>();
    private TextView name_tv, emailId_tv, dob_tv, tag_line_tv, about_me_tv;
    private static final int PROFILE_PIC_SIZE = 120;

    public GmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gmail_details, container, false);
        con = this.getActivity();
        typeface = Typeface.createFromAsset(con.getAssets(), "fonts/Ubuntu-Light.ttf");
        name_tv = (TextView) rootView.findViewById(R.id.userName);
        emailId_tv = (TextView) rootView.findViewById(R.id.emailId);
        dob_tv = (TextView) rootView.findViewById(R.id.dob);
        tag_line_tv = (TextView) rootView.findViewById(R.id.tag_line);
        about_me_tv = (TextView) rootView.findViewById(R.id.about_me);
        progress = new ProgressDialog(con);
        progress.setMessage(getResources().getString(R.string.please_wait_facebooklogin));
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();
        name_tv.setText(LoginResponseValues.getInstance().getName());
        emailId_tv.setText(LoginResponseValues.getInstance().getEmailId());
        dob_tv.setText(LoginResponseValues.getInstance().getDob());
        tag_line_tv.setText(LoginResponseValues.getInstance().getToggleLine());
        about_me_tv.setText(LoginResponseValues.getInstance().getAboutme());
        name_tv.setTypeface(typeface);
        emailId_tv.setTypeface(typeface);
        dob_tv.setTypeface(typeface);
        tag_line_tv.setTypeface(typeface);
        about_me_tv.setTypeface(typeface);

        String photoUrl = LoginResponseValues.getInstance().getPhotoUrl();
        setProfilePic(photoUrl, rootView);

        progress.dismiss();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void setProfilePic(String profile_pic, View rootView) {
        profile_pic = profile_pic.substring(0,
                profile_pic.length() - 2)
                + PROFILE_PIC_SIZE;
        ImageView user_picture = (ImageView) rootView.findViewById(R.id.profile_pic);
        new LoadProfilePic(user_picture).execute(profile_pic);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class LoadProfilePic extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmap_img;

        public LoadProfilePic(ImageView bitmap_img) {
            this.bitmap_img = bitmap_img;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap new_icon = null;
            try {
                InputStream in_stream = new java.net.URL(url).openStream();
                new_icon = BitmapFactory.decodeStream(in_stream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return new_icon;
        }

        protected void onPostExecute(Bitmap result_img) {
            bitmap_img.setImageBitmap(result_img);
        }
    }
}
