package com.example.home_planing.entities;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String description;

    public Task() {}
    public Task(int id,String description) {
        this.description = description;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    }

