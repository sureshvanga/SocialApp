package com.socialapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;


public class GmailDetails extends Activity {
    private TextView name_tv, emailId_tv, dob_tv, tag_line_tv, about_me_tv;
    Typeface typeface;
    String name, emailId, dob, tagle_line, about_me, photo_url;
    private String TAG = "GmailDetails";
    private static final int PROFILE_PIC_SIZE = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmail_details);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Light.ttf");

        setUpview();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("NAME");
            emailId = extras.getString("EmailID");
            dob = extras.getString("DOB");
            tagle_line = extras.getString("TAGLE_LINE");
            about_me = extras.getString("ABOUT_ME");
            photo_url = extras.getString("PHOTO_URL");

        }
        Log.e(TAG, "=-=-=-" + name);
        name_tv.setText("" + name);
        emailId_tv.setText("" + emailId);
        dob_tv.setText("DOB: " + dob);
        tag_line_tv.setText("Tagle Line: " + tagle_line);
        about_me_tv.setText("About me: " + about_me);
        setProfilePic(photo_url);

    }

    private void setUpview() {

        name_tv = (TextView) findViewById(R.id.userName);
        emailId_tv = (TextView) findViewById(R.id.emailId);
        dob_tv = (TextView) findViewById(R.id.dob);
        tag_line_tv = (TextView) findViewById(R.id.tag_line);
        about_me_tv = (TextView) findViewById(R.id.about_me);
        name_tv.setTypeface(typeface);
        emailId_tv.setTypeface(typeface);
        dob_tv.setTypeface(typeface);
        tag_line_tv.setTypeface(typeface);
        about_me_tv.setTypeface(typeface);


    }

    private void setProfilePic(String profile_pic) {
        profile_pic = profile_pic.substring(0,
                profile_pic.length() - 2)
                + PROFILE_PIC_SIZE;
        ImageView user_picture = (ImageView) findViewById(R.id.profile_pic);
        new LoadProfilePic(user_picture).execute(profile_pic);
    }
 /*
    Perform background operation asynchronously, to load user profile picture with new dimensions from the modified url
    */

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
