package com.example.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class activity_sign_up extends AppCompatActivity {

    private TextInputEditText username, email, password;
    private Button btn_register;

    //firebase
    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        username =(TextInputEditText) findViewById(R.id.SignUpUsername);
        email =(TextInputEditText) findViewById(R.id.SignUpEmail);
        password =(TextInputEditText) findViewById(R.id.SignUpPassword);

        btn_register = findViewById(R.id.id_bt_create);
        
        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();


                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(activity_sign_up.this,"All filed are required",Toast.LENGTH_SHORT).show();

                }else if (txt_password.length() < 8){
                    Toast.makeText(activity_sign_up.this,"password must be at least 8 characters",Toast.LENGTH_SHORT).show();
                }else {
                    register(txt_username, txt_email , txt_password);
                }
            }
        });
    }

    //register account to database
    private void register( String username, String email,String password){

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           FirebaseUser firebaseUser = auth.getCurrentUser();
                           String userid = firebaseUser.getUid();

                           reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                           HashMap<String, String> hashMap = new HashMap<>();
                           hashMap.put("id", userid);
                           hashMap.put("username",username);
                           hashMap.put("imageURL","default");

                           reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){

                                       Intent intent = new Intent(activity_sign_up.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                   }
                               }
                           });
                       }
                    }
                });

    }

}
