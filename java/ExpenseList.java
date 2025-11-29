package com.example.expense;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class ExpenseList extends AppCompatActivity {
    TableLayout tableLayout;
    DBHelper dbHelper;
    Spinner spinnerFilter;
    TextView tvTotalExpense;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        tableLayout = findViewById(R.id.tableLayoutExpenses);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        imageButton = findViewById(R.id.imageButton);

        // Load ExampleFragment
        Expense_bottom fragment = new Expense_bottom();
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin and commit transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ExpenseList.this, MainActivity.class);
                startActivity(i);
            }
        });

        dbHelper = new DBHelper(this);

        // Filter options
        ArrayList<String> filters = new ArrayList<>();
        filters.add("All");
        filters.add("This Week");
        filters.add("This Month");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, filters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        // Default load all
        loadExpenses("All");

        // Spinner listener for sorting
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                loadExpenses(selected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
    private void loadExpenses(String filter) {
        // Remove old rows except header
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }

        Cursor cursor = dbHelper.getFilteredExpenses(filter);
        double total = 0.0;

        if (cursor.moveToFirst()) {
            do {
                TableRow row = new TableRow(this);

                int id = cursor.getInt(0);

                TextView tvName = new TextView(this);
                tvName.setText(cursor.getString(1));

                TextView tvAmount = new TextView(this);
                double amount = cursor.getDouble(2);
                total += amount;
                tvAmount.setText(String.valueOf(cursor.getDouble(2)));

                TextView tvCategory = new TextView(this);
                tvCategory.setText(cursor.getString(3));

                TextView tvDate = new TextView(this);
                tvDate.setText(cursor.getString(4));

                android.widget.Button btnDelete = new android.widget.Button(this);
                btnDelete.setText("Delete");
                btnDelete.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                btnDelete.setTextColor(android.graphics.Color.WHITE);

                // Handle delete click
                btnDelete.setOnClickListener(v -> {
                    dbHelper.deleteExpense(id);  // Delete from database
                    loadExpenses(filter);        // Refresh table
                });

                android.widget.Button btnEdit = new android.widget.Button(this);
                btnEdit.setText("Edit");
                btnEdit.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.BLUE));
                btnEdit.setTextColor(android.graphics.Color.WHITE);

                btnEdit.setOnClickListener(v -> {
                    // Example: Open an edit dialog or a new activity to edit the expense
                    Intent intent = new Intent(this, EditExpenseActivity.class);
                    intent.putExtra("expense_id", id); // Pass ID to edit screen
                    startActivity(intent);
                });

                row.addView(tvName);
                row.addView(tvAmount);
                row.addView(tvCategory);
                row.addView(tvDate);
                row.addView(btnDelete);
                row.addView(btnEdit);// Add button at end of row

                tableLayout.addView(row);
            } while (cursor.moveToNext());
        }
        cursor.close();

        tvTotalExpense.setText("Total: ₹" + String.format("%.2f", total));
    }

}