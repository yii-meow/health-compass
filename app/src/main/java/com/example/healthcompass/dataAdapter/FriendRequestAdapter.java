package com.example.healthcompass.dataAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcompass.R;
import com.example.healthcompass.data.Friend.FriendClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FriendRequestAdapter extends BaseAdapter {
    private Context context;
    private List<FriendClass> userList;
    private String currentUser;

    public FriendRequestAdapter(Context context, List<FriendClass> userList, String currentUser) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_friend_request_item, parent, false);
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

        holder.btnAccept.setOnClickListener(v -> acceptFriendRequest(user.getUsername()));
        holder.btnDecline.setOnClickListener(v -> declineFriendRequest(user.getUsername()));

        return convertView;
    }

    private void acceptFriendRequest(String friendUsername) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.child(currentUser).child("Friends").child(friendUsername).setValue(true);
        userRef.child(friendUsername).child("Friends").child(currentUser).setValue(true);

        userRef.child(currentUser).child("Friend Request").child(friendUsername).removeValue();
        userRef.child(friendUsername).child("Add Friend").child(currentUser).removeValue();

        Toast.makeText(context, "Friend request accepted", Toast.LENGTH_SHORT).show();
    }

    private void declineFriendRequest(String friendUsername) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.child(currentUser).child("Friend Request").child(friendUsername).removeValue();
        userRef.child(friendUsername).child("Add Friend").child(currentUser).removeValue();

        Toast.makeText(context, "Friend request declined", Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder {
        TextView tvUsername;
        TextView tvName;
        ImageView imgGender;
        Button btnAccept;
        Button btnDecline;

        ViewHolder(View view) {
            tvUsername = view.findViewById(R.id.txtUsernameFriendRequest);
            tvName = view.findViewById(R.id.txtNameFriendRequest);
            imgGender = view.findViewById(R.id.imgProfileFriendRequest);
            btnAccept = view.findViewById(R.id.btnAccept);
            btnDecline = view.findViewById(R.id.btnDecline);
        }
    }
}