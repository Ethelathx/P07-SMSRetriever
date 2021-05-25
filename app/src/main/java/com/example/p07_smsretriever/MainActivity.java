package com.example.p07_smsretriever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        //---------------Display Frag 1-------------
        Fragment f1 = new NumberFragment();
        ft.replace(R.id.frame1, f1);
        //---------------Display Frag 1-------------


        //---------------Display Frag 2-------------
        Fragment f2 = new WordFragment();
        ft.replace(R.id.frame2, f2);
        //---------------Display Frag 2-------------

        ft.commit();
    }
}