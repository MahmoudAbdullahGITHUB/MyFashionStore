package com.example.myfashionstore.NavigationScreens;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfashionstore.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NavViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView textView;
    public View myView;

    public NavViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.item_image);
        textView = itemView.findViewById(R.id.item_text);
        myView = itemView;

//        myView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = getAdapterPosition();
//                System.out.println("my position is = "+position);
//
//
//            }
//        });

    }

}