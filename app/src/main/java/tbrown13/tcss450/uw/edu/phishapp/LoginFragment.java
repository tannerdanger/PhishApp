package tbrown13.tcss450.uw.edu.phishapp;

import android.content.Context;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import tbrown13.tcss450.uw.edu.phishapp.model.Credentials;
import tbrown13.tcss450.uw.edu.phishapp.utils.SendPostAsyncTask;

//TODO: DOn't login if data is incorrect
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment  {

    private OnFragmentInteractionListener mListener;
    private Credentials mCredentials;
    private String mFirebaseToken;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //retrieve the stored credentials from SharedPrefs
        if (prefs.contains(getString(R.string.keys_prefs_email)) &&
                prefs.contains(getString(R.string.keys_prefs_password))) {

            final String email = prefs.getString(getString(R.string.keys_prefs_email), "");
            final String password = prefs.getString(getString(R.string.keys_prefs_password), "");
            //Load the two login EditTexts with the credentials found in SharedPrefs
            EditText emailEdit = getActivity().findViewById(R.id.edit_login_email);
            emailEdit.setText(email);
            EditText passwordEdit = getActivity().findViewById(R.id.edit_login_password);
            passwordEdit.setText(password);

            getFirebaseToken(email, password);


        }
    }

    private void doLogin(String email, String password){
        //create credentials
        Credentials cred = new Credentials.Builder(email, password).build();

        //build web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_login))
                .appendPath(getString(R.string.ep_with_token))
                .build();

        //build JSON Object
        JSONObject msg = cred.asJSONObject();
        try{
            msg.put("token", mFirebaseToken);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        mCredentials = cred;

        //instantiate and execute AsyncTask
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleLoginOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button b = (Button) view.findViewById(R.id.login_button_login);
        // b.setOnClickListener(this);

        final EditText pw = (EditText)view.findViewById(R.id.edit_login_password);
        final EditText uname = (EditText)view.findViewById(R.id.edit_login_email);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(v);

            }
        });

        b = view.findViewById(R.id.login_button_register);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterClicked();
            }
        });



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void attemptLogin(final View theButton) {

        EditText emailEdit = getActivity().findViewById(R.id.edit_login_email);
        EditText passwordEdit = getActivity().findViewById(R.id.edit_login_password);

        boolean isValidLogin = true;

        if(emailEdit.getText().length() == 0){
            isValidLogin = false;
            emailEdit.setError("Fiend must not be empty.");
        }else if( emailEdit.getText().toString().chars().filter(ch -> ch == '@').count() != 1){
            isValidLogin = false;
            emailEdit.setError("Fiend must contain a valid email address");
        }

        if (passwordEdit.getText().length() == 0){
            isValidLogin = false;
            passwordEdit.setError("Field must not be empty");
        }

        if(isValidLogin){

            getFirebaseToken(emailEdit.getText().toString(), passwordEdit.getText().toString());
//            //create credentials
//            Credentials cred = new Credentials.Builder(
//                    emailEdit.getText().toString(),
//                    passwordEdit.getText().toString())
//                    .build();
//
//            //build web service URL
//            Uri uri = new Uri.Builder()
//                    .scheme("https")
//                    .appendPath(getString(R.string.ep_base_url))
//                    .appendPath(getString(R.string.ep_login))
//                    .build();
//
//            //build JSON Object
//            JSONObject msg = cred.asJSONObject();
//
//            mCredentials = cred;
//
//            //instantiate and execute AsyncTask
//            //Feel free to add a handler for onPreExecution so that a progress bar
//            //is displayed or maybe disable buttons.
//            new SendPostAsyncTask.Builder(uri.toString(), msg)
//                    .onPreExecute(this::handleLoginOnPre)
//                    .onPostExecute(this::handleLoginOnPost)
//                    .onCancelled(this::handleErrorsInTask)
//                    .build()
//                    .execute();
        }

    }
    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR",  result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
       // mListener.onWaitFragmentInteractionShow();
    }


    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleLoginOnPost(String result) {
        try {

            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");

            mListener.onWaitFragmentInteractionHide();
            if (success) {
                //Login was successful. Inform the Activity so it can do its thing.
                saveCredentials(mCredentials);
                mListener.onLoginSuccess(mCredentials);
                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and inform the user
                ((TextView) getView().findViewById(R.id.edit_login_email))
                        .setError("Login Unsuccessful");
            }

        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR",  result
                    + System.lineSeparator()
                    + e.getMessage());

            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.edit_login_email))
                    .setError("Login Unsuccessful");
        }
    }

    private void saveCredentials(final Credentials credentials) {
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_email), credentials.getEmail()).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_password), credentials.getPassword()).apply();
    }

    private void getFirebaseToken(final String email, final String password) {
        mListener.onWaitFragmentInteractionShow();

        //add this app on this device to listen for the topic all
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //the call to getInstanceId happens asynchronously. task is an onCompleteListener
        //similar to a promise in JS.
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM: ", "getInstanceId failed", task.getException());
                        mListener.onWaitFragmentInteractionHide();
                        return;
                    }

                    // Get new Instance ID token
                    mFirebaseToken = task.getResult().getToken();

                    Log.d("FCM: ", mFirebaseToken);
                    //the helper method that initiates login service
                    doLogin(email, password);
                });
        //no code here. wait for the Task to complete.
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLoginSuccess(Credentials c);
        void onRegisterClicked();
    }
}
