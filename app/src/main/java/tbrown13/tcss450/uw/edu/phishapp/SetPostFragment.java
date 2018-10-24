package tbrown13.tcss450.uw.edu.phishapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tbrown13.tcss450.uw.edu.phishapp.set.SetPost;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetPostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SetPostFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String mURL;

    public SetPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();
        if(getArguments() != null){
            SetPost set = (SetPost)getArguments().getSerializable("set");
            updateContent(set);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_set_post, container, false);

        Button b = (Button)v.findViewById(R.id.setpost_button_fullpost);
        b.setOnClickListener(v1 -> {
            mListener.onSetFragmentInteraction(mURL);
        });

        return v;
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
        void onSetFragmentInteraction(String setUrl);
    }

    private void updateContent(SetPost set){

        mURL = set.getSetUrl();

        TextView tv = getActivity().findViewById(R.id.setpost_tv_setdate);
        tv.setText(set.getSetDate());

        tv =getActivity().findViewById(R.id.setpost_tv_setLoc);
        tv.setText(set.getSetLocation());

        tv =getActivity().findViewById(R.id.setpost_tv_data);
        tv.setText(Html.fromHtml(set.getSetData(), 0));

        tv =getActivity().findViewById(R.id.setpost_tv_notes);
        tv.setText(Html.fromHtml(set.getSetNotes(), 0));
    }
}
