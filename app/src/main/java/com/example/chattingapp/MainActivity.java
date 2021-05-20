package com.example.chattingapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList; // import the ArrayList class

import com.bumptech.glide.Glide;
import com.example.chattingapp.Model.User;

import com.example.chattingapp.myrecyclerview.MyAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{

    RecyclerView recyclerView;

    private String s1[],s2[];

    private ArrayList<Integer> images = new ArrayList<>();

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    //get image to profile header from database
    private CircleImageView profile_image;
    private TextView username;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addImage();
        initToolbar();


        recyclerView = findViewById(R.id.recyclerView);

        s1 = getResources().getStringArray(R.array.all_chat);
        s2 = getResources().getStringArray(R.array.description);

        //setAdapter
        MyAdapter myAdapter = new MyAdapter(this,s1,s2,images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //get image to profile header from database
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer);
        View headerView = navigationView.getHeaderView(0);
        username = (TextView) headerView.findViewById(R.id.profile_username);
        profile_image = (CircleImageView) headerView.findViewById(R.id.id_profile);

//        firebaseUser and reference
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.drawable.profile);
                }else{
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabItem tabChats = findViewById(R.id.tab_item1_chats);
        TabItem tabUsers = findViewById(R.id.tab_item2_users);
        ViewPager viewPager = findViewById(R.id.view_pager);

        //pager adapter
        PagerAdapter pagerAdapter = new
                PagerAdapter(getSupportFragmentManager(),
                            tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        //tab is selected or clicked
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    //Menu on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
//        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem.OnActionExpandListener onActionExpandListener= new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(MainActivity.this,"Search chats",Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(MainActivity.this,"Search chats Collapse",Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView=(SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("search chats...");



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,activity_sign_in.class));
                finish();
                return true;
        }
        return false;
    };


    //Toolbar
    void initToolbar(){
        // create reference to toolbar view
        toolbar = findViewById(R.id.toolbar);

        // Use toolbar to replace default action bar
        setSupportActionBar(toolbar);

        // remove title from the toolbar
        getSupportActionBar().setTitle("All Chats");

        // init drawerlayout
        drawerLayout = findViewById(R.id.drawerLayout);
        //create toggle menu
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    //function add image
    void addImage(){
        //add image
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);

    }




}