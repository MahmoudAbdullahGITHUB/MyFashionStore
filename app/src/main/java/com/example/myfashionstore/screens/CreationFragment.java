package com.example.myfashionstore.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfashionstore.NavigationScreens.ShirtsActivity;
import com.example.myfashionstore.R;
import com.example.myfashionstore.data.PostModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.example.myfashionstore.NavigationScreens.*;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CreationFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private static final float END_SCALE = 0.7f;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton addShirt;
    private FloatingActionButton addShort;
    private FloatingActionButton postingFA;
    private ImageView imageShirt;
    private ImageView imageShort;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RelativeLayout parentLayout;
    private TextInputEditText postDescription;
    private String currentDate, currentTime;
    private String shirtImageUrlResult = "" , shortImageUrlResult = "" , trouserImageUrlResult = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_creation, container, false);

        drawerLayout = view.findViewById(R.id.drawer_layout);
        addShirt = view.findViewById(R.id.add_shirt);
        addShort = view.findViewById(R.id.add_short);
        postingFA = view.findViewById(R.id.posting_FA);
        navigationView = view.findViewById(R.id.navigation_view);
        parentLayout = view.findViewById(R.id.parent_relative_layout);
        imageShirt = view.findViewById(R.id.image_shirt);
        imageShort = view.findViewById(R.id.image_short);
        postDescription = view.findViewById(R.id.post_description);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //this line just to lock the slide menu and just open it by the button
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        navigationDrawer();
        animateNavigationDrawer();


        addShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "opened", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        addShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "opened", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        postingFA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "pressed FA", Toast.LENGTH_SHORT).show();
                publishPost();
            }
        });


        return view;
    }

    private void publishPost() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = firebaseDatabase.getReference("Users");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        currentDate = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        currentTime = timeFormat.format(calendar.getTime());
        //long postTimeInMillis = calendar.getTimeInMillis();

        PostModel postModel = new PostModel(currentDate, currentTime,
                Objects.requireNonNull(postDescription.getText()).toString(), shirtImageUrlResult , shortImageUrlResult);
        databaseReference.child(userId).child("myPosts").push().setValue(postModel);
    }

    private void animateNavigationDrawer() {
        //drawerLayout.setScrimColor(getResources().getColor(R.color.orange));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaleOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaleOffset;
                parentLayout.setScaleX(offsetScale);
                parentLayout.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = parentLayout.getWidth() * diffScaleOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                parentLayout.setTranslationX(xTranslation);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                System.out.println("opened ");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                System.out.println("closed ");
                parentLayout.clearAnimation();

            }
        });
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shirts_part:
                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext()
                        , ShirtsActivity.class);
                //this line mean i will receive data back from the second activity
                startActivityForResult(intent, 555);
                break;

            case R.id.shorts_part:
                Intent intent2 = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext()
                        , ShortsActivity.class);
                //this line mean i will receive data back from the second activity
                startActivityForResult(intent2, 556);
                break;

            case R.id.trousers_part:
                Intent intent3 = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext()
                        , TrousersActivity.class);
                startActivityForResult(intent3, 557);
                break;


        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 555) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                shirtImageUrlResult = data.getStringExtra("modelImage");
                Glide.with(Objects.requireNonNull(getActivity()).getApplicationContext()).load(shirtImageUrlResult).into(imageShirt);
            }
            if (resultCode == RESULT_CANCELED) {
                shirtImageUrlResult = "No Image Selected";
            }
            System.out.println("imageUrlResult = " + shirtImageUrlResult);
        }
        if (requestCode == 556) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                shortImageUrlResult = data.getStringExtra("modelImage");
                Glide.with(Objects.requireNonNull(getActivity()).getApplicationContext()).load(shortImageUrlResult).into(imageShort);
            }
            if (resultCode == RESULT_CANCELED) {
                shortImageUrlResult = "No Image Selected";
            }
            System.out.println("imageUrlResult = " + shortImageUrlResult);
        }
        if (requestCode == 557) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                shortImageUrlResult = data.getStringExtra("modelImage");
                Glide.with(Objects.requireNonNull(getActivity()).getApplicationContext()).load(shortImageUrlResult).into(imageShort);
            }
            if (resultCode == RESULT_CANCELED) {
                shortImageUrlResult = "No Image Selected";
            }
            System.out.println("imageUrlResult = " + shortImageUrlResult);
        }
    }

}