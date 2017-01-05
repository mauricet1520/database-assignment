package com.tiy.datatbase;

import java.sql.SQLException;

/**
 * Created by crci1 on 1/4/2017.
 */
public class DbRunner {
    public static void main(String[] args) throws SQLException {
        MyDatabase database = new MyDatabase();
        database.init();
    }
}
