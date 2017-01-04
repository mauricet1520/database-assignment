package com.tiy.datatbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by crci1 on 1/3/s.
 */
public class ToDoRunner {
    static MyDatabase database;
    Connection connection;
    ArrayList<ToDoItem> items;

    public static void main(String[] args) throws SQLException {

        new ToDoRunner().initItem();

        boolean repeat = true;
        while (repeat) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Things to do");
            System.out.println("Enter 1: View items");
            System.out.println("Enter 2: Add new item");
            System.out.println("Enter 3: Delete item");
            System.out.println("Enter 4: Exit");
            String userSelectString = scanner.nextLine();
            int userSelectNum = Integer.parseInt(userSelectString);

            switch (userSelectNum) {
                case 1:
                    new ToDoRunner().viewTodos();
                    break;
                case 2:
                    new ToDoRunner().enterTask();
                    break;
                case 3:
                    new ToDoRunner().deleteTodos();
                    break;
                case 4:
                    repeat = false;
                    break;
                default:
                    System.out.println("Choose the right number");
                    break;
            }
        }

    }

    //Create the database

    public void initItem() throws SQLException {
        if (database == null) {
            database = new MyDatabase();
            database.init();

        }

    }

    //Insert into database

    public void insertItem(String text) throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:./main");
        initItem();
        database.insertToDo(connection, text);
    }

    //Enter to do item

    public void enterTask() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter item to do");
        String holder = scanner.nextLine();
        insertItem(holder);
        viewTodos();
    }

    //View and update the databasen

    public void viewTodos() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:./main");
        items = database.selectToDos(connection);

        boolean updateChanges = true;
        while (updateChanges) {
            int looper = 0;
            items = database.selectToDos(connection);
            System.out.println("Todo list: " + items.toString());
            System.out.println();

            for (ToDoItem itemInLoop : items) {

                itemInLoop = new ToDoItem(items.get(looper).getId(), items.get(looper).getText(), items.get(looper).isDone());
                System.out.println("id = " + itemInLoop.getId() + " Todo item = " + itemInLoop.getText() + " Completed = " + itemInLoop.isDone());
                looper++;
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter id to mark completed or 0 to cancel");
            String userInput = scanner.nextLine();
            int userInt = Integer.parseInt(userInput);
            if (userInt == 0) {
                updateChanges = false;
                System.out.println();
            } else {
                ToDoItem retrieveToDoItem = database.retrieveToDobyId(connection, userInt);
                database.toggleToDo(connection, retrieveToDoItem.getText());
            }
        }


    }

    //Delete item database


    public void deleteTodos() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:./main");
        items = database.selectToDos(connection);


        boolean deleteChanges = true;
        while (deleteChanges) {
            int looper = 0;
            items = database.selectToDos(connection);
            System.out.println("Todo list: " + items.toString());
            System.out.println();

            for (ToDoItem itemInLoop : items) {
                itemInLoop = new ToDoItem(items.get(looper).getId(), items.get(looper).getText(), items.get(looper).isDone());
                System.out.println("id = " + itemInLoop.getId() + " Todo item = " + itemInLoop.getText() + " Completed = " + itemInLoop.isDone());
                looper++;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter id to delete completed or 0 to cancel");
            String userInput = scanner.nextLine();
            int userInt = Integer.parseInt(userInput);

            if (userInt == 0) {
                deleteChanges = false;
                System.out.println();
            } else {
                database.deleteToDoById(connection, userInt);
            }
        }

    }

    @Override
    public String toString() {
        return super.toString();
    }
}
