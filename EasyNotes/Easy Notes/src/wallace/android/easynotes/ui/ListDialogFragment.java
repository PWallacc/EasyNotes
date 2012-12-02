package wallace.android.easynotes.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import wallace.android.easynotes.HomePage;
import wallace.android.easynotes.R;
import wallace.android.easynotes.adapters.ViewPagerAdapter;
import wallace.android.easynotes.models.NoteRow;
import wallace.android.easynotes.utils.DatePickerFragment;
import wallace.android.easynotes.utils.GenericUtilities;
import wallace.android.easynotes.utils.TimePickerFragment;

//Gets rid of the warning.
//@SuppressLint("ValidFragment")
public class ListDialogFragment extends DialogFragment implements OnClickListener {
	private String noteTitle;
	private String noteText;
	private String noteDate;
	private String noteTime;
	
	private static TextView noteTitleTextView;
	private static TextView noteTextView;
	private static TextView noteDateTextView;
	private static TextView noteTimeTextView;
	
	private ImageButton timeChangeButton;
	private ImageButton dateChangeButton;
	private ImageButton saveButton;
	
	private NoteRow currentNote;
	
	private static String time;
	private static String date;
	
	// Date variables
	public static int currentMonth;
	public static int currentDay;
	public static int currentYear;
	
	// Time Variables
	public static int currentHour;
	public static int currentMinute;
	public static String currentAmPm;

	
	public ListDialogFragment(){}
	
	public ListDialogFragment(NoteRow note)
	{
		this.currentNote = note;
		this.noteTitle = note.getNoteTitle();
		this.noteText = note.getNote();
		this.noteDate = note.getDate();	
		this.noteTime = note.getTime();	
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getDialog().setCancelable(true);
    	//Get the view
        View view = inflater.inflate(R.layout.list_dialog, container, false);
        
        //get the text views
        noteTitleTextView = (TextView) view.findViewById(R.id.dialog_note_title);
        noteTextView = (TextView) view.findViewById(R.id.dialog_note_text);
        noteDateTextView = (TextView) view.findViewById(R.id.dialog_date_display);
        noteTimeTextView = (TextView) view.findViewById(R.id.dialog_time_display);
        
        //Set the text views
        noteTitleTextView.setText(noteTitle);
        noteTextView.setText(noteText);
        noteDateTextView.setText(noteDate);
        noteTimeTextView.setText(noteTime);
        
        //Get the buttons
        timeChangeButton = (ImageButton) view.findViewById(R.id.dialog_time_change_button);
        dateChangeButton = (ImageButton) view.findViewById(R.id.dialog_date_change_button);
        saveButton = (ImageButton) view.findViewById(R.id.dialog_save_note_button);
        
        //Set the listeners
       	timeChangeButton.setOnClickListener((OnClickListener) this);
    	dateChangeButton.setOnClickListener((OnClickListener) this);
    	saveButton.setOnClickListener((OnClickListener) this);
    	
    	//Misc
    	noteDateTextView.setFocusable(false);
    	noteTimeTextView.setFocusable(false);


        return view;
    }

	public void onClick(View v) {
		switch(v.getId()){
			case R.id.dialog_time_change_button:
				DialogFragment timeFragment = new TimePickerFragment();
				((TimePickerFragment) timeFragment).setFrom(1);
				timeFragment.show(HomePage.getInstance().getFragmentManager(), "Time Picker");				
				break;
				
			case R.id.dialog_date_change_button:
				DialogFragment dateFragment = new DatePickerFragment();
				((DatePickerFragment) dateFragment).setFrom(1);
				dateFragment.show(HomePage.getInstance().getFragmentManager(), "Date Picker");
				break;
				
			case R.id.dialog_save_note_button:
				InputMethodManager mgr = (InputMethodManager) this.getActivity().getSystemService(v.getContext().INPUT_METHOD_SERVICE);
			    mgr.hideSoftInputFromWindow(noteTitleTextView.getWindowToken(), 0);
			    mgr.hideSoftInputFromWindow(noteTextView.getWindowToken(), 0);
			    //Update the current note
				currentNote.setNoteTitle(noteTitleTextView.getText().toString());
				currentNote.setNote(noteTextView.getText().toString());
				currentNote.setDate(noteDateTextView.getText().toString());
				currentNote.setTime(noteTimeTextView.getText().toString());
				
				//Update the database and notify the adapter
				ViewPagerAdapter.updateNote(currentNote);
				ViewPagerAdapter.notifyAdapterOfChange();
				
				break;
			default:
				Toast.makeText(v.getContext(), "ERROR" + v, Toast.LENGTH_SHORT).show();	
		}		
	}
	
	public static void setTimeFromPicker(int hour, int minute, String amPm){
		//Get the time from the picker and update the screen
		currentHour = hour;
		currentMinute = minute;
		currentAmPm = amPm;
		time = pad(hour) + ":" + pad(minute) + " " + amPm;
		noteTimeTextView.setText(time);		
	}
	public static void setDateFromPicker(int month, int day, int year){
		//Get the date from the picker and update the screen
		currentMonth = month;
		currentDay = day;
		currentYear = year;
		String tmpMonth = GenericUtilities.getMonth(month);
		date = tmpMonth + ", " + day + " " + year;

		noteDateTextView.setText(date);		
	}
	
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
}

