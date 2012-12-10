package jharris.onestop;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Instances;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

/**
 * This is the class that is responsible for the handling of the Calendar objects.
 * 
 * @author Joshua Harris
 */
public class CalendarHandler extends Activity{
	private ContentResolver homeContentResolver;
	private CalendarViewObject calendarViewObject;
	private Activity applicationActivity;
	
	// Projection of results from the query on the Calendar
	public static final String[] INSTANCE_PROJECTION = new String[] {
		Instances.BEGIN,         		// 0
		Instances.TITLE,         		// 1
		Instances.CALENDAR_COLOR,		// 2
		Instances.CALENDAR_DISPLAY_NAME // 3
	};

	// The indices for the projection array above.
	private static final int PROJECTION_BEGIN_INDEX = 0;
	private static final int PROJECTION_TITLE_INDEX = 1;
	private static final int PROJECTION_COLOR_INDEX = 2;
	private static final int PROJECTION_CALENDAR_NAME_INDEX = 3;
	
	
	public CalendarHandler(ContentResolver contentResolver, Activity applicationActivity) {
		homeContentResolver = contentResolver;
		calendarViewObject = new CalendarViewObject();
		this.applicationActivity = applicationActivity;
	}
	
	/**
	 * Responsible for handling all the other drawing functions and passing back all the Hoe activity needs
	 *  
	 * @return a list of views for the home activity to draw
	 */
	public CalendarViewObject drawCalendarOnHome() {
		// Set the time ranges for the query
        int[] currentTime = getCurrentTime();
        long startMillis = getBeginTime(currentTime);
		long endMillis = getEndTime(currentTime);
			
		Cursor cursor = queryForCalendarEvents(startMillis, endMillis);
		
		// Move the cursor to the first position, if it returns false it is empty
		if(!cursor.moveToFirst()) {
			return null;
		}
		
		// Go through the results and draw each Event entry
		do {
		    createEventEntry(cursor);
		} while (cursor.moveToNext());
		
		return calendarViewObject;
	}
	
	/**
	* This method is responsible for drawing a single event entry
	*
	* @param cursor - the cursor pointing to the current query result
	*/
	private void createEventEntry(Cursor cursor) {
		String title = null;
		long beginVal = 0;
		int color = 0;
		String calendarName = null;
		
		// Get the field values from the query
		beginVal = cursor.getLong(PROJECTION_BEGIN_INDEX);
		title = cursor.getString(PROJECTION_TITLE_INDEX);
		color = cursor.getInt(PROJECTION_COLOR_INDEX);
		calendarName = cursor.getString(PROJECTION_CALENDAR_NAME_INDEX);
		           
		TextView calendarNameView = makeCalendarNameView(color, calendarName);
		
		TextView calendarEventName = makeCalendarEventNameView(title);
		
		TextView calendarEventTime = makeCalendarEventTimeView(beginVal);
		
		LinearLayout eventInfo = new LinearLayout(applicationActivity);
		eventInfo.setOrientation(LinearLayout.HORIZONTAL);
		eventInfo.addView(calendarEventName);
		eventInfo.addView(calendarEventTime);
		
		calendarViewObject.addEventTuple(calendarNameView, eventInfo);
	}
	
	/**
	* This function makes the view that contains the event time
	*
	* @param beginVal - the time value in milliseconds to be formated and displayed
	*/
	private TextView makeCalendarEventTimeView(long beginVal) {
		TextView calendarEventTime = new TextView(applicationActivity);
		LayoutParams calendarEventTimeParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		calendarEventTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		calendarEventTime.setLayoutParams(calendarEventTimeParams);
		calendarEventTime.setPadding(0, 5, 5, 0);
		calendarEventTime.setGravity(Gravity.RIGHT);
		
		// Format the time to appear in 12hr AM/PM fashion
		SimpleDateFormat formater = new SimpleDateFormat("h:mm a");
		String time = formater.format(new Date(beginVal));
		calendarEventTime.setText(time);
		
		return calendarEventTime;
	}

	/**
	* This function is responsible for creating the view that holds the events name
	*
	* @param title - the title of the event
	*/
	private TextView makeCalendarEventNameView(String title) {
		TextView calendarEventName = new TextView(applicationActivity);
		LayoutParams calendarEventNameParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		calendarEventName.setLayoutParams(calendarEventNameParams);		
		
		calendarEventName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		calendarEventName.setPadding(15, 5, 0, 0);
		calendarEventName.setText(title);
		
		return calendarEventName;
	}

	/**
	* This function is responsible for creating the view that holds the calendar's name in it. It also
	* makes sure to color the name the same color as the calendar.
	*
	* @param color - the color to make the text
	* @param calendarName - the title of the calendar
	*/
	private TextView makeCalendarNameView(int color, String calendarName) {
		TextView calendarNameView = new TextView(applicationActivity);
		LayoutParams calendarNameParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		calendarNameView.setLayoutParams(calendarNameParams);
		
		calendarNameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		calendarNameView.setPadding(15, 5, 0, 0);
		calendarNameView.setTextColor(color);
		calendarNameView.setText(calendarName + ":");
		
		return calendarNameView;
	}
	
	/**
	* This function is responsible for the querying of the calendar events.
	*
	* @param startMillis - the start time in milliseconds
	* @param endMillis - the end time in milliseconds
	*/
	private Cursor queryForCalendarEvents(long startMillis, long endMillis) {
		Cursor cursor = null;
		
		// Construct the query with the desired date range.
		Uri.Builder queryBuilder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(queryBuilder, startMillis);
		ContentUris.appendId(queryBuilder, endMillis);
		
		// Submit the query
		cursor =  homeContentResolver.query(queryBuilder.build(), 
		    INSTANCE_PROJECTION, 
		     null, 
		     null, 
		     null);
		return cursor;
	}

	/**
	* @param currentTime - an array containing information on the current date
	*/
	private static long getEndTime(int[] currentTime) {
		// Set the end time to today at Midnight
		Calendar endTime = Calendar.getInstance();
		endTime.set(currentTime[0], currentTime[1], currentTime[2], 24, 0);
		long endMillis = endTime.getTimeInMillis();
		return endMillis;
	}

	/**
	* @param currentTime - an array containing information on the current date
	*/
	private static long getBeginTime(int[] currentTime) {
		// Set the begining time to today at 0 O'Clock
        Calendar beginTime = Calendar.getInstance();
		beginTime.set(currentTime[0], currentTime[1], currentTime[2], 0, 0);
		long startMillis = beginTime.getTimeInMillis();
		
		return startMillis;
	}
    
    /**
	* Uses the Calendar instance to gain information about the current time and date
	*/
    private static int[] getCurrentTime() {
    	Calendar calendar = Calendar.getInstance();
    	
    	int year = calendar.get(Calendar.YEAR);
    	int month = calendar.get(Calendar.MONTH);
    	int day = calendar.get(Calendar.DAY_OF_MONTH);
    	
    	int[] results = {year, month, day};
    	
    	return results; 	
    }
	
}
