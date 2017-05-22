package com.schradersoft.timetofishmobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.schradersoft.timetofishmobile.domain.models.Photo;
import com.schradersoft.timetofishmobile.domain.models.User;
import com.schradersoft.timetofishmobile.core.Api;
import com.schradersoft.timetofishmobile.core.IJsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IJsonResponseHandler<JSONObject> {

    private User _user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Api.getInstance(this).IsSignedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        if (_user == null) {
            Api.getInstance(this).Get("user/me", this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_trips) {

        } else if (id == R.id.nav_contest) {
            ContestFragment fragment = new ContestFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, fragment).commit();
        } else if (id == R.id.nav_galleries) {

        } else if (id == R.id.nav_schedule) {

        } else if (id == R.id.nav_lists) {

        } else if (id == R.id.nav_fish_on) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_signout) {
            Api.getInstance(this).Signout();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void HandleResponse(JSONObject response) {
        final GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(User.class, new User.UserDeserializer());
        gsonBuilder.registerTypeAdapter(Photo.class, new Photo.PhotoDeserializer());
        final Gson gson = gsonBuilder.create();

        try {
            _user = gson.fromJson(response.getString("user"), User.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RefreshUserProfile();
    }

    @Override
    public void HandleException(VolleyError error) {

    }

    private void RefreshUserProfile() {
        TextView username = (TextView) findViewById(R.id.profile_username);
        TextView email = (TextView) findViewById(R.id.profile_email);
        CircleImageView view = (CircleImageView) findViewById(R.id.profile_pic);

        String url = _user.getAvatar().getImage().getUri();
        Photo.PhotoDownloader imageLoader = new Photo.PhotoDownloader(url, view, getApplicationContext());
        imageLoader.execute((Hashtable<String, Bitmap>)null);

        username.setText(_user.getUsername());
        email.setText(_user.getEmail());
    }
}
