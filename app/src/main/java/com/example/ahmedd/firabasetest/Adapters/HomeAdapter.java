package com.example.ahmedd.firabasetest.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.Model.PostModel;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {


    private Context context;
    private List<Photos> postModels;


    public HomeAdapter(Context context, List<Photos> postModels) {
        this.postModels = postModels;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Photos postModel = postModels.get(position);
        String imgUrl = postModel.getUrl();
        String date = postModel.getDate();
        String emoji = postModel.getName();
        String userName = postModel.getUserName();
        String userImg = postModel.getUserImage();

        if (isImage(imgUrl)){
            Glide.with(context).load(imgUrl).into(holder.post_img);
        }
        if (isImage(userImg)){
            Glide.with(context).load(userImg).into(holder.user_image);
        }

        holder.txt_date.setText(date);
        holder.txt_emoji.setText(emoji);
        holder.user_name.setText(userName);
        holder.commenter_name.setText(userName);

    }


    /**
     * return true if the image has a url to load from
     * To check if the image exists or no
     * if imageUrl ==> default then there is on image
     * */
    private boolean isImage(String imgUrl){
        return !imgUrl.equals("default");
    }



    @Override
    public int getItemCount() {
        return postModels.size();

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        //Post data view
        TextView txt_emoji,commenter_name,txt_date,txt_menu_option;
        ImageView post_img;

        //User data views
        TextView user_name;
        ImageView user_image;

        ViewHolder(View itemView) {
            super(itemView);

            //Post data view
            post_img = itemView.findViewById(R.id.post_img);
            commenter_name = itemView.findViewById(R.id.commentor_name);
            txt_emoji = itemView.findViewById(R.id.txt_post_emoji);
            txt_menu_option = itemView.findViewById(R.id.txt_menu_option);
            txt_date = itemView.findViewById(R.id.txt_date);

            //User data views
            user_name = itemView.findViewById(R.id.user_name_cardView_img);
            user_image = itemView.findViewById(R.id.user_image);

        }
    }




    public interface MyOnClickListener {
        void myOnClickListener(int position, Photos photosItem);
    }

}

