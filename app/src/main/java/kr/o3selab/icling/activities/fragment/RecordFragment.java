package kr.o3selab.icling.activities.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.record.RecordItemFragment;
import kr.o3selab.icling.activities.fragment.setting.ProfileFragment;
import kr.o3selab.icling.models.RidingData;

public class RecordFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, null);

        View item = view.findViewById(R.id.record_item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordItemFragment fragment = new RecordItemFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", new RidingData());
                fragment.setArguments(bundle);
                fragment.setActivity(activity);
                activity.addFragment(fragment, true);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setTitle("주행기록");
    }
}
