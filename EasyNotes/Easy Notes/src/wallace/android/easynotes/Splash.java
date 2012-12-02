package wallace.android.easynotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Splash extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.splash_screen);
	    
        Thread splashTread = new Thread() {
            @Override
            public void run() {
 
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                		goToHomePage();
                }
            }
        };
        splashTread.start();
 
    }
	
	private void goToHomePage(){
		Intent intent = new Intent(this.getApplicationContext(), HomePage.class);
    	startActivityForResult(intent, 0);

	}
}
