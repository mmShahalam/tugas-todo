package com.example.todo;

public class Todo {

    private int id;
    private String what;
    private String time;

    public Todo(int id, String what, String time) {
        this.id = id;
        this.what = what;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getWhat() {
        return what;
    }

    public String getTime() {
        return time;
    }
}

