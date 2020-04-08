package com.a_nikhil.financier.commons;

import androidx.annotation.NonNull;

public class User {
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
        maxIncome = 0.0;
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

    public Double getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(Double maxIncome) {
        this.maxIncome = maxIncome;
    }

    // FIXME: 08-04-2020 add getter and setter to array

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", maxIncome=" + maxIncome +
                '}';
    }
}
