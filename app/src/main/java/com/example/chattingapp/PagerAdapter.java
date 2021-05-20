package com.example.chattingapp;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chattingapp.fragments.ChatsFragment;
import com.example.chattingapp.fragments.UsersFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    public PagerAdapter( FragmentManager fm, int numOfTabs) {
        super(fm,numOfTabs);
        this.numOfTabs = numOfTabs;


    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  new ChatsFragment();
            case 1:
                return new UsersFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
