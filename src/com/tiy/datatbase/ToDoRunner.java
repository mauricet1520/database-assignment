package com.tiy.datatbase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.assertFalse;

/**
 * Created by crci1 on 1/3/s.
 */
public class ToDoRunner {
     static MyDatabase database;
     Connection connection;
     static ArrayList<ToDoItem> items;

    public static void main(String[] args) throws SQLException {

        new ToDoRunner().initItem();

        boolean repeat = true;
        ToDoRunner myRunner = new ToDoRunner();
        myRunner.connection = DriverManager.getConnection("jdbc:h2:./main");
        items = new ArrayList<>();
        database = new MyDatabase();

        myRunner.insertItem();

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
                    myRunner.viewTodos();
                    break;
                case 2:
                    myRunner.insertItem();
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

    public void insertItem() throws SQLException {
        initItem();
        int userId = -1;
        String text = null;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a username");
        String holderForUser = scanner.nextLine();

        int user = database.retrieveUserIfExist(connection, holderForUser);

        if (user == -1) {
            System.out.println("Enter your full name");
            String holderFullname = scanner.nextLine();
            userId = database.insertUser(connection, holderForUser, holderFullname);
            System.out.println("Enter item to do");
            text = scanner.nextLine();
        }
        else {
            System.out.println("Enter item to do");
             text = scanner.nextLine();
        }
        database.insertToDo(connection, text, userId);

    }

    //Enter to do item

    public void enterTask() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter item to do");
        String holder = scanner.nextLine();
        viewTodos();
    }

    //View and update the database

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

                itemInLoop = new ToDoItem(items.get(looper).getId(),
                        items.get(looper).getText(),
                        items.get(looper).isDone(),
                        items.get(looper).getUserId());
                System.out.println("To do item =id = " + itemInLoop.getId()
                        + " Todo item = " + itemInLoop.getText()
                        + " Completed = " + itemInLoop.isDone()
                        + " UserId = " + itemInLoop.getUserId());
                looper++;
            }
            System.out.println();
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
                itemInLoop = new ToDoItem(items.get(looper).getId(), items.get(looper).getText(), items.get(looper).isDone(),
                        items.get(looper).getUserId());
                System.out.println("id = "
                        + itemInLoop.getId() + " Todo item = "
                        + itemInLoop.getText()
                        + " Completed = " + itemInLoop.isDone()
                        + " UserId =" + itemInLoop.getUserId());
                looper++;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter UserId to delete completed or 0 to cancel");
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
