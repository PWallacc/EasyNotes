package wallace.android.easynotes.sqlLite;

import java.util.ArrayList;
import java.util.List;

import wallace.android.easynotes.models.NoteRow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NotesDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.COLUMN_NOTE, MySQLiteHelper.COLUMN_NOTE_TITLE, 
	      MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_TIME };

  public NotesDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }
  
  //
  // Create a note from a NoteRow
  //
  public NoteRow createNote(NoteRow note){
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_NOTE_TITLE, note.getNoteTitle().toString());
	    values.put(MySQLiteHelper.COLUMN_NOTE, note.getNote().toString());
	    values.put(MySQLiteHelper.COLUMN_DATE, note.getDate().toString());
	    values.put(MySQLiteHelper.COLUMN_TIME, note.getTime().toString());
	    long insertId = database.insert(MySQLiteHelper.TABLE_NOTES, null,
	        values);
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    NoteRow newNote = cursorToNoteRow(cursor);
	    cursor.close();
	    return newNote;
  }

  public void deleteNote(NoteRow note) {
    long id = note.getId();
    database.delete(MySQLiteHelper.TABLE_NOTES, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
    System.out.println("Note deleted with id: " + id);
  }

  public List<NoteRow> getAllNotes() {
    List<NoteRow> notes = new ArrayList<NoteRow>();
    Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      NoteRow note = cursorToNoteRow(cursor);
      notes.add(note);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return notes;
  }

  private NoteRow cursorToNoteRow(Cursor cursor) {
    NoteRow note = new NoteRow();
    note.setId(cursor.getLong(0));
    note.setNote(cursor.getString(1));
    note.setNoteTitle(cursor.getString(2));
    note.setDate(cursor.getString(3));
    note.setTime(cursor.getString(4));

    return note;
  }
  
  public boolean updateNote(long rowId, String noteTitle, 
  String noteText, String noteDate, String noteTime) 
  {
      ContentValues values = new ContentValues();
      values.put(MySQLiteHelper.COLUMN_NOTE_TITLE, noteTitle);
      values.put(MySQLiteHelper.COLUMN_NOTE, noteText);
      values.put(MySQLiteHelper.COLUMN_DATE, noteDate);
      values.put(MySQLiteHelper.COLUMN_TIME, noteTime);
      return database.update(MySQLiteHelper.TABLE_NOTES, values, 
    		  MySQLiteHelper.COLUMN_ID + "=" + rowId, null) > 0;
  }
} 