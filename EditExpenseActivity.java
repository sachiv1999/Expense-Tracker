package com.example.expense;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class EditExpenseActivity extends AppCompatActivity {
    EditText edtName, edtAmount, edtCategory, edtDate;
    Button btnUpdate;
    DBHelper dbHelper;

    ImageButton imageButton;
    int expenseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
// Initialize views
        edtName = findViewById(R.id.edtName);
        edtAmount = findViewById(R.id.edtAmount);
        edtCategory = findViewById(R.id.edtCategory);
        edtDate = findViewById(R.id.edtDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(EditExpenseActivity.this, ExpenseList.class);
                startActivity(i);
            }
        });

        dbHelper = new DBHelper(this);

        // Get expense id passed from previous activity
        expenseId = getIntent().getIntExtra("expense_id", -1);

        if (expenseId != -1) {
            loadExpenseData(expenseId);
        }

        // Update button click
        btnUpdate.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String amount = edtAmount.getText().toString().trim();
            String category = edtCategory.getText().toString().trim();
            String date = edtDate.getText().toString().trim();

            if (name.isEmpty() || amount.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.updateExpense(expenseId, name, amount, category, date);
            Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // go back to previous screen
        });
    }

    // Load existing data
    private void loadExpenseData(int id) {
        Cursor cursor = dbHelper.getExpenseById(id);
        if (cursor != null && cursor.moveToFirst()) {
            edtName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            edtAmount.setText(cursor.getString(cursor.getColumnIndexOrThrow("amount")));
            edtCategory.setText(cursor.getString(cursor.getColumnIndexOrThrow("category")));
            edtDate.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            cursor.close();
        }
    }
}
