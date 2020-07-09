package com.example.myfashionstore.NavigationScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfashionstore.R;
import com.example.myfashionstore.data.ImageModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShortsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewShorts;
    private ArrayList<ImageModel> shortsArrayList;
    private FirebaseRecyclerOptions<ImageModel> options;
    private FirebaseRecyclerAdapter<ImageModel, NavViewHolder> adapter;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shorts);
        recyclerViewShorts = findViewById(R.id.recycler_shorts);
        recyclerViewShorts.setHasFixedSize(true);
        shortsArrayList = new ArrayList<ImageModel>();
        database = FirebaseDatabase.getInstance();

        getShorts();

        recyclerViewShorts.setAdapter(adapter);
        recyclerViewShorts.setLayoutManager(new GridLayoutManager(this, 2));

    }


    private void getShorts(){
        mDatabaseReference = database.getReference().child("AllCategories").child("shorts");
        mDatabaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<ImageModel>().setQuery(mDatabaseReference, ImageModel.class).build();

        adapter = new FirebaseRecyclerAdapter<ImageModel, NavViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NavViewHolder holder,final int position, @NonNull final ImageModel model) {
                shortsArrayList.add(model);
                System.out.println("my model2 = " + model.getName());
                holder.textView.setText(model.getName());
                Glide.with(getApplicationContext()).load(model.getUrl()).into(holder.imageView);

                holder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ShortsActivity.super.onBackPressed();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("modelImage",model.getUrl());
                        setResult(RESULT_OK,resultIntent);
                        finish();
                    }
                });
            }

            @NonNull
            @Override
            public NavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Toast.makeText(getApplicationContext(), "model.getName()", Toast.LENGTH_SHORT).show();
                System.out.println("my shorts model1 = ");
                NavViewHolder navViewHolder = new NavViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_creation
                        , parent, false));

                return navViewHolder;
            }
        };
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
