package tbrown13.tcss450.uw.edu.phishapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button b = (Button) view.findViewById(R.id.register_button_register);
        final EditText pw = (EditText)view.findViewById(R.id.register_edittext_password);
        final EditText pwconfirm = (EditText)view.findViewById(R.id.register_edittext_pwconfirm);
        final EditText uname = (EditText)view.findViewById(R.id.register_edittext_uname);

        b.setOnClickListener(v ->{
            String pass1 = pw.getText().toString();
            String pass2 = pwconfirm.getText().toString();
            String user = uname.getText().toString();

            boolean isValid = true;

            if(pass1.compareTo(pass2) != 0){
                isValid = false;
                pwconfirm.setError("Passwords don't match");
            }

            if(pass1.compareTo("") == 0){
                isValid = false;
                pw.setError("Password can not be blank, genius");
            }

            if(pass2.compareTo("")==0){
                isValid = false;
                pwconfirm.setError("Password can not be blank, genius");
            }

            if(pass1.length() < 6){
                isValid = false;
                pw.setError("Make that password longer homie");
            }

            if(user.compareTo("") == 0){
                isValid = false;
                uname.setError("Username can't be blank dude");
            }

            if(!user.contains("@")){
                isValid = false;
                uname.setError("That's not an email address...");
            }

            if(isValid)
                mListener.onFragmentInteraction_Registered(user, pass1);

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
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction_Registered(String username, String password);
    }
}
