package com.soufianekre.cashnote.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "balance_history")
public class BalanceHistory implements Parcelable {


    @ColumnInfo(name ="value")
    private int value;

    @PrimaryKey
    @ColumnInfo(name="days")
    private int days;

    @ColumnInfo(name ="year")
    private int year;

    public BalanceHistory() {
    }




    @Ignore
    protected BalanceHistory(Parcel in) {
        value = in.readInt();
        days = in.readInt();
        year = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value);
        dest.writeInt(days);
        dest.writeInt(year);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BalanceHistory> CREATOR = new Creator<BalanceHistory>() {
        @Override
        public BalanceHistory createFromParcel(Parcel in) {
            return new BalanceHistory(in);
        }

        @Override
        public BalanceHistory[] newArray(int size) {
            return new BalanceHistory[size];
        }
    };


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
