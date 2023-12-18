package com.example.myapplication.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "notes")
public class Notes implements Serializable {
    @PrimaryKey (autoGenerate = true)
    public
    int ID = 0;
    @ColumnInfo (name = "title")
    public
    String title = "";
    @ColumnInfo (name = "notes")
    public
    String notes = "";
    @ColumnInfo (name = "date")
    public
    String date = "";
    @ColumnInfo (name = "pinned")
    public
    boolean pinned = false;


    public int getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
    public String getNotes() {
        return notes;
    }
    public String getDate() {
        return date;
    }
    public boolean isPinned() {
        return pinned;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
