package wallace.android.easynotes.models;


public class NoteRow
{
    private String noteTitle = new String();
    private String note = new String();    
    private String date = new String();  
    private String time = new String();  
    private long id;  
    /**
     * Default Constructor - does nothing
     */
    public NoteRow() { }
	public String getNoteTitle() {
		return noteTitle;
	}
	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

}
