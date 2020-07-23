package com.example.citizensapp;

import com.google.firebase.database.Exclude;

public class Upload {

    private String mImageUrl, mPotholeType, mAddress,mLandmark,mDimension, mComment;
    private String mKey;
    private String VideoDescription;
    private String VideoUri;


    public Upload(String ImageUrl, String mPotholeType, String mAddress, String mLandmark, String mDimension, String mComment) {
        this.mImageUrl = ImageUrl;
        this.mPotholeType = mPotholeType;
        this.mAddress = mAddress;
        this.mLandmark = mLandmark;
        this.mDimension = mDimension;
        this.mComment = mComment;
        this.mKey = mKey;


    }

    public Upload() {

    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmPotholeType() {
        return mPotholeType;
    }

    public void setmPotholeType(String mPotholeType) {
        this.mPotholeType = mPotholeType;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmLandmark() {
        return mLandmark;
    }

    public void setmLandmark(String mLandmark) {
        this.mLandmark = mLandmark;
    }

    public String getmDimension() {
        return mDimension;
    }

    public void setmDimension(String mDimension) {
        this.mDimension = mDimension;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    @Exclude

    public String getKey(){
        return mKey;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }




    //  public String getVideoUrl(){
    //     return mVideoUrl;
    // }

    //  public void setVideoUrl(String videoUrl){
    //    mVideoUrl = videoUrl;
    //  }

}

