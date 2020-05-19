package com.example.ahmedd.firabasetest.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ahmedd.firabasetest.Model.SearchByImagesModel;
import com.example.ahmedd.firabasetest.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final Activity context;
    private final List<SearchByImagesModel> listItems;

    private final int RIGHT_VIEW = 1;
    private final int MID_VIEW = 2;
    private final int LEFT_VIEW = 3;
    private final static int DEFAULT_LIST_COUNT = 6;


    public SearchAdapter(Activity context, List<SearchByImagesModel> listItems) {
        this.context = context;
        this.listItems = listItems;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case RIGHT_VIEW:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_images_right_view, parent, false);
                break;
            case LEFT_VIEW:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_images_left_view, parent, false);
                break;
            case MID_VIEW:
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_images_mid_view, parent, false);

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (listItems != null) {
            SearchByImagesModel galleryModel = listItems.get(position);
            //setAPiImages(holder, galleryModel, position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (position == 0) return RIGHT_VIEW;
        else if (position % 3 != 0) return MID_VIEW;
        else if (position % 2 == 1 && position % 3 == 0) return LEFT_VIEW;
        else if (position % 2 == 0 && position % 3 == 0) return RIGHT_VIEW;
        return -1;
    }


    @Override
    public int getItemCount() {
        return DEFAULT_LIST_COUNT;
      /*  if (listItems != null) {
            return listItems.size();
        } else {
            return DEFAULT_LIST_COUNT;
        }*/
    }








    private void setAPiImages(ViewHolder holder, SearchByImagesModel galleryModel, int position) {
        if (!context.isDestroyed()) {

            switch (holder.getItemViewType()) {
                case RIGHT_VIEW:
                    if (galleryModel.getImageUrl0() != null) {
                        Glide.with(context).load(galleryModel.getImageUrl0()).into(holder.img_large_in_right_cardview_gallery_0);
                    } else {
                        holder.img_large_in_right_cardview_gallery_0.setVisibility(View.INVISIBLE);
                    }
                    if (galleryModel.getImageUrl2() != null) {
                        Glide.with(context).load(galleryModel.getImageUrl1()).into(holder.img_in_right_cardview_gallery_1);
                    } else {
                        holder.img_in_right_cardview_gallery_1.setVisibility(View.INVISIBLE);
                    }

                    if (galleryModel.getImageUrl2() != null) {
                        Glide.with(context).load(galleryModel.getImageUrl2()).into(holder.img_in_right_cardview_gallery_2);
                    } else {
                        holder.img_in_right_cardview_gallery_2.setVisibility(View.INVISIBLE);
                    }


                    break;
                case MID_VIEW:

                    if (galleryModel.getImageUrl0() != null) {
                        Glide.with(context).load(galleryModel.getImageUrl0()).into(holder.start_img);
                    } else {
                        holder.start_img.setVisibility(View.INVISIBLE);
                    }

                    if (galleryModel.getImageUrl1() != null) {
                        Glide.with(context).load(galleryModel.getImageUrl1()).into(holder.mid_img);
                    } else {
                        holder.mid_img.setVisibility(View.INVISIBLE);
                    }
                    if (galleryModel.getImageUrl2() != null) {
                        Glide.with(context).load(galleryModel.getImageUrl2()).into(holder.end_img);
                    } else {
                        holder.end_img.setVisibility(View.INVISIBLE);
                    }


                    break;
                case LEFT_VIEW:

                    if (galleryModel.getImageUrl0() != null) {
                        Glide.with(context).load(galleryModel.getImageUrl0()).into(holder.img_large_in_left_cardview_gallery_0);
                    } else {
                        holder.img_large_in_left_cardview_gallery_0.setVisibility(View.INVISIBLE);
                    }

                    if (galleryModel.getImageUrl1() != null) {
                        Glide.with(context).load(galleryModel.getImageUrl1()).into(holder.img_in_left_cardview_gallery_1);
                    } else {
                        holder.img_in_left_cardview_gallery_1.setVisibility(View.INVISIBLE);
                    }

                    if (galleryModel.getImageUrl2() != null) {
                        Glide.with(context).load(galleryModel.getImageUrl2()).into(holder.img_in_left_cardview_gallery_2);
                    } else {
                        holder.img_in_left_cardview_gallery_2.setVisibility(View.INVISIBLE);
                    }


                    //onImageCliked.onClick(position);
                    break;


            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        /*Right card*/
        final ImageView img_large_in_right_cardview_gallery_0;
        final ImageView img_in_right_cardview_gallery_1;
        final ImageView img_in_right_cardview_gallery_2;

        /*mid card*/
        final ImageView start_img;
        final ImageView mid_img;
        final ImageView end_img;


        /*Left card*/
        final ImageView img_large_in_left_cardview_gallery_0;
        final ImageView img_in_left_cardview_gallery_1;
        final ImageView img_in_left_cardview_gallery_2;

        ViewHolder(View itemView) {
            super(itemView);

            img_large_in_right_cardview_gallery_0 = itemView.findViewById(R.id.img_large_in_right_cardview_gallery_0);

            img_in_right_cardview_gallery_1 = itemView.findViewById(R.id.img_in_right_cardview_gallery_1);
            img_in_right_cardview_gallery_2 = itemView.findViewById(R.id.img_in_right_cardview_gallery_2);

            start_img = itemView.findViewById(R.id.start_img);
            mid_img = itemView.findViewById(R.id.mid_img);
            end_img = itemView.findViewById(R.id.end_img);


            img_large_in_left_cardview_gallery_0 = itemView.findViewById(R.id.img_large_in_left_cardview_gallery_0);
            img_in_left_cardview_gallery_1 = itemView.findViewById(R.id.img_in_left_cardview_gallery_1);
            img_in_left_cardview_gallery_2 = itemView.findViewById(R.id.img_in_left_cardview_gallery_2);


        }
    }
}
