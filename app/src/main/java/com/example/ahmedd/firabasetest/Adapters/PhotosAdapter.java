package com.example.ahmedd.firabasetest.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {


    private Context context;
    private List<Photos> photosList;
    private MyOnClickListener onAsProfileImgClickListener;
    private MyOnClickListener onDescriptionClickListener;
    private MyOnClickListener onDeleteClickListener;
    private MyUpdateAndCancelClickListener onCaneclClickListener;
    private MyUpdateAndCancelClickListener onMenuClickListener;


    public PhotosAdapter(Context context, List<Photos> photosList) {
        this.photosList = photosList;
        this.context = context;

    }


    public void setOnMenuClickListener(MyUpdateAndCancelClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public void setOnCaneclClickListener(MyUpdateAndCancelClickListener onCaneclClickListener) {
        this.onCaneclClickListener = onCaneclClickListener;
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
                    .inflate(R.layout.cardview_image, parent, false);
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





            if (onDescriptionClickListener!=null){
                holder.img_description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        holder.img_description.setVisibility(View.GONE);
                        onDescriptionClickListener.myOnClickListener(position,photosItem);
                        holder.editText_description.setVisibility(View.VISIBLE);

                    }
                });
            }



            if (onMenuClickListener!=null){
                holder.txt_menu_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onMenuClickListener.myUpdateAndCancelClickListener(position,photosItem,holder.txt_menu_option,holder.editText_description
                                ,holder.img_description,holder.btn_update_photo_info,
                                holder.btn_cancel_update_photo_info);
                    }
                });
            }
/*

            holder.txt_menu_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    PopupMenu popupMenu = new PopupMenu(context,holder.txt_menu_option);
                    popupMenu.inflate(R.menu.cardview_home_menu);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()){

                                case R.id.set_as_ProfileImage :
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("ImageURL", photosItem.getUrl());
                                    MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                            .updateChildren(hashMap);
                                    Snackbar.make(holder.txt_menu_option, "Profile Picture Updated Successful  ", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    break;
                                case R.id.edit_photoInfo :
                                    holder.img_description.setVisibility(View.GONE);
                                    onDescriptionClickListener.myOnClickListener(position,photosItem);
                                    holder.editText_description.setVisibility(View.VISIBLE);
                                    holder.btn_update_photo_info.setVisibility(View.VISIBLE);
                                    holder.btn_cancel_update_photo_info.setVisibility(View.VISIBLE);

                                    break;
                                case R.id.delete_menu :
                                    MyFireBase.getReferenceOnPhotos().child(MyFireBase.getCurrentUser().getUid())
                                            .child("Myphotos").child(photosItem.getKey()).removeValue();
                                    Snackbar.make(holder.txt_menu_option, "Photo Deleted", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    break;
                            }

                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
*/


            if(onCaneclClickListener!=null){
                holder.btn_cancel_update_photo_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCaneclClickListener.myUpdateAndCancelClickListener(position,photosItem,holder.txt_menu_option,holder.editText_description
                                                                                ,holder.img_description,holder.btn_update_photo_info,
                                                                                                holder.btn_cancel_update_photo_info);
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
        TextView img_description;
        TextView img_date;
        TextView txt_menu_option;
        TextView txt_empty_cardView;
        EditText editText_description;
        ImageView img_;
        ImageView user_profileImg_cardView_img;
        ImageView profileImage_updated;
        ImageButton btn_update_photo_info;
        ImageButton btn_cancel_update_photo_info;


        public ViewHolder(View itemView) {
            super(itemView);

            user_name_cardView_img = itemView.findViewById(R.id.user_name_cardView_img);
            txt_name = itemView.findViewById(R.id.txt_name_cardView_photoActivity);
            txt_empty_cardView = itemView.findViewById(R.id.txt_empty_cardView);
            txt_menu_option = itemView.findViewById(R.id.txt_menu_option);
            editText_description = itemView.findViewById(R.id.editText_description);
            img_description = itemView.findViewById(R.id.img_description);
            img_date = itemView.findViewById(R.id.img_date);
            img_ = itemView.findViewById(R.id.img_cardView_photoActivity);
            user_profileImg_cardView_img = itemView.findViewById(R.id.user_profileImg_cardView_img);
            profileImage_updated = itemView.findViewById(R.id.profileImage_updated);
            btn_update_photo_info = itemView.findViewById(R.id.btn_update_photo_info);
            btn_cancel_update_photo_info = itemView.findViewById(R.id.btn_cancel_update_photo_info);

        }






    }

    public interface MyOnClickListener{
        void myOnClickListener(int position,Photos photosItem);

    }

    public interface MyUpdateAndCancelClickListener {
        void myUpdateAndCancelClickListener(int position, Photos photosItem,TextView txtOptionMenu, EditText editText_description, TextView txtDescription
                                                                           ,ImageButton update,ImageButton cancel );
    }


}
