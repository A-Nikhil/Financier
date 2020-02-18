package com.a_nikhil.financier.commons;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class User {
    private String firestoreID;
    private String name;
    private String email;
    private String phone;
    private String password;
    private Double maxIncome;
    private HashMap<String, Expenditure> expenditures;

    public User() {
        name = "";
        email = "";
        phone = "";
        password = "";
        firestoreID = "";
        maxIncome = 0.0;
        expenditures = new HashMap<>();
    }

    public User(String name, String email, String phone, String password, Double maxIncome) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.maxIncome = maxIncome;
    }

    public User(String name, String email, String phone, String password, Double maxIncome, String firestoreID) {
        this.firestoreID = firestoreID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.maxIncome = maxIncome;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirestoreID() {
        return firestoreID;
    }

    public void setFirestoreID(String firestoreID) {
        this.firestoreID = firestoreID;
    }

    public Double getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(Double maxIncome) {
        this.maxIncome = maxIncome;
    }

    public HashMap<String, Expenditure> getExpenditures() {
        return expenditures;
    }

    public void setExpenditures(HashMap<String, Expenditure> expenditures) {
        this.expenditures = expenditures;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "firestoreID='" + firestoreID + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", maxIncome=" + maxIncome +
                '}';
    }
}
