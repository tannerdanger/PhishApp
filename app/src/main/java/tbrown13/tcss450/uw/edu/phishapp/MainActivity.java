package tbrown13.tcss450.uw.edu.phishapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import tbrown13.tcss450.uw.edu.phishapp.model.Credentials;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (findViewById(R.id.frame_main_fragment_container) != null) { //Good
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_main_fragment_container, new LoginFragment())
                        .commit();
            }
        }
    }


    @Override
    public void onLoginSuccess(Credentials creds) {
        Log.wtf("Main Activity", "Started onLoginSuccess in Main Activity");
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("credentials", creds);
        startActivity(i);
        Log.wtf("Main Activity", "Ended onWaitFragmentInteractionHide in Main Activity");
    }

    @Override
    public void onRegisterClicked(){

        getSupportFragmentManager().popBackStack();
        loadFragment(new RegisterFragment());

    }

    @Override
    public void onRegisterSuccess(Credentials creds){
        Log.wtf("Main Activity", "Started onRegisterSuccess in Main Activity");
        //getSupportFragmentManager().popBackStack();

        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("credentials", creds);
        startActivity(i);

        Log.wtf("Main Activity", "ended onRegisterSuccess in Main Activity");
   }

   private void loadFragment(Fragment frag){
        Log.wtf("Main Activity", "Started Load Fragment in Main Activity");
       FragmentTransaction transaction = getSupportFragmentManager()
               .beginTransaction()
//               .replace(R.id.frame_main_fragment_container, frag)
               .replace(R.id.frame_main_fragment_container, frag)
               .addToBackStack(null);
       transaction.commit();
       Log.wtf("Main Activity", "ended Load Fragment in Main Activity");
   }


    @Override
    public void onWaitFragmentInteractionShow() {
        Log.wtf("Main Activity", "Started onWaitFragmentInteractionShow in Main Activity");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main_fragment_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
        Log.wtf("Main Activity", "ended onWaitFragmentInteractionShow in Main Activity");
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        Log.wtf("Main Activity", "Started onWaitFragmentInteractionHide in Main Activity");
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
        Log.wtf("Main Activity", "Ended onWaitFragmentInteractionHide in Main Activity");
    }

}
