package com.example.ahmedd.firabasetest.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private Context context;
    private List<Photos> photosList;

    public PhotosAdapter(Context context, List<Photos> photosList ) {
        this.photosList = photosList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Photos photosItem = photosList.get(position);

        holder.txt_name.setText(photosItem.getName());
        if (photosItem.getUrl().equals("default")){
            holder.img_.setImageResource(R.mipmap.ic_launcher);
        }else {
                Picasso.get().load(photosItem.getUrl()).into(holder.img_);
        }


    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_name;
        ImageView img_;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name_cardView_photoActivity);
            img_ = itemView.findViewById(R.id.img_cardView_photoActivity);
        }
    }
}
