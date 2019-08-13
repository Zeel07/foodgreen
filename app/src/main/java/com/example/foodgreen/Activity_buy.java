package com.example.foodgreen;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Activity_buy extends AppCompatActivity implements View.OnClickListener {
    android.support.v7.widget.Toolbar toolbar;
    Button btn_expected_date, btn_expected_time;
    EditText buy_expected_date, buy_expected_time;
    EditText dish_name, dish_quantity, dish_description, new_food_category;
    int expectedHour,expectedMinute,btnexpectedYear,btnExpectedMonth,btnExpectedDay;
    String data_dish_name, data_quantity, data_description, data_expected_time, data_expected_date;
    Button submit_order;

    SharedPreferences sp;

    public Spinner food_categories_spinner;   // food category spinner
    Long count;   // to save number of food categories in database
    ArrayAdapter<String> food_categories_adapter;   // adapter for spinner
    String food_category_choice = "ENTER NEW CATEGORY";   // the selected category
    String food_category_key;   // to store child key from firebase
    List<String> food_categories = new ArrayList<String>();

    String user_data_email, user_data_password, user_data_phonenum, user_data_username;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private boolean isUserAuth;
    DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference food_categories_ref = root_ref.child("food_categories");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.new_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FoodGreen");
        toolbar.setTitleTextColor(Color.BLACK);

        // If user is not logged in, redirect user to login
        sp = getSharedPreferences("user_data", MODE_PRIVATE);
        if (!sp.contains("LOGGED_IN")){
            Toast.makeText(Activity_buy.this, "Please log in to make order", Toast.LENGTH_SHORT).show();
            Intent redirect = new Intent(Activity_buy.this, activity_login.class);
            startActivity(redirect);
        }
        else {
            user_data_email = sp.getString("email", "");
            user_data_password = sp.getString("password", "");
            user_data_phonenum = sp.getString("phonenum", "");
            user_data_username = sp.getString("username", "");
        }

        // fetch categories from database
        food_categories_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    food_category_key = ds.getKey().toString();
                    Log.i("KEY: ", ds.getKey().toString());
                    Log.i("CHILDREN COUNT: ", String.valueOf(ds.getChildrenCount()));
                    count = ds.getChildrenCount();
                    for (long i=0; i<count; i++) {
                        Log.i("Category: ", ds.child(String.valueOf(i)).getValue(String.class));
                        food_categories.add(ds.child(String.valueOf(i)).getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // assign ids
        food_categories.add("ENTER NEW CATEGORY");
        dish_name = findViewById(R.id.buy_dishname);
        dish_quantity = findViewById(R.id.edit_quantity);
        dish_description = findViewById(R.id.editDescription);
        submit_order = findViewById(R.id.buy_place_order);
        btn_expected_date = findViewById(R.id.btn_expected_date);
        btn_expected_time =  findViewById(R.id.btn_expected_time);
        buy_expected_date = findViewById(R.id.buy_expected_date);
        buy_expected_time =  findViewById(R.id.buy_expected_time);

        btn_expected_date.setOnClickListener(this);
        btn_expected_time.setOnClickListener(this);


        // when submit button placed
        submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_order();
            }
        });
        new_food_category = findViewById(R.id.new_food_category);
        new_food_category.setVisibility(View.INVISIBLE);   // initially, set edittext as invisible
        food_categories_spinner = (Spinner) findViewById(R.id.food_categories_spinner_buy);
        food_categories_adapter = new ArrayAdapter<String>(Activity_buy.this, android.R.layout.simple_spinner_item, food_categories);
        food_categories_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food_categories_spinner.setAdapter(food_categories_adapter);
        food_categories_adapter.notifyDataSetChanged();
        food_categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                food_category_choice = food_categories_spinner.getItemAtPosition(position).toString();
                Log.i("TAG: ", "INSIDE LISTENER");
                Log.i("CHOICE: ", food_categories_spinner.getSelectedItem().toString());
                if (food_category_choice.equals("ENTER NEW CATEGORY")){
                    new_food_category.setVisibility(View.VISIBLE);
                } else {
                    new_food_category.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == btn_expected_date){
            final Calendar c = Calendar.getInstance();
            btnexpectedYear = c.get(Calendar.YEAR);
            btnExpectedMonth = c.get(Calendar.MONTH);
            btnExpectedDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            buy_expected_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            Toast.makeText(Activity_buy.this,dayOfMonth + "-" + (monthOfYear + 1) + "-" + year,Toast.LENGTH_LONG).show();
                        }
                    }, btnexpectedYear, btnExpectedMonth, btnExpectedDay);
            datePickerDialog.show();

        }
        if(v == btn_expected_time){
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            expectedHour = c.get(Calendar.HOUR_OF_DAY);
            expectedMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            buy_expected_time.setText(hourOfDay + ":" + minute);
                        }
                    }, expectedHour, expectedMinute, false);
            timePickerDialog.show();


        }
    }

    public void submit_order(){
        if (food_category_choice.equals("ENTER NEW CATEGORY") && new_food_category.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Please enter new food category or select from drop down", Toast.LENGTH_SHORT).show();
            return;
        }
        if (food_category_choice.equals("ENTER NEW CATEGORY") && !new_food_category.getText().toString().equals("")){
            food_category_choice = new_food_category.getText().toString();

            // New category needs to be added in firebase
            DatabaseReference update_food_category = FirebaseDatabase.getInstance().getReference("food_categories").child(food_category_key);
            update_food_category.child(String.valueOf(count)).setValue(food_category_choice);
        }

        // assign edittext values
        data_dish_name = dish_name.getText().toString();
        data_quantity = dish_quantity.getText().toString();
        data_description = dish_description.getText().toString();
        data_expected_time = buy_expected_time.getText().toString();
        data_expected_date = buy_expected_date.getText().toString();

        // save into model
        model_activity_buy model = new model_activity_buy(data_dish_name, data_quantity, data_description, data_expected_time, data_expected_date, food_category_choice, user_data_email, user_data_phonenum, user_data_username);

        // create userId
        FirebaseUser userId = mAuth.getCurrentUser();

        // enter into database
        databaseReference.child("buy_data_open").push().setValue(model);
        Toast.makeText(getApplicationContext(), "Order submitted", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Activity_buy.this, MainActivity.class);
        startActivity(i);
    }
}
