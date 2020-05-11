package com.example.myfashionstore.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toolbar;

import com.example.myfashionstore.R;
import com.example.myfashionstore.data.ViewerPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolBarID);
        tabLayout = findViewById(R.id.tabLayoutID);
        viewPager = findViewById(R.id.viewPagerID);

        setActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        ViewerPagerAdapter viewerPagerAdapter = new ViewerPagerAdapter(getSupportFragmentManager());

        viewerPagerAdapter.addFragment(new FollowingFragment(),"PEOPLE");
        viewerPagerAdapter.addFragment(new TrendingFragment(),"HASHTAGS");

        viewPager.setAdapter(viewerPagerAdapter);
    }


}
