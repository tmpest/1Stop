package jharris.onestop;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * This is the activity for task creation view
 * 
 * @author Joshua Harris
 */
public class TaskActivity extends Activity {
	
	/**
	 * The onCreate method for the Activity
	 * 
	 * @param savedInstanceState - the state variable
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);
	}
	
	/**
	 * Create Options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * OnClick function for the submit button to add a task
	 *
	 * @param V - a view for the onClick event
	 */
	public void createTask(View V) {
		EditText taskName = (EditText) findViewById(R.id.taskName);
		DatePicker taskDue = (DatePicker) findViewById(R.id.taskDueDate);
		
		String resultName = taskName.getText().toString();
		Date resultDate = new GregorianCalendar(taskDue.getYear(), taskDue.getMonth(), 
				taskDue.getDayOfMonth()).getTime();
		
		Intent intent = new Intent(this, Home.class);
		
		intent.putExtra(AppKeys.TASK_NAME, resultName);
		intent.putExtra(AppKeys.TASK_DATE, resultDate);
		
		startActivity(intent);
	}
	
	/**
	 * This class is used to set up a Date Picker
	 * 
	 * @author Google's Android Developer Tutorial on Pickers
	 */
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

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

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
		}
	}
}
