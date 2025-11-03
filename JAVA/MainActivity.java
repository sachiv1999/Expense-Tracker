package com.example.expense;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etExpenseName, etAmount, etCategory, etDate;
    Button btnAddExpense ;
    
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etExpenseName = findViewById(R.id.etExpenseName);
        etAmount = findViewById(R.id.etAmount);
        etCategory = findViewById(R.id.etCategory);
        etDate = findViewById(R.id.etDate);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        // Load ExampleFragment
        Expense_bottom fragment = new Expense_bottom();
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin and commit transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        dbHelper = new DBHelper(this);
       

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
                    
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add expense", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}
