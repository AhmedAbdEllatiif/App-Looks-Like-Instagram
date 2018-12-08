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

import com.example.ahmedd.firabasetest.Model.Chats;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    private MyOnclickListener onCardClickListener;
    private MyOnclickListener onFollowClickListener;
    private Boolean isOnline;
    private String theLastMessage;

    public UsersAdapter(Context context, List<User> userList,Boolean isOnline) {
        this.context = context;
        this.userList = userList;
        this.isOnline = isOnline;
    }

    public void setOnCardClickListener(MyOnclickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }

    public void setOnFollowClickListener(MyOnclickListener onFollowClickListener) {
        this.onFollowClickListener = onFollowClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_user_usersfragment, parent, false);

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


        getLastMessage(userItem.getId(),holder.lastMessage);



          if(onCardClickListener != null){
            holder.userCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListener.onClick(position,userItem);
                }
            });
        }

        if(onFollowClickListener != null){
            holder.txt_follow_userFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFollowClickListener.onClick(position,userItem);
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
        TextView lastMessage;
        TextView txt_follow_userFragment;
        CardView userCardView;
        ImageView profile_pic;



        public ViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName_AtCardView_userFragment);
            lastMessage = itemView.findViewById(R.id.txt_lastMessage_userItem_userFragment);
            txt_follow_userFragment = itemView.findViewById(R.id.txt_follow_userFragment);
            userCardView = itemView.findViewById(R.id.cardView_user_userFragment);
            profile_pic = itemView.findViewById(R.id.profile_pic_AtCardView_userFragment);


        }
    }

    private void getLastMessage(final String userID, final TextView lastMessage){

        final String currentUserID = MyFireBase.getCurrentUser().getUid();
        theLastMessage = "default";
        MyFireBase.getReferenceOnChats().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats chats = snapshot.getValue(Chats.class);

                    if (chats.getSender().equals(currentUserID) && chats.getReceiver().equals(userID) ||
                            chats.getSender().equals(userID) && chats.getReceiver().equals(currentUserID)){

                        theLastMessage = chats.getMessage();
                    }
                }

                switch (theLastMessage){
                    case "default" :
                        lastMessage.setText(""); break;
                    default:
                        lastMessage.setText(theLastMessage);
                        break;

                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public interface MyOnclickListener{
        void onClick(int position, User userItem);
    }

}
