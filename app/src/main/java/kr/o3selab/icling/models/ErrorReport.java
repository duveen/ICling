package kr.o3selab.icling.models;

import android.os.Build;

import java.io.Serializable;

/**
 * Created by O3SE Lab on 2017-04-09.
 */

public class ErrorReport implements Serializable {

    public User mUser;
    public String mModel;
    public String mManufacturer;
    public String mAndroidVersion;
    public String mLogs;

    public ErrorReport(String logs) {
        mUser = Constants.user;
        mModel = Build.MODEL;
        mManufacturer = Build.MANUFACTURER;
        mAndroidVersion = Build.VERSION.RELEASE;
        mLogs = logs;
    }
}
