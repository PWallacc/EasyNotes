package wallace.android.easynotes.utils;

import java.util.Calendar;

import wallace.android.easynotes.adapters.ViewPagerAdapter;
import wallace.android.easynotes.ui.ListDialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
	private int fromWhichScreen;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	public void onDateSet(DatePicker datePicker, int year, int month, int day)
	{
		// Auto-generated method stub
		if(fromWhichScreen == 0){
			ViewPagerAdapter.setDateFromPicker(month, day, year);
		} else{
			ListDialogFragment.setDateFromPicker(month, day, year);
		}
	}
	
	public void setFrom(int fromWhere){
		this.fromWhichScreen = fromWhere;
	}
}
