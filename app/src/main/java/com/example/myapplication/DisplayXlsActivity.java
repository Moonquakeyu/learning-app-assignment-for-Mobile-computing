package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class DisplayXlsActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_xls);

        TextView tvXlsContent = findViewById(R.id.tvXlsContent);

        try {
            // Assuming that your Excel file is stored in the assets folder
            InputStream inputStream = getAssets().open("glossary.xls");

            // Create an instance of YourClass (or the actual class where readExcelFile is defined)
            YourClass yourClassInstance = new YourClass();

            // Call the readExcelFile method with the InputStream
            yourClassInstance.readExcelFile(inputStream, tvXlsContent,2,50);

        } catch (Exception e) {
            e.printStackTrace();
            tvXlsContent.setText("Error loading XLS file.");
        }
    }
}
