package com.socialapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import adapter.ActionBarTitleSetter;
import model.LoginResponseValues;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, ActionBarTitleSetter {

    private static String TAG = MainActivity.class.getSimpleName();
    Typeface typeface;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    Context _ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _ctx = this;
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Ubuntu-Light.ttf");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);


    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                if (LoginResponseValues.getInstance().getIsGmail() == 0) {
                    fragment = new HomeFragment();
                    title = getString(R.string.title_home);
                } else {
                    //Gmail Fragment
                    fragment = new GmailFragment();
                    title = getString(R.string.title_home);
                }

                break;
            case 1:
                if (LoginResponseValues.getInstance().getIsGmail() == 0) {
                    fragment = new FriendsFragment();
                    title = getString(R.string.title_friends);

                } else {
                    finish();
                   }
                break;
            case 2:
                if (LoginResponseValues.getInstance().getIsGmail() == 0) {
                    fragment = new PostsFragment();
                    title = getString(R.string.title_messages);
                } else {
                    Toast.makeText(this, "Please sign in to facebook account", Toast.LENGTH_SHORT).show();
                }

                break;
            case 3:
                if (LoginResponseValues.getInstance().getIsGmail() == 0) {
                    fragment = new GroupsFragment();
                    title = getString(R.string.title_groups);
                } else {
                    Toast.makeText(this, "Please sign in to facebook account", Toast.LENGTH_SHORT).show();
                }

                break;
            case 4:

               // finish();
                Intent intent = new Intent(MainActivity.this, SocialAppActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private void displayGmailView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;

            case 4:
                finish();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onBackPressed() {

    }
}