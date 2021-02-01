package com.soufianekre.cashnote.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.util.List;

@Entity(tableName = "account")
public class CashAccount implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    int accountId;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "type")
    private int type;
    @ColumnInfo(name = "color")
    private int color;

    @ColumnInfo(name = "transaction_list")
    private List<CashTransaction> transactionsList;


    public CashAccount(int accountId, String name, String description, int type, int color, List<CashTransaction> transactionsList) {
        this.accountId = accountId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.color = color;
        this.transactionsList = transactionsList;
    }



    @Ignore
    public CashAccount() {
    }


    @Ignore
    protected CashAccount(Parcel in) {
        accountId = in.readInt();
        name = in.readString();
        description = in.readString();
        type = in.readInt();
        color = in.readInt();
        transactionsList = in.createTypedArrayList(CashTransaction.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(accountId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(type);
        dest.writeInt(color);
        dest.writeTypedList(transactionsList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CashAccount> CREATOR = new Creator<CashAccount>() {
        @Override
        public CashAccount createFromParcel(Parcel in) {
            return new CashAccount(in);
        }

        @Override
        public CashAccount[] newArray(int size) {
            return new CashAccount[size];
        }
    };

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<CashTransaction> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(List<CashTransaction> transactionsList) {
        this.transactionsList = transactionsList;
    }
}
