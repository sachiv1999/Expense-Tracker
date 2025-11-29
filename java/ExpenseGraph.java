package com.example.expense;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Map;

public class ExpenseGraph extends AppCompatActivity {

    ImageButton imageButton;
    //private BarChart barChart;
    private PieChart pieChart;
    private DBHelper dbHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_graph);

        imageButton = findViewById(R.id.imageButton);
        //barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
        dbHelper = new DBHelper(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ExpenseGraph.this, MainActivity.class);
                startActivity(i);
            }
        });

        // Load ExampleFragment
        Expense_bottom fragment = new Expense_bottom();
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin and commit transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();


        loadExpenseData();
    }



    private void loadExpenseData() {
        Map<String, Float> categoryMap = dbHelper.getCategoryTotals();
        //setupBarChart(categoryMap);
        setupPieChart(categoryMap);
    }
//    private void setupBarChart(Map<String, Float> categoryMap) {
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        ArrayList<String> categories = new ArrayList<>();
//        int index = 0;
//
//        for (Map.Entry<String, Float> entry : categoryMap.entrySet()) {
//            categories.add(entry.getKey());
//            entries.add(new BarEntry(index++, entry.getValue()));
//        }
//
//        BarDataSet dataSet = new BarDataSet(entries, "Expenses by Category");
//        dataSet.setColors(new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN});
//        dataSet.setValueTextSize(14f);
//
//        BarData data = new BarData(dataSet);
//        barChart.setData(data);
//
//        Description desc = new Description();
//        desc.setText("Amount by Category");
//        barChart.setDescription(desc);
//        barChart.animateY(1500);
//        barChart.invalidate();
//    }

    private void setupPieChart(Map<String, Float> categoryMap) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Float> entry : categoryMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Distribution");
        dataSet.setColors(new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN});
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        Description desc = new Description();
        desc.setText("Expense Percentage by Category");
        pieChart.setDescription(desc);
        pieChart.setUsePercentValues(true);
        pieChart.animateY(1500);
        pieChart.invalidate();
    }
}