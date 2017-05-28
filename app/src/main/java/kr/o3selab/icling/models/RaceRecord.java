package kr.o3selab.icling.models;

import android.support.annotation.NonNull;

public class RaceRecord implements Comparable<RaceRecord> {
    private String mAndroidId;
    private Double mTime;

    public RaceRecord() {

    }

    public RaceRecord(String mAndroidId, Double mTime) {
        this.mAndroidId = mAndroidId;
        this.mTime = mTime;
    }

    public String getmAndroidId() {
        return mAndroidId;
    }

    public void setmAndroidId(String mAndroidId) {
        this.mAndroidId = mAndroidId;
    }

    public Double getmTime() {
        return mTime;
    }

    public void setmTime(Double mTime) {
        this.mTime = mTime;
    }

    @Override
    public String toString() {
        return mAndroidId + " " + mTime;
    }

    @Override
    public boolean equals(Object obj) {
        return mAndroidId.equals(((RaceRecord) obj).mAndroidId) && mTime.equals(((RaceRecord) obj).mTime);
    }

    @Override
    public int compareTo(@NonNull RaceRecord o) {
        return this.mTime.compareTo(o.mTime);
    }
}
