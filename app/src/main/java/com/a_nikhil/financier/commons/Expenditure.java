package com.a_nikhil.financier.commons;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Expenditure implements Parcelable {
    private String title;
    private Double amount;
    private String date;
    private Category category;

    public Expenditure() {
    }

    public Expenditure(String title, Double amount, String date, Category category) {
        this.title = title;
        this.amount = amount;
        this.date = formatDate(date);
        this.category = category;
    }

    protected Expenditure(Parcel in) {
        title = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readDouble();
        }
        date = in.readString();
        category = Category.valueOf(in.readString());
    }

    public static final Creator<Expenditure> CREATOR = new Creator<Expenditure>() {
        @Override
        public Expenditure createFromParcel(Parcel in) {
            return new Expenditure(in);
        }

        @Override
        public Expenditure[] newArray(int size) {
            return new Expenditure[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        // If date is not of the format "DD/MM/YYYY => format it
        return date.length() != 10 ? formatDate(date) : date;
    }

    public void setDate(String date) {
        this.date = formatDate(date);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @NonNull
    @Override
    public String toString() {
        return "Expenditure{" +
                "title='" + title + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", category=" + category +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        if (amount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(amount);
        }
        parcel.writeString(date);
        parcel.writeString(category.toString());
    }

    private String formatDate(String date) {
        if (date.length() == 10)
            return date;
        String[] dateComponents = date.split("/");
        dateComponents[0] = Integer.parseInt(dateComponents[0]) < 10 ?
                "0" + dateComponents[0] : dateComponents[0];
        dateComponents[1] = Integer.parseInt(dateComponents[1]) < 10 ?
                "0" + dateComponents[1] : dateComponents[1];
        return dateComponents[0] + "/" + dateComponents[1] + "/" + dateComponents[2];
    }
}
