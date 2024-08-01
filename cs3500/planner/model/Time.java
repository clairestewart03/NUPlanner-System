package cs3500.planner.model;

import java.util.List;
import java.util.Objects;

/**
 * Class representing the duration of an Event.
 * Time includes the start and end days of the event and the start and end times.
 */
public class Time {
  private final Day startDay;
  private final Day endDay;
  private String startTime;
  //INVARIANT: startTime.length() == 4
  private String endTime;
  //INVARIANT: endTime.length() == 4

  /**
   * Creates a Time for the Event.
   * @param startDay the day the Event starts
   * @param endDay the day the Event ends
   * @param startTime start time of the Event
   * @param endTime end time of the Event
   * @throws IllegalArgumentException if start or end day is null
   * @throws IllegalArgumentException if start or end times are not 4 digits
   * @throws IllegalArgumentException if time is not a valid clock time
   * @throws IllegalArgumentException if start or end day is not a valid Day
   */
  public Time(Day startDay, Day endDay, String startTime, String endTime) {
    if (startDay == null || endDay == null) {
      throw new IllegalArgumentException("Start and end days cannot be null");
    }

    if (startTime.length() != 4 || endTime.length() != 4) {
      throw new IllegalArgumentException("Start or end time must be 4 digits");
    }


    if (!validClockTime(startTime, endTime)) {
      throw new IllegalArgumentException("Start and end time must be a valid clock time");
    }

    if (!validDay(startDay, endDay)) {
      throw new IllegalArgumentException("Day must be valid");
    }
    else {
      this.startDay = startDay;
      this.endDay = endDay;
      this.startTime = startTime;
      this.endTime = endTime;
    }
  }


  /**
   * Checks if the given day is one of the valid days.
   * @return true if it's a valid day, false if not
   */
  private boolean validDay(Day startDay, Day endDay) {
    return this.createListOfDays().contains(startDay)
            && this.createListOfDays().contains(endDay);
  }


  /**
   * Checks to see if the hours and minutes of the start and end times are valid
   * 24-hour clock times.
   * @return true if the times are valid, false if not
   */

  private boolean validClockTime(String startTime, String endTime) {
    return Integer.parseInt(startTime.substring(0, 2)) >= 0
            && Integer.parseInt(startTime.substring(0, 2)) < 24
            && Integer.parseInt(endTime.substring(0, 2)) >= 0
            && Integer.parseInt(endTime.substring(0, 2)) < 24
            && Integer.parseInt(startTime.substring(2, 4)) >= 0
            && Integer.parseInt(startTime.substring(2, 4)) < 60
            && Integer.parseInt(endTime.substring(2, 4)) >= 0
            && Integer.parseInt(endTime.substring(2, 4)) < 60;
  }

  /**
   * Compares this time to the given time to determine if they overlap.
   * @param time the time to compare to
   * @return false if the times overlap, true if not
   */
  public boolean noTimeConflict(Time time) {
    int timeStartDayIndex = time.startDay.dayIndex();
    int timeEndDayIndex = time.endDay.dayIndex();
    int thisStartDayIndex = this.startDay.dayIndex();
    int thisEndDayIndex = this.endDay.dayIndex();

    time.removeZeros();
    this.removeZeros();

    Boolean ret = true;
    //Checks for time conflict with events on the same day.
    if (this.startDay == time.startDay
            && this.startDay == time.endDay
            && this.startDay == this.endDay) {
      ret = (Integer.parseInt(time.startTime) < Integer.parseInt(this.startTime)
              && Integer.parseInt(time.endTime) <= Integer.parseInt(this.startTime))
              || (Integer.parseInt(time.startTime) >= Integer.parseInt(this.endTime)
              && Integer.parseInt(time.endTime) > Integer.parseInt(this.endTime));
    }
    //Checks for time conflict with events spanning multiple days.
    if ((timeEndDayIndex > thisStartDayIndex
            && timeEndDayIndex < thisEndDayIndex)
            || timeStartDayIndex > thisStartDayIndex
            && timeStartDayIndex < thisEndDayIndex) {
      ret = false;
    }
    return ret;
  }

