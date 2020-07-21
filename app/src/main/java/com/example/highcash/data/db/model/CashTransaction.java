package com.example.highcash.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CashTransaction implements Parcelable {

    private String title;
    private int balance;
    private long lastUpdatedDate;
    private long date;
    private int accountSourceId;
    private boolean isExpense;


    public CashTransaction() {
    }

    public CashTransaction(String title, int balance, long lastUpdatedDate, int accountSourceId, long date, boolean isExpense) {
        this.title = title;
        this.balance = balance;
        this.lastUpdatedDate = lastUpdatedDate;
        this.accountSourceId = accountSourceId;
        this.date = date;
        this.isExpense = isExpense;
    }

    protected CashTransaction(Parcel in) {
        title = in.readString();
        balance = in.readInt();
        lastUpdatedDate = in.readLong();
        accountSourceId = in.readInt();
        date = in.readLong();
        isExpense = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(balance);
        dest.writeLong(lastUpdatedDate);
        dest.writeInt(accountSourceId);
        dest.writeLong(date);
        dest.writeByte((byte) (isExpense ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CashTransaction> CREATOR = new Creator<CashTransaction>() {
        @Override
        public CashTransaction createFromParcel(Parcel in) {
            return new CashTransaction(in);
        }

        @Override
        public CashTransaction[] newArray(int size) {
            return new CashTransaction[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public long getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(long lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public int getAccountSourceId() {
        return accountSourceId;
    }

    public void setAccountSourceId(int accountSourceId) {
        this.accountSourceId = accountSourceId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }
}
