package com.example.sqlite_examples.service;

import android.content.ContentValues;
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

public class FriendServiceSQLiteViaContentValues extends SQLiteOpenHelper implements FriendDao {
    public FriendServiceSQLiteViaContentValues(@Nullable Context context) {
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
        ContentValues values = new ContentValues();
        values.putNull("id");
        values.put("name", f.getName());
        values.put("birthday", f.getBirthday().toString());
        values.put("address", f.getAddress());
        values.put("phone", f.getPhone());
        getWritableDatabase().insert(Helper.FRIEND_TABLE, null, values);
    }

    @Override
    public void update(Friend f) {
        ContentValues values = new ContentValues();
        values.put("id", f.getId());
        values.put("name", f.getName());
        values.put("birthday", f.getBirthday().toString());
        values.put("address", f.getAddress());
        values.put("phone", f.getPhone());
        getWritableDatabase().update(Helper.FRIEND_TABLE, values, "id=?", new String[]{String.valueOf(f.getId())});
    }

    @Override
    public void delete(int id) {
        getWritableDatabase().delete(Helper.FRIEND_TABLE, "id=?", new String[]{String.valueOf(id)});
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Friend getAFriendById(int id) {
        Friend result = null;
        Cursor cursor = getReadableDatabase().query(Helper.FRIEND_TABLE, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor.moveToFirst())
            result = new Friend(
                    cursor.getInt(0),
                    cursor.getString(1),
                    LocalDate.parse(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        cursor.close();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Friend> getAllFriends() {
        List<Friend> result = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(Helper.FRIEND_TABLE, null, null,null,null,null, null);
        while(cursor.moveToNext())
            result.add(new Friend(
                    cursor.getInt(0),
                    cursor.getString(1),
                    LocalDate.parse(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4)
            ));
        cursor.close();
        return result;
    }
}
