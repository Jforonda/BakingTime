package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {

    private String mStepID;
    private String mShortDescription;
    private String mDescription;
    private String mVideoURL;
    private String mThumbnailURL;

    public Step (String stepID, String shortDescription, String description, String videoURL,
          String thumbnailURL) {
        mStepID = stepID;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;
        mThumbnailURL = thumbnailURL;
    }

    // Getters and setters

    public String getStepID() {
        return mStepID;
    }

    public void setStepID(String stepID) {
        mStepID = stepID;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        mShortDescription = shortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getVideoURL() {
        return mVideoURL;
    }

    public void setVideoURL(String videoURL) {
        mVideoURL = videoURL;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        mThumbnailURL = thumbnailURL;
    }

    // Parcelable methods

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mStepID);
        out.writeString(mShortDescription);
        out.writeString(mDescription);
        out.writeString(mVideoURL);
        out.writeString(mThumbnailURL);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    private Step(Parcel in) {
        mStepID = in.readString();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
    }
}
