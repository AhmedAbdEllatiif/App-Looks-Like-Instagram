package com.example.ahmedd.firabasetest.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    private MyOnclickListener onCardClickListener;
    private Boolean isOnline;

    public UsersAdapter(Context context, List<User> userList,Boolean isOnline) {
        this.context = context;
        this.userList = userList;
        this.isOnline = isOnline;
    }

    public void setOnCardClickListener(MyOnclickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final User userItem = userList.get(position);

        holder.userName.setText(userItem.getUserName());
        if (userItem.getImageURL().equals("default")) {
            holder.profile_pic.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.get().load(userItem.getImageURL()).into(holder.profile_pic);
        }

        if (isOnline){
            if (userItem.getStatus().equals("Online")){
            holder.status_online.setVisibility(View.VISIBLE);
            holder.status_offline.setVisibility(View.GONE);
            }else if(userItem.getStatus().equals("Offline")){
                holder.status_online.setVisibility(View.GONE);
                holder.status_offline.setVisibility(View.VISIBLE);
            }
            else {
                holder.status_online.setVisibility(View.GONE);
                holder.status_offline.setVisibility(View.GONE);
            }
        }

        if(onCardClickListener != null){
            holder.userCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListener.onClick(position,userItem);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        CardView userCardView;
        ImageView profile_pic;
        ImageView status_online;
        ImageView status_offline;

        public ViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName_AtCardView);
            userCardView = itemView.findViewById(R.id.cardView_user);
            profile_pic = itemView.findViewById(R.id.profile_pic_AtCardView);
            status_online = itemView.findViewById(R.id.img_statusOnline);
            status_offline = itemView.findViewById(R.id.img_statusOffline);

        }
    }

    public interface MyOnclickListener{
        void onClick(int position, User userItem);
    }

}
