package com.a_nikhil.financier.commons;

import androidx.annotation.NonNull;

public class User {
    private String firestoreID;
    private String name;
    private String email;
    private String phone;
    private String password;
    private Double maxIncome;

    public User() {
        name = "";
        email = "";
        phone = "";
        password = "";
        firestoreID = "";
        maxIncome = 0.0;
    }

    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public User(String firestoreID, String name, String email, String phone, String password) {
        this.firestoreID = firestoreID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public User(String name, String email, String phone, String password, Double maxIncome) {
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

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    private String tableName = "user";
    private String nameColumn = "name";
    private String emailColumn = "email";
    private String phoneColumn = "phone";
    private String passwordColumn = "password";
    private String fireStoreIDColumn = "id";
    private String createUserTableSQL = "create table " + tableName + "(" +
            nameColumn + " varchar(30)," +
            emailColumn + " varchar(30)," +
            phoneColumn + " varchar(10)," +
            passwordColumn + " varchar(30)," +
            fireStoreIDColumn + " text" +
            ")";

    public String getTableName() {
        return tableName;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public String getEmailColumn() {
        return emailColumn;
    }

    public String getPhoneColumn() {
        return phoneColumn;
    }

    public String getPasswordColumn() {
        return passwordColumn;
    }

    public String getCreateUserTableSQL() {
        return createUserTableSQL;
    }

    public String getFireStoreIDColumn() {
        return fireStoreIDColumn;
    }
}
