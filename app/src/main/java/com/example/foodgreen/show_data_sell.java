package com.example.foodgreen;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class show_data_sell extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase firebaseDatabase;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference sell_data_open_ref = root_ref.child("sell_data_open");
        Log.i("Test", "Inside show_data_sell");
        sell_data_open_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String data_dish_name = ds.child("data_dish_name").getValue(String.class);
                    Log.i("Dish name: ", data_dish_name);
                }
                /*Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String value = map.getClass().toString();
                Log.i("Data: ", value);*/
                /*for (DataSnapshot sell_data_snapshot: dataSnapshot.getChildren()){
                    Log.i("Data: ", sell_data_snapshot.child("sell_data_open").getValue(String.class));
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Canceled: ", "Canceled");
            }
        });
    }
}
