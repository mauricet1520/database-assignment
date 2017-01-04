package com.tiy.datatbase;

import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by crci1 on 1/3/2017.
 */
public class MyDatabase {
    public final static String DB_URL = "jdbc:h2:./main";

    public void init() throws SQLException {
        // we'll add some implementation code here once we have a unit test method for it
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY, text VARCHAR, is_done BOOLEAN)");

    }

    //Insert into database

    public void insertToDo(Connection conn, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO todos VALUES (NULL, ?, false)");
        stmt.setString(1, text);
        stmt.execute();
    }

    //Delete record by String


    public void deleteToDo(Connection conn, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos WHERE text = ?");
        stmt.setString(1, text);
        stmt.execute();
    }

    //DELETE record by Id

    public void deleteToDoById(Connection conn, int theId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos WHERE id = ?");
        stmt.setInt(1, theId);
        stmt.execute();
    }

    //SELECT into database

    public ArrayList<ToDoItem> selectToDos(Connection conn) throws SQLException {
        ArrayList<ToDoItem> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            items.add(new ToDoItem(id, text, isDone));
        }
        return items;
    }

    //UPDATE database

    public void toggleToDo(Connection conn, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE todos SET is_done = NOT is_done WHERE text = ?");
        stmt.setString(1, text);
        stmt.execute();
    }

    //Retrieve by string


    public ToDoItem retrieveToDo(Connection conn, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE text = ?");
        stmt.setString(1, text);
        ResultSet results = stmt.executeQuery();
        results.next();
        int id = results.getInt("id");
        String retrieveText = results.getString("text");
        boolean isDone = results.getBoolean("is_done");
        ToDoItem retrieveToDo = new ToDoItem(id, retrieveText, isDone);

        return retrieveToDo;

    }

    //Insert by id

    public ToDoItem retrieveToDobyId(Connection conn, int theId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE id = ?");
        stmt.setInt(1, theId);
        ResultSet results = stmt.executeQuery();
        results.next();
        int id = results.getInt("id");
        String retrieveText = results.getString("text");
        boolean isDone = results.getBoolean("is_done");
        ToDoItem retrieveToDo = new ToDoItem(id, retrieveText, isDone);

        return retrieveToDo;

    }


}
