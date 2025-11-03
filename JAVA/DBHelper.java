package com.example.expense;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ExpenseDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "expenses";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "amount REAL, " +
                "category TEXT, " +
                "date TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertExpense(String name, double amount, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("amount", amount);
        cv.put("category", category);
        cv.put("date", date);
        long result = db.insert(TABLE_NAME, null, cv);
        return result != -1;
    }

    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
    }

    //expenselist sort week and monnth wise.
    public Cursor getFilteredExpenses(String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query ;

        if (filter.equals("This Week")) {
            // last 7 days
            query = "SELECT * FROM expenses WHERE date >= date('now', '-7 days')";
        } else if (filter.equals("This Month")) {
            // current month
            query = "SELECT * FROM expenses WHERE strftime('%m', date) = strftime('%m', 'now') AND strftime('%Y', date) = strftime('%Y', 'now')";
        } else {
            query = "SELECT * FROM expenses";
        }

        return db.rawQuery(query, null);
    }


    // Fetch total amount grouped by category
    public Map<String, Float> getCategoryTotals() {
        Map<String, Float> categoryTotals = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT category, SUM(amount) as total FROM " + TABLE_NAME + " GROUP BY category";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                float total = cursor.getFloat(1);
                categoryTotals.put(category, total);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryTotals;
    }

    //delete data row
    public boolean deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("expenses", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public Cursor getExpenseById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM expenses WHERE id=?", new String[]{String.valueOf(id)});
    }
    public void updateExpense(int id, String title, String amount, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", title);
        values.put("amount", amount);
        values.put("category", category);
        values.put("date", date);
        db.update("expenses", values, "id=?", new String[]{String.valueOf(id)});
    }
}
