package com.example.ahmedd.firabasetest.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {


    private Context context;
    private List<Photos> photosList;
    private MyOnClickListener onAsProfileImgClickListener;
    private MyOnClickListener onDescriptionClickListener;
    private MyOnClickListener onDeleteClickListener;
    private MyOnClickListener onMenueClickListener;

    public HomeAdapter(Context context, List<Photos> photosList) {
        this.photosList = photosList;
        this.context = context;

    }


    public void setOnMenueClickListener(MyOnClickListener onMenueClickListener) {
        this.onMenueClickListener = onMenueClickListener;
    }

    public void setOnDeleteClickListener(MyOnClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
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
                .inflate(R.layout.cardview_image_homefragment, parent, false);
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

        holder.user_name_cardView_img.setText(photosItem.getUserName());


        if (photosItem.getUrl().equals("default")){
            holder.img_.setImageResource(R.mipmap.ic_launcher);
        }else {
            Picasso.get().load(photosItem.getUrl()).into(holder.img_);
        }

        //set Image profile for every post
        MyFireBase.getReferenceOnAllUsers().child(photosItem.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                if (user.getImageURL().equals("default")) {
                    holder.user_profileImg_cardView_img.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImageURL()).into(holder.user_profileImg_cardView_img);
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

                }
            });
        }


        if (onDescriptionClickListener!=null){
            holder.img_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.img_description.setVisibility(View.GONE);
                    onDescriptionClickListener.myOnClickListener(position,photosItem);
                    holder.write_description.setVisibility(View.VISIBLE);

                }
            });
        }




    }


    @Override
    public int getItemCount() {
        return photosList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView user_name_cardView_img;
        TextView txt_name;
        TextView txt_setAsProfileImg;
        TextView img_description;
        TextView img_date;
        TextView txt_empty_cardView;

        EditText write_description;
        ImageView img_;
        ImageView user_profileImg_cardView_img;
        ImageView profileImage_updated;

        public ViewHolder(View itemView) {
            super(itemView);

            user_name_cardView_img = itemView.findViewById(R.id.user_name_cardView_img_homefrgament);
            txt_setAsProfileImg = itemView.findViewById(R.id.txt_setAsProfileImg_homefrgament);
            txt_name = itemView.findViewById(R.id.txt_name_cardView_photoActivity_homefrgament);
            txt_empty_cardView = itemView.findViewById(R.id.txt_empty_cardView);

            write_description = itemView.findViewById(R.id.write_description_homefrgament);
            img_description = itemView.findViewById(R.id.img_description_homefrgament);
            img_date = itemView.findViewById(R.id.img_date_homefrgament);
            img_ = itemView.findViewById(R.id.img_cardView_photoActivity_homefrgament);
            user_profileImg_cardView_img = itemView.findViewById(R.id.user_profileImg_cardView_img_homefrgament);
            profileImage_updated = itemView.findViewById(R.id.profileImage_updated_homefrgament);
        }
    }

    public interface MyOnClickListener{
        void myOnClickListener(int position,Photos photosItem);
    }

}

