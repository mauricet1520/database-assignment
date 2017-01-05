package com.tiy.datatbase;

/**
 * Created by crci1 on 1/3/2017.
 */

public class ToDoItem {
    private int id;
    private String text;
    private boolean isDone;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;


    public ToDoItem(int id, String text, boolean isDone, int userId) {
        this.id = id;
        this.text = text;
        this.isDone = isDone;
        this.userId = userId;
    }


    public ToDoItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if (isDone) {
            return text + " (done)";
        } else {
            return text;
        }
        // A one-line version of the logic above:
        // return text + (isDone ? " (done)" : "");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}

