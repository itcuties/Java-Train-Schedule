package com.itcuties.java.snippets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Sample Train Schedule implementation.
 * 
 * @author itcuties
 *
 */
public class TrainSchedule {

	// Train schedule
	private List<Date> schedule;
	
	/**
	 * Initialize object
	 * 
	 * @param trainFrequency - in minutes
	 * @param firstTrain - date of the first train
	 * @param lastTrain - date of the last train
	 */
	public TrainSchedule(short trainFrequency, Date firstTrain, Date lastTrain) {
		schedule = new ArrayList<Date>();
		
		// Train frequency in milliseconds
		long trainFrequencyMillis = trainFrequency*60*1000;
		
		// First and last train date in milliseconds
		long firstTrainMillis	  = firstTrain.getTime();
		long lastTrainMillis	  = lastTrain.getTime();
		
		
		// Iterate from the first train date to the last train date to initialize the schedule
		for (long i=firstTrainMillis; i < lastTrainMillis; i += trainFrequencyMillis) {
			schedule.add(new Date(i));
		}
		
		// Add the last train literally
		schedule.add(lastTrain);
	}

	/**
	 * Return time to next train in milliseconds
	 * @param time
	 * @return
	 */
	public long timeToNextTrain(Date time) {
		// Current time in milliseconds
		long currentTime = time.getTime();
		
		// Check if the trains are riding
		if (currentTime <= schedule.get(0).getTime()) // You are before the first train
			return schedule.get(0).getTime() - currentTime; // Time to the first train
		
		if (currentTime >= schedule.get(schedule.size()-1).getTime()) {	// You are after the last train
			// You need to take the first train from the next day schedule
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(schedule.get(0).getTime() + 24*60*60*1000); // Set the next schedule first train time using this schedule first train time + one day in milliseconds
			
			return calendar.getTimeInMillis() - currentTime;
		}
		
		// We are in the schedule - go through it
		for (int i=0; i < schedule.size(); i++) {
			if (i == schedule.size() - 1) // We are at the last element, need to brake the loop
				break;
			// Time of the previous and next trains
			long previous = schedule.get(i).getTime();
			long next     = schedule.get(i+1).getTime();
			
			if (currentTime > previous && currentTime < next)
				return next - currentTime;
			
		}
		
		// Should never happen :)
		return -1;
	}
	
	/**
	 * Get the train schedule.
	 * @return
	 */
	public List<Date> getSchedule() {
		return schedule;
	}
	
	/**
	 * Testing !!!
	 * @param args
	 */
	public static void main(String[] args) {
		// Set the calendar to 06:00 AM today
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 6);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		// The time of the first train
		Date firstTrain = calendar.getTime();
		
		// Let's change the hour 
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		Date lastTrain = calendar.getTime();
		
		// Create the TrainSchedule object
		TrainSchedule ts = new TrainSchedule((short)7, firstTrain, lastTrain);
		
		// Display the schedule
		System.out.println("Today's schedule:");
		for (Date d: ts.getSchedule())
			System.out.println(d);
		
		System.out.println("=========================================");
		
		calendar = Calendar.getInstance();
		// Let's check how much time we have to the next train
		System.out.println("It's " + calendar.getTime() + " you have " + ts.timeToNextTrain(calendar.getTime()) / 1000 + " seconds to the next train");
		
		// Let's check how it would behave when we are before the schedule
		calendar.set(Calendar.HOUR_OF_DAY, 5);
		System.out.println("It's " + calendar.getTime() + " you have " + ts.timeToNextTrain(calendar.getTime()) / 1000 + " seconds to the first train from today's schedule");
		
		// Let's check how it would behave when we are before the schedule
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		System.out.println("It's " + calendar.getTime() + " you have " + ts.timeToNextTrain(calendar.getTime()) / 1000 + " seconds to the first train from the next schedule");
		
	}
}
