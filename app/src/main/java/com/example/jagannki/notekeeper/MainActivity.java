package com.example.jagannki.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NoteRecyclerAdapter noteRecyclerAdapter;
    private CourseRecyclerAdapter courseRecyclerAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);

                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //the same class implements the onNavigationItemSelected hence passing this
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //connect this activity to the SettingsActivity we created
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
           displayNotes();
        } else if (id == R.id.nav_courses) {
            displayCourses();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleSelection(String notes) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteRecyclerAdapter.notifyDataSetChanged();

//n
    }



    private void initiateDisplayContent() {
//        final ListView listNotes = findViewById(R.id.list_notes);
        final List<NoteInfo> notes = DataManager.getInstance().getNotes();

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
        recyclerView = findViewById(R.id.list_items);
        //we can either use a linear or a grid or  StaggeredGrid style layout manager
        layoutManager = new LinearLayoutManager(this);

        gridLayout = new GridLayoutManager(this,getResources().getInteger(R.integer.course_grid_span));
        //the adapter is responsible for populating data with the view. we set the view to item_list_view in adapter and set the contents with the data
        noteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        courseRecyclerAdapter = new CourseRecyclerAdapter(this, courses);
        displayNotes();

    }

    private void displayNotes() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteRecyclerAdapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_notes).setChecked(true);
    }

    private void displayCourses() {
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setAdapter(courseRecyclerAdapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_courses).setChecked(true);
    }

}
