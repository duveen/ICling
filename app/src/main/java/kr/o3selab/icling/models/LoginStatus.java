package kr.o3selab.icling.models;

import java.io.Serializable;

public class LoginStatus implements Serializable {

    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";

    public String mStatus = null;
    public String mUID = null;

    public LoginStatus(String status, String uid) {
        mStatus = status;
        mUID = uid;
    }

    public boolean isLogin() {
        return mStatus != null && mStatus.equals(LOGIN);
    }
}
