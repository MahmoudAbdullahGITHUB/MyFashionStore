//package com.example.myfashionstore.NavigationScreens;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.myfashionstore.R;
//import com.example.myfashionstore.data.UploadImage;
//
//import java.util.ArrayList;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class NavListAdapter extends RecyclerView.Adapter<NavListAdapter.NavViewHolder> {
//    private ArrayList<UploadImage> myModelList = new ArrayList<>();
//
//    @NonNull
//    @Override
//    public NavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new NavViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_creation
//                ,parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull NavViewHolder holder, int position) {
//        holder.textView.setText(myModelList.get(position).getName());
//    }
//
//    @Override
//    public int getItemCount() {
//        return myModelList.size();
//    }
//
//    /**
//     * this method just to fill myModeList arraylist from outside
//     * or i can fill it direct by the constructor from outside
//     */
//    public void setList (ArrayList<UploadImage> myList){
//        this.myModelList = myList;
//        notifyDataSetChanged();
//    }
//
//    public class NavViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//        TextView textView;
//        public NavViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            imageView = itemView.findViewById(R.id.item_image);
//            textView = itemView.findViewById(R.id.item_text);
//        }
//    }
//}
