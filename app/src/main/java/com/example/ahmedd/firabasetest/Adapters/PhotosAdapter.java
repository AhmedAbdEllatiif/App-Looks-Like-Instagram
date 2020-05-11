package com.example.ahmedd.firabasetest.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

    private static final String TAG = "PhotosAdapter";
    public static final int GRID_VIEW = 1;
    public static final int ORDINARY_VIEW = 2;


    private Context context;
    private List<Photos> photosList;
    private int myViewType;

    private MyUpdateAndCancelClickListener onCaneclClickListener;
    private MyUpdateAndCancelClickListener onUpdateClickListener;
    private MyUpdateAndCancelClickListener onMenuClickListener;


    public PhotosAdapter(Context context, List<Photos> photosList,int myViewType) {
        this.photosList = photosList;
        this.context = context;
        this.myViewType = myViewType;

    }


    public void setOnMenuClickListener(MyUpdateAndCancelClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public void setOnUpdateClickListener(MyUpdateAndCancelClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public void setOnCaneclClickListener(MyUpdateAndCancelClickListener onCaneclClickListener) {
        this.onCaneclClickListener = onCaneclClickListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == GRID_VIEW){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_image_grid, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_image, parent, false);
        }

        return new ViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Photos photosItem = photosList.get(position);

           if (holder.getItemViewType() == ORDINARY_VIEW){
               holder.txt_name.setText(photosItem.getName());


               if(photosItem.getDescription().equals("")){
                   holder.img_description.setHint("Write a description...");
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



               if (onMenuClickListener!=null){
                   holder.txt_menu_option.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           onMenuClickListener.myUpdateAndCancelClickListener(position,photosItem,holder.txt_menu_option,holder.txt_name,
                                   holder.editTxt_name_photoInfo ,holder.editText_description
                                   ,holder.img_description,holder.btn_update_photo_info,
                                   holder.btn_cancel_update_photo_info);
                       }
                   });
               }

               if (onUpdateClickListener!=null){
                   holder.btn_update_photo_info.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           onUpdateClickListener.myUpdateAndCancelClickListener(position,photosItem,holder.txt_menu_option,holder.txt_name,
                                   holder.editTxt_name_photoInfo ,holder.editText_description
                                   ,holder.img_description,holder.btn_update_photo_info,
                                   holder.btn_cancel_update_photo_info);
                       }
                   });
               }


               if(onCaneclClickListener!=null){
                   holder.btn_cancel_update_photo_info.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           onCaneclClickListener.myUpdateAndCancelClickListener(position,photosItem,holder.txt_menu_option,holder.txt_name,
                                   holder.editTxt_name_photoInfo ,holder.editText_description
                                   ,holder.img_description,holder.btn_update_photo_info,
                                   holder.btn_cancel_update_photo_info);
                       }
                   });
               }
           }
           else {
               Log.e(TAG, "onBindViewHolder: Grid");
               if (photosItem.getUrl().equals("default")){
                   holder.img_grid.setImageResource(R.mipmap.ic_launcher);
               }else {
                   //Picasso.get().load(photosItem.getUrl()).into(holder.img_grid);
                   Glide.with(context)
                           .load(photosItem.getUrl())
                           .into(holder.img_grid);
               }
           }

    }


    @Override
    public int getItemCount() {
        return photosList.size();

    }


    @Override
    public int getItemViewType(int position) {
        return myViewType;

    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView user_name_cardView_img;
        TextView txt_name;
        TextView img_description;
        TextView img_date;
        TextView txt_menu_option;
        TextView txt_empty_cardView;
        EditText editText_description;
        EditText editTxt_name_photoInfo;
        ImageView img_;
        ImageView user_profileImg_cardView_img;
        ImageView profileImage_updated;
        ImageButton btn_update_photo_info;
        ImageButton btn_cancel_update_photo_info;

        //Grid layout views
        ImageView img_grid;


        public ViewHolder(View itemView) {
            super(itemView);

            user_name_cardView_img = itemView.findViewById(R.id.user_name_cardView_img);
            txt_name = itemView.findViewById(R.id.txt_name_cardView_photoActivity);
            txt_empty_cardView = itemView.findViewById(R.id.txt_empty_cardView);
            txt_menu_option = itemView.findViewById(R.id.txt_menu_option);
            editText_description = itemView.findViewById(R.id.editText_description);
            editTxt_name_photoInfo = itemView.findViewById(R.id.editTxt_name_photoInfo);
            img_description = itemView.findViewById(R.id.img_description);
            img_date = itemView.findViewById(R.id.img_date);
            img_ = itemView.findViewById(R.id.img_cardView_photoActivity);
            user_profileImg_cardView_img = itemView.findViewById(R.id.user_profileImg_cardView_img);
            profileImage_updated = itemView.findViewById(R.id.profileImage_updated);
            btn_update_photo_info = itemView.findViewById(R.id.btn_update_photo_info);
            btn_cancel_update_photo_info = itemView.findViewById(R.id.btn_cancel_update_photo_info);

            //Grid layout views
            img_grid = itemView.findViewById(R.id.img_grid);

        }






    }

    public interface MyOnClickListener{
        void myOnClickListener(int position,Photos photosItem);

    }

    public interface MyUpdateAndCancelClickListener {
        void myUpdateAndCancelClickListener(int position, Photos photosItem,TextView txtOptionMenu,TextView txt_name,EditText editTxt_name, EditText editText_description, TextView txtDescription
                                                                           ,ImageButton update,ImageButton cancel );
    }


}
