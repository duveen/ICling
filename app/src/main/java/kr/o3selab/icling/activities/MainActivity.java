package kr.o3selab.icling.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import kr.o3selab.icling.R;
import kr.o3selab.icling.models.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.printLog("MainActivity onCreate");
        Constants.printLog("User = " + Constants.user.toString());
    }
}
