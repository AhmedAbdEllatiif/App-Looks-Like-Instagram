package com.example.ahmedd.firabasetest.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private Context context;
    private List<Photos> photosList;
    private MyOnClickListener onAsProfileImgClickListener;
    private MyOnClickListener onDescriptionClickListener;

    public void setOnDeleteClickListener(MyOnClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    private MyOnClickListener onDeleteClickListener;


    public PhotosAdapter(Context context, List<Photos> photosList ) {
        this.photosList = photosList;
        this.context = context;
    }

    public void setOnAsProfileImgClickListener(MyOnClickListener onSetAsProfileImgClickListener) {
        this.onAsProfileImgClickListener = onSetAsProfileImgClickListener;
    }

    public void setOnDescriptionClickListener(MyOnClickListener onDescriptionClickListener) {
        this.onDescriptionClickListener = onDescriptionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Photos photosItem = photosList.get(position);

        holder.txt_name.setText(photosItem.getName());


        if(photosItem.getDescription().equals("")){
            holder.img_description.setText("");
        }else {

        holder.img_description.setText(photosItem.getDescription());
        }


        holder.img_date.setText(photosItem.getDate());

        if (photosItem.getUrl().equals("default")){
            holder.img_.setImageResource(R.mipmap.ic_launcher);
        }else {
                Picasso.get().load(photosItem.getUrl()).into(holder.img_);
        }

        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userItem = dataSnapshot.getValue(User.class);

                holder.user_name_cardView_img.setText(userItem.getUserName());

                        if (userItem.getImageURL().equals("default")){
                            holder.img_.setImageResource(R.mipmap.ic_launcher);
                        }else {
                            Picasso.get().load(userItem.getImageURL()).into(holder.user_profileImg_cardView_img);
                        }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (onAsProfileImgClickListener!=null){
            holder.txt_setAsProfileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAsProfileImgClickListener.myOnClickListener(position,photosItem);



                /*    holder.profileImage_updated.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.profileImage_updated.setVisibility(View.GONE);
                        }
                    },2000);*/
                }
            });
        }


        if (onDescriptionClickListener!=null){
            holder.img_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDescriptionClickListener.myOnClickListener(position,photosItem);
                }
            });
        }
        if (onDeleteClickListener!=null){
            holder.delete_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.myOnClickListener(position,photosItem);
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView user_name_cardView_img;
        TextView txt_name;
        TextView txt_setAsProfileImg;
        TextView img_description;
        TextView img_date;
        ImageView delete_cardView;
        ImageView img_;
        ImageView user_profileImg_cardView_img;
        ImageView profileImage_updated;

        public ViewHolder(View itemView) {
            super(itemView);

            user_name_cardView_img = itemView.findViewById(R.id.user_name_cardView_img);
            txt_setAsProfileImg = itemView.findViewById(R.id.txt_setAsProfileImg);
            txt_name = itemView.findViewById(R.id.txt_name_cardView_photoActivity);
            delete_cardView = itemView.findViewById(R.id.delete_cardView);
            img_description = itemView.findViewById(R.id.img_description);
            img_date = itemView.findViewById(R.id.img_date);
            img_ = itemView.findViewById(R.id.img_cardView_photoActivity);
            user_profileImg_cardView_img = itemView.findViewById(R.id.user_profileImg_cardView_img);
            profileImage_updated = itemView.findViewById(R.id.profileImage_updated);
        }
    }

    public interface MyOnClickListener{
        void myOnClickListener(int position,Photos photosItem);
    }

}
