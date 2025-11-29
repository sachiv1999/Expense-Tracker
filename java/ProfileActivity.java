package com.example.expense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    ImageView imgResult;
    ImageButton back;
    TextView txtName, txtMobile, txtStatus;

    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgResult = findViewById(R.id.imgResult);
        txtName = findViewById(R.id.txtName);
        txtMobile = findViewById(R.id.txtMobile);
        txtStatus = findViewById(R.id.txtStatus);
        back = findViewById(R.id.imageButton);
        logout = findViewById(R.id.logout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences sp = getSharedPreferences("MyUser", MODE_PRIVATE);

        String first = sp.getString("first", "");
        String last = sp.getString("last", "");
        String mobile = sp.getString("mobile", "");
        String status = sp.getString("status", "");
        String image = sp.getString("image", "");

        txtName.setText(first + " " + last);
        txtMobile.setText(mobile);
        txtStatus.setText(status);

        if (!image.isEmpty()) {
            imgResult.setImageURI(Uri.parse(image));
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

}