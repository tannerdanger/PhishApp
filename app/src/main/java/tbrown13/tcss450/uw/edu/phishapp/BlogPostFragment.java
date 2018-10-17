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

import tbrown13.tcss450.uw.edu.phishapp.blog.BlogPost;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlogPostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BlogPostFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String mURL;

    public BlogPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();
        if(getArguments() != null){
            BlogPost blog = (BlogPost) getArguments().getSerializable("blog");
            updateContent(blog);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blog_post, container, false);

        Button b = (Button)v.findViewById(R.id.blogpost_button_fullpost);
        b.setOnClickListener(v1 -> {
            mListener.onBlogFragmentInteraction(mURL);
        });

        return v;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onBlogFragmentInteraction(mURL);
//        }
//    }

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
        void onBlogFragmentInteraction(String blogURL);
    }


    private void updateContent(BlogPost blog){
        TextView tv = getActivity().findViewById(R.id.blogpost_tv_title);
        tv.setText(blog.getTitle());

        tv = getActivity().findViewById(R.id.blogpost_tv_postdate);
        tv.setText(blog.getPubDate());

        tv = getActivity().findViewById(R.id.blogpost_tv_content);
        tv.setText(Html.fromHtml(blog.getTeaser(), 0));

        mURL = blog.getUrl();
    }
}
