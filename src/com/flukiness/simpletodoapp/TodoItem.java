package com.flukiness.simpletodoapp;

import java.util.Date;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.*;
import com.activeandroid.query.Select;

@Table(name = "TodoItems")
public class TodoItem extends Model {
    @Column(name = "Name")
    public String name;
    @Column(name = "DueDate")
    public Date dueDate;
    
    public TodoItem() {
    	super();
    }
    
    public TodoItem(String name) {
    	super();
    	this.name = name;
    }
    
    public static List<TodoItem> getAll() {
    	return new Select().from(TodoItem.class).orderBy("ID ASC").execute();
    }
}
