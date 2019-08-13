package com.example.foodgreen;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class new_order_sell extends AppCompatActivity implements View.OnClickListener{

    android.support.v7.widget.Toolbar toolbar;
    Button btn_cooked_date, btn_cooked_time, btn_expire_date,btn_expire_time;
    EditText sell_cook_date, sell_cook_time, sell_expire_date, sell_expire_time;
    EditText dish_name, dish_quantity, dish_description;
    EditText dish_price, new_food_category;
    Button submit_order;
    List<String> food_categories = new ArrayList<String>();
    int ExpireHour,ExpireMinute,btnCookedYear,btnCookedDay,btnCookedMonth,btnExpireYear,btnExpireMonth,btnExpireDay,CookHour,CookMinute;
    String data_dish_name, data_price, data_quantity, data_description, data_cook_date, data_cook_time, data_expire_date, data_expire_time;
    Button capureimg;
    ImageView imageView;
    SharedPreferences sp;

    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    public Spinner food_categories_spinner;   // food category spinner
    Long count;   // to save number of food categories in database
    ArrayAdapter<String> food_categories_adapter;   // adapter for spinner
    String food_category_choice = "ENTER NEW CATEGORY";   // the selected category
    String food_category_key;   // to store child key from firebase

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private boolean isUserAuth;

    String user_data_email, user_data_password, user_data_phonenum, user_data_username;

    private Uri filePath;
    String image_name, image_extension;
    private static final String UPLOAD_URL = "http://foodgreen.000webhostapp.com/upload_image.php";
    DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference food_categories_ref = root_ref.child("food_categories");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__sell);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.new_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FoodGreen");
        toolbar.setTitleTextColor(Color.BLACK);

        // If user is not logged in, force user to log in
        sp = getSharedPreferences("user_data", MODE_PRIVATE);
        if (!sp.contains("LOGGED_IN")){
            Toast.makeText(new_order_sell.this, "Please log in to make order", Toast.LENGTH_SHORT).show();
            Intent redirect = new Intent(new_order_sell.this, activity_login.class);
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

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);

        // Assign views
        food_categories.add("ENTER NEW CATEGORY");
        dish_name = findViewById(R.id.enter_dishname);
        dish_quantity = findViewById(R.id.enter_quantity);
        dish_description = findViewById(R.id.enter_description);
        dish_price = findViewById(R.id.enter_price);
        submit_order = findViewById(R.id.submit_button);
        new_food_category = findViewById(R.id.new_food_category);
        new_food_category.setVisibility(View.INVISIBLE);   // initially, set edittext as invisible
        food_categories_spinner = (Spinner) findViewById(R.id.food_categories_spinner);
        food_categories_adapter = new ArrayAdapter<String>(new_order_sell.this, android.R.layout.simple_spinner_item, food_categories);
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

        btn_cooked_date = findViewById(R.id.btn_cooked_date);
        btn_cooked_time = findViewById(R.id.btn_cooked_time);
        btn_expire_date = findViewById(R.id.btn_expire_date);
        btn_expire_time =  findViewById(R.id.btn_expire_time);

        sell_cook_date = findViewById(R.id.sell_cook_date);
        sell_cook_time = findViewById(R.id.sell_cook_time);
        sell_expire_date = findViewById(R.id.sell_expire_date);
        sell_expire_time  =  findViewById(R.id.sell_expire_time);

        imageView = (ImageView) findViewById(R.id.dishimage);
        capureimg = (Button) findViewById(R.id.capureimage);

        btn_cooked_date.setOnClickListener(this);
        btn_cooked_time.setOnClickListener(this);
        btn_expire_date.setOnClickListener(this);
        btn_expire_time.setOnClickListener(this);

        capureimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });

        submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_data();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v == btn_cooked_date){
            final Calendar c = Calendar.getInstance();
            btnCookedYear = c.get(Calendar.YEAR);
            btnCookedMonth = c.get(Calendar.MONTH);
            btnCookedDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            sell_cook_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            Toast.makeText(new_order_sell.this,dayOfMonth + "-" + (monthOfYear + 1) + "-" + year,Toast.LENGTH_LONG).show();
                        }
                    }, btnCookedYear, btnCookedMonth, btnCookedDay);
            datePickerDialog.show();
        }

        if(v == btn_cooked_time){
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            CookHour = c.get(Calendar.HOUR_OF_DAY);
            CookMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            sell_cook_time.setText(hourOfDay + ":" + minute);
                        }
                    }, CookHour, CookMinute, false);
            timePickerDialog.show();


        }
        if (v == btn_expire_date){
            final Calendar c = Calendar.getInstance();
            btnExpireYear = c.get(Calendar.YEAR);
            btnExpireMonth = c.get(Calendar.MONTH);
            btnExpireDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            sell_expire_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            Toast.makeText(new_order_sell.this,dayOfMonth + "-" + (monthOfYear + 1) + "-" + year,Toast.LENGTH_LONG).show();
                        }
                    }, btnCookedYear, btnCookedMonth, btnCookedDay);
            datePickerDialog.show();

        }
        if(v == btn_expire_time){
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            ExpireHour = c.get(Calendar.HOUR_OF_DAY);
            ExpireMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            sell_expire_time.setText(hourOfDay + ":" + minute);
                        }
                    }, CookHour, CookMinute, false);
            timePickerDialog.show();


        }
    }


    private String getPath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id =  cursor.getString(0);
        Log.i("document id: ", document_id);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ",
                new String[]{document_id}, null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        image_extension = path.substring(path.lastIndexOf(".")+1);
        cursor.close();
        return path;
    }

    private void uploadImage(){
        String name = image_name;
        String path = getPath(filePath);
        try {
            String uploadID = UUID.randomUUID().toString();
            new MultipartUploadRequest(this, UPLOAD_URL)
                    .addFileToUpload(path, "image").addParameter("name", name).startUpload();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void submit_data(){
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

        data_dish_name = dish_name.getText().toString();
        data_price = dish_price.getText().toString();
        data_quantity = dish_quantity.getText().toString();
        data_description = dish_description.getText().toString();
        data_cook_time = sell_cook_time.getText().toString();
        data_cook_date = sell_cook_date.getText().toString();
        data_expire_time = sell_expire_time.getText().toString();
        data_expire_date = sell_expire_date.getText().toString();
        //Log.i("flag 1", "flag 1");
        FirebaseUser userId = mAuth.getCurrentUser();
        //Log.e("user ID", "userId :: " + userId);
        //Log.i("flag 2", "flag 2");
        image_name = databaseReference.child("sell_data_open").push().getKey();
        uploadImage();
        model_new_sell_order model = new model_new_sell_order(data_dish_name, data_price, data_quantity, data_description, data_cook_time, data_cook_date, data_expire_time, data_expire_date, image_name + "." + image_extension, food_category_choice, user_data_email, user_data_phonenum, user_data_username);
        databaseReference.child("sell_data_open").push().setValue(model);
        //Log.i("flag 3", "flag 3");
        Toast.makeText(getApplicationContext(), "Order submitted", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(new_order_sell.this, MainActivity.class);
        startActivity(i);
    }

    // fucntion for selecting the image from gallery or camera
    private void selectimage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK){
            if(requestCode==REQUEST_CAMERA){
                Bundle bundle=data.getExtras();
                final Bitmap bmp=(Bitmap)bundle.get("data");
                filePath = data.getData();
                imageView.setImageBitmap(bmp);
            }
            else if(requestCode==SELECT_FILE)
            {
                Uri selectedImageUri=data.getData();
                filePath = data.getData();
                imageView.setImageURI(selectedImageUri);
            }
        }

    }
}
