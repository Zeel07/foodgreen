package com.example.foodgreen;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class bid_sell extends AppCompatActivity {
    TextView name,contact,address;
    ImageView homebutton, buybutton;
    EditText bidvalue;
    Button bid;
    String parent_key;   // To store intent value which is parent value
    String buyercontactNo,sellercontactNo;
    String buyermessage,sellermessage;
    android.support.v7.widget.Toolbar toolbar;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =1 ;

    SharedPreferences sp;
    String user_data_email, user_data_password, user_data_phonenum, user_data_username;
    String data_buyer_email, data_buyer_phonenum, data_buyer_username;
    TextView show_dish_name, show_description, show_expected_time, show_expected_date, show_quantity, show_food_category;  // textviews to show data
    long bid_count = 0;
    String data_dish_name, data_description, data_expected_date, data_expected_time, data_quantity, data_food_category;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference buy_data_open_ref = root_ref.child("buy_data_open");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_bid);
        toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.new_bar);
        setSupportActionBar(toolbar);
        name=(TextView)findViewById(R.id.buyerrname);
        contact=(TextView)findViewById(R.id.buyerrcontact);
        address=(TextView)findViewById(R.id.buyeraddress);
        bidvalue=(EditText)findViewById(R.id.etbid);
        bid=(Button) findViewById(R.id.bidbutton);
        show_dish_name = findViewById(R.id.dishname);
        show_description = findViewById(R.id.orderdescription);
        show_expected_time = findViewById(R.id.expectedtime);
        show_expected_date = findViewById(R.id.expecteddate);
        show_quantity = findViewById(R.id.quantity);
        show_food_category = findViewById(R.id.food_category);

        // If user is not logged in, redirect user to login
        sp = getSharedPreferences("user_data", MODE_PRIVATE);
        if (!sp.contains("LOGGED_IN")){
            Toast.makeText(bid_sell.this, "Please log in to make order", Toast.LENGTH_SHORT).show();
            Intent redirect = new Intent(bid_sell.this, activity_login.class);
            startActivity(redirect);
        }
        else {
            user_data_email = sp.getString("email", "");
            user_data_password = sp.getString("password", "");
            user_data_phonenum = sp.getString("phonenum", "");
            user_data_username = sp.getString("username", "");
        }

        Intent intent = getIntent();
        parent_key = intent.getStringExtra("parent_value");
        DatabaseReference get_order_data_ref = buy_data_open_ref.child(parent_key);
        get_order_data_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    bid_count = dataSnapshot.child("bid_data").getChildrenCount();
                } catch (Exception e){
                    e.printStackTrace();
                }
                data_dish_name = dataSnapshot.child("data_dish_name").getValue(String.class);
                data_description = dataSnapshot.child("data_description").getValue(String.class);
                data_expected_time = dataSnapshot.child("data_expected_time").getValue(String.class);
                data_expected_date = dataSnapshot.child("data_expected_date").getValue(String.class);
                data_quantity = dataSnapshot.child("data_quantity").getValue(String.class);
                data_food_category = dataSnapshot.child("food_category").getValue(String.class);
                data_buyer_email = dataSnapshot.child("user_data_email").getValue(String.class);
                data_buyer_phonenum = dataSnapshot.child("user_data_phonenum").getValue(String.class);
                data_buyer_username = dataSnapshot.child("user_data_username").getValue(String.class);
                show_dish_name.setText(data_dish_name);
                show_description.setText(data_description);
                show_expected_time.setText(data_expected_time);
                show_expected_date.setText(data_expected_date);
                show_quantity.setText(data_quantity);
                show_food_category.setText(data_food_category);
                name.setText(data_buyer_username);
                contact.setText(data_buyer_phonenum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bidvalue.getText().toString().equals("")){
                    Toast.makeText(bid_sell.this, "Please enter bid value!", Toast.LENGTH_SHORT).show();
                    return;
                }
                model_bid modelBid = new model_bid(user_data_email, user_data_phonenum, user_data_username, "Dalhousie University", bidvalue.getText().toString());
                buy_data_open_ref.child(parent_key).child("bid_data").child(String.valueOf(bid_count)).setValue(modelBid);

                sendConfirmationMessage();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(buyercontactNo, null, buyermessage, null, null);
                smsManager.sendTextMessage(sellercontactNo, null, sellermessage, null, null);
                Toast.makeText(getApplicationContext(), "Order Confirmed",
                        Toast.LENGTH_LONG).show();
                Intent redirect = new Intent(bid_sell.this, SellView.class);
                startActivity(redirect);
            }
        });
    }
    protected void sendConfirmationMessage(){
        buyercontactNo = data_buyer_phonenum;
        sellercontactNo = user_data_phonenum;
        buyermessage = "Your order with dish name: "+ data_dish_name +" has new bid and details of the bid are\n" +
                "Seller name : "+ user_data_username + "\n" +
                "Address :"+ "Park Victoria" +"\n"+
                "Contact :"+ user_data_phonenum +"\n"+
                "Bid value: " + bidvalue.getText().toString() + "\n\n" + "Thanks for using FoodGreen.";
        sellermessage = "You added a bid for dish name: " + data_dish_name + " \n" +
                "Name of buyer: "+ data_buyer_username +"\n" +
                "Address :"+ "Park Victoria" +"\n"+
                "Contact :" + data_buyer_phonenum +"\n"+
                "Bid value: " + bidvalue.getText().toString() + "\n\n" + "Thanks for using FoodGreen.";;
        Log.i("message",buyermessage);
        Log.i("message",sellermessage);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){

                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


}

