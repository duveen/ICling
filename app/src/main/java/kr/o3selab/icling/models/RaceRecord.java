package kr.o3selab.icling.models;

import android.support.annotation.NonNull;

public class RaceRecord implements Comparable<RaceRecord> {
    private String mUserId;
    private Double mTime;

    public RaceRecord() {

    }

    public RaceRecord(String mUserId, Double mTime) {
        this.mUserId = mUserId;
        this.mTime = mTime;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public Double getmTime() {
        return mTime;
    }

    public void setmTime(Double mTime) {
        this.mTime = mTime;
    }

    @Override
    public String toString() {
        return mUserId + " " + mTime;
    }

    @Override
    public boolean equals(Object obj) {
        return mUserId.equals(((RaceRecord) obj).mUserId) && mTime.equals(((RaceRecord) obj).mTime);
    }

    @Override
    public int compareTo(@NonNull RaceRecord o) {
        return this.mTime.compareTo(o.mTime);
    }
}
