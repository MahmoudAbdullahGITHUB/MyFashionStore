package com.example.myfashionstore.screens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.myfashionstore.R;
import com.example.myfashionstore.data.ViewerPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    public ChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);

        toolbar = view.findViewById(R.id.toolBarID);
        tabLayout = view.findViewById(R.id.tabLayoutID);
        viewPager = view.findViewById(R.id.viewPagerID);

        getActivity().setActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;

    }

    private void setupViewPager(ViewPager viewPager){
        ViewerPagerAdapter viewerPagerAdapter = new ViewerPagerAdapter(getActivity().
                getSupportFragmentManager());

        viewerPagerAdapter.addFragment(new FollowingFragment(),"POPULAR");
        viewerPagerAdapter.addFragment(new TrendingFragment(),"NEWEST");

        viewPager.setAdapter(viewerPagerAdapter);
    }

}
