package wallace.android.easynotes.adapters;

import java.util.List;

import wallace.android.easynotes.R;
import wallace.android.easynotes.models.NoteRow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NoteListAdapter extends ArrayAdapter<NoteRow> {
  private final Context context;
  private final List<NoteRow> values;

  public NoteListAdapter(Context context, List<NoteRow> values) {
    super(context, R.layout.home_page, values);
    this.context = context;
    this.values = values;
  }
  
  static class RowHolder
  {
	  protected TextView notetitle;
	  protected TextView note;
	  protected TextView date;
	  protected TextView time;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
	  if (convertView == null) {
		    LayoutInflater inflater = (LayoutInflater) context
		            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    convertView = inflater.inflate(R.layout.row_layout, null);
	      RowHolder holder = new RowHolder();
	      holder.notetitle = (TextView) convertView.findViewById(R.id.title);
	      holder.date = (TextView) convertView.findViewById(R.id.date);
	      holder.time = (TextView) convertView.findViewById(R.id.time);
	      holder.note = (TextView) convertView.findViewById(R.id.text);
	      convertView.setTag(holder);
	  }
	  RowHolder holder = (RowHolder) convertView.getTag();
	  String noteTitle = values.get(position).getNoteTitle();
	  String date = values.get(position).getDate();
	  String time = values.get(position).getTime();
	  String note = values.get(position).getNote();
	  long id = values.get(position).getId();
	  
	  
	  holder.notetitle.setText(noteTitle);
	  holder.date.setText(date);
	  holder.time.setText(time);
	  holder.note.setText(note);
	    
    return convertView;
  }
} 
