package tbrown13.tcss450.uw.edu.phishapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tbrown13.tcss450.uw.edu.phishapp.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment {


    public DisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();

        if(getArguments() != null){

//           Credentials cred = (Credentials)getArguments().getSerializable(getString(R.string.key_email));
            String email = (String) getArguments().getSerializable(getString(R.string.key_email));
            updateContent(email);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    public void updateContent(String email){

        TextView tv = getActivity().findViewById(R.id.display_textview_username);
        tv.setText(email);
    }

}
