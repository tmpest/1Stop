package jharris.onestop;

import java.util.Calendar;
import java.util.Date;

/**
 * This class holds individual Task data
 * 
 * @author Joshua
 */
public class Task {
	private String taskTitle;
	private Date taskDueDate;
	
	/**
	 * Constructors
	 */
	
	public Task() {
		taskTitle = "";
		
		Calendar deviceTime = Calendar.getInstance();
		taskDueDate = deviceTime.getTime();
	}
	
	public Task(String taskTitle, Date taskDueDate){
		this.taskTitle = taskTitle;
		this.taskDueDate = taskDueDate;
	}
	
	/**
	 * Getters and Setters
	 */
	
	public String getTaskTitle() {
		return taskTitle;
	}
	
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	
	public Date getTaskDueDate() {
		return taskDueDate;
	}
	
	public void setTaskDueDate(Date taskDueDate) {
		this.taskDueDate = taskDueDate;
	}
}
