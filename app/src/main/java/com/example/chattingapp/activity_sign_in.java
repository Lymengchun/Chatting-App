 package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


 public class activity_sign_in extends AppCompatActivity {


    private TextInputEditText email, password,username;
    private Button btn_login, btn_register;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

     @Override
     protected void onStart() {
         super.onStart();

         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

         //check if user is null
         if (firebaseUser != null){
             startActivity(new Intent(activity_sign_in.this,MainActivity.class));
             finish();
         }
     }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        auth = FirebaseAuth.getInstance();
        email =(TextInputEditText)  findViewById(R.id.Email);
        password =(TextInputEditText)  findViewById(R.id.Password);


        btn_login = findViewById(R.id.id_bt_sign_in);
        btn_register = findViewById(R.id.id_sign_up);





        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();


                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(activity_sign_in.this,"All filed are required",Toast.LENGTH_SHORT).show();

                }else{

                    auth.signInWithEmailAndPassword(txt_email,txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(activity_sign_in.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(activity_sign_in.this,"Authentication failed!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(activity_sign_in.this,activity_sign_up.class));

            }
        });



    }

}