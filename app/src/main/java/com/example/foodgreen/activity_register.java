package com.example.foodgreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_register extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Emailpassword";
    android.support.v7.widget.Toolbar toolbar;
    private EditText Reg_Uname, Reg_pnum, Reg_pass, Reg_email, Reg_repass;
    Button btn_reg;

    private String username, email, password, phonenum, repassword, userId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private boolean isUserAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        Log.i("Message", "activity_register called");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Reg_Uname = findViewById(R.id.edituname);
        Reg_pnum = findViewById(R.id.editpnum);
        Reg_email = findViewById(R.id.editemail);
        Reg_pass = findViewById(R.id.editpass);
        btn_reg = findViewById(R.id.registerbutton);
        Reg_repass = findViewById(R.id.editrepass);
        toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.new_bar);
        setSupportActionBar(toolbar);

        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view == btn_reg){
            registerUser();
        }
    }

    private void registerUser(){
        username = Reg_Uname.getText().toString();
        email = Reg_email.getText().toString();
        phonenum = Reg_pnum.getText().toString();
        password = Reg_pass.getText().toString();
        repassword = Reg_repass.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (username.length() == 0 || username.length() > 50){
            Reg_Uname.setError("Please enter valid name!");
        } else if (email.length() == 0){
            Reg_email.setError("Please enter Email");
        }  else if (!email.matches(emailPattern)) {
            Reg_email.setError("Enter Valid Email");
        } else if (password.length() == 0) {
            Reg_pass.setError("Enter valid password");
        } else if (repassword.length() == 0) {
            Reg_repass.setError("Enter valid password");
        } else if (!password.equals(repassword)) {
            Reg_repass.setError("Enter valid password");
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity_register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.e(TAG, "signup ==>> onComplete: ");

                // if task is not successful
                if (!task.isSuccessful()){
                    Log.e("-->>>", "---->> " + task.getException());
                    if (task.getException().toString().contains("The email address is already in use by another account")) {
                        Toast.makeText(activity_register.this, email + " The email address is already in use by another account.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "signup onComplete:111 ");
                    Toast.makeText(activity_register.this, "User successfully register",
                            Toast.LENGTH_SHORT).show();
                    createUser();

                }
            }
        });
    }

    public void createUser(){
        username = Reg_Uname.getText().toString();
        email = Reg_email.getText().toString();
        phonenum = Reg_pnum.getText().toString();
        password = Reg_pass.getText().toString();
        model_register model = new model_register(email,password,username,phonenum);

        FirebaseUser userId = mAuth.getCurrentUser();
        Log.e(TAG, "userId :: " + userId);

        databaseReference.child(userId.getUid()).setValue(model);
        Intent i = new Intent(activity_register.this, activity_login.class);
        startActivity(i);

    }
}
