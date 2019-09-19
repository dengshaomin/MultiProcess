package com.code.multiprocess;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MessageModel implements Parcelable {

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel source) {
            return new MessageModel(source);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    private long index;

    private long time;

    public MessageModel(long index, long time) {
        this.index = index;
        this.time = time;
    }

    protected MessageModel(Parcel in) {
        this.index = in.readLong();
        this.time = in.readLong();
    }
    public void readFromParcel(Parcel dest){
        this.index = dest.readLong();
        this.time = dest.readLong();
    }
    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.index);
        dest.writeLong(this.time);
    }

    @NonNull
    @Override
    public String toString() {
        return this.index + "   " + this.time;
    }
}
