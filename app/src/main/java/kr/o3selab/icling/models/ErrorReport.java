package kr.o3selab.icling.models;

import android.os.Build;

import java.io.Serializable;

public class ErrorReport implements Serializable {

    public Long mRegdate;
    public User mUser;
    public String mModel;
    public String mManufacturer;
    public String mAndroidVersion;
    public String mLogs;

    public ErrorReport(String logs) {
        mRegdate = System.currentTimeMillis();
        mUser = Constants.user;
        mModel = Build.MODEL;
        mManufacturer = Build.MANUFACTURER;
        mAndroidVersion = Build.VERSION.RELEASE;
        mLogs = logs;
    }
}
