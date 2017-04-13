package kr.o3selab.icling.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.utils.Debug;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_menu_button)
    View menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Debug.d("MainActivity onCreate");
        Debug.d("User = " + Constants.user.toString());
    }
}
