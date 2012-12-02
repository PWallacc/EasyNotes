package wallace.android.easynotes.utils;

import java.util.Calendar;

import wallace.android.easynotes.adapters.ViewPagerAdapter;
import wallace.android.easynotes.ui.ListDialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
	private String amPm;
	private int fromWhichScreen;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// Create a new instance of TimePickerDialog and return it
		return  new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}
	
	public void onTimeSet(TimePicker timePicker, int hour, int minute) {
		if(hour > 12){
			amPm = "pm";
		} else{
			amPm = "am";
		}
		int finalHour = convertTo12Hour(hour);
		if(fromWhichScreen == 0){
			ViewPagerAdapter.setTimeFromPicker(finalHour, minute, amPm);		
		} else{
			ListDialogFragment.setTimeFromPicker(finalHour, minute, amPm);
		}
	}
	
	private int convertTo12Hour(int hour){
		int tmpHour = hour;
		   if (hour >= 12) {
			   hour = tmpHour-12;
		    }
		    if (hour == 0) {
		        hour = 12;
		    }
		return hour;
	}
	
	public void setFrom(int fromWhich){
		this.fromWhichScreen = fromWhich;
	}
}
