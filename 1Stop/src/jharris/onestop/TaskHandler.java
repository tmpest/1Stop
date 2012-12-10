package jharris.onestop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.PriorityQueue;

import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

/**
 * This is the class responsible for handling the task operations. It will 
 * create the view for the task objects, handle the deletion, creation, and 
 * editing of tasks. 
 * 
 * @author Joshua Harris
 */

public class TaskHandler extends Activity{
	private static final int INITIAL_PRIORITY_QUEUE_SIZE = 10;
	private PriorityQueue<Task> taskList;
	private final Activity applicationActivity;
	
	/**
	 * Constructor
	 */
	
	public TaskHandler(Activity applicationActivity) {
		TaskComparator comparator = new TaskComparator();
		taskList = new PriorityQueue<Task>(INITIAL_PRIORITY_QUEUE_SIZE, comparator);
		this.applicationActivity = applicationActivity;
	}
	
	
	/**
	 * Adds a task into the list of tasks to do.
	 * 
	 * @param taskDueDate - the Date in which to acomplish the task by
	 * @param taskTitle - the title of the task
	 */
	public void addTask(Date taskDueDate, String taskTitle) {
		Task newTask = new Task(taskTitle, taskDueDate);
		taskList.add(newTask);
	}
	
	/**
	 * This will search through the tasks list and remove the corresponding 
	 * task or do nothing if there is no match.
	 * 
	 * @param taskTitle - the title of the task to remove.
	 */
	public void removeTask(String taskTitle) {
		Iterator<Task> taskListIterator = taskList.iterator();
		
		Task taskToRemove = null;
		boolean isFound = false;

		while(taskListIterator.hasNext()) {
			taskToRemove = taskListIterator.next();
			
			if(taskToRemove.getTaskTitle().equals(taskTitle)) {
				isFound = true;
				break;
			}
		}
		
		if(isFound) {
			taskList.remove(taskToRemove);
		}
	}
	
	/**
	 * This is the function responsible for drawing the layouts for each task.
	 * It returns a list of layout to be added to view
	 * 
	 * @return list of linear layouts to be added to a view
	 */
	public ArrayList<LinearLayout> drawTaskList() {
		ArrayList<LinearLayout> taskViews = new ArrayList<LinearLayout>();
		Iterator<Task> taskIter = taskList.iterator();
		
		while(taskIter.hasNext()){
			Task currTask = taskIter.next();
			
			// The Linear Layout holding the Task information
			LinearLayout taskEntry = new LinearLayout(applicationActivity);
			taskEntry.setOrientation(LinearLayout.HORIZONTAL);
			taskEntry.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			
			// Make the text view that holds the name of the Task
			TextView taskName = new TextView(applicationActivity);
			taskName.setPadding(10, 5, 5, 5);
			taskName.setText(currTask.getTaskTitle());
			
			// Formating the Date object
			SimpleDateFormat formater = new SimpleDateFormat("MM/dd");
			String date = formater.format(currTask.getTaskDueDate());
			
			// Make the text view that holds the date information
			TextView taskDate = new TextView(applicationActivity);
			taskDate.setPadding(15, 5, 5, 5);
			taskDate.setText(date);
			taskDate.setGravity(Gravity.RIGHT);
			
			// Checkbox for the entry
			CheckBox removeTask = new CheckBox(applicationActivity);
			removeTask.setOnCheckedChangeListener(null); //TODO Set Listener!
			
			// Add the views to the entry view before adding it to the list
			taskEntry.addView(removeTask);
			taskEntry.addView(taskName);
			taskEntry.addView(taskDate);
			
			taskViews.add(taskEntry);
		}
		
		return taskViews;
	}
	
	/**
	 * Comparator implementation for the priority queue of tasks. It is sorted
	 * by dates in order from those occurring sooner to those occurring later.
	 * 
	 * @author Joshua Harris
	 */
	private class TaskComparator implements Comparator<Task> {
		
		public int compare(Task lhs, Task rhs) {
			Date lhsDueDate = lhs.getTaskDueDate();
			Date rhsDueDate = rhs.getTaskDueDate();
			
			if(lhsDueDate.before(rhsDueDate)) 
				return 1;
			else
				return -1;
		}
		
	}
}
