package com.a_nikhil.financier.commons;

public class Expenditure {
    private String title;
    private Double amount;
    private String date;
    private Category category;

    public Expenditure(String title, Double amount, String date, Category category) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }
}
