package tbrown13.tcss450.uw.edu.phishapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import tbrown13.tcss450.uw.edu.phishapp.blog.BlogPost;
import tbrown13.tcss450.uw.edu.phishapp.set.SetPost;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BlogFragment.OnListFragmentInteractionListener,
        BlogPostFragment.OnFragmentInteractionListener,
        WaitFragment.OnFragmentInteractionListener,
        SetFragment.OnListFragmentInteractionListener,
        SetPostFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set display to display fragment
        DisplayFragment dfrag = new DisplayFragment();
        Bundle args = new Bundle();

        args.putSerializable(getString(R.string.key_email), getIntent().getSerializableExtra(getString(R.string.key_email)));


        dfrag.setArguments(args);
        loadFragment(dfrag);


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
        getMenuInflater().inflate(R.menu.home, menu);
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

        if (id == R.id.action_logout){
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homefragment) {
            //Set display to display fragment
            DisplayFragment dfrag = new DisplayFragment();
            Bundle args = new Bundle();
            args.putString("email", getIntent().getStringExtra("email"));
            args.putString("pw", getIntent().getStringExtra("pw"));
            dfrag.setArguments(args);
            loadFragment(dfrag);
        } else if (id == R.id.blogFragment) {
            //loadFragment(new BlogFragment());
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_blog))
                    .appendPath(getString(R.string.ep_get))
                    .build();

            new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleBlogGetOnPostExecute)
                    .build().execute();

        } else if (id == R.id.setFragment) {

            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_setlist))
                    .appendPath(getString(R.string.ep_recent))
                    .build();

            new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleSetGetOnPostExecute)
                    .build().execute();

        } else if (id == R.id.chatfragment){
            loadFragment(new ChatFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(BlogPost blog) {
        final Bundle args = new Bundle();
        args.putSerializable("blog", blog);

        BlogPostFragment blogpost = new BlogPostFragment();
        blogpost.setArguments(args);
        loadFragment(blogpost);



    }

    @Override
    public void onBlogFragmentInteraction(String url) {

        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e){
            Toast.makeText(this, "Invalid url request", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onWaitFragmentInteractionShow() {
        Log.wtf("Home Activity", "Started onWaitFragmentInteractionShow in Home Activity");
        getSupportFragmentManager()
                .beginTransaction()
                //.add(R.id.frame_main_fragment_container, new WaitFragment(), "WAIT")fragmentContainer
                .add(R.id.fragmentContainer, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
        Log.wtf("Home Activity", "Ended onWaitFragmentInteractionShow in Home Activity");
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        Log.wtf("Home Activity", "Started onWaitFragmentInteractionHide in Home Activity");
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
        Log.wtf("Home Activity", "Ended onWaitFragmentInteractionHide in Home Activity");
    }

    private void handleSetGetOnPostExecute(final String result){
        //parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("response")) {
                JSONObject response = root.getJSONObject("response");
                if(response.has("data")){
                    JSONArray data = response.getJSONArray("data");

                    List<SetPost> sets = new ArrayList<>();

                    for(int i = 0; i < data.length(); i++){
                        JSONObject jsonset = data.getJSONObject(i);
                        sets.add(new SetPost.Builder(
                                jsonset.getString("showdate"),
                                jsonset.getString("location"),
                                jsonset.getString("venue"))
                                .addUrl(jsonset.getString("url"))
                                .addData(jsonset.getString("setlistdata"))
                                .addNotes(jsonset.getString("setlistnotes"))
                                .build());
                    }
                    SetPost[] setsAsArray = new SetPost[sets.size()];
                    setsAsArray = sets.toArray(setsAsArray);

                    Bundle args = new Bundle();
                    args.putSerializable(SetFragment.ARG_SET_LIST, setsAsArray);
                    Fragment frag = new SetFragment();
                    frag.setArguments(args);

                    onWaitFragmentInteractionHide();
                    loadFragment(frag);

                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
                onWaitFragmentInteractionHide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    private void handleBlogGetOnPostExecute(final String result) {
        //parse JSON

        try {
            JSONObject root = new JSONObject(result);
            if (root.has("response")) {
                JSONObject response = root.getJSONObject("response");
                if (response.has("data")) {
                    JSONArray data = response.getJSONArray("data");

                    List<BlogPost> blogs = new ArrayList<>();

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);
                        blogs.add(new BlogPost.Builder(jsonBlog.getString("pubdate"),
                                jsonBlog.getString("title"))
                                .addTeaser(jsonBlog.getString("teaser"))
                                .addUrl(jsonBlog.getString("url"))
                                .build());
                    }

                    BlogPost[] blogsAsArray = new BlogPost[blogs.size()];
                    blogsAsArray = blogs.toArray(blogsAsArray);


                    Bundle args = new Bundle();
                    args.putSerializable(BlogFragment.ARG_BLOG_LIST, blogsAsArray);
                    Fragment frag = new BlogFragment();
                    frag.setArguments(args);

                    onWaitFragmentInteractionHide();
                    loadFragment(frag);
                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
                onWaitFragmentInteractionHide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    private void loadFragment(Fragment frag) {
        Log.wtf("Home Activity", "Started loadFragment in Home Activity");
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
        Log.wtf("Home Activity", "Ended loadFragment in Home Activity");
    }

    @Override
    public void onListFragmentInteraction(SetPost set) {
        final Bundle args = new Bundle();
        args.putSerializable("set", set);
//
        SetPostFragment setpost = new SetPostFragment();
        setpost.setArguments(args);
        loadFragment(setpost);

    }

    @Override
    public void onSetFragmentInteraction(String setUrl) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(setUrl));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e){
            Toast.makeText(this, "Invalid url request", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void logout() {

        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();


        //close the app
        finishAndRemoveTask();

        //or close this activity and bring back the Login
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
        //End this Activity and remove it from the Activity back stack.
        //finish();
    }


}
