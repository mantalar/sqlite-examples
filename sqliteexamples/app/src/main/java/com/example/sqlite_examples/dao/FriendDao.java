package com.example.sqlite_examples.dao;

import com.example.sqlite_examples.model.Friend;

import java.util.List;

public interface FriendDao {
    void insert(Friend f);
    void update(Friend f);
    void delete(int id);
    Friend getAFriendById(int id);
    List<Friend> getAllFriends();
}
