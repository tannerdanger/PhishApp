package tbrown13.tcss450.uw.edu.phishapp;


import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import tbrown13.tcss450.uw.edu.phishapp.model.Credentials;
import tbrown13.tcss450.uw.edu.phishapp.utils.SendPostAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Credentials mCredentials;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button b = (Button) view.findViewById(R.id.register_button_register);

//        final EditText pw = (EditText)view.findViewById(R.id.register_edittext_password);
//        final EditText pwconfirm = (EditText)view.findViewById(R.id.register_edittext_pwconfirm);
//        final EditText uname = (EditText)view.findViewById(R.id.register_edittext_uname);
//        final EditText email = (EditText)view.findViewById(R.id.register_edittext_email);
//        final EditText fname = (EditText)view.findViewById(R.id.register_edittext_fname);
//        final EditText lname = (EditText)view.findViewById(R.id.register_edittext_lname);

        b.setOnClickListener(v ->{
            attemptRegister(v);
//            Credentials credentials;
//            String pass1 = pw.getText().toString();
//            String pass2 = pwconfirm.getText().toString();
//            String user = uname.getText().toString();
//
//
//            boolean isValid = true;
//
//            if(pass1.compareTo(pass2) != 0){
//                isValid = false;
//                pwconfirm.setError("Passwords don't match");
//            }
//
//            if(pass1.compareTo("") == 0){
//                isValid = false;
//                pw.setError("Password can not be blank, genius");
//            }
//
//            if(pass2.compareTo("")==0){
//                isValid = false;
//                pwconfirm.setError("Password can not be blank, genius");
//            }
//
//            if(pass1.length() < 6){
//                isValid = false;
//                pw.setError("Make that password longer homie");
//            }
//
//            if(user.compareTo("") == 0){
//                isValid = false;
//                uname.setError("Username can't be blank dude");
//            }
//
//            if(!email.getText().toString().contains("@")){
//                isValid = false;
//                email.setError("That's not an email address...");
//            }
//
//            if(fname.getText().toString().compareTo("") == 0){
//                isValid = false;
//                fname.setError("Name field cannot be blank...");
//            }
//
//            if(lname.getText().toString().compareTo("") == 0){
//                isValid = false;
//                lname.setError("Name field cannot be blank...");
//            }
//
//            if(isValid) {
//                credentials = new Credentials.Builder(
//                        email.getText().toString(),
//                        pass1
//                ).addUsername(uname.getText().toString())
//                        .addFirstName(fname.getText().toString())
//                        .addLastName(lname.getText().toString())
//                        .build();
//
//                mCredentials = credentials;
//
//                mListener.onRegisterSuccess(mCredentials);
//            }
        });

        return view;
    }

    private void attemptRegister(final View theButton){

        final EditText pw = (EditText)getActivity().findViewById(R.id.register_edittext_password);
        final EditText pwconfirm = (EditText)getActivity().findViewById(R.id.register_edittext_pwconfirm);
        final EditText uname = (EditText)getActivity().findViewById(R.id.register_edittext_uname);
        final EditText email = (EditText)getActivity().findViewById(R.id.register_edittext_email);
        final EditText fname = (EditText)getActivity().findViewById(R.id.register_edittext_fname);
        final EditText lname = (EditText)getActivity().findViewById(R.id.register_edittext_lname);

        boolean isValid = true;

        if(pw.getText().toString().compareTo(
                pwconfirm.getText().toString()) != 0){
            isValid = false;
            pwconfirm.setError("Passwords don't match");
        }

        if(pw.getText().toString().compareTo("") == 0){
            isValid = false;
            pw.setError("Password can not be blank, genius");
        }

        if(pwconfirm.getText().toString().compareTo("")==0){
            isValid = false;
            pwconfirm.setError("Password can not be blank, genius");
        }

        if(pw.getText().toString().length() < 6){
            isValid = false;
            pw.setError("Make that password longer homie");
        }

        if(uname.getText().toString().compareTo("") == 0){
            isValid = false;
            uname.setError("Username can't be blank dude");
        }

        if(!email.getText().toString().contains("@")){
            isValid = false;
            email.setError("That's not an email address...");
        }

        if(fname.getText().toString().compareTo("") == 0){
            isValid = false;
            fname.setError("Name field cannot be blank...");
        }

        if(lname.getText().toString().compareTo("") == 0){
            isValid = false;
            lname.setError("Name field cannot be blank...");
        }

        if(isValid){
            Credentials cred = new Credentials.Builder(
                    email.getText().toString(),
                    pw.getText().toString())
                    .addUsername(uname.getText().toString())
                    .addFirstName(fname.getText().toString())
                    .addLastName(lname.getText().toString())
                    .build();

            //build web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath("tcss450a18.herokuapp.com")
                    .appendPath("register")
                    .build();

            //Build JSON object
            JSONObject msg = cred.asJSONObject();

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



    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleLoginOnPost(String result) {

        try {

            Log.d("JSON result", result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");

            mListener.onWaitFragmentInteractionHide();
            if(success){
                //register successful
                mListener.onRegisterSuccess(mCredentials);
            }else {
                //register unsuccessful
                ((TextView) getView().findViewById(R.id.edit_login_email))
                        .setError("Register Unsuccessful");
            }

        }catch (JSONException e){
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

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR",  result);
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

        void onRegisterSuccess(Credentials c);
    }
}
