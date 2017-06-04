package kr.o3selab.icling.utils;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kr.o3selab.icling.activities.fragment.home.event.HomeDataListener;
import kr.o3selab.icling.common.Constants;
import kr.o3selab.icling.models.RidingData;

public class SynchronizedData {

    public static boolean syncFlag = false;
    public static HomeDataListener homeDataListener = null;

    private static int sumHR = 0;
    private static int sumHRFlag = 0;
    private static long sumTT = 0;
    private static double sumTD = 0;

    public static void setListener(HomeDataListener l) {
        homeDataListener = l;
    }

    public static void sync(final Context context) {
        if (syncFlag) {
            if (homeDataListener != null)
                homeDataListener.onSyncHomeData(sumHR / sumHRFlag, sumTT, sumTD);
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserRidingData/" + Constants.user.mUserID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Debug.d("onDataChange");
                if (dataSnapshot.getValue() == null) return;
                DBHelper helper = DBHelper.getInstance(context);

                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    RidingData item = value.getValue(RidingData.class);
                    if (!helper.findRidingData(item.mStartTime.toString())) helper.insert(item);

                    sumHR += item.mAverageHeartRate;
                    sumHRFlag++;
                    sumTT += item.getTotalTime().time;
                    sumTD += item.mTotalDistance;
                }

                syncFlag = true;
                if (homeDataListener != null)
                    homeDataListener.onSyncHomeData(sumHR / sumHRFlag, sumTT, sumTD);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
