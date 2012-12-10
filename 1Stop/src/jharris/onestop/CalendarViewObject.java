package jharris.onestop;

import java.util.ArrayList;

import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class is designed to make accessing a piece of event data simple
 * 
 * @author Joshua Harris
 */
public class CalendarViewObject {
	
	private ArrayList<TextView> calendarNameList;
	private ArrayList<LinearLayout> eventInfoList;

	/**
	 * Constructor
	 */
	public CalendarViewObject() {
		calendarNameList = new ArrayList<TextView>();
		eventInfoList = new ArrayList<LinearLayout>();
	}
	
	/**
	 * Adds an event to the list of events
	 * 
	 * @param calendarName - name of the calendar it belongs to
	 * @param eventInfo - info for the event (title and time)
	 */
	public void addEventTuple(TextView calendarName, LinearLayout eventInfo) {
		calendarNameList.add(calendarName);
		eventInfoList.add(eventInfo);
	}

	/**
	 * Getters
	 */
	
	public TextView getCalendarNameView(int index) {
		return calendarNameList.get(index);
	}
	
	public LinearLayout getEventInfoView(int index) {
		return eventInfoList.get(index);
	}
	
	public int getCount() {
		return calendarNameList.size();
	}
}
