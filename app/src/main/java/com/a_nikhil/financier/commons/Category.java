package com.a_nikhil.financier.commons;

public enum Category {

    FOOD("food", 1, 1),
    RENT("rent", 1, 2),
    MORTGAGE("mortgage", 1, 3),
    HOUSEHOLD("household", 1, 4),
    CASUAL("casual", 3, 5),
    WORK("work", 1, 6),
    OUTDOORS("outdoors", 2, 7),
    RECREATION("recreation", 3, 8),
    TRAVEL("travel", 2, 9),
    STATIONARY("stationary", 1, 10),
    EDUCATION("education", 1, 11);

    private String description;
    private int category;
    private int index;

    Category(String description, int category, int index) {
        this.description = description;
        this.category = category;
        this.index = index;
    }

    public String getDescription() {
        return description.toUpperCase();
    }

    public int getCategory() {
        return category;
    }

    public int getIndex() {
        return index;
    }

    public Category getCategoryFromIndex(int index) {
        Category[] myCategories = {FOOD, RENT, MORTGAGE, HOUSEHOLD, CASUAL, WORK, OUTDOORS,
                RECREATION, TRAVEL, STATIONARY, EDUCATION};
        return myCategories[index - 1];
    }
}
