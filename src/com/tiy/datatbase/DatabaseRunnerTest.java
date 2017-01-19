package com.tiy.datatbase;

import org.h2.engine.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by crci1 on 1/3/2017.
 */
public class DatabaseRunnerTest {

    MyDatabase myDatabase = null;

    @Before
    public void setUp() throws Exception {
        if (myDatabase == null) {
            myDatabase = new MyDatabase();
            myDatabase.init();

        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInit() throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        PreparedStatement todoQuery = conn.prepareStatement("SELECT * FROM todos");
        ResultSet results = todoQuery.executeQuery();
        assertNotNull(results);

    }

    @Test
    public void testInsertToDo() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo";

        String username = "Reece";
        String fullname = "Thomas";

        int userId = myDatabase.insertUser(conn, username, fullname);
        myDatabase.insertToDo(conn, todoText, userId);
        // make sure we can retrieve the todo we just created
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos where text = ?");
        stmt.setString(1, todoText);
        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
        // count the records in results to make sure we get what we expected
        int numResults = 0;
        while (results.next()) {
            numResults++;
        }
        assertEquals(1, numResults);

        ToDoItem retrieveItem = myDatabase.retrieveToDo(conn, todoText);
        assertNotNull(retrieveItem);
        assertEquals(todoText, retrieveItem.getText());
        myDatabase.deleteToDo(conn, todoText);
        myDatabase.deleteUser(conn, username);

        results = stmt.executeQuery();
        numResults = 0;
        while (results.next()) {
            numResults++;
        }
        assertEquals(0, numResults);
    }

    @Test
    public void testInsertUser() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String fullname = "Maurice Thomas";
        String username = "mauricet1520@gmail.com";
        int newUserId = myDatabase.insertUser(conn, username, fullname);
        // make sure we can retrieve the todo we just created
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, username);
        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
//         count the records in results to make sure we get what we expected
        int userIdRetrieve = -1;
        int numResults = 0;
        while (results.next()) {
            numResults++;
            userIdRetrieve = results.getInt("id");
        }
        assertEquals(1, numResults);
        assertEquals(newUserId, userIdRetrieve);

//        ToDoItem retrieveItem = myDatabase.retrieveToDo(conn, todoText);
//        assertNotNull(retrieveItem);
//        assertEquals(todoText, retrieveItem.getText());

        myDatabase.deleteUser(conn, username);

        results = stmt.executeQuery();
        numResults = 0;
        while (results.next()) {
            numResults++;
        }
        assertEquals(0, numResults);
    }

    @Test
    public void testSelectAllToDos() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String firstToDoText = "UnitTest-ToDo1";
        String secondToDoText = "UnitTest-ToDo2";
        String username = "reece@gmail.com";
        String fullname = "Thomas";

        int userId = myDatabase.insertUser(conn, username, fullname);

        myDatabase.insertToDo(conn, firstToDoText, userId);
        myDatabase.insertToDo(conn, secondToDoText, userId);

        ArrayList<ToDoItem> todos = myDatabase.selectToDos(conn);
        System.out.println("Found " + todos.size() + " todos in the database");

        assertTrue("There should be at least 2 todos in the database (there are " +
                todos.size() + ")", todos.size() > 1);

        myDatabase.deleteToDo(conn, firstToDoText);
        myDatabase.deleteToDo(conn, secondToDoText);
    }

    @Test
    public void testInsertToDoForUser() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo";
        String todoText2 = "UnitTest-ToDo2";

        // adding a call to insertUser, so we have a user to add todos for
        String username = "unittester@tiy.com";
        String fullName = "Unit Tester";
        int userID = myDatabase.insertUser(conn, username, fullName);

        String username2 = "unitester2@tiy.com";
        String fullName2 = "Unit Tester 2";
        int userID2 = myDatabase.insertUser(conn, username2, fullName2);

        myDatabase.insertToDo(conn, todoText, userID);
        myDatabase.insertToDo(conn, todoText2, userID2);

        // make sure each user only has one todo item
        ArrayList<ToDoItem> todosUser1 = myDatabase.selectToDosForUser(conn, userID);
        ArrayList<ToDoItem> todosUser2 = myDatabase.selectToDosForUser(conn, userID2);

        assertEquals(1, todosUser1.size());
        assertEquals(1, todosUser2.size());

        // make sure each todo item matches
        ToDoItem todoUser1 = todosUser1.get(0);
        assertEquals(todoText, todoUser1.getText());
        ToDoItem todoUser2 = todosUser2.get(0);
        assertEquals(todoText2, todoUser2.getText());

        myDatabase.deleteToDo(conn, todoText);
        myDatabase.deleteToDo(conn, todoText2);
        // make sure we remove the test user we added earlier
        myDatabase.deleteUser(conn, username);
        myDatabase.deleteUser(conn, username2);

    }


    @Test
    public void testInsertToggleToDo() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo";
        String username = "reece@gmail.com";
        String fullname = "Thomas";

        int userId = myDatabase.insertUser(conn, username, fullname);
        myDatabase.insertToDo(conn, todoText, userId);

        ToDoItem retrieveItem = myDatabase.retrieveToDo(conn, todoText);
        assertEquals(false, retrieveItem.isDone());
        myDatabase.toggleToDo(conn, todoText);
        retrieveItem = myDatabase.retrieveToDo(conn, todoText);
        assertEquals(true, retrieveItem.isDone());
        myDatabase.toggleToDo(conn, todoText);
        retrieveItem = myDatabase.retrieveToDo(conn, todoText);
        assertEquals(false, retrieveItem.isDone());

        myDatabase.toggleToDo(conn, todoText);

        myDatabase.deleteToDo(conn, todoText);

    }


    @Test
    public void testIfUserExist() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String userName = "mauricet1520@gmail.com";
        String fullname = "MLT";
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, userName);
        int newUserId = myDatabase.insertUser(conn, userName, fullname);
        int retrieveId = myDatabase.retrieveUserIfExist(conn, userName);
        assertEquals(newUserId, retrieveId);
        myDatabase.deleteUser(conn, userName);
        retrieveId = myDatabase.retrieveUserIfExist(conn, userName);
        assertEquals(-1, retrieveId);


    }

    @Test
    public void testSelectUser() throws SQLException{

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String userName = "mauricet1520@gmail.com";
        String fullname = "MLT";

        int userId = myDatabase.insertUser(conn,userName,fullname);

//        User newUser = myDatabase.selectUser(conn, userName);



    }


}