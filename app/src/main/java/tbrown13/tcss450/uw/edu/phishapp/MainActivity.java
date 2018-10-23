package tbrown13.tcss450.uw.edu.phishapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import tbrown13.tcss450.uw.edu.phishapp.model.Credentials;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (findViewById(R.id.frame_main_container) != null) { //TODO: frame_main_fragment_container??
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_main_container, new LoginFragment())
                        .commit();
            }
        }
    }


    @Override
    public void onLoginSuccess(Credentials creds) {

        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("credentials", creds);
//        i.putExtra("pw", password);
        startActivity(i);
    }

    @Override
    public void onRegisterClicked(){

        getSupportFragmentManager().popBackStack();
        loadFragment(new RegisterFragment());
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_main_container, new RegisterFragment())
//                .addToBackStack(null);
//
//        transaction.commit();
    }

    @Override
    public void onRegisterSuccess(Credentials creds){

        getSupportFragmentManager().popBackStack();

   }

   private void loadFragment(Fragment frag){

       FragmentTransaction transaction = getSupportFragmentManager()
               .beginTransaction()
               .replace(R.id.frame_main_fragment_container, frag)
               .addToBackStack(null);
       transaction.commit();
   }


    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main_fragment_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }
}
