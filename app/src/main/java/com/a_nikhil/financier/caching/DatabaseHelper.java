package com.a_nikhil.financier.caching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.a_nikhil.financier.commons.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "financier";
    private final User user = new User();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(user.getCreateUserTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("drop table if exists " + user.getTableName());
        onCreate(database);
    }

    public boolean insertUser(User user) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(this.user.getNameColumn(), user.getName());
        values.put(this.user.getPhoneColumn(), user.getPhone());
        values.put(this.user.getEmailColumn(), user.getEmail());
        values.put(this.user.getPasswordColumn(), user.getPassword());
        try {
            database.insert(this.user.getTableName(), null, values);
            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseTest", e.getMessage());
            return false;
        }
    }

    public User getUserData() {
        String selectQuery = "select * from " + this.user.getTableName();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        User user = new User();
        if (cursor.moveToFirst()) {
            do {
                user = new User(
                        cursor.getString(cursor.getColumnIndex(this.user.getNameColumn())),
                        cursor.getString(cursor.getColumnIndex(this.user.getEmailColumn())),
                        cursor.getString(cursor.getColumnIndex(this.user.getPhoneColumn())),
                        cursor.getString(cursor.getColumnIndex(this.user.getPasswordColumn()))
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
            String sqlQuery = "update " + this.user.getTableName() + " set " +
                    "name = ?, " +
                    "phone = ?, " +
                    "email = ?, " +
                    "password = ? " +
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
            String sqlQuery = "delete from " + this.user.getTableName()
                    + " where email = ?";
            database.execSQL(sqlQuery, new String[]{user.getEmail()});
            return true;
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseTest", e.getMessage());
            return false;
        }
    }

    public void wipeClean() {
        SQLiteDatabase database = getWritableDatabase();
        try {
            String sqlQuery = "delete from user";
            database.execSQL(sqlQuery, new String[]{user.getEmail()});
            database.close();
        } catch (Exception e) {
            database.close();
            Log.d("DatabaseInteraction", e.getMessage());
        }
    }
}
