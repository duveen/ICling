package kr.o3selab.icling.activities.fragment.record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.models.RidingData;

public class RecordItemFragment extends BaseFragment {

    View mBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_item, null);
        TAG = "RecordItemFragment";

        RidingData ridingData = (RidingData) savedInstanceState.getSerializable("item");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enableBackButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBackButton.setVisibility(View.GONE);
        mBackButton.setOnClickListener(null);
    }

    void enableBackButton() {
        mBackButton = activity.getBackButton();
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeFragment();
            }
        });
    }
}
