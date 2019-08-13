package com.example.foodgreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyView extends AppCompatActivity {

    ListView food;
    ArrayList<String> dish_name_array = new ArrayList<String>();
    ArrayList<String> price_array = new ArrayList<String>();
    ArrayList<Integer> price_array_long = new ArrayList<Integer>();   // to store price in long
    int max_price;   // it will be used to make price filter
    ArrayList<String> quantity_array = new ArrayList<String>();
    ArrayList<String> location_array = new ArrayList<String>();
    ArrayList<String> images_array = new ArrayList<String>();
    ArrayList<String> key_array = new ArrayList<String>();   // To store parent key values
    String[] dish, price, quantity, location, images;
    List<String> food_categories = new ArrayList<String>();
    Long count;
    public String last_food_category;   // just to store last food category and later add it to adapter

    android.support.v7.widget.Toolbar toolbar;
    ImageView homeButton, sellButton, neworderbutton, filter;
    Spinner foodcategory;
    TextView pricetext;
    SeekBar pricebar;
    Button okButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyview);
        food=(ListView) findViewById(R.id.foodlist);
        toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.new_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FoodGreen");

        homeButton = findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home_intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(home_intent);
            }
        });

        sellButton = findViewById(R.id.sellbtn);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sell_intent = new Intent(v.getContext(), SellView.class);
                startActivity(sell_intent);
            }
        });
        neworderbutton = findViewById(R.id.addbtn);
        neworderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_order_button = new Intent(v.getContext(), Activity_buy.class);
                startActivity(new_order_button);
            }
        });


        filter = findViewById(R.id.filterbtn);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog mBuilder = new AlertDialog.Builder(BuyView.this).create();
                View mView =getLayoutInflater().inflate(R.layout.filterview,null);
                mBuilder.setTitle("Filter");


                foodcategory=(Spinner)mView.findViewById(R.id.foodcategory);
                pricebar=(SeekBar)mView.findViewById(R.id.pricebar);
                pricetext=(TextView) mView.findViewById(R.id.pricetext);
                okButton = (Button) mView.findViewById(R.id.okButton);
                cancelButton = (Button) mView.findViewById(R.id.cancelButton);

                pricebar.setMax(max_price);
                pricebar.setProgress(max_price);
                pricetext.setText(String.valueOf(pricebar.getProgress()));
                pricebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        pricetext.setText(String.valueOf(pricebar.getProgress()));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                food_categories.clear();
                FirebaseDatabase firebaseDatabase;
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference food_categories_ref = root_ref.child("food_categories");
                food_categories_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){

                            String food_category_key = ds.getKey().toString();
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


                //ArrayAdapter<String> adapter=new ArrayAdapter<String>(BuyView.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.foodmenu));
                food_categories.add("SELECT CATEGORY");
                ArrayAdapter<String> foodAdapter = new ArrayAdapter<String>(BuyView.this, android.R.layout.simple_spinner_item, food_categories);
                foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                foodcategory.setAdapter(foodAdapter);
                foodAdapter.notifyDataSetChanged();


                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            FirebaseDatabase firebaseDatabase;
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
                            final String food_category = foodcategory.getSelectedItem().toString();

                            Log.i("selected category", food_category);
                            Log.i("dish name array size", String.valueOf(dish_name_array.size()));

                            DatabaseReference sell_data_filter_ref = root_ref.child("sell_data_open");

                            //Query query = root_ref.child("sell_data_open").orderByChild("food_category").equalTo(food_category);

                            sell_data_filter_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    // first case is that both filters are selected
                                    if (!food_category.equals("SELECT CATEGORY") &&
                                            Integer.parseInt(pricetext.getText().toString()) < max_price) {
                                        // dataSnapshot is the "issue" node with all children with id 0
                                        Log.i("WHERE? :", "INSIDE FIRST LOOP");
                                        dish_name_array.clear();
                                        price_array.clear();
                                        quantity_array.clear();
                                        images_array.clear();
                                        location_array.clear();
                                        key_array.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if ((Integer.parseInt(ds.child("data_dish_price").getValue(String.class)) <= Integer.parseInt(pricetext.getText().toString()))
                                                && food_category.equals(ds.child("food_category").getValue(String.class)))
                                            {
                                                dish_name_array.add(ds.child("data_dish_name").getValue(String.class));
                                                price_array.add(ds.child("data_dish_price").getValue(String.class));
                                                quantity_array.add(ds.child("data_dish_quantity").getValue(String.class));
                                                images_array.add(ds.child("image_name").getValue(String.class));
                                                location_array.add("Dalhousie University");
                                                key_array.add(ds.getKey().toString());
                                            }
                                        }

                                        int count;
                                        dish = new String[dish_name_array.size()];
                                        price = new String[dish_name_array.size()];
                                        quantity = new String[dish_name_array.size()];
                                        location = new String[dish_name_array.size()];
                                        images = new String[dish_name_array.size()];

                                        Log.i("Count: ", Integer.toString(dish_name_array.size()));
                                        for (count = 0; count < dish_name_array.size(); count++) {
                                            dish[count] = dish_name_array.get(count);
                                            price[count] = price_array.get(count);
                                            quantity[count] = quantity_array.get(count);
                                            location[count] = location_array.get(count);
                                            images[count] = images_array.get(count);
                                        }
                                        CustomListView customListView = new CustomListView();
                                        food.setAdapter(customListView);
                                        customListView.notifyDataSetChanged();

                                        // onclick event of item in listview

                                        food.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent confirm_buy = new Intent(view.getContext(), confirm_order_buyer.class);
                                                confirm_buy.putExtra("parent_value", key_array.get(position));
                                                startActivity(confirm_buy);

                                            }
                                        });
                                        }

                                    // second case if that ONLY food category is selected
                                    else if(!food_category.equals("SELECT CATEGORY")){
                                        Log.i("WHERE? :", "INSIDE SECOND LOOP");
                                        dish_name_array.clear();
                                        price_array.clear();
                                        quantity_array.clear();
                                        images_array.clear();
                                        location_array.clear();
                                        key_array.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            Log.i("Catrgory: ", food_category);
                                            if (food_category.equals(ds.child("food_category").getValue(String.class)))
                                            {
                                                //Log.i("Test", "INSIDE sell data");
                                                dish_name_array.add(ds.child("data_dish_name").getValue(String.class));
                                                price_array.add(ds.child("data_dish_price").getValue(String.class));
                                                quantity_array.add(ds.child("data_dish_quantity").getValue(String.class));
                                                images_array.add(ds.child("image_name").getValue(String.class));
                                                location_array.add("Dalhousie University");
                                                key_array.add(ds.getKey().toString());
                                            }
                                        }

                                        int count;
                                        dish = new String[dish_name_array.size()];
                                        price = new String[dish_name_array.size()];
                                        quantity = new String[dish_name_array.size()];
                                        location = new String[dish_name_array.size()];
                                        images = new String[dish_name_array.size()];

                                        Log.i("Count: ", Integer.toString(dish_name_array.size()));
                                        for (count = 0; count < dish_name_array.size(); count++) {
                                            dish[count] = dish_name_array.get(count);
                                            price[count] = price_array.get(count);
                                            quantity[count] = quantity_array.get(count);
                                            location[count] = location_array.get(count);
                                            images[count] = images_array.get(count);
                                        }
                                        CustomListView customListView = new CustomListView();
                                        food.setAdapter(customListView);
                                        customListView.notifyDataSetChanged();

                                        // onclick event of item in listview

                                        food.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent confirm_buy = new Intent(view.getContext(), confirm_order_buyer.class);
                                                confirm_buy.putExtra("parent_value", key_array.get(position));
                                                startActivity(confirm_buy);

                                            }
                                        });
                                    }

                                    // Third case is ONLY price seekbar is selected
                                    else if (Integer.parseInt(pricetext.getText().toString()) < max_price){
                                        Log.i("WHERE? :", "INSIDE THIRD LOOP");
                                        dish_name_array.clear();
                                        price_array.clear();
                                        quantity_array.clear();
                                        images_array.clear();
                                        location_array.clear();
                                        key_array.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if ((Integer.parseInt(ds.child("data_dish_price").getValue(String.class)) <= Integer.parseInt(pricetext.getText().toString())))
                                            {
                                                //Log.i("Test", "INSIDE sell data");

                                                dish_name_array.add(ds.child("data_dish_name").getValue(String.class));
                                                price_array.add(ds.child("data_dish_price").getValue(String.class));
                                                quantity_array.add(ds.child("data_dish_quantity").getValue(String.class));
                                                images_array.add(ds.child("image_name").getValue(String.class));
                                                location_array.add("Dalhousie University");
                                                key_array.add(ds.getKey().toString());
                                            }
                                        }

                                        int count;
                                        dish = new String[dish_name_array.size()];
                                        price = new String[dish_name_array.size()];
                                        quantity = new String[dish_name_array.size()];
                                        location = new String[dish_name_array.size()];
                                        images = new String[dish_name_array.size()];

                                        Log.i("Count: ", Integer.toString(dish_name_array.size()));
                                        for (count = 0; count < dish_name_array.size(); count++) {
                                            dish[count] = dish_name_array.get(count);
                                            price[count] = price_array.get(count);
                                            quantity[count] = quantity_array.get(count);
                                            location[count] = location_array.get(count);
                                            images[count] = images_array.get(count);
                                        }
                                        CustomListView customListView = new CustomListView();
                                        food.setAdapter(customListView);
                                        customListView.notifyDataSetChanged();

                                        // onclick event of item in listview

                                        food.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent confirm_buy = new Intent(view.getContext(), confirm_order_buyer.class);
                                                confirm_buy.putExtra("parent_value", key_array.get(position));
                                                startActivity(confirm_buy);

                                            }
                                        });
                                    }

                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(BuyView.this,
                                    "Filter applied",Toast.LENGTH_SHORT).show();
                            mBuilder.dismiss();

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBuilder.dismiss();
                    }
                });

                mBuilder.setView(mView);
                mBuilder.show();
            }
        });



        FirebaseDatabase firebaseDatabase;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference sell_data_open_ref = root_ref.child("sell_data_open");

        sell_data_open_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("Test", "INSIDE sell data");
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    dish_name_array.add(ds.child("data_dish_name").getValue(String.class));
                    price_array.add(ds.child("data_dish_price").getValue(String.class));
                    price_array_long.add(Integer.parseInt(ds.child("data_dish_price").getValue(String.class)));
                    quantity_array.add(ds.child("data_dish_quantity").getValue(String.class));
                    images_array.add(ds.child("image_name").getValue(String.class));
                    location_array.add("Dalhousie University");
                    key_array.add(ds.getKey().toString());
                }

                int count;
                max_price = Collections.max(price_array_long);
                Log.i("Max price", String.valueOf(max_price));
                dish = new String[dish_name_array.size()];
                price = new String[dish_name_array.size()];
                quantity = new String[dish_name_array.size()];
                location = new String[dish_name_array.size()];
                images = new String[dish_name_array.size()];

                Log.i("Count: ", Integer.toString(dish_name_array.size()));
                for (count=0; count < dish_name_array.size(); count++){
                    dish[count] = dish_name_array.get(count);
                    price[count] = price_array.get(count);
                    quantity[count] = quantity_array.get(count);
                    location[count] = location_array.get(count);
                    images[count] = images_array.get(count);
                }
                CustomListView customListView=new CustomListView();
                food.setAdapter(customListView);
                customListView.notifyDataSetChanged();

                // onclick event of item in listview

                food.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent confirm_buy = new Intent(view.getContext(), confirm_order_buyer.class);
                        confirm_buy.putExtra("parent_value", key_array.get(position));
                        startActivity(confirm_buy);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void openDialog(){

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent login_intent = new Intent(this, activity_login.class);
        startActivity(login_intent);
        return super.onOptionsItemSelected(item);
    }
    class CustomListView extends BaseAdapter{

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view=getLayoutInflater().inflate(R.layout.buy_list_layout,null);
            
            ImageView mImageView=(ImageView)view.findViewById(R.id.imageview);
            TextView  dishname=(TextView) view.findViewById(R.id.dishname);
            TextView  priceval=(TextView) view.findViewById(R.id.valprice);
            TextView  quantityval=(TextView) view.findViewById(R.id.valquantity);
            TextView  locationval=(TextView) view.findViewById(R.id.vallocation);

            Picasso.get().load("http://foodgreen.000webhostapp.com/images/" + images[position]).into(mImageView);
            dishname.setText(dish[position]);
            priceval.setText(price[position]);
            quantityval.setText(String.valueOf(quantity[position]));
            locationval.setText(location[position]);
            return view;
        }
    }
}