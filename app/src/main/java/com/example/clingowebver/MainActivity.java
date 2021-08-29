package com.example.clingowebver;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {


    AppBarLayout appBarLayout;
    TabLayout tablayout;
    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Managing the fragmena
        appBarLayout = findViewById(R.id.appbar);
        viewPager = findViewById(R.id.viewpager_id);
        tablayout = findViewById(R.id.tablayout);


        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddFragment(new FirstPage(), "Cllingo");
        pagerAdapter.AddFragment(new setting(), "Setting");


        viewPager.setAdapter(pagerAdapter);
        tablayout.setupWithViewPager(viewPager);


    }







}
