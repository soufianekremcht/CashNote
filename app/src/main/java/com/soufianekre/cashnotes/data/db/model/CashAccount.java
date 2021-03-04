package com.soufianekre.cashnotes.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cash_account")
public class CashAccount implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "type")
    private int type;
    @ColumnInfo(name = "color")
    private int color;
    @ColumnInfo(name = "created_at")
    private long createdAt;

    public CashAccount(int id, String name, String description, int type, int color, long createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.color = color;
        this.createdAt = createdAt;
    }
    @Ignore
    public CashAccount() {
    }

    @Ignore
    protected CashAccount(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        type = in.readInt();
        color = in.readInt();
        createdAt = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(type);
        dest.writeInt(color);
        dest.writeLong(createdAt);
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
