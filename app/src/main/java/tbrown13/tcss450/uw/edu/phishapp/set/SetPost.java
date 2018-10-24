package tbrown13.tcss450.uw.edu.phishapp.set;

import java.io.Serializable;

import tbrown13.tcss450.uw.edu.phishapp.blog.BlogPost;

public class SetPost implements Serializable {

    private final String mSetDate;
    private final String mSetLocation;
    private final String mSetVenue;
    private final String mSetData;
    private final String mSetNotes;
    private final String mSetUrl;

    public String getSetDate() {
        return mSetDate;
    }

    public String getSetLocation() {
        return mSetLocation;
    }

    public String getSetVenue() {
        return mSetVenue;
    }

    public String getSetData() {
        return mSetData;
    }

    public String getSetUrl(){
        return mSetUrl;
    }

    public String getSetNotes() {
        return mSetNotes;
    }

    private SetPost(final Builder builder){
        this.mSetDate = builder.mSetDate;
        this.mSetLocation = builder.mSetLocation;
        this.mSetVenue = builder.mSetVenue;
        this.mSetData = builder.mData;
        this.mSetNotes = builder.mNotes;
        this.mSetUrl = builder.mUrl;
    }


    /**
     * Inner builder class for building a setlist
     */
    public static class Builder {
        private final String mSetDate;
        private final String mSetLocation;
        private final String mSetVenue;
        private String mUrl = "";
        private String mData = "";
        private  String mNotes = "";


        public Builder(String setDate, String location, String venue){
            this.mSetDate = setDate;
            this.mSetLocation = location;
            this.mSetVenue = venue;
        }

        public Builder addUrl (final String val){
            mUrl = val;
            return this;
        }

        public Builder addData( final String val){
            mData = val;
            return this;
        }

        public Builder addNotes( final String val ){
            mNotes = val;
            return this;
        }

        public SetPost build(){
            return new SetPost(this);
        }

    }


}
