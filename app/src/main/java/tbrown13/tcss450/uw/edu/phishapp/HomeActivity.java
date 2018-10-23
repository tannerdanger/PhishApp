package tbrown13.tcss450.uw.edu.phishapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import tbrown13.tcss450.uw.edu.phishapp.blog.BlogPost;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BlogFragment.OnListFragmentInteractionListener,
        BlogPostFragment.OnFragmentInteractionListener {

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

        args.putSerializable("credentials", getIntent().getSerializableExtra("credentials"));

//        args.putString("email", getIntent().getStringExtra("email"));
//        args.putString("pw", getIntent().getStringExtra("pw"));
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
            loadFragment(new BlogFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /************ Tanners methods *******/
    private void loadFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null);
        transaction.commit();
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
}
