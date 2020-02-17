package com.a_nikhil.financier.caching;

class UserDatabase {
    private String tableName = "user";
    private String nameColumn = "name";
    private String emailColumn = "email";
    private String phoneColumn = "phone";
    private String passwordColumn = "password";
    private String firestoreIDColumn = "id";
    private String maxIncomeColumn = "income";
    private String createUserTableSQL = "create table " + tableName + "(" +
            nameColumn + " varchar(30)," +
            emailColumn + " varchar(30)," +
            phoneColumn + " varchar(10)," +
            passwordColumn + " varchar(30)," +
            maxIncomeColumn + " double," +
            firestoreIDColumn + " text" +
            ")";

    String getTableName() {
        return tableName;
    }

    String getNameColumn() {
        return nameColumn;
    }

    String getEmailColumn() {
        return emailColumn;
    }

    String getPhoneColumn() {
        return phoneColumn;
    }

    String getPasswordColumn() {
        return passwordColumn;
    }

    String getCreateUserTableSQL() {
        return createUserTableSQL;
    }

    String getFirestoreIDColumn() {
        return firestoreIDColumn;
    }

    String getMaxIncomeColumn() {
        return maxIncomeColumn;
    }
}
