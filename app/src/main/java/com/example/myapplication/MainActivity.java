package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.Adapter.NotesListAdapter;
import com.example.myapplication.DataBase.RoomDB;
import com.example.myapplication.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    FloatingActionButton fab_add;
    NotesListAdapter notesListAdapter;
    RoomDB database;
    List<Notes> notes = new ArrayList<>();
    SearchView searchView_home;
    Notes selectedNote;
    FloatingActionButton fab_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);

        searchView_home = findViewById(R.id.searchView_home);

        database = RoomDB.getInstance(this);

        notes = database.mainDAO().getALL();

        fab_more = findViewById(R.id.fab_more);

        updateRecyclre(notes);

        fab_add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            startActivityForResult(intent, 101);
        });

        fab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Казак МС-32, курсовая работа", Toast.LENGTH_SHORT).show();
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter (newText);
                return true;
            }
        });

    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes singleNote : notes) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    ||singleNote.getNotes().toLowerCase().contains(newText.toLowerCase()) ){
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getALL());
                notesListAdapter.notifyDataSetChanged();
            }

        }
        if (requestCode == 102){
            if (resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getALL());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecyclre(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this,notes, notesCLickListener );
        recyclerView.setAdapter(notesListAdapter);

    }

    private final NotesCLickListener notesCLickListener = new NotesCLickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopup (cardView);
        }
    };


    private void showPopup(CardView cardView) {

        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.pin) {
            if (selectedNote.isPinned()) {
                database.mainDAO().pin(selectedNote.getID(), false);
                Toast.makeText(MainActivity.this, "Откреплено", Toast.LENGTH_SHORT).show();
            } else {
                database.mainDAO().pin(selectedNote.getID(), true);
                Toast.makeText(MainActivity.this, "Прикреплено", Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.mainDAO().getALL());
            notesListAdapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == R.id.delete) {
            database.mainDAO().delete(selectedNote);
            notes.remove(selectedNote);
            notesListAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }



}
