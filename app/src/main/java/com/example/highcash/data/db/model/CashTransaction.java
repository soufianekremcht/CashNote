package com.example.highcash.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CashTransaction implements Parcelable {

    private String name;
    private int balance;
    private long lastUpdatedDate;
    private int accountSourceId;
    private boolean isExpense;
    private String accountParentName;
    private TransactionCategory category;
    private String notes;


    public CashTransaction() {
    }

    public CashTransaction(String name, int balance, long lastUpdatedDate, int accountSourceId, boolean isExpense, String accountParentName, TransactionCategory category, String notes) {
        this.name = name;
        this.balance = balance;
        this.lastUpdatedDate = lastUpdatedDate;
        this.accountSourceId = accountSourceId;
        this.isExpense = isExpense;
        this.accountParentName = accountParentName;
        this.category = category;
        this.notes = notes;
    }

    protected CashTransaction(Parcel in) {
        name = in.readString();
        balance = in.readInt();
        lastUpdatedDate = in.readLong();
        accountSourceId = in.readInt();
        isExpense = in.readByte() != 0;
        accountParentName = in.readString();
        notes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(balance);
        dest.writeLong(lastUpdatedDate);
        dest.writeInt(accountSourceId);
        dest.writeByte((byte) (isExpense ? 1 : 0));
        dest.writeString(accountParentName);
        dest.writeString(notes);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }

    public String getAccountParentName() {
        return accountParentName;
    }

    public void setAccountParentName(String accountParentName) {
        this.accountParentName = accountParentName;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
