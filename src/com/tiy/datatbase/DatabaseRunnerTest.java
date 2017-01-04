package com.tiy.datatbase;

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
        myDatabase.insertToDo(conn, todoText);
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

        myDatabase.insertToDo(conn, firstToDoText);
        myDatabase.insertToDo(conn, secondToDoText);

        ArrayList<ToDoItem> todos = myDatabase.selectToDos(conn);
        System.out.println("Found " + todos.size() + " todos in the database");

        assertTrue("There should be at least 2 todos in the database (there are " +
                todos.size() + ")", todos.size() > 1);

        myDatabase.deleteToDo(conn, firstToDoText);
        myDatabase.deleteToDo(conn, secondToDoText);
    }


    @Test
    public void testInsertToggleToDo() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String todoText = "UnitTest-ToDo";
        myDatabase.insertToDo(conn, todoText);
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


}