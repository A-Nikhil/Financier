package com.a_nikhil.financier.caching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.Expenditure;
import com.a_nikhil.financier.commons.User;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "financier";
    private final UserDatabase userDatabase = new UserDatabase();
    private final ExpenditureDatabase expenditureDatabase = new ExpenditureDatabase();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(userDatabase.getCreateUserTableSQL());
        database.execSQL(expenditureDatabase.getCreateExpenditureTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("drop table if exists " + userDatabase.getTableName());
        database.execSQL("drop table if exists " + expenditureDatabase.getTableName());
        onCreate(database);
    }

    // Users
    public boolean insertUser(User user) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(userDatabase.getNameColumn(), user.getName());
        values.put(userDatabase.getPhoneColumn(), user.getPhone());
        values.put(userDatabase.getEmailColumn(), user.getEmail());
        values.put(userDatabase.getPasswordColumn(), user.getPassword());
        values.put(userDatabase.getPasswordColumn(), user.getPassword());
        values.put(userDatabase.getFirestoreIDColumn(), user.getFirestoreID());
        values.put(userDatabase.getMaxIncomeColumn(), user.getMaxIncome());

        // FIXME: 18-02-2020 Update to add expenditures
        try {
            database.insert(userDatabase.getTableName(), null, values);
            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseTest", e.getMessage());
            return false;
        }
    }

    public User getUserData() {
        String selectQuery = "select * from " + userDatabase.getTableName();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        User user = new User();
        if (cursor.moveToFirst()) {
            do {
                user = new User(
                        cursor.getString(cursor.getColumnIndex(userDatabase.getNameColumn())),
                        cursor.getString(cursor.getColumnIndex(userDatabase.getEmailColumn())),
                        cursor.getString(cursor.getColumnIndex(userDatabase.getPhoneColumn())),
                        cursor.getString(cursor.getColumnIndex(userDatabase.getPasswordColumn())),
                        cursor.getDouble(cursor.getColumnIndex(userDatabase.getMaxIncomeColumn())),
                        cursor.getString(cursor.getColumnIndex(userDatabase.getFirestoreIDColumn()))
                );
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return user;
    }

    public boolean updateUser(User user, String identityEmail) {
        SQLiteDatabase database = getWritableDatabase();
        try {
            String sqlQuery = "update " + userDatabase.getTableName() + " set " +
                    "name = ?, " +
                    "phone = ?, " +
                    "email = ?, " +
                    "password = ?, " +
                    "firestoreID = ? " +
                    "where email = ?";
            database.execSQL(
                    sqlQuery,
                    new String[]{
                            user.getName(),
                            user.getPhone(),
                            user.getEmail(),
                            user.getPassword(),
                            identityEmail
                    });
            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseTest", e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase database = getWritableDatabase();
        try {
            String sqlQuery = "delete from " + userDatabase.getTableName()
                    + " where email = ?";
            database.execSQL(sqlQuery, new String[]{user.getEmail()});
            return true;
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseTest", e.getMessage());
            return false;
        }
    }

    // Expenditures
    public boolean insertExpenditure(Expenditure expenditure) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(expenditureDatabase.getTitleColumn(), expenditure.getTitle());
        values.put(expenditureDatabase.getAmountColumn(), expenditure.getAmount());
        values.put(expenditureDatabase.getDateColumn(), expenditure.getDate());
        values.put(expenditureDatabase.getCategoryColumn(), expenditure.getCategory().getDescription());
        try {
            database.insert(expenditureDatabase.getTableName(), null, values);
            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseTest", e.getMessage());
            return false;
        }
    }

    public Expenditure getExpenditureData() {
        String selectQuery = "select * from " + expenditureDatabase.getTableName();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        Expenditure expenditure = new Expenditure();
        if (cursor.moveToFirst()) {
            do {
                expenditure = new Expenditure(
                        cursor.getString(cursor.getColumnIndex(expenditureDatabase.getTitleColumn())),
                        cursor.getDouble(cursor.getColumnIndex(expenditureDatabase.getAmountColumn())),
                        cursor.getString(cursor.getColumnIndex(expenditureDatabase.getDateColumn())),
                        returnCategory(cursor.getString(cursor.getColumnIndex(expenditureDatabase.getCategoryColumn())))
                );
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return expenditure;
    }

    public ArrayList<Expenditure> getExpenditureDataAsList() {
        String selectQuery = "select * from " + expenditureDatabase.getTableName();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        ArrayList<Expenditure> expenditures = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                expenditures.add(new Expenditure(
                        cursor.getString(cursor.getColumnIndex(expenditureDatabase.getTitleColumn())),
                        cursor.getDouble(cursor.getColumnIndex(expenditureDatabase.getAmountColumn())),
                        cursor.getString(cursor.getColumnIndex(expenditureDatabase.getDateColumn())),
                        returnCategory(cursor.getString(cursor.getColumnIndex(expenditureDatabase.getCategoryColumn())))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return expenditures;
    }

    private Category returnCategory(String categoryText) {
        return Category.valueOf(categoryText.toUpperCase());
    }

    public boolean updateUser(Expenditure expenditure, String date, String title) {
        SQLiteDatabase database = getWritableDatabase();
        try {
            String sqlQuery = "update " + expenditureDatabase.getTableName() + " set " +
                    "title = ?, " +
                    "date = ?, " +
                    "amount = ?, " +
                    "category = ?, " +
                    "where email = ? and title = ?";
            database.execSQL(
                    sqlQuery,
                    new String[]{
                            expenditure.getTitle(),
                            expenditure.getDate(),
                            expenditure.getAmount().toString(),
                            expenditure.getCategory().toString(),
                            date, title
                    });
            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseTest", e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(Expenditure expenditure) {
        SQLiteDatabase database = getWritableDatabase();
        try {
            String sqlQuery = "delete from " + expenditureDatabase.getTableName()
                    + " where title = ? and amount = ? and date = ?";
            database.execSQL(sqlQuery,
                    new String[]{expenditure.getTitle(), expenditure.getAmount().toString(),
                            expenditure.getDate()});
            return true;
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseTest", e.getMessage());
            return false;
        }
    }

    // Global
    public boolean wipeClean() {
        SQLiteDatabase database = getWritableDatabase();
        try {
            database.execSQL("delete from user");
            database.execSQL("delete from Expenditure");
            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseInteraction", e.getMessage());
            return false;
        }
    }
}
