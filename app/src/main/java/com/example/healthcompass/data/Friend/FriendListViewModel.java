package com.example.healthcompass.data.Friend;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FriendListViewModel extends ViewModel {
    private final MutableLiveData<List<FriendClass>> friendList = new MutableLiveData<>();

    public MutableLiveData<List<FriendClass>> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<FriendClass> friends) {
        friendList.setValue(friends);
    }
}