  /**
   * Removes the leading zero for the start and end times of this Time.
   */
  private void removeZeros() {
    if (this.startTime.substring(0,1).equals("0")) {
      this.startTime = this.startTime.substring(1, 4);
    }
    if (this.endTime.substring(0,1).equals("0")) {
      this.endTime = this.endTime.substring(1, 4);
    }
  }


  /**
   * Creates an array of the days of the week.
   * @return an array of the days of the week
   */
  private List<Day> createListOfDays() {
    List<Day> list = List.of(Day.SUNDAY, Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY,
            Day.FRIDAY, Day.SATURDAY);
    return list;
  }

  /**
   * Chose to override equals because a Time has multiple fields, all of which must be equal.
   * Therefore, the regular equals method would not ensure that two Times are the exact same
   * object.
   *
   * @param other the Time to compare to this Time.
   * @return true if this and other are the same exact Time, false if not.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof Time) {
      Time time = (Time) other;
      this.removeZeros();
      time.removeZeros();
      return this.startDay.equals(time.startDay)
              && this.endDay.equals(time.endDay)
              && this.startTime.equals(time.startTime)
              && this.endTime.equals(time.endTime);
    }
    else {
      return false;
    }
  }

  /**
   * We chose to use the start time and end time to override hashcode because this should ensure
   * a unique hash code for each Time.
   *
   * @return a hashcode based on this start time and end time.
   */
  @Override
  public int hashCode() {
    return Objects.hash(Integer.parseInt(this.startTime) + Integer.parseInt(this.endTime));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    this.removeZeros();
    sb.append(this.startDay.toString()).append(":"
            + " ").append(this.startTime).append(" -> ").append(this.endDay.toString()).append(":"
            + " ").append(this.endTime);
    return sb.toString();

  }


  public boolean sameDay(Day day) {
    return this.startDay.equals(day);
  }

  /**
   * Returns a string for the File writer to write into the XML file representing the time of the
   * event.
   * @return String in XML format representing the duration of an event.
   */
  public String printTimeXML() {
    StringBuilder sb = new StringBuilder();
    sb.append("<start-day>").append(this.startDay.toString()).append("</start-day>\n");
    sb.append("<start>").append(this.startTime).append("</start>\n");
    sb.append("<end-day>").append(this.endDay.toString()).append("</end-day>\n");
    sb.append("<end>").append(this.endTime).append("</end>");
    return sb.toString();
  }

  /**
   * Makes an Array of Integers to represent the fields of this Time.
   * @return an Array of Integers
   */
  public Integer[] timeInfo() {
    this.removeZeros();
    Integer[] info = new Integer[4];
    info[0] = this.startDay.dayIndex();
    info[1] = Integer.parseInt(this.startTime);
    info[2] = this.endDay.dayIndex();
    info[3] = Integer.parseInt(this.endTime);
    return info;
  }

  public int startDayIndexHelp() {
    return this.startDay.dayIndex();
  }

  public int endDayIndexHelp() {
    return this.endDay.dayIndex();
  }

  public String startTimeTextHelp() {
    this.removeZeros();
    return this.startTime;
  }

  public String endTimeTextHelp() {
    this.removeZeros();
    return this.endTime;
  }

  /*
  public boolean timeInBetween(String time) {
    int timeInt = Integer.parseInt(time);
    this.removeZeros();
    int startInt = Integer.parseInt(this.startTime);
    int endInt = Integer.parseInt(this.endTime);
    if (this.startDay == this.endDay) {
      return (timeInt >= startInt && timeInt <= endInt);
    }
    else {
      int daysInBetween = this.endDayIndexHelp() - this.startDayIndexHelp() - 1;
      for (int index = this.startDayIndexHelp(); index < daysInBetween; index++) {

      }

    }

  }


   */


}
