package wallace.android.easynotes.adapters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import wallace.android.easynotes.HomePage;
import wallace.android.easynotes.R;
import wallace.android.easynotes.models.NoteRow;
import wallace.android.easynotes.sqlLite.NotesDataSource;
import wallace.android.easynotes.ui.ListDialogFragment;
import wallace.android.easynotes.utils.DatePickerFragment;
import wallace.android.easynotes.utils.GenericUtilities;
import wallace.android.easynotes.utils.TimePickerFragment;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPagerAdapter extends PagerAdapter implements OnClickListener, OnItemClickListener, OnItemLongClickListener
{	
	//list adapter
	private static NoteListAdapter listAdapter;
	
	//Old list of notes that was used before I had the database up
	//private List<NoteRow> rowList = new ArrayList<NoteRow>();
	
	//Define a NoteRow
	private NoteRow rowData;
	
	
	//Calendar for date/time info
	private static final Calendar c = Calendar.getInstance();
	private static String date;
	private static String time;
	
	//Get the views from the xml to create NoteRows
	private static TextView dateDisplay;
	private static TextView timeDisplay;
	private TextView noteTitle;
	private TextView noteText;
	private ListView noteList;
	
	//Get the image buttons
	private ImageButton timeChangeButton;
	private ImageButton dateChangeButton;
	private ImageButton addNoteButton;
	
	//SqlLite variables
	private static NotesDataSource sqlLiteDatabase;
	
	// Used to vibrate the phone upon longItemClick
	Vibrator vibrator;

	
	//Returns the number of panes in the ViewPager
    public int getCount() {
        return 2;
    }
     
	@Override
    public Object instantiateItem(View collection, int position) {
    	//Open our database connection
    	if(sqlLiteDatabase == null){
    		sqlLiteDatabase = new NotesDataSource(collection.getContext());
    		sqlLiteDatabase.open();
    	}

    	vibrator = (Vibrator) collection.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    	
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Initialize to first pane
        int resId = R.layout.first_pane;
        View view = inflater.inflate(resId, null);
        switch (position) {
        case 0:        	
            resId = R.layout.first_pane;
            view = inflater.inflate(resId, null);

            timeChangeButton = (ImageButton) view.findViewById(R.id.time_change_button);
            dateChangeButton = (ImageButton) view.findViewById(R.id.date_change_button);
            addNoteButton = (ImageButton) view.findViewById(R.id.add_note_button);
        	timeChangeButton.setOnClickListener((OnClickListener) this);
        	dateChangeButton.setOnClickListener((OnClickListener) this);
        	addNoteButton.setOnClickListener((OnClickListener) this);
        	
        	timeDisplay = (TextView) view.findViewById(R.id.time_display);
        	dateDisplay = (TextView) view.findViewById(R.id.date_display);
        	noteTitle = (TextView) view.findViewById(R.id.note_title);
        	noteText = (TextView) view.findViewById(R.id.note_text);
   		
    		//Set the hints and make the views non focusable
        	timeDisplay.setText(getTodaysTime());
        	dateDisplay.setText(getTodaysDate());
    		timeDisplay.setFocusable(false);
    		dateDisplay.setFocusable(false);

    		break;
        case 1:
            resId = R.layout.second_pane;
            view = inflater.inflate(resId, null);
			
            //Get all of the notes
		    List<NoteRow> values = sqlLiteDatabase.getAllNotes();
		    if(values.size() > 0){
				listAdapter = new NoteListAdapter(collection.getContext(), values);	
		    }
		    //Get the noteList by id.
            noteList = (ListView) view.findViewById(R.id.notes_list);
            noteList.setOnItemClickListener((OnItemClickListener) this);
            noteList.setOnItemLongClickListener((OnItemLongClickListener) this);
			noteList.setAdapter(listAdapter);
            break;
        }
        

       ((ViewPager) collection).addView(view, 0);        
        return view;
    }
    
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }
    
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }
    
    @Override
    public Parcelable saveState() {
        return null;
    }

	public void onClick(View v) {
		switch(v.getId()){
			case R.id.time_change_button:
				DialogFragment timeFragment = new TimePickerFragment();
				((TimePickerFragment) timeFragment).setFrom(0);
				timeFragment.show(HomePage.getInstance().getFragmentManager(), "Time Picker");
				
				break;
				
			case R.id.date_change_button:
				DialogFragment dateFragment = new DatePickerFragment();
				((DatePickerFragment) dateFragment).setFrom(0);
				dateFragment.show(HomePage.getInstance().getFragmentManager(), "Date Picker");
				break;
				
			case R.id.add_note_button:

				//Create our NoteRow
				if(noteTitle.getText().toString().isEmpty() == false && noteText.getText().toString().isEmpty() == false){
					if(noteTitle.getText().length() <= 18){
						rowData = new NoteRow();
						rowData.setDate(date);
						rowData.setTime(time);
						rowData.setNote((String) noteText.getText().toString());
						rowData.setNoteTitle((String) noteTitle.getText().toString());
						
						// Add it to our list and set the adapter
						sqlLiteDatabase.createNote(rowData);
						listAdapter = new NoteListAdapter(v.getContext(), sqlLiteDatabase.getAllNotes());				
						noteList.setAdapter(listAdapter);
						listAdapter.notifyDataSetChanged();
						
						//Reset the fields
						noteTitle.setText("");
						noteText.setText("");
					} else{
						Toast toast = Toast.makeText(v.getContext(), "Please make sure your title is less than 18 charactors long...", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP, 0, 200);
						toast.show();						
					}
				} else{
					//Toast
					Toast toast = Toast.makeText(v.getContext(), "Please make sure you have input for the title and note!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 200);
					toast.show();
				}
				
				break;
			default:
				Toast.makeText(v.getContext(), "ERROR" + v, Toast.LENGTH_SHORT).show();
		}		
	}
	
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		NoteRow note = (NoteRow) noteList.getItemAtPosition(position);
		//DEBUG Toast.makeText(view.getContext(), position + " " + note.getId() + " " + note.getNoteTitle(), Toast.LENGTH_SHORT).show();
		//Make a new dialog
		DialogFragment newDialog = new ListDialogFragment(note);
		newDialog.show(HomePage.getInstance().getFragmentManager(), "Edit Note");
		
	}
	
	public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position,	long arg3) {
		//vibrate
		vibrator.vibrate(50);
		
		//New ALertDialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(adapterView.getContext());
 
		// set title
		alertDialogBuilder.setTitle("Delete Note?");
 
		// set dialog message
		alertDialogBuilder.setMessage("Are you sure you want to delete this note?")
			.setCancelable(false)
			.setPositiveButton("Yes, Delete it!",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, close
				// current activity
				NoteRow note = (NoteRow) noteList.getItemAtPosition(position);
				sqlLiteDatabase.deleteNote(note);
				listAdapter = new NoteListAdapter(view.getContext(), sqlLiteDatabase.getAllNotes());				
				noteList.setAdapter(listAdapter);
				listAdapter.notifyDataSetChanged();
				
			}
		});
		alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
 
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
 
		// show it
		alertDialog.show();

		return true;
	}

	public static void notifyAdapterOfChange(){
		listAdapter.notifyDataSetChanged();
	}
	
	public static void updateNote(NoteRow note){
		sqlLiteDatabase.updateNote(note.getId(), note.getNoteTitle(), note.getNote(), note.getDate(), note.getTime());
	}
	
	
	public static void setTimeFromPicker(int hour, int minute, String amPm){
		//Get the time from the picker and update the screen
		time = pad(hour) + ":" + pad(minute) + " " + amPm;
		timeDisplay.setText(time);		
	}
	public static void setDateFromPicker(int month, int day, int year){
		//Get the date from the picker and update the screen

		String tmpMonth = GenericUtilities.getMonth(month);
		date = tmpMonth + ", " + day + " " + year;

		dateDisplay.setText(date);		
	}
	
	
	private static String getTodaysDate(){		

		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
		String month = month_date.format(c.getTime());
		date = month + ", " + day + " " + year;
		
		return date;
	}
	
	private static String getTodaysTime(){		
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		time = pad(hour) + ":" + pad(minute);
		
		int amPm = c.get(Calendar.AM_PM);
		if(amPm == 0){
			time += " am";
		} else {
			time += " pm";
		}
		
		return time;
	}
	
	private static String pad(int c) {
		if(c == 0)
			return "12";
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
}
