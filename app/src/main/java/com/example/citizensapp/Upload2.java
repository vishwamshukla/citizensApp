package com.example.citizensapp;
import com.google.firebase.database.Exclude;

public class Upload2 {

    private String mImageUrl, mPotholeType,mLandmark, mDate, mTime,  mUserId, mTimeKey, mStatus;

    private String mKey;

    public Upload2(String mImageUrl, String mPotholeType, String mLandmark, String mDate, String mTime, String mUserId, String mTimeKey, String mStatus) {
        this.mImageUrl = mImageUrl;
        this.mPotholeType = mPotholeType;
        this.mLandmark = mLandmark;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mUserId = mUserId;
        this.mTimeKey = mTimeKey;
        this.mStatus = mStatus;
        this.mKey = mKey;
    }
    public Upload2() {

    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmPotholeType() {
        return mPotholeType;
    }

    public void setmPotholeType(String mPotholeType) {
        this.mPotholeType = mPotholeType;
    }

    public String getmLandmark() {
        return mLandmark;
    }

    public void setmLandmark(String mLandmark) {
        this.mLandmark = mLandmark;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmTimeKey() {
        return mTimeKey;
    }

    public void setmTimeKey(String mTimeKey) {
        this.mTimeKey = mTimeKey;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
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

