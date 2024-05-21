package com.example.healthcompass.dataAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcompass.R;
import com.example.healthcompass.data.Friend.FriendClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddFriendAdapter extends BaseAdapter {
    private Context context;
    private List<FriendClass> userList;
    private String currentUser;

    public AddFriendAdapter(Context context, List<FriendClass> userList, String currentUser) {
        this.context = context;
        this.userList = userList;
        this.currentUser = currentUser;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_add_friend_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FriendClass user = userList.get(position);

        holder.tvUsername.setText(user.getUsername());
        holder.tvName.setText(user.getName());

        if ("Male".equals(user.getGender())) {
            holder.imgGender.setImageResource(R.drawable.male);
        } else if ("Female".equals(user.getGender())) {
            holder.imgGender.setImageResource(R.drawable.female);
        }

        if ("Pending".equals(user.getStatus())) {
            holder.imgAddFriend.setImageResource(R.drawable.pending);
            holder.imgAddFriend.setEnabled(false);
        } else {
            holder.imgAddFriend.setImageResource(R.drawable.addfriend);
            holder.imgAddFriend.setEnabled(true);
            holder.imgAddFriend.setOnClickListener(v -> {
                addFriend(user.getUsername());
                holder.imgAddFriend.setImageResource(R.drawable.pending);
                holder.imgAddFriend.setEnabled(false);
            });
        }

        return convertView;
    }

    private void addFriend(String friendUsername) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").child(currentUser).child("Add Friend").child(friendUsername).setValue("Pending");
        database.child("Users").child(friendUsername).child("Friend Request").child(currentUser).setValue("Pending");

        Toast.makeText(context, "Friend request sent to " + friendUsername, Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder {
        TextView tvUsername;
        TextView tvName;
        ImageView imgGender;
        ImageView imgAddFriend;

        ViewHolder(View view) {
            tvUsername = view.findViewById(R.id.txtUsernameAddFriend);
            tvName = view.findViewById(R.id.txtNameAddFriend);
            imgGender = view.findViewById(R.id.imgProfileAddFriend);
            imgAddFriend = view.findViewById(R.id.imgAddFriend);
        }
    }
}

