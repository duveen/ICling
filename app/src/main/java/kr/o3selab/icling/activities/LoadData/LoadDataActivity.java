package kr.o3selab.icling.activities.loaddata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.utils.Debug;
import me.relex.circleindicator.CircleIndicator;

public class LoadDataActivity extends FragmentActivity {

    @Bind(R.id.load_data_view_pager)
    ViewPager mViewPager;
    @Bind(R.id.load_data_indicator)
    CircleIndicator mCircleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);
        Debug.d("LoadDataActivity onCreate");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
