package com.example.ahmedd.firabasetest.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.R;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private static final String TAG = "PostsAdapter";
    public static final int GRID_VIEW = 1;
    public static final int POST_VIEW = 2;


    private Context context;
    private List<Photos> postModelList;
    private User currentUser;

    private int myViewType;


    private OnPostMenuOptionClickListener onMenuClickListener;


    public PostsAdapter(Context context, int myViewType) {
        this.context = context;
        this.myViewType = myViewType;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == GRID_VIEW) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_image_grid, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_post, parent, false);
        }

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Photos postModelItem = postModelList.get(position);
        int viewType = holder.getItemViewType();
        //To set the according to viewType
        switch (viewType){
            case POST_VIEW:
                setPostData_POST_VIEW(postModelItem,holder);
                onMenuOptionsClicked(holder);
                break;
            case GRID_VIEW:
                setPostData_GRID_VIEW(postModelItem,holder);
                break;
        }


    }


    /**
     * To set post data and user data
     * This method used only if the getViewType == POST_VIEW
     * */
    private void setPostData_POST_VIEW(Photos postModelItem, ViewHolder holder){

        String postImgUrl = postModelItem.getUrl();
        String userName = postModelItem.getUserName();
        String userImg = postModelItem.getUserImage();

        if (isImage(postImgUrl)){// To check if the image exists or no
            Glide.with(context).load(postImgUrl).into(holder.post_img);
        }

        if (isImage(userImg)) {// To check if the image exists or no
            Glide.with(context).load(userImg).into(holder.user_image);
        }

        holder.txt_emoji.setText(postModelItem.getName());
        holder.txt_date.setText(postModelItem.getDate());
        holder.user_name.setText(userName);
        holder.commenter_name.setText(userName.trim());


    }

    /**
     * To image from post data
     * This method only user if the getViewType == GRID_VIEW
     * */
    private void setPostData_GRID_VIEW(Photos postModelItem,ViewHolder holder){
        Log.d(TAG, "setPostData_GRID_VIEW: ");
        String postImgUrl = postModelItem.getUrl();

        if (isImage(postImgUrl)){// To check if the image exists or no
            Glide.with(context).load(postModelItem.getUrl()).into(holder.img_grid);
        }
    }

    /**
     * To handle on MenuOption clicked
     * */
    private void onMenuOptionsClicked(ViewHolder holder){
        if (onMenuClickListener != null) {
            holder.txt_menu_option.setOnClickListener(v -> {
            });
        }
    }


    /**
     * return true if the image has a url to load from
     * To check if the image exists or no
     * if imageUrl ==> default then there is on image
     * */
    private boolean isImage(String imgUrl){
        if (imgUrl != null){
        return !imgUrl.equals("default");
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return postModelList.size();

    }


    @Override
    public int getItemViewType(int position) {
        return myViewType;

    }



   static class ViewHolder extends RecyclerView.ViewHolder {

        //Post data view
        TextView txt_emoji;
        TextView commenter_name;
        TextView txt_date;
        TextView txt_menu_option;
        ImageView post_img;

        //User data views
        TextView user_name;
        ImageView user_image;

        //Grid layout views
        ImageView img_grid;


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

            //Grid layout views
            img_grid = itemView.findViewById(R.id.img_grid);

        }


    }


    /*///////////////////////////////////////////////Adapter Setters///////////////////////////////////////////*/
    public void setOnMenuClickListener(OnPostMenuOptionClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setPostModelList(List<Photos> postModelList) {
        this.postModelList = postModelList;
    }

    /*/////////////////////////////////////Adapter Interfaces//////////////////////////////////////////////////*/
    public interface OnPostMenuOptionClickListener {
        void onMenuOptionsClicked(User user,Photos postModel,int position);
    }


}
