package tbrown13.tcss450.uw.edu.phishapp;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment  {

    private OnFragmentInteractionListener mListener;

 //   private View.OnClickListener buttonListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button b = (Button) view.findViewById(R.id.login_button_login);
        // b.setOnClickListener(this);

        final EditText pw = (EditText)view.findViewById(R.id.login_edittext_password);
        final EditText uname = (EditText)view.findViewById(R.id.login_edittext_uname);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pw.getText().toString();
                String username = uname.getText().toString();

                if(username.contains("@")
                        && password.compareTo("")!=0) {
                    mListener.onFragmentInteraction_Login(username, password);
                }else{
                    if(!username.contains("@"))
                        uname.setError("Username is not an email");

                    if(username.compareTo("") == 0)
                        uname.setError("Username can not be blank");

                    if(password.compareTo("")==0)
                        pw.setError("Password can not be blank, yo.");
                }
            }
        });

        b = view.findViewById(R.id.login_button_register);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction_Register();
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
        // TODO: Update argument type and name
        void onFragmentInteraction_Login(String username, String password);
        void onFragmentInteraction_Register();
    }
}