package com.example.jagannki.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_INFO="com.example.jagannki.mk.NOTEINFO";
    public static final String NOTE_Position="com.example.jagannki.mk.NOTEPosition";

    public static final String ORIGINAL_NOTE_COURSE_ID = "ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE_ID = "ORIGINAL_NOTE_TITLE_ID";
    public static final String ORIGINAL_NOTE_TEXT_ID = "ORIGINAL_NOTE_TEXT_ID";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo note;
    private boolean mNewNote;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private Spinner spinnerCourses;
    private int mNotePosition;

    private boolean isCancelling;
    private String mOriginalCourseId;
    private String mOriginalText;
    private String mOriginalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//savedInstanceState will not be null and will contain the value set in onSaveInstanceState before activity is destroyed.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerCourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,courses);

        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        if(savedInstanceState == null) {//the bundle saved instance state will be null when onCreate is called initially. But once its getting destroyed and recreated since we have overriden onSaveInstanceState which is invoked
            //dusing destiry the savedInstanceState will not be null and will have the original values
            saveOriginalNoteValues();
        }else{
            restoreOriginalValues(savedInstanceState);
        }
        textNoteTitle = findViewById(R.id.text_note_title);
        textNoteText = findViewById(R.id.text_note_text);
        if(!mNewNote) {
            displayNote(spinnerCourses, textNoteTitle, textNoteText);
        }
    }

    private void restoreOriginalValues(Bundle savedInstanceState) {
        mOriginalCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE_ID);
        mOriginalText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT_ID);
    }

    //This method is called when this activity is destroyed. We are saving the initial state variables so that we can recreate the original vales when its created again
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID,mOriginalCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE_ID,mOriginalTitle);
        outState.putString(ORIGINAL_NOTE_TEXT_ID,mOriginalText);
    }

    private void saveOriginalNoteValues() {
        if(mNewNote){return;
        }
        mOriginalCourseId = note.getCourse().getCourseId();
        mOriginalText = note.getText();
        mOriginalTitle = note.getTitle();
    }

    @Override
//    Kishore - This method is called when a user clicks the back button or when finish is invoked. In Android we dont have specific button on save when user makes changes and clicks back we autiomatically save the data.
    protected void onPause() {
        super.onPause();
        if(isCancelling){
            if(mNewNote) {//if its a new note and user clicks cancel then remove the note so that its not changed.Remember we save the note when user created it
                DataManager.getInstance().removeNote(mNotePosition);
            }else{
                storePreviousNoteValues();
            }
        }else {
            saveNote();
        }
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalCourseId);
        note.setCourse(course);
        note.setText(mOriginalText);
        note.setTitle(mOriginalTitle);
    }

    private void saveNote() {
        note.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        note.setTitle(textNoteTitle.getText().toString());
        note.setText(textNoteText.getText().toString());
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        textNoteTitle.setText(note.getTitle());
        textNoteText.setText(note.getText());

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int index = courses.indexOf(note.getCourse());
        spinnerCourses.setSelection(index);
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
//        note = intent.getParcelableExtra(NOTE_INFO);
        int position  = intent.getIntExtra(NOTE_Position, POSITION_NOT_SET);
        if (position == POSITION_NOT_SET) mNewNote = true;
        else mNewNote = false;
        if(mNewNote){
           createNewNote();//by default save the empty node that we created, so that even if user clicks back its persisted.
        }else{
            note= DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        note = dm.getNotes().get(mNotePosition);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    //KISHORE- This method is invoked when user clicks on a menu item in the app
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {

            sendEmail();
            return true;
        }else if (id == R.id.action_cancel){
            isCancelling = true;
            finish();//finish means exit . it will call onPause method
        }else if (id == R.id.action_next){
           moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(mNotePosition < lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();
        ++mNotePosition;
        note= DataManager.getInstance().getNotes().get(mNotePosition);
        saveOriginalNoteValues();
        displayNote(spinnerCourses, textNoteTitle, textNoteText);
        invalidateOptionsMenu();//during invalidation it invokes the onPrepareOptions menu so that eveytime we press next we check if its last item and if so disable next

    }

    /**
     * We are going to use implicit intent in adroid to send email. In the AndroidManifest.xml we can specify the category or capability. So when
     * any sender wants to invoke then android will automatically use an extrenal app with that capability and invoke it. if multiple
     * capability apps are present then it will prompt for user selection.
     * we will use an implicit intent to send an email
     */
    private void sendEmail() {
        CourseInfo course = (CourseInfo) spinnerCourses.getSelectedItem();
        String subject = textNoteTitle.getText().toString();
        String text = "Checkout what I learned in course " + textNoteText;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");//mimetype for identifying email
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}
