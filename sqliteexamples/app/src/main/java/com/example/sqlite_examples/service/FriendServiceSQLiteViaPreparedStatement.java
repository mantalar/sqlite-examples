package com.example.sqlite_examples.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.sqlite_examples.dao.FriendDao;
import com.example.sqlite_examples.helper.Helper;
import com.example.sqlite_examples.model.Friend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendServiceSQLiteViaPreparedStatement extends SQLiteOpenHelper implements FriendDao {
    public FriendServiceSQLiteViaPreparedStatement(@Nullable Context context) {
        super(context, Helper.DBNAME, null, Helper.DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Helper.CREATE_TABLE_FRIEND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL(Helper.DROP_TABLE_FRIEND);
            onCreate(db);
        }
    }

    @Override
    public void insert(Friend f) {
        String sql = "insert into friend values(?,?,?,?,?)";
        getWritableDatabase().execSQL(sql, new Object[]{null, f.getName(), f.getBirthday().toString(), f.getAddress(), f.getPhone()});
    }

    @Override
    public void update(Friend f) {
        String sql = "update friend set name=?, birthday=?, address=?, phone=? where id=?";
        getWritableDatabase().execSQL(sql, new Object[]{f.getName(), f.getBirthday(), f.getAddress(), f.getPhone(), f.getId()});
    }

    @Override
    public void delete(int id) {
        String sql = "delete from friend where id=?";
        getWritableDatabase().execSQL(sql, new Object[]{id});
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    @Override
    public Friend getAFriendById(int id) {
        Friend result = null;
        String sql = "select * from frend where id=?";
        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(id)});
        if(cursor.moveToFirst()) {
            result = new Friend(
                    cursor.getInt(0),
                    cursor.getString(1),
                    LocalDate.parse(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        cursor.close();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Friend> getAllFriends() {
        List<Friend> result = new ArrayList<>();
        String sql = "select * from friend";
        Cursor cursor = getWritableDatabase().rawQuery(sql, null);
        while(cursor.moveToNext()) {
            result.add(new Friend(
                    cursor.getInt(0),
                    cursor.getString(1),
                    LocalDate.parse(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4)
            ));
        }
        cursor.close();
        return result;
    }
}
