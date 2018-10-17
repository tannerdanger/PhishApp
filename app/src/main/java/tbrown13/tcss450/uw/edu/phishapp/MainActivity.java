package tbrown13.tcss450.uw.edu.phishapp;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (findViewById(R.id.frame_main_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_main_container, new LoginFragment())
                        .commit();
            }
        }
    }


    @Override
    public void onFragmentInteraction_Login(String email, String password) {

//        Bundle args = new Bundle();
//        args.putString("email", email);
//        args.putString("pw", password);
//
//        DisplayFragment displayFragment;
//        displayFragment = new DisplayFragment();
//        displayFragment.setArguments(args);



        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("email", email);
        i.putExtra("pw", password);
        startActivity(i);


//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragmentContainer, displayFragment);
//               // .addToBackStack(null);
//
//
//        transaction.commit();

    }

    @Override
    public void onFragmentInteraction_Register(){

        getSupportFragmentManager().popBackStack();

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, new RegisterFragment())
                .addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onFragmentInteraction_Registered(String username, String password){

        getSupportFragmentManager().popBackStack();

//        final Bundle args = new Bundle();
//        args.putString("user", username);
//        args.putString("pw", password);
//
////        DisplayFragment displayFragment = new DisplayFragment();
////        displayFragment.setArguments(args);
//        HomeActivity home = new HomeActivity();
//
//
//
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_main_container, displayFragment);
//               // .addToBackStack(null);
//        //add to backstack?
//
//        transaction.commit();
   }


}
