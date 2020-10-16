package com.a_nikhil.financier;

import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.Expenditure;

import java.util.ArrayList;

public class DummyExpenditures {
    public ArrayList<Expenditure> getExpenditureDataAsList() {
        ArrayList<Expenditure> expenditures = new ArrayList<>();
        String[] names = {"Hey yo", "Review 2", "Review 2 again", "Nothing"};
        Category[] categories = {Category.SOCIAL, Category.EDUCATION, Category.RECREATION, Category.AMENITIES};
        String[] dates = {"22/9/2020", "1/10/2020", "2/10/2020", "3/10/2020"};
        Double[] amounts = {12.0, 20000.0, 200.0, 50.0};
        for (int i = 0; i < names.length; i++) {
            expenditures.add(new Expenditure(names[i], amounts[i], dates[i], categories[i]));
        }
        return expenditures;
    }
}
