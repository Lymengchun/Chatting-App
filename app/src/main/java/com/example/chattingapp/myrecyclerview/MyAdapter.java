package com.example.chattingapp.myrecyclerview;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chattingapp.Model.User;
import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String data1[],data2[];
    ArrayList<Integer> images;
    Context context;

    FirebaseUser firebaseUser;
    DatabaseReference reference;




    public MyAdapter(Context ct, String[] s1, String[] s2, ArrayList<Integer> img){
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.username.setText(data1[position]);
        holder.recently_chat.setText(data2[position]);
        holder.myImage.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username, recently_chat;
        ImageView myImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.name);
            recently_chat = itemView.findViewById(R.id.recently_chat);
            myImage = itemView.findViewById(R.id.profile);

        }
    }








//    private Context context;
//    private List<User> users;
//
//    //constructor
//    public MyAdapter(Context context,List<User> users){
//        this.users = users;
//        this.context =context;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.my_row,parent,false);
//
//        return new MyAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        User user = users.get(position);
//        holder.username.setText(user.getUsername());
//        if(user.getImageURL().equals("default")){
//            holder.profile_image.setImageResource(R.drawable.profile);
//        }else{
//            Glide.with(context).load(user.getImageURL()).into(holder.profile_image);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return users.size();
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        public TextView username;
//        public ImageView profile_image;
//
//        //constructor ViewHolder
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            username = itemView.findViewById(R.id.name);
//            profile_image = itemView.findViewById(R.id.profile);
//        }
//    }



}
