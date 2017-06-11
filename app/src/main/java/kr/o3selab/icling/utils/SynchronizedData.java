package kr.o3selab.icling.utils;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kr.o3selab.icling.activities.fragment.home.event.HomeDataListener;
import kr.o3selab.icling.activities.fragment.home.event.HomeDistanceListener;
import kr.o3selab.icling.activities.fragment.home.event.HomeKcalListener;
import kr.o3selab.icling.common.Constants;
import kr.o3selab.icling.models.RidingData;

public class SynchronizedData {

    public static volatile SynchronizedData me;
    public HomeDataListener homeDataListener = null;
    public HomeKcalListener homeKcalListener = null;
    public HomeDistanceListener homeDistanceListener = null;

    private int sumHR = 0;
    private int sumHRFlag = 0;
    private long sumTT = 0;
    private double sumTD = 0;

    private State state = State.BEFORE;

    public enum State {BEFORE, SYNCING, SYNC}

    public static SynchronizedData getInstance() {
        if (me == null) me = new SynchronizedData();
        return me;
    }

    public static void removeInstance() {
        if (me != null) me = null;
    }

    public SynchronizedData addHomeDataListener(HomeDataListener l) {
        me.homeDataListener = l;
        return me;
    }

    public void removeHomeDataListener() {
        me.homeDataListener = null;
    }

    public SynchronizedData addHomeKcalListener(HomeKcalListener l) {
        me.homeKcalListener = l;
        return me;
    }

    public void removeHomeKcalListener() {
        me.homeKcalListener = null;
    }

    public SynchronizedData addHomeDistanceListener(HomeDistanceListener l) {
        me.homeDistanceListener = l;
        return me;
    }

    public void removeHomeDistanceListener() {
        me.homeDistanceListener = null;
    }

    public void sync(final Context context) {
        Debug.d("SyncState: " + state.toString());
        if (me.state.equals(State.SYNC)) {
            if (me.homeDataListener != null)
                me.homeDataListener.onSyncHomeData(sumHR / sumHRFlag, sumTT, sumTD);
            if (me.homeKcalListener != null)
                me.homeKcalListener.onSyncKcalData();
            if (me.homeDistanceListener != null)
                me.homeDistanceListener.onSyncDistanceData();
            return;
        }

        if (me.state.equals(State.SYNCING)) {
            return;
        }

        me.state = State.SYNCING;

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

                    if (item.mAverageHeartRate != 0) {
                        sumHR += item.mAverageHeartRate;
                        sumHRFlag++;
                    }
                    sumTT += item.getTotalTime().time;
                    sumTD += item.mTotalDistance;
                }

                me.state = State.SYNC;

                if (me.homeDataListener != null)
                    me.homeDataListener.onSyncHomeData(sumHR / sumHRFlag, sumTT, sumTD);
                if (me.homeKcalListener != null)
                    me.homeKcalListener.onSyncKcalData();
                if (me.homeDistanceListener != null)
                    me.homeDistanceListener.onSyncDistanceData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
