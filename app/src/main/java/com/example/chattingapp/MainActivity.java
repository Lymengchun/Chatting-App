package com.example.chattingapp;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.viewpager.widget.ViewPager;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;

import com.bumptech.glide.Glide;
import com.example.chattingapp.Model.User;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{



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

//    StorageReference storageReference;
//    private static final int IMAGE_REQUEST = 1;
//    private Uri imageUri;
//    private StorageTask uploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initToolbar();


        //get image to profile header from database
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer);
        View headerView = navigationView.getHeaderView(0);
        username = (TextView) headerView.findViewById(R.id.profile_username);
        profile_image = (CircleImageView) headerView.findViewById(R.id.id_profile);

//        storageReference = FirebaseStorage.getInstance().getReference("uploads");



        // firebaseUser and reference
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

//        profile_image.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openImage();
//            }
//        }
//        );

    }


    //Menu on toolbar
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

    //logout
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

    //upload picture
//    private void openImage(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,IMAGE_REQUEST);
//    }
//
//    private String getFileExtension(Uri uri){
//        ContentResolver contentResolver = getBaseContext().getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//    }
//
//    private void uploadImage(){
//        final ProgressDialog pd = new ProgressDialog(getBaseContext());
//        pd.setMessage("uploading");
//        pd.show();
//
//        if (imageUri != null){
//            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
//                    +"."+getFileExtension(imageUri));
//
//            uploadTask = fileReference.putFile(imageUri);
//            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()){
//                        throw task.getException();
//                    }
//                    return fileReference.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()){
//                        Uri downloadUri = task.getResult();
//                        String mUri = downloadUri.toString();
//
//                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
//                        HashMap<String,Object>map = new HashMap<>();
//                        map.put("imageURL",mUri);
//                        reference.updateChildren(map);
//
//                        pd.dismiss();
//                    }else {
//                        Toast.makeText(getBaseContext(),"Failed!",Toast.LENGTH_SHORT).show();
//                        pd.dismiss();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                    pd.dismiss();
//                }
//            });
//        }else{
//            Toast.makeText(getBaseContext(),"No image selected",Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
//               && data != null && data.getData() != null){
//            imageUri = data.getData();
//
//            if (uploadTask != null && uploadTask.isInProgress()){
//                Toast.makeText(getBaseContext(),"Upload in preogress",Toast.LENGTH_SHORT).show();
//            }else{
//                uploadImage();
//            }
//        }
//    }
}