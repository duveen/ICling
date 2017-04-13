package kr.o3selab.icling.activities.loaddata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.MainActivity;
import kr.o3selab.icling.common.GlobalApplication;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.models.User;
import kr.o3selab.icling.utils.Debug;

public class FinalData extends Fragment {

    private View mView;
    private View mStartButton;

    public FinalData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_final_data, null);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mStartButton = mView.findViewById(R.id.final_data_start_button);
        mStartButton.setOnClickListener(startListener);
    }

    View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setMessage("정보를 저장하고 있습니다.");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();

            User user = Constants.user;

            SharedPreferences sharedPreferences = Constants.getSharedPreferences(getContext());

            user.mHeight = sharedPreferences.getInt(Constants.USER_HEIGHT, -1);
            user.mWeight = sharedPreferences.getInt(Constants.USER_WEIGHT, -1);
            user.mAge = sharedPreferences.getInt(Constants.USER_AGE, -1);
            user.mSex = sharedPreferences.getInt(Constants.USER_SEX, -1);
            user.mRadius = sharedPreferences.getInt(Constants.BIKE_RADIUS, -1);
            user.mRegdate = System.currentTimeMillis();
            user.mUUID = GlobalApplication.getUUID();
            user.mLoginStatus = true;

            Constants.user = user;

            DatabaseReference reference = null;
            if (user.mLoginType.equals(User.GOOGLE)) {
                reference = FirebaseDatabase.getInstance().getReference(Constants.GOOGLE_USER);
            } else if (user.mLoginType.equals(User.KAKAO)) {
                reference = FirebaseDatabase.getInstance().getReference(Constants.KAKAO_USER);
            }

            if (reference != null) {
                reference
                        .child(String.valueOf(user.mUUID))
                        .setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pd.dismiss();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(getContext(), "데이터 저장에 실패했습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                                Debug.e(e.getMessage());
                            }
                        });
            }
        }
    };
}
