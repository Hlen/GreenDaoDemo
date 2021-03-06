package com.homer.greendaodemo;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.homer.greendaodemo.greendao.entity.Note;
import com.homer.greendaodemo.greendao.entity.NoteType;
import com.homer.greendaodemo.greendao.gen.DaoSession;
import com.homer.greendaodemo.greendao.gen.NoteDao;

import java.text.DateFormat;
import java.util.Date;

public class NoteActivity extends ListActivity{

    private NoteDao noteDao;
    private Cursor cursor;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        noteDao = daoSession.getNoteDao();


        // You usually do not need to work with the database directly when using greenDAO. But you still can...
        Object rawDatabase = daoSession.getDatabase().getRawDatabase();
        String textColumn = NoteDao.Properties.Text.columnName;
        // SQLCipher 3.5.0 does not understand "COLLATE LOCALIZED ASC", so use standard sorting
        String orderBy = textColumn + " COLLATE NOCASE ASC";
        if (rawDatabase instanceof SQLiteDatabase) {
            cursor = ((SQLiteDatabase) rawDatabase)
                    .query(noteDao.getTablename(), noteDao.getAllColumns(), null, null, null, null, orderBy);
        } else {
            cursor = ((android.database.sqlite.SQLiteDatabase) rawDatabase)
                    .query(noteDao.getTablename(), noteDao.getAllColumns(), null, null, null, null, orderBy);
        }
        String[] from = {textColumn, NoteDao.Properties.Comment.columnName};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from,
                to);
        setListAdapter(adapter);

        editText = (EditText) findViewById(R.id.editTextNote);
        addUiListeners();
    }

    protected void addUiListeners() {
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNote();
                    return true;
                }
                return false;
            }
        });

        final View button = findViewById(R.id.buttonAdd);
        button.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                button.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onMyButtonClick(View view) {
        addNote();
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());
        Note note = new Note(null, noteText, comment, new Date(), NoteType.TEXT);
        noteDao.insert(note);
        Log.d("DaoExample", "Inserted new note, ID: " + note.getId());

        cursor.requery();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        noteDao.deleteByKey(id);
        Log.d("DaoExample", "Deleted note, ID: " + id);
        cursor.requery();
    }
}
