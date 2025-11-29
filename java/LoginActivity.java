package com.example.expense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    ImageView imgProfile;
    EditText edtFirst, edtLast, edtMobile;
    Spinner spnStatus;
    Button btnUpload, btnSubmit;

    Uri imageUri;
    String[] statusList = {"Customer", "Businessman"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imgProfile = findViewById(R.id.imgProfile);
        edtFirst = findViewById(R.id.edtFirst);
        edtLast = findViewById(R.id.edtLast);
        edtMobile = findViewById(R.id.edtMobile);
        spnStatus = findViewById(R.id.spnStatus);
        btnUpload = findViewById(R.id.btnUpload);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set Spinner Data
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusList);
        spnStatus.setAdapter(adapter);

        // Upload Image Button
        btnUpload.setOnClickListener(v -> selectImage());

        // Submit Button
        btnSubmit.setOnClickListener(v -> sendData());
    }

    // Select Image
    private void selectImage() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, 100);
    }

    // Set image in ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imgProfile.setImageURI(imageUri);
        }
    }

    private void sendData() {

        String first = edtFirst.getText().toString();
        String last = edtLast.getText().toString();
        String mobile = edtMobile.getText().toString();
        String status = spnStatus.getSelectedItem().toString();
        String image = imageUri != null ? imageUri.toString() : "";

        // Save data in SharedPreferences
        SharedPreferences sp = getSharedPreferences("MyUser", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        ed.putString("first", first);
        ed.putString("last", last);
        ed.putString("mobile", mobile);
        ed.putString("status", status);
        ed.putString("image", image);
        ed.apply();

        // Move to MainActivity
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);

    }
}