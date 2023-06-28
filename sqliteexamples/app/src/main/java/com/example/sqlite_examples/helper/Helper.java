package com.example.sqlite_examples.helper;

public class Helper {

    public static final String ACTIVITY_MODE = "activity mode";

    public static final int ADD_MODE = 0;
    public static final int EDIT_MODE = 1;
    public static final int VIEW_MODE = 2;

    public static final String DBNAME = "romandb";
    public static final int DBVERSION = 1;

    public static final String TAG_FRIEND = "friend";

    public static final String FRIEND_TABLE = "friend";
    public static final String CREATE_TABLE_FRIEND = "create table if not exists friend(" +
            "id integer not null primary key autoincrement," +
            "name varchar(40) not null," +
            "birthday varchar(10)," +
            "address varchar(100)," +
            "phone varchar(24))";
    public static final String DROP_TABLE_FRIEND = "drop table if exists friend";

}
