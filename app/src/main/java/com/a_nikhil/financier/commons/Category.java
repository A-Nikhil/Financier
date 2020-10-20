package com.a_nikhil.financier.commons;

public enum Category {

    FOOD("food", 1, 1),
    HOUSEHOLD("household", 1, 2),
    SOCIAL("social", 3, 3),
    WORK("work", 1, 4),
    AMENITIES("amenities", 2, 5),
    RECREATION("recreation", 3, 6),
    TRAVEL("travel", 2, 7),
    EDUCATION("education", 1, 8);

    private final String description;
    private final int category;
    private final int index;

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

    public static Category getCategoryFromIndex(int index) {
        Category[] myCategories = {FOOD, HOUSEHOLD, SOCIAL, WORK, AMENITIES, RECREATION, TRAVEL, EDUCATION};
        return myCategories[index - 1];
    }
}
