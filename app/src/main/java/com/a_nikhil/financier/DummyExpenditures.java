package com.a_nikhil.financier;

import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.Expenditure;

import java.util.ArrayList;
import java.util.Random;

public class DummyExpenditures {
    public ArrayList<Expenditure> getExpenditureDataAsList() {
        ArrayList<Expenditure> expenditures = new ArrayList<>();
        String[] names = {"Hey yo", "Review 2", "Review 2 again", "Nothing"};
        Category[] categories = {Category.SOCIAL, Category.EDUCATION, Category.RECREATION, Category.AMENITIES};
        String[] dates = {"22/9/2020", "1/10/2020", "2/10/2020", "3/10/2020"};
        Double[] amounts = {12.0, 10000.0, 8000.0, 5000.0};
        for (int i = 0; i < names.length; i++) {
            expenditures.add(new Expenditure(names[i], amounts[i], dates[i], categories[i]));
        }
        return expenditures;
    }

    public ArrayList<Expenditure> getExpenditureDataAsList(int a) {
        int max = 20000, min = 1000;
        ArrayList<Expenditure> expenditures = new ArrayList<>();
        for (int i=1; i<=31; i++) {
            int random_int = (int) (Math.random() * (max - min + 1) + min);
            expenditures.add(new Expenditure("", (double)random_int, i + "/10/2020", Category.AMENITIES));
        }
        return expenditures;
    }
}
