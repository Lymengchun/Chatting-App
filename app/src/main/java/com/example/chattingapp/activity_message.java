package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.chattingapp.Model.Chat;
import com.example.chattingapp.Model.User;
import com.example.chattingapp.myrecyclerview.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_message extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fUser;
    DatabaseReference reference;

    //activity chat
    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //chat box recyclerView
        recyclerView = findViewById(R.id.recyclerView_chatBox);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        //send text message activity
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        profile_image = findViewById(R.id.profile_toolbar_message);
        username = findViewById(R.id.username_toolbar_message);

        intent = getIntent();
        String userid = intent.getStringExtra("userid");
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        btn_send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fUser.getUid(),userid,msg);

                }else {
                    Toast.makeText(activity_message.this,"you can't send empty message",Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });




        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.drawable.profile);

                }else {
                    Glide.with(activity_message.this).load(user.getImageURL());
                }
                readMessages(fUser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender,final String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object>hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);

//        add user to chat fragment

        String userid = intent.getStringExtra("userid");
       final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fUser.getUid())
                .child(userid);

       chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (!dataSnapshot.exists()){
                   chatRef.child("id").setValue(userid);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }

    private void readMessages(final String myid,final String userid,final String imageurl){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(activity_message.this,mChat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}