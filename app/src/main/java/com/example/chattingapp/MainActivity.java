package com.example.chattingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList; // import the ArrayList class

import com.example.chattingapp.myrecyclerview.MyAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private String s1[],s2[];

    private ArrayList<Integer> images = new ArrayList<>();

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addImage();
        initToolbar();


        recyclerView = findViewById(R.id.recyclerView);

        s1 = getResources().getStringArray(R.array.all_chat);
        s2 = getResources().getStringArray(R.array.description);

        MyAdapter myAdapter = new MyAdapter(this,s1,s2,images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);


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
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
        images.add(R.drawable.profile);
    }

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



}