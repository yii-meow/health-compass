package com.example.healthcompass.dataAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthcompass.R;
import com.example.healthcompass.data.Friend.FriendClass;

import java.util.List;

public class FriendListAdapter extends BaseAdapter {
    private Context context;
    private List<FriendClass> friends;

    public FriendListAdapter(Context context, List<FriendClass> friends) {
        this.context = context;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_friend_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FriendClass friend = friends.get(position);

        holder.tvUsername.setText(friend.getUsername());
        holder.tvName.setText(friend.getName());

        if ("Male".equals(friend.getGender())) {
            holder.imgGender.setImageResource(R.drawable.male);
        } else if ("Female".equals(friend.getGender())) {
            holder.imgGender.setImageResource(R.drawable.female);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvUsername;
        TextView tvName;
        ImageView imgGender;

        ViewHolder(View view) {
            tvUsername = view.findViewById(R.id.txtUsernameFriendList);
            tvName = view.findViewById(R.id.txtNameFriendList);
            imgGender = view.findViewById(R.id.imgProfileFriendList);
        }
    }
}