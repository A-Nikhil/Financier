package com.a_nikhil.financier.commons;

import java.util.HashMap;

public class CategoryColorMap {
    private HashMap<Category, String> colorMap;
    private String[] categories;

    public CategoryColorMap() {
        colorMap = new HashMap<>();
        colorMap.put(Category.valueOf("FOOD"), "#F4D03F");
        colorMap.put(Category.valueOf("HOUSEHOLD"), "#C39BD3");
        colorMap.put(Category.valueOf("SOCIAL"), "#48C9B0");
        colorMap.put(Category.valueOf("WORK"), "#5DADE2");
        colorMap.put(Category.valueOf("AMENITIES"), "#B3B6B7");
        colorMap.put(Category.valueOf("RECREATION"), "#F0B27A");
        colorMap.put(Category.valueOf("TRAVEL"), "#E59866");
        colorMap.put(Category.valueOf("EDUCATION"), "#D98880");

        categories = new String[]{"FOOD", "HOUSEHOLD", "SOCIAL", "WORK", "AMENITIES",
                "RECREATION", "TRAVEL", "EDUCATION"};
    }

    public HashMap<Category, String> getColorMap() {
        return colorMap;
    }

    public String[] getCategories() {
        return categories;
    }
}
