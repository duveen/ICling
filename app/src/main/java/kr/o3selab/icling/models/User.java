package kr.o3selab.icling.models;

import java.io.Serializable;

public class User implements Serializable {

    public static String KAKAO = "kakao";
    public static String GOOGLE = "google";

    public String mLoginType = null;
    public String mUserID = null;
    public String mUserEmail = null;
    public Integer mHeight = null;
    public Integer mWeight = null;
    public Integer mAge = null;
    public Integer mSex = null;
    public Integer mRadius = null;
    public Long mRegdate = null;

    public User() {

    }

    public User(String type, String email) {
        mLoginType = type;
        mUserEmail = email;
    }

    public boolean isData() {
        if (mHeight == null) return false;
        else return true;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return this.mLoginType.equals(user.mLoginType) && this.mUserEmail.equals(user.mUserEmail);
    }

    @Override
    public String toString() {
        return mLoginType + ":" + mUserEmail;
    }

}
