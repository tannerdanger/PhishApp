package tbrown13.tcss450.uw.edu.phishapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
            String username = getArguments().getString("email");
            String password = getArguments().getString("pw");
            updateContent(username, password);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    public void updateContent(String username, String password){
        TextView tv = getActivity().findViewById(R.id.display_textview_password);
        tv.setText(password);
        tv = getActivity().findViewById(R.id.display_textview_username);
        tv.setText(username);
    }

}