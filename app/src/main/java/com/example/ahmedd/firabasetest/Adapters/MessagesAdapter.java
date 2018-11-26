package com.example.ahmedd.firabasetest.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Model.Chats;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>{

        public final int MSG_TYPE_LEFT = 0;
        public final int MSG_TYPE_RIGHT = 1;

        private Context context;
        private List<Chats> chatsList;
        private String ImgURl;
        private String time;
        private FirebaseUser currentUser;


    public MessagesAdapter(Context context, List <Chats> chatsList,String ImgURl) {
        this.context = context;
        this.chatsList = chatsList;
        this.ImgURl = ImgURl;

    }



        @NonNull
        @Override
        public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_rightchat, parent, false);
            Log.e("Layout","sender");
        return new ViewHolder(view);
        }

        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_leftchat, parent, false);
            Log.e("Layout","receiver");
            return new ViewHolder(view);
        }

    }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder (@NonNull ViewHolder holder,final int position){

        Chats chatsItem = chatsList.get(position);


            holder.message.setText(chatsItem.getMessage().toString().trim());

            switch (holder.getItemViewType()) {
                case MSG_TYPE_RIGHT:


                    //To get last message siza - 1
                    if (position == (chatsList.size() - 1)) {
                        if (chatsItem.getIsSeen()) {
                            holder.txt_seen.setText(chatsItem.getTime() + " ");
                            holder.txt_seen.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.seen_eye, 0);
                        } else {
                            holder.txt_seen.setText(chatsItem.getTime() + " Delivered");
                        }

                    } else {
                        holder.txt_seen.setText(chatsItem.getTime() + " ");
                    }
                    break;


                case MSG_TYPE_LEFT:
                    //To get last message siza - 1
                    if (ImgURl.equals("default")){
                        holder.profile_pic.setImageResource(R.mipmap.ic_launcher);
                    }else {
                        Picasso.get().load(ImgURl).into(holder.profile_pic);
                    }

                    holder.txt_seen.setText(chatsItem.getTime() + " ");

                    break;
            }






            /*if (ImgURl.equals("default")){
            holder.profile_pic.setImageResource(R.mipmap.ic_launcher);
        }else {
            Picasso.get().load(ImgURl).into(holder.profile_pic);
        }

        //To get last message siza - 1
        if (position == (chatsList.size()-1)){
           if (chatsItem.getIsSeen()){
               holder.txt_seen.setText(chatsItem.getTime()+" ");
               holder.txt_seen.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.seen_eye, 0);
           }else {
               holder.txt_seen.setText(chatsItem.getTime() + " Delivered");
           }

        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }*/

    }


    @Override
        public int getItemCount () {
        return chatsList.size();
    }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView message;
            TextView txt_seen;
            ImageView profile_pic;


            public ViewHolder(View itemView) {
                super(itemView);

                message = itemView.findViewById(R.id.txt_chat);
                txt_seen = itemView.findViewById(R.id.txt_seen);
                profile_pic = itemView.findViewById(R.id.img_chat);

            }
        }

    @Override
    public int getItemViewType(int position) {
        currentUser = MyFireBase.getCurrentUser();
        if (chatsList.get(position).getSender().equals(currentUser.getUid())){
            Log.e("getItemType","sender");
            return MSG_TYPE_RIGHT;
        }else {
            Log.e("getItemType","receiver");
            return MSG_TYPE_LEFT;
        }

    }
}


