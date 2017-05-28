package kr.o3selab.icling.activities.fragment.ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.layout.RankItemLayout;
import kr.o3selab.icling.models.RaceRecord;

public class LevelFragment extends BaseFragment {

    @BindView(R.id.ranking_parent)
    LinearLayout parentLayout;

    @BindView(R.id.ranking_list)
    LinearLayout listLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking_content, null);
        ButterKnife.bind(this, view);

        String map = getArguments().getString("map", "");
        if (map.equals("")) return view;

        String level = getArguments().getString("level", "");
        if (level.equals("")) return view;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Record/" + map + "/" + level);

        Query query = reference.orderByChild("mTime").limitToFirst(20);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<RaceRecord> raceRecords = new LinkedList<>();

                if (dataSnapshot.getValue() == null) {
                    parentLayout.removeAllViewsInLayout();
                    TextView textView = new TextView(getContext());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextSize(15);
                    textView.setText("검색 결과가 존재하지 않습니다.");
                    textView.setTextColor(getResources().getColor(R.color.font_gray));
                    textView.setGravity(Gravity.CENTER);
                    parentLayout.addView(textView);
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RaceRecord record = snapshot.getValue(RaceRecord.class);
                    raceRecords.add(record);
                }
                Collections.sort(raceRecords);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        drawRankList(raceRecords);
                    }
                }).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void drawRankList(List<RaceRecord> sortedList) {
        for (int i = 0; i < sortedList.size(); i++) {
            final int fi = i;
            RaceRecord record = sortedList.get(i);
            final RankItemLayout itemLayout = new RankItemLayout(getContext(), fi + 1, record);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listLayout.addView(itemLayout, fi);
                }
            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
