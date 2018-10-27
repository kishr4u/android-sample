package com.example.jagannki.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter noteRecyclerAdapter;


//    private ArrayAdapter<NoteInfo> adapterNotes1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);

                startActivity(intent);
            }
        });

        initiateDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteRecyclerAdapter.notifyDataSetChanged();

//n
    }



    private void initiateDisplayContent() {
//        final ListView listNotes = findViewById(R.id.list_notes);
        final List<NoteInfo>  notes = DataManager.getInstance().getNotes();

//        adapterNotes1 = new ArrayAdapte r<>(this, android.R.layout.simple_list_item_1,notes);
//        listNotes.setAdapter(adapterNotes1);
//
//        listNotes.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
//                NoteInfo noteInfo = (NoteInfo) listNotes.getItemAtPosition(i);
//                //intent.putExtra(NoteActivity.NOTE_INFO,noteInfo); NoteActivity is a parcelable so it can be used across process to pass data, this is better and faster than java serialization
//                //since we are runnning in same process we can just pass position
//                intent.putExtra(NoteActivity.NOTE_Position,i);
//                startActivity(intent);
//            }
//        });
        final RecyclerView recyclerView = findViewById(R.id.list_notes);
        //we can either use a linear or a grid or  StaggeredGrid style layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //the adapter is responsible for populating data with the view. we set the view to item_list_view in adapter and set the contents with the data
        noteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);
        recyclerView.setAdapter(noteRecyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

}
