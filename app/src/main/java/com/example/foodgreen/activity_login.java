package com.example.foodgreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_login extends AppCompatActivity implements View.OnClickListener{
    android.support.v7.widget.Toolbar toolbar;
    private static final String TAG = "EmailPassword";
    private TextView create_acc, forgot_pass;
    private Button btnLogin;
    private EditText Email, Password;
    public String email;
    private String password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth_data_fetch = FirebaseAuth.getInstance();
    DatabaseReference root_ref = FirebaseDatabase.getInstance().getReference();
    SharedPreferences sp;   // to store user data locally which will be used in whole app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);
        create_acc = findViewById(R.id.textcreate);
        forgot_pass = findViewById(R.id.textforgotpass);
        btnLogin = findViewById(R.id.loginButton);
        Email = findViewById(R.id.login_email);
        Password = findViewById(R.id.login_passw);
        toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.new_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FoodGreen");
        toolbar.setTitleTextColor(Color.BLACK);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    // User is signed in
                }
                else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        btnLogin.setOnClickListener(this);

        create_acc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_login.this, activity_register.class);
                startActivity(intent);
            }

        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_login.this, activity_register.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view){
        if (view == btnLogin) {
            email = Email.getText().toString();
            password = Password.getText().toString();

            if (email.length() == 0) {
                Email.setError("Enter email address");
            } else if (password.length() == 0) {
                Password.setError("Enter password");
            } else {
                btnLogin.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                signIn(email, password);
            }

        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void signIn(String email, String password) {
        email = Email.getText().toString();
        password = Password.getText().toString();
        Log.d(TAG, "signIn:" + email);
        final String finalEmail = email;
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(activity_login.this, "EmailId or Password is incorrect",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(activity_login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            sp = getSharedPreferences("user_data", MODE_PRIVATE);

                            root_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                                        Log.i("CHILD: ", String.valueOf(ds.getChildrenCount()));


                                        try{
                                            if (ds.child("email").getValue().toString().equals(finalEmail)){
                                                sp.edit().putBoolean("LOGGED_IN", true).apply();
                                                sp.edit().putString("email", finalEmail).apply();
                                                sp.edit().putString("password", ds.child("password").getValue().toString()).apply();
                                                sp.edit().putString("phonenum", ds.child("phonenum").getValue().toString()).apply();
                                                sp.edit().putString("username", ds.child("username").getValue().toString()).apply();
                                                //Log.i("PASSWORD: ", ds.child("password").getValue().toString());
                                                break;
                                            }
                                        } catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

}
