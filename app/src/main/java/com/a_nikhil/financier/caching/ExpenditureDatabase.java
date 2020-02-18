package com.a_nikhil.financier.caching;

class ExpenditureDatabase {
    private String tableName = "Expenditure";
    private String titleColumn = "title";
    private String amountColumn = "amount";
    private String dateColumn = "date";
    private String categoryColumn = "category";
    private String createExpenditureTableSQL = "create table " + tableName + "(" +
            titleColumn + " text," +
            categoryColumn + " varchar(10)," +
            dateColumn + " date," +
            amountColumn + " double" +
            ")";


    String getCreateExpenditureTableSQL() {
        return createExpenditureTableSQL;
    }

    String getTableName() {
        return tableName;
    }

    String getTitleColumn() {
        return titleColumn;
    }

    String getAmountColumn() {
        return amountColumn;
    }

    String getDateColumn() {
        return dateColumn;
    }

    String getCategoryColumn() {
        return categoryColumn;
    }
}
