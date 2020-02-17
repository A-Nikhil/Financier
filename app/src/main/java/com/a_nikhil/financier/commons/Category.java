package com.a_nikhil.financier.commons;

public enum Category {

    FOOD("food", 1),
    RENT("rent", 1),
    MORTGAGE("mortgage", 1),
    HOUSEHOLD("household", 1),
    CASUAL("casual", 3),
    WORK("word", 1),
    OUTDOORS("outdoors", 2),
    RECREATION("recreation", 3),
    TRAVEL("travel", 2),
    STATIONARY("stationary", 1),
    EDUCATION("education", 1);

    private String description;
    private int category;

    Category(String description, int category) {
        this.description = description;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }
}
