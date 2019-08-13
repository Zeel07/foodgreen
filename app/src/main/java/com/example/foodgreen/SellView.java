package com.example.foodgreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

public class SellView extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    ListView food;

    // create arraylist to store dynamically
    ArrayList<String> dish_name_array = new ArrayList<String>();
    ArrayList<String> location_array = new ArrayList<String>();
    ArrayList<String> quantity_array = new ArrayList<String>();
    ArrayList<String> time_array = new ArrayList<String>();
    ArrayList<String> date_array = new ArrayList<String>();
    ArrayList<String> key_array = new ArrayList<String>();   // to store parent value. It will be passed with Intent
    String[] dish, quantity, location, time, date;
    List<String> food_categories = new ArrayList<String>();
    Long count;

    ImageView homebutton, buybutton, neworderbutton, filter;
    Spinner foodcategory;
    TextView pricetext, pricemax;
    SeekBar pricebar;
    Button okButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellview);
        toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.new_bar);
        setSupportActionBar(toolbar);
        food=(ListView) findViewById(R.id.foodlist);

        homebutton = findViewById(R.id.homebtn);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home_button = new Intent(v.getContext(), MainActivity.class);
                startActivity(home_button);
            }
        });

        buybutton = findViewById(R.id.buybtn);
        buybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buy_button = new Intent(v.getContext(), BuyView.class);
                startActivity(buy_button);
            }
        });

        neworderbutton = findViewById(R.id.addbtn);
        neworderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_order_button = new Intent(v.getContext(), new_order_sell.class);
                startActivity(new_order_button);
            }
        });

        filter=findViewById(R.id.filterbtn);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog mBuilder=new AlertDialog.Builder(SellView.this).create();
                View mView =getLayoutInflater().inflate(R.layout.filterview,null);
                mBuilder.setTitle("Filter");

                foodcategory=(Spinner)mView.findViewById(R.id.foodcategory);
                pricebar=(SeekBar)mView.findViewById(R.id.pricebar);
                pricetext=(TextView) mView.findViewById(R.id.pricetext);
                pricemax = (TextView) mView.findViewById(R.id.pricemax);

                // make price seekbars invisible as there is not filter on filter on seller side
                pricebar.setVisibility(View.INVISIBLE);
                pricetext.setVisibility(View.INVISIBLE);
                pricemax.setVisibility(View.INVISIBLE);
                okButton = (Button) mView.findViewById(R.id.okButton);
                cancelButton = (Button) mView.findViewById(R.id.cancelButton);

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
                ArrayAdapter<String> foodAdapter = new ArrayAdapter<String>(SellView.this, android.R.layout.simple_spinner_item, food_categories);
                foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                foodcategory.setAdapter(foodAdapter);
                foodAdapter.notifyDataSetChanged();

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!foodcategory.getSelectedItem().toString().equalsIgnoreCase("SELECT CATEGORY"))
                        {

                            FirebaseDatabase firebaseDatabase;
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
                            String food_category = foodcategory.getSelectedItem().toString();

                            CustomListView customListView=new CustomListView();
                            food.setAdapter(null);
                            customListView.notifyDataSetChanged();

                            dish_name_array.clear();
                            quantity_array.clear();
                            location_array.clear();
                            key_array.clear();

                            Log.i("selecte category", food_category);
                            Log.i("dish name array size", String.valueOf(dish_name_array.size()));
                            Query query = root_ref.child("buy_data_open").orderByChild("food_category").equalTo(food_category);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // dataSnapshot is the "issue" node with all children with id 0
                                        dish_name_array.clear();
                                        quantity_array.clear();
                                        location_array.clear();
                                        key_array.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            Log.i("Test", "INSIDE sell data");

                                            dish_name_array.add(ds.child("data_dish_name").getValue(String.class));
                                            quantity_array.add(ds.child("data_quantity").getValue(String.class));
                                            location_array.add("Dalhousie University");
                                            key_array.add(ds.getKey().toString());
                                        }

                                        int count;
                                        dish = new String[dish_name_array.size()];
                                        quantity = new String[dish_name_array.size()];
                                        location = new String[dish_name_array.size()];

                                        Log.i("Count: ", Integer.toString(dish_name_array.size()));
                                        for (count = 0; count < dish_name_array.size(); count++) {
                                            dish[count] = dish_name_array.get(count);
                                            quantity[count] = quantity_array.get(count);
                                            location[count] = location_array.get(count);
                                        }
                                        CustomListView customListView = new CustomListView();
                                        food.setAdapter(customListView);
                                        customListView.notifyDataSetChanged();

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Toast.makeText(SellView.this,
                                    "Filter applied",Toast.LENGTH_SHORT).show();
                            mBuilder.dismiss();
                        }
                        else {
                            mBuilder.dismiss();
                        }
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

        // fetching data part
        FirebaseDatabase firebaseDatabase;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference buy_data_open_ref = root_ref.child("buy_data_open");

        buy_data_open_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    dish_name_array.add(ds.child("data_dish_name").getValue(String.class));
                    quantity_array.add(ds.child("data_quantity").getValue(String.class));
                    time_array.add(ds.child("data_expected_time").getValue(String.class));
                    date_array.add(ds.child("data_expected_date").getValue(String.class));
                    location_array.add("Dalhousie University");
                    key_array.add(ds.getKey().toString());
                    //Log.i("Key value: ", ds.getKey().toString());   // To get parent value
                }

                int count;
                dish = new String[dish_name_array.size()];
                quantity = new String[dish_name_array.size()];
                location = new String[dish_name_array.size()];
                time = new String[dish_name_array.size()];
                date = new String[dish_name_array.size()];
                for (count=0; count < dish_name_array.size(); count++){
                    dish[count] = dish_name_array.get(count);
                    time[count] = time_array.get(count);
                    date[count] = date_array.get(count);
                    quantity[count] = quantity_array.get(count);
                    location[count] = location_array.get(count);
                }

                CustomListView customListView=new CustomListView();
                food.setAdapter(customListView);
                customListView.notifyDataSetChanged();
                // onclick event of item in listview

                food.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent confirm_bid = new Intent(view.getContext(), bid_sell.class);
                        confirm_bid.putExtra("parent_value", key_array.get(position));
                        startActivity(confirm_bid);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
    class CustomListView extends BaseAdapter {

        @Override
        public int getCount() {
            return dish.length;
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

            View view=getLayoutInflater().inflate(R.layout.sell_list_layout,null);

            TextView dishname=(TextView) view.findViewById(R.id.dishname);
            TextView quantityval=(TextView) view.findViewById(R.id.valquantity);
            TextView locationval=(TextView) view.findViewById(R.id.vallocation);
            TextView timeval = (TextView) view.findViewById(R.id.valexpectedtime);
            TextView dateval = (TextView) view.findViewById(R.id.valexpecteddate);

            //mImageView.setImageResource(images[position]);
            dishname.setText(dish[position]);
            quantityval.setText(String.valueOf(quantity[position]));
            locationval.setText(location[position]);
            timeval.setText(time[position]);
            dateval.setText(date[position]);
            return view;
        }
    }
}
