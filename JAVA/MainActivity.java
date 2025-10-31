package com.example.expense;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;


import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etExpenseName, etAmount, etCategory, etDate;
    Button btnAddExpense,btnHistory, btnGraph ;
    TableLayout tableLayout;
    DBHelper dbHelper;
//    ArrayList<String> expenseList;
//    ArrayAdapter<String> adapter;
//    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etExpenseName = findViewById(R.id.etExpenseName);
        etAmount = findViewById(R.id.etAmount);
        etCategory = findViewById(R.id.etCategory);
        etDate = findViewById(R.id.etDate);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        tableLayout = findViewById(R.id.tableLayout);
        btnHistory = findViewById(R.id.btnHistory);
        btnGraph = findViewById(R.id.graph);

        dbHelper = new DBHelper(this);
        loadExpenses();
        
        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpenseGraph.class);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpenseList.class);
                startActivity(intent);
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etExpenseName.getText().toString();
                String amountStr = etAmount.getText().toString();
                String category = etCategory.getText().toString();
                String date = etDate.getText().toString();

                if (name.isEmpty() || amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                boolean inserted = dbHelper.insertExpense(name, amount, category, date);

                if (inserted) {
                    Toast.makeText(MainActivity.this, "Expense Added", Toast.LENGTH_SHORT).show();
                    etExpenseName.setText("");
                    etAmount.setText("");
                    etCategory.setText("");
                    etDate.setText("");
                    loadExpenses();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add expense", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void loadExpenses() {
        // Remove old rows except header
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }

        Cursor cursor = dbHelper.getAllExpenses();
        if (cursor.moveToFirst()) {
            do {
                TableRow row = new TableRow(this);

                TextView tvName = new TextView(this);
                tvName.setText(cursor.getString(1));

                TextView tvAmount = new TextView(this);
                tvAmount.setText(String.valueOf(cursor.getDouble(2)));

                TextView tvCategory = new TextView(this);
                tvCategory.setText(cursor.getString(3));

                TextView tvDate = new TextView(this);
                tvDate.setText(cursor.getString(4));

                row.addView(tvName);
                row.addView(tvAmount);
                row.addView(tvCategory);
                row.addView(tvDate);

                tableLayout.addView(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}
