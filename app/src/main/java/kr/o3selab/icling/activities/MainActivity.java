package kr.o3selab.icling.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kr.o3selab.icling.R;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.utils.Debug;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Debug.d("MainActivity onCreate");
        Debug.d("User = " + Constants.user.toString());
    }
}
