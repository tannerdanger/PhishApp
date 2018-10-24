package tbrown13.tcss450.uw.edu.phishapp;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tbrown13.tcss450.uw.edu.phishapp.SetFragment.OnListFragmentInteractionListener;
import tbrown13.tcss450.uw.edu.phishapp.set.SetPost;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SetPost} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySetRecyclerViewAdapter extends RecyclerView.Adapter<MySetRecyclerViewAdapter.ViewHolder> {

    private final List<SetPost> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySetRecyclerViewAdapter(List<SetPost> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDateView.setText(mValues.get(position).getSetDate());
        holder.mLocationView.setText(mValues.get(position).getSetLocation());
        holder.mVenueView.setText(Html.fromHtml(mValues.get(position).getSetVenue(), 0));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mLocationView;
        public final TextView mVenueView;
        public SetPost mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.set_date);
            mLocationView = (TextView) view.findViewById(R.id.set_location);
            mVenueView = (TextView) view.findViewById(R.id.set_venue);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mVenueView.getText() + "'";
        }
    }
}
