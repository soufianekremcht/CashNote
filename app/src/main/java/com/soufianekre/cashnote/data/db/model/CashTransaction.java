package com.soufianekre.cashnote.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "cash_transaction",foreignKeys = @ForeignKey(entity = CashAccount.class,
        parentColumns = "id",
        childColumns = "account_id",
        onDelete = CASCADE))

public class CashTransaction implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    private String name;
    private int balance;
    private String notes;
    @ColumnInfo(name = "last_updated_date")
    private long lastUpdatedDate;
    @ColumnInfo(name = "is_expense")
    private boolean isExpense;
    @ColumnInfo(name = "category")
    private CashCategory category;
    @ColumnInfo(name = "account_id")
    private int accountId;


    public CashTransaction(int id, String name, int balance, String notes, long lastUpdatedDate, boolean isExpense, CashCategory category, int accountId) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.notes = notes;
        this.lastUpdatedDate = lastUpdatedDate;
        this.isExpense = isExpense;
        this.category = category;
        this.accountId = accountId;
    }


    @Ignore
    public CashTransaction() {
    }
    @Ignore
    protected CashTransaction(Parcel in) {
        id = in.readInt();
        name = in.readString();
        balance = in.readInt();
        notes = in.readString();
        lastUpdatedDate = in.readLong();
        isExpense = in.readByte() != 0;
        accountId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(balance);
        dest.writeString(notes);
        dest.writeLong(lastUpdatedDate);
        dest.writeByte((byte) (isExpense ? 1 : 0));
        dest.writeInt(accountId);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(long lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }

    public CashCategory getCategory() {
        return category;
    }

    public void setCategory(CashCategory category) {
        this.category = category;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int account_id) {
        this.accountId = account_id;
    }
}
