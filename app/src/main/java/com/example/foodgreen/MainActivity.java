package com.example.foodgreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    ImageView buybutton, sellbutton;
    android.support.v7.widget.Toolbar toolbar;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.new_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FoodGreen");
        toolbar.setTitleTextColor(Color.BLACK);
        buybutton = findViewById(R.id.buybtn);
        sellbutton = findViewById(R.id.sellbtn);

        // Initialize logged in as false if it is not true
        sp = getSharedPreferences("user_data", MODE_PRIVATE);
        if (sp.contains("LOGGED_IN")){
            //Toast.makeText(getApplicationContext(), sp.getString("username", ""), Toast.LENGTH_SHORT).show();
        }


        // onclick event to show buy page
        buybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buyIntent = new Intent(v.getContext(), BuyView.class);
                startActivity(buyIntent);
            }
        });

        sellbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellIntent = new Intent(v.getContext(), SellView.class);
                startActivity(sellIntent);
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
}
