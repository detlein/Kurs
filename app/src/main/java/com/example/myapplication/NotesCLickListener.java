package com.example.myapplication;

import androidx.cardview.widget.CardView;

import com.example.myapplication.Models.Notes;

public interface NotesCLickListener {

    void onClick (Notes notes);
    void onLongClick (Notes notes, CardView cardView);
}
