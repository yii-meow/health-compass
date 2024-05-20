package com.example.healthcompass.dataAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.healthcompass.ui.profile.AddFriendFragment;
import com.example.healthcompass.ui.profile.FriendListFragment;
import com.example.healthcompass.ui.profile.FriendRequestFragment;

public class VpAdapter extends FragmentStateAdapter {
    private final Fragment[] fragments;

    public VpAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[] {
                new FriendListFragment(),
                new AddFriendFragment(),
                new FriendRequestFragment()
        };
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
