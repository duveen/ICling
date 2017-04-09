package kr.o3selab.icling.activities.LoadData;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import me.relex.circleindicator.CircleIndicator;

public class LoadDataActivity extends AppCompatActivity {

    @Bind(R.id.load_data_view_pager)
    ViewPager mViewPager;
    @Bind(R.id.load_data_indicator)
    CircleIndicator mCircleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);

        ButterKnife.bind(this);

        mViewPager.setAdapter(new LoadDataAdapter(getSupportFragmentManager(), 4));
        mViewPager.setOnTouchListener(onTouchListener);
        mCircleIndicator.setViewPager(mViewPager);

    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };
}
