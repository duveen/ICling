package kr.o3selab.icling.activities.loaddata;

import android.content.DialogInterface;
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
import android.widget.TextView;

import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.R;

public class UserData extends Fragment {

    private ViewGroup mContainer;
    private View mView;
    private EditText mHeightEditText;
    private EditText mWeightEditText;
    private TextView mAgeTextView;
    private TextView mSexTextView;

    private Integer mHeight;
    private Integer mWeight;
    private Integer mAge;
    private Integer mSex;

    private AlertDialog mAgeAlert;
    private AlertDialog mSexAlert;

    String sexs[] = {"남자", "여자"};

    public UserData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = container;
        mView = inflater.inflate(R.layout.fragment_user_data, null);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 키
        mHeightEditText = (EditText) mView.findViewById(R.id.user_data_height_field);

        // 몸무게
        mWeightEditText = (EditText) mView.findViewById(R.id.user_data_weight_field);

        // 나이
        mAgeTextView = (TextView) mView.findViewById(R.id.user_data_age_field);
        mAgeTextView.setOnClickListener(ageButtonListener);

        // 성별
        mSexTextView = (TextView) mView.findViewById(R.id.user_data_sex_field);
        mSexTextView.setOnClickListener(sexButtonListener);

        // 다음 버튼
        View nextButton = mView.findViewById(R.id.user_data_next_button);
        nextButton.setOnClickListener(nextButtonListener);

        initalData();
    }

    void initalData() {
        mHeight = 0;
        mWeight = 0;
        mAge = 1;
        mSex = 0;

        SharedPreferences sharedPreferences = Constants.getSharedPreferences(getContext());
        int height = sharedPreferences.getInt(Constants.USER_HEIGHT, -1);
        int weight = sharedPreferences.getInt(Constants.USER_WEIGHT, -1);
        int age = sharedPreferences.getInt(Constants.USER_AGE, -1);
        int sex = sharedPreferences.getInt(Constants.USER_SEX, -1);

        if (height != -1) mHeightEditText.setText(String.valueOf(height));
        if (weight != -1) mWeightEditText.setText(String.valueOf(weight));
        if (age != -1) {
            mAge = age;
            mAgeTextView.setText(age + " 세");
        }
        if (sex != -1) {
            mSex = sex;
            mSexTextView.setText(sexs[mSex]);
        }
    }

    View.OnClickListener ageButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String ages[] = new String[100];
            for (int i = 1; i <= 100; i++) {
                ages[i - 1] = String.valueOf(i);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
            builder.setSingleChoiceItems(ages, mAge - 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAge = which + 1;
                    mAgeAlert.dismiss();
                    mAgeTextView.setText(mAge + " 세");
                }
            });

            mAgeAlert = builder.create();
            mAgeAlert.show();
        }
    };


    View.OnClickListener sexButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
            builder.setSingleChoiceItems(sexs, mSex, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSex = which;
                    mSexAlert.dismiss();
                    mSexTextView.setText(sexs[mSex]);
                }
            });

            mSexAlert = builder.create();
            mSexAlert.show();
        }
    };

    View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (mHeightEditText.getText().toString().equals("")) {
                showWarningMessage("키를 입력해주세요!");
                return;
            }

            if (mWeightEditText.getText().toString().equals("")) {
                showWarningMessage("몸무게를 입력해주세요!");
                return;
            }

            if (mAgeTextView.getText().toString().equals("나이")) {
                showWarningMessage("나이를 선택해주세요!");
                return;
            }

            if (mSexTextView.getText().toString().equals("성별")) {
                showWarningMessage("성별을 선택해주세요!");
                return;
            }

            mHeight = Integer.parseInt(mHeightEditText.getText().toString());
            mWeight = Integer.parseInt(mWeightEditText.getText().toString());

            SharedPreferences.Editor editor = Constants.getEditor(getContext());
            editor.putInt(Constants.USER_HEIGHT, mHeight);
            editor.putInt(Constants.USER_WEIGHT, mWeight);
            editor.putInt(Constants.USER_AGE, mAge);
            editor.putInt(Constants.USER_SEX, mSex);
            editor.commit();

            ViewPager viewPager = (ViewPager) mContainer.findViewById(R.id.load_data_view_pager);
            viewPager.setCurrentItem(2);
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
