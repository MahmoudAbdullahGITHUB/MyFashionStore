package com.example.myfashionstore.PersonalFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfashionstore.R;
import com.example.myfashionstore.ViewHolder.PostViewHolder;
import com.example.myfashionstore.data.PostModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class OutFitsFragment extends Fragment {

    private RecyclerView outFitsRecyclerView;
    private ArrayList<PostModel> postsArrayList;
    private FirebaseRecyclerOptions<PostModel> options;
    private FirebaseRecyclerAdapter<PostModel, PostViewHolder> adapter;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseReference;


    public OutFitsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_out_fits, container, false);

        outFitsRecyclerView = view.findViewById(R.id.out_fit_recycler);
        outFitsRecyclerView.setHasFixedSize(true);
        postsArrayList = new ArrayList<PostModel>();
        database = FirebaseDatabase.getInstance();

        Toast.makeText(getActivity().getApplicationContext(), "outfits here", Toast.LENGTH_SHORT).show();
        System.out.println("outFits here");

        getPosts();

        outFitsRecyclerView.setAdapter(adapter);
        outFitsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return view;
    }



    private void getPosts() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mDatabaseReference = database.getReference().child("Users").child(userId).child("myPosts");
        mDatabaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<PostModel>().setQuery(mDatabaseReference, PostModel.class).build();

        System.out.println("my outfits");


        adapter = new FirebaseRecyclerAdapter<PostModel, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull PostModel model) {
                postsArrayList.add(model);

                System.out.println("my post model = " + model.getPostDescription());
                Toast.makeText(getActivity().getApplicationContext(), "posts here", Toast.LENGTH_SHORT).show();
                //holder.textView.setText(model.getPostDescription());
                Glide.with(getActivity().getApplicationContext()).load(model.getShirtImageUrl()
                ).into(holder.imageShirt);

                Glide.with(getActivity().getApplicationContext()).load(model.getShortImageUrl()
                ).into(holder.imageShort);

                //                holder.myView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //ShirtsActivity.super.onBackPressed();
//                        Intent resultIntent = new Intent();
//                        resultIntent.putExtra("modelImage", model.getUrl());
//                        setResult(RESULT_OK, resultIntent);
//                        finish();
//                    }
//                });
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Toast.makeText(getActivity().getApplicationContext(), "posts", Toast.LENGTH_SHORT).show();
                System.out.println("my model outfits = ");
                PostViewHolder postViewHolder = new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post
                        , parent, false));

                return postViewHolder;
            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }
}
