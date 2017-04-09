package kr.o3selab.icling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kr.o3selab.icling.Activity.MainActivity;
import kr.o3selab.icling.Models.Constants;

public class LoadingActivity extends AppCompatActivity {

    private boolean isFirst;

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = Constants.getSharedPreferences(this);
        isFirst = sharedPreferences.getBoolean(Constants.APP_FIRST_OPEN, true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if(isFirst) {



        } else {
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
        }

    }
}
