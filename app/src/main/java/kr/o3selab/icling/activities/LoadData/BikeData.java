package kr.o3selab.icling.activities.loaddata;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.R;

public class BikeData extends Fragment {

    private ViewGroup mContainer;
    private View mView;

    private EditText mRadiusEditText;

    private Integer mRadius;

    public BikeData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = container;
        mView = inflater.inflate(R.layout.fragment_bike_data, null);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 자전거 바퀴 반지름
        mRadiusEditText = (EditText) mView.findViewById(R.id.bike_data_radius);

        // 다음 버튼
        View nextButton = mView.findViewById(R.id.bike_data_next_button);
        nextButton.setOnClickListener(nextButtonListener);

        initalData();
    }

    void initalData() {
        mRadius = 0;

        SharedPreferences sharedPreferences = Constants.getSharedPreferences(getContext());
        int radius = sharedPreferences.getInt(Constants.BIKE_RADIUS, -1);

        if (radius != -1) mRadiusEditText.setText(String.valueOf(radius));
    }

    View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (mRadiusEditText.getText().toString().equals("")) {
                showWarningMessage("자전거 바퀴 반지름을 입력해주세요!");
                return;
            }

            mRadius = Integer.parseInt(mRadiusEditText.getText().toString());

            SharedPreferences.Editor editor = Constants.getEditor(getContext());
            editor.putInt(Constants.BIKE_RADIUS, mRadius);
            editor.commit();

            ViewPager viewPager = (ViewPager) mContainer.findViewById(R.id.load_data_view_pager);
            viewPager.setCurrentItem(3);
        }
    };

    private void showWarningMessage(String message) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton(R.string.default_ok, null)
                .setCancelable(true)
                .show();
    }
}
