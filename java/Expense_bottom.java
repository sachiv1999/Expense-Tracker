package com.example.expense;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class Expense_bottom extends Fragment {
    ImageButton list, add, graph;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_expense_bottom, container, false);

        // Initialize buttons
        list = view.findViewById(R.id.btnlist);
        add = view.findViewById(R.id.btnadd);
        graph = view.findViewById(R.id.btngraph);

        // Handle button clicks
        list.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ExpenseList.class);
            startActivity(intent);
        });

        add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        graph.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ExpenseGraph.class);
            startActivity(intent);
        });

        return view;

    }

}