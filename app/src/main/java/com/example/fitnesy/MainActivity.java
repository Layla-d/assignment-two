package com.example.fitnesy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.prefs.PreferenceChangeEvent;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String NAME ="NAME";
    public static final String WEIGHT ="WEIGHT";
    public static final String HEIGHT ="HEIGHT";
    public static final String FLAG = "FLAG";
    public Spinner gender;
    public EditText name;
    public EditText weight;
    public EditText height;
    public Button save;
    public Button BMI;
    public Button timer;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gender = findViewById(R.id.spinner);
        name = findViewById(R.id.editTextTextPersonName);
        weight = findViewById(R.id.editTextTextPersonName3);
        height = findViewById(R.id.editTextTextPersonName2);
        save = findViewById(R.id.button);
        BMI = findViewById(R.id.button6);
        timer = findViewById(R.id.button8);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.Gender, R.layout.color_spinner_layout);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        gender.setAdapter(arrayAdapter);
        gender.setOnItemSelectedListener(this);
//        setupViews();
        setupSharedPrefs();
        checkPreferences();

//
        BMI.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Double weightInt = Double.parseDouble(weight.getText().toString());
                double heightInt = Double.parseDouble(height.getText().toString());

                double bmi = weightInt / (heightInt * heightInt);
                String outputBMI = String.valueOf(bmi);
                String output = String.format("%.2f", Double.parseDouble(outputBMI));
                Toast toast = Toast.makeText(MainActivity.this, "Your BMI is " + output, Toast.LENGTH_LONG);
                toast.show();

            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameSaved = name.getText().toString();
                String heightSaved = height.getText().toString();
                String weighSaved = weight.getText().toString();
                editor.putString(NAME, nameSaved);
                editor.putString(HEIGHT, heightSaved);
                editor.putString(WEIGHT, weighSaved);
                editor.commit();

            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namePassed = name.getText().toString();
                Intent intent = new Intent(MainActivity.this, Timer.class);
                intent.putExtra("data", namePassed);
                startActivity(intent);
            }
        });
        
    }

    private void checkPreferences() {
        boolean flag = preferences.getBoolean(FLAG, false);
        if(flag){
            String nameSaved = preferences.getString(NAME, "");
            String heightSaved = preferences.getString(HEIGHT, "");
            String weightSaved = preferences.getString(WEIGHT, "");
            name.setText(nameSaved);
            height.setText(heightSaved);
            weight.setText(weightSaved);
        }
    }

//    private void setupViews() {
//
//    }

    private void setupSharedPrefs() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
     Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}