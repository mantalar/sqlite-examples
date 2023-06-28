package com.example.sqlite_examples;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlite_examples.adapter.FriendAdapter;
import com.example.sqlite_examples.helper.Helper;
import com.example.sqlite_examples.model.Friend;
import com.example.sqlite_examples.service.FriendServiceSQLiteViaContentValues;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final List<Friend> mList = new ArrayList<>();
    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("SQLite Examples");

        adapter = new FriendAdapter(this, mList);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::addFriend);
    }

    private void addFriend(View view) {
        Intent intent = new Intent(this, AddAndViewActivity.class);
        intent.putExtra(Helper.ACTIVITY_MODE, Helper.ADD_MODE);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        try(FriendServiceSQLiteViaContentValues db = new FriendServiceSQLiteViaContentValues(this)){
            mList.clear();
            mList.addAll(db.getAllFriends());
            adapter.notifyDataSetChanged();
        }
    }
}