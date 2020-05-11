package com.example.myfashionstore.screens;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.myfashionstore.R;
import com.example.myfashionstore.data.ViewerPagerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageView;
    GoogleSignInClient mGoogleSignInClient;

    FirebaseDatabase database ;
    private DatabaseReference mDatabase;




    private String TAG = "authenta";

    public PersonFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        toolbar = view.findViewById(R.id.toolBarID);
        tabLayout = view.findViewById(R.id.tabLayoutID);
        viewPager = view.findViewById(R.id.viewPagerID);

        getActivity().setActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPagerID,
                new FollowingFragment()).commit();


        imageView = view.findViewById(R.id.settingsID);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*System.out.println(TAG + "1= " + mGoogleSignInClient);
                System.out.println(TAG + "3= " + FirebaseAuth.getInstance().getCurrentUser());
                System.out.println(TAG + "5= " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                System.out.println(TAG + "6= " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

                 */
                signOut();

            }
        });



        //setUserName();
        //ReadUserName();



        return view;

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewerPagerAdapter viewerPagerAdapter = new ViewerPagerAdapter(getActivity().
                getSupportFragmentManager());

        viewerPagerAdapter.addFragment(new FollowingFragment(), "OUTFITS");
        viewerPagerAdapter.addFragment(new TrendingFragment(), "ITEMS");
        viewerPagerAdapter.addFragment(new RecentFragment(), "CHALLENGES");

        viewPager.setAdapter(viewerPagerAdapter);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        // Google sign out
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            System.out.println("you existed from google");
                        }
                    });
        }
        getActivity().finish();
        System.out.println(TAG + "44= " );
        startActivity(new Intent(getActivity(), MainActivity.class));
    }


    private void setUserName(){
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users").child("userName");
        mDatabase.setValue("Mahmoud");

    }

    private void ReadUserName(){
        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


}