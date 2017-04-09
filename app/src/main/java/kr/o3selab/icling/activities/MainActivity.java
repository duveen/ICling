package kr.o3selab.icling.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kr.o3selab.icling.R;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.models.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = Constants.user;

        Constants.printLog(user.mLoginType + " " + user.mUserEmail);
    }
}
