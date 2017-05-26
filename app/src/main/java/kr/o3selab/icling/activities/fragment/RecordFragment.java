package kr.o3selab.icling.activities.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.record.RecordItemFragment;
import kr.o3selab.icling.models.RidingData;
import kr.o3selab.icling.utils.DBHelper;

public class RecordFragment extends BaseFragment {

    @BindView(R.id.record_list)
    LinearLayout mItemListLayout;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm");

    private LayoutInflater mInflater;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, null);
        ButterKnife.bind(this, view);

        mInflater = inflater;
        initData();

        return view;
    }

    private void initData() {
        DBHelper helper = DBHelper.getInstance(getContext());

        final Vector<RidingData> datas = helper.getRidingData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int flag = 0;
                for (final RidingData data : datas) {
                    final int iFlag = flag++;
                    final View item = mInflater.inflate(R.layout.fragment_record_list_item, null);

                    LinearLayout parent = (LinearLayout) item.findViewById(R.id.record_item);

                    TextView dateView = (TextView) item.findViewById(R.id.record_list_item_date);
                    dateView.setText(mDateFormat.format(new Date(data.mStartTime)));

                    TextView timeView = (TextView) item.findViewById(R.id.record_list_item_time);
                    timeView.setText(mTimeFormat.format(new Date(data.mStartTime)));

                    TextView dTimeView = (TextView) item.findViewById(R.id.record_list_item_d_time);
                    RidingData.RTime rTime = data.getRidingTime();
                    dTimeView.setText(rTime.toString());

                    TextView distanceView = (TextView) item.findViewById(R.id.record_list_item_d_distance);
                    distanceView.setText(String.format("%.2f", data.mTotalDistance));

                    parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecordItemFragment fragment = new RecordItemFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("item", data);
                            fragment.setArguments(bundle);
                            fragment.setActivity(activity);
                            activity.addFragment(fragment, true);
                        }
                    });
                    parent.setEnabled(true);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mItemListLayout.addView(item, iFlag);
                        }
                    });
                }
            }
        }).start();


    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setTitle("주행기록");
    }


}
