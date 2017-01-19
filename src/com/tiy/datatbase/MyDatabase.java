package com.tiy.datatbase;

import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by crci1 on 1/3/2017.
 */
public class MyDatabase {
    public final static String DB_URL = "jdbc:h2:./main";

    //Create table

    public void init() throws SQLException {
        // we'll add some implementation code here once we have a unit test method for it
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY, text VARCHAR, is_done BOOLEAN, user_id INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, fullname VARCHAR)");

    }

    //insert a user in user table

    public int insertUser(Connection conn, String username, String fullname) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, fullname);
        stmt.execute();

        stmt = conn.prepareStatement("SELECT * FROM users where username = ?");
        stmt.setString(1, username);
        ResultSet results = stmt.executeQuery();
        results.next();
        return results.getInt("id");
    }

    //Insert into database

    public void insertToDo(Connection conn, String text, int userId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO todos VALUES (NULL, ?, false, ?)");
        stmt.setString(1, text);
        stmt.setInt(2, userId);
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
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos WHERE user_id = ?");
        stmt.setInt(1, theId);
        stmt.execute();
    }

    //Delete user from user Table
    public void deleteUser(Connection conn, String username) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users where username = ?");
        stmt.setString(1, username);
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
            int userId = results.getInt("user_id");
            items.add(new ToDoItem(id, text, isDone, userId));
        }
        return items;
    }

    public ArrayList<ToDoItem> selectToDosForUser(Connection conn, int userID) throws SQLException {
        ArrayList<ToDoItem> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos " +
                "INNER JOIN users ON todos.user_id = users.id " +
                "WHERE users.id = ?");
        stmt.setInt(1, userID);
        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            int idUser = results.getInt("user_id");
            items.add(new ToDoItem(id, text, isDone, idUser));
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
        int idUser = results.getInt("user_id");
        ToDoItem retrieveToDo = new ToDoItem(id, retrieveText, isDone, idUser);

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
        int userId = results.getInt("user_id");
        ToDoItem retrieveToDo = new ToDoItem(id, retrieveText, isDone, userId);

        return retrieveToDo;

    }

    public Users selectUser(Connection conn, String userName) throws SQLException{

//        ArrayList<Users> users = new ArrayList<>();
        Users users= null;

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users Where username = ?");
        stmt.setString(1, userName);
        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            int id = results.getInt("id");
            String username = results.getString("username");
            String fullname = results.getString("fullname");

             users = new Users(id, username, fullname);
        }
        return users;
    }

    public int retrieveUserIfExist(Connection connection, String userName) throws SQLException {
        int id;
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, userName);
        ResultSet results = stmt.executeQuery();


        while (results.next()) {
            if (results.getString("username").equals(userName)) {
                id = results.getInt("id");
                return id;
            }

        }

        return -1;

    }


}
