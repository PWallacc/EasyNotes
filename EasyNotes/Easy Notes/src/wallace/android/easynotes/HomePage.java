package wallace.android.easynotes;

import wallace.android.easynotes.adapters.ViewPagerAdapter;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

public class HomePage extends FragmentActivity
{
	public static HomePage thisInstance;
	
	View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    view = getWindow().getDecorView().findViewById(android.R.id.content);
	    hideSoftKeyboard(this, view);
	    setContentView(R.layout.home_page);
	    thisInstance = this;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	    ViewPagerAdapter adapter = new ViewPagerAdapter();
	    ViewPager myPager = (ViewPager) findViewById(R.id.twopanepager);
	    myPager.setAdapter(adapter);
	    myPager.setCurrentItem(0);	
	    
	}
	
	public static void hideSoftKeyboard(FragmentActivity activity, View view) {
		  InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		  inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	
	public static HomePage getInstance(){
		return thisInstance;
	}
}
