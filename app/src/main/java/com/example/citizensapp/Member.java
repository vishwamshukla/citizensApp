package com.example.citizensapp;

public class Member {
    private String name;
    private String Videourl;

    public Member() {
    //empty Constructor needed
    }
    public Member(String mName, String mVideourl){
        if (mName.trim().equals("")){
            mName = "No Name";
        }
        name = mName;
        mVideourl = Videourl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideourl() {
        return Videourl;
    }

    public void setVideourl(String videourl) {
        Videourl = videourl;
    }
}
