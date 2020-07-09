package com.example.myfashionstore.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.myfashionstore.R;
import com.example.myfashionstore.data.ViewerPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.security.PrivateKey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
//import androidx.appcompat.widget.Toolbar;

public class FeedFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed,container,false);

        toolbar = view.findViewById(R.id.toolBarID);
        tabLayout = view.findViewById(R.id.tabLayoutID);
        viewPager = view.findViewById(R.id.viewPagerID);

        getActivity().setActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        // to make this fragment is the default in this activity look note # 14 on the NoteBook
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPagerID,
                new FollowingFragment()).commit();


        imageView = view.findViewById(R.id.search_iconID);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setupViewPager(ViewPager viewPager){
        ViewerPagerAdapter viewerPagerAdapter = new ViewerPagerAdapter(getActivity().
                getSupportFragmentManager());

        viewerPagerAdapter.addFragment(new FollowingFragment(),"FOLLOWING");
        viewerPagerAdapter.addFragment(new TrendingFragment(),"TRENDING");
        viewerPagerAdapter.addFragment(new RecentFragment(),"RECENT");

        viewPager.setAdapter(viewerPagerAdapter);
    }

}
