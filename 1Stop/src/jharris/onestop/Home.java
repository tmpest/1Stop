package jharris.onestop;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;



/**
* This is the Home Activity. It is the primary page of the app and is responsible for displaying 
* the relevant information for the user
*
* @author Joshua Harris
*/
public class Home extends Activity {
	public static final Logger log = Logger.getLogger("DevLog");
	private TaskHandler taskHandler = new TaskHandler(this);
	/**
	* The onCreate method called by Android when the App is started
	*
	* @param savedInstanceState - state of the system used to start the onCreate process
	*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        drawCalendarFeatures();
        
        Intent intent = getIntent();
        Bundle data = intent.getExtras();

        if(data != null) {
        	String name = (String) data.get(AppKeys.TASK_NAME);
            Date dueDate = (Date) data.get(AppKeys.TASK_DATE);
        	taskHandler.addTask(dueDate, name);
        }
        
        drawTaskFeatures();
       
    }
    
    /**
     * Called when the user clicks the add button
     * 
     * @param view - the view this originated from
     */
    public void startCreateTask(View view) {
    	Intent intent = new Intent(this, TaskActivity.class);
    	startActivity(intent);
    }
    /**
	* This method generates the menu toolbar when requested
	*
	* @param menu - The menu object containing information on what to populate the menu with
	*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    /**
     * This method is meant to wrap up the drawing of the calendar features of the page
     */
	private void drawCalendarFeatures() {
		CalendarHandler calendarHandler = new CalendarHandler(getContentResolver(), this);
        CalendarViewObject calendarViewObject = calendarHandler.drawCalendarOnHome();
        
        // Find the calendar layout to which to add event entries
        LinearLayout calendarLayout = (LinearLayout) findViewById(R.id.calendar_entries);
        
        log.info("Count of Cal Objects: " + calendarViewObject.getCount());
        
        for(int i = 0; i < calendarViewObject.getCount(); i++) {
        	calendarLayout.addView(calendarViewObject.getCalendarNameView(i));
        	calendarLayout.addView(calendarViewObject.getEventInfoView(i));
        }
	}
	
	/**
	 * This method is meant to handle the drawing of the task features
	 */
	private void drawTaskFeatures() {
		ArrayList<LinearLayout> taskEntries = taskHandler.drawTaskList();
		LinearLayout taskPane = (LinearLayout) findViewById(R.id.task_entries);
		
		for(int i = 0; i < taskEntries.size(); i++) {
			taskPane.addView(taskEntries.get(i));
		}
	}
}