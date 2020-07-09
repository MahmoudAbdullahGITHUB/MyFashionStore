package com.example.myfashionstore.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfashionstore.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageShirt;
    public ImageView imageShort;
    //public TextView textView;
    public View myView;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        imageShirt = itemView.findViewById(R.id.item_shirt);
        imageShort = itemView.findViewById(R.id.item_short);
        //textView = itemView.findViewById(R.id.item_description);
        myView = itemView;


    }


}
