package cs3500.planner.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class representing an existing Schedule for a User.
 */
public class Schedule {
  private final String id;
  private ArrayList<NUEvent> events;

  /**
   * Creates a Schedule for a User.
   *
   * @param id     the User that owns the schedule
   * @param events the list of events in the schedule
   * @throws IllegalArgumentException if the id or events is null
   */
  public Schedule(String id, ArrayList<NUEvent> events) {
    if (id == null || events == null) {
      throw new IllegalArgumentException("ID and schedule cannot be null");
    } else {
      this.id = id;
      this.events = events;
    }
  }

  /**
   * Adds the event to the schedule as long as there is no time conflict.
   * Adds the event to each invitee's schedules as well, as long as there is no time conflict.
   *
   * @param name     the name of the event
   * @param invitees the list of users invited to the event
   * @param location the location of the event and whether it is online
   * @param time     the duration of the event
   * @param host     the user creating the event
   */
  public void addEvent(String name, ArrayList<User> invitees, Location location,
                       Time time, User host) {
    NUEvent e = new NUEvent(name, invitees, location, time, host);
    if (this.events.isEmpty()) {
      this.events.add(e);
    } else if (!checkEventsForConflicts(e)) {
      this.events.add(e);
    }
  }

  /**
   * Checks if there is a time conflict with the given Time and the given list of events.
   *
   * @param event the event to compare to the list of events
   * @return true if any event has a conflict, false if not
   */
  public boolean checkEventsForConflicts(NUEvent event) {
    boolean conflict = false;
    for (NUEvent e : this.events) {
      if (e.checkForConflict(event)) {
        conflict = true;
      }
    }
    return conflict;
  }

  /**
   * Determines if the schedule contains the given event.
   *
   * @param e the event to check
   * @return true if the schedule contains the event, false if not
   */
  public boolean containsEvent(NUEvent e) {
    boolean ret = false;
    for (NUEvent event : events) {
      if (event.equals(e)) {
        ret = true;
      }
    }
    return ret;
  }

  /**
   * Removes the given event from the schedule.
   */
  public void removeEvent(NUEvent e) {
    this.events.remove(e);
  }

  /**
   * Chose to override equals because a schedule has multiple fields, all of which must be equal.
   * Therefore, the regular equals method would not ensure that two schedules are the exact same
   * object.
   *
   * @param other the schedule to compare to this schedule.
   * @return true if this and other are the same exact schedule, false if not.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof Schedule) {
      Schedule s = (Schedule) other;
      return this.id.equals(s.id)
              && this.events.equals(s.events);
    } else {
      return false;
    }
  }

  /**
   * We chose to use the id and events to override hashcode because this should ensure a unique
   * hash code for each schedule.
   *
   * @return a hashcode based on this id and list of events.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.id.length() + this.events.size());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Sunday: ").append(this.printEvents(Day.SUNDAY)).append("\n");
    sb.append("Monday: ").append(this.printEvents(Day.MONDAY)).append("\n");
    sb.append("Tuesday: ").append(this.printEvents(Day.TUESDAY)).append("\n");
    sb.append("Wednesday: ").append(this.printEvents(Day.WEDNESDAY)).append("\n");
    sb.append("Thursday: ").append(this.printEvents(Day.THURSDAY)).append("\n");
    sb.append("Friday: ").append(this.printEvents(Day.FRIDAY)).append("\n");
    sb.append("Saturday: ").append(this.printEvents(Day.SATURDAY)).append("\n");

    return sb.toString();
  }

  /**
   * Converts the given day into a string.
   *
   * @param day the day to convert to a string
   * @return a string representation of the given day.
   */
  public String printEvents(Day day) {
    StringBuilder sb = new StringBuilder();
    for (NUEvent e : events) {
      sb.append(e.toString2(day));
    }
    return sb.toString();
  }

  /**
   * Returns the id of this Schedule to be used as the file name of its XML document.
   *
   * @return this schedule's id
   */
  public String fileName() {
    return this.id;
  }

  /**
   * Converts the contents of this schedule to a string in XML format.
   *
   * @return a string used to create an XML File of this schedule
   */
  public String printXML() {
    StringBuilder sb = new StringBuilder();
    sb.append("<schedule id=\"").append(this.id).append("\">\n");
    sb.append(this.printEventsXML());
    sb.append("</schedule>");
    return sb.toString();
  }

  /**
   * Converts the contents of this schedule's list of events into a String that can be used to
   * write an XML file.
   *
   * @return a String in XML format of this schedule's list of events.
   */
  private String printEventsXML() {
    StringBuilder sb = new StringBuilder();
    for (NUEvent e : events) {
      sb.append(e.printEventXML()).append("\n");
    }
    return sb.toString();
  }

  /**
   * Modifies each event in this schedule that matches the given eventName.
   *
   * @param eventName the event the client wants to modify.
   * @param name      the name the client wants to modify the event's name to.
   * @param invitees  the list of users the client wants to modify the invitees to.
   * @param location  the Location the client wants to modify the event's location to.
   * @param time      the Time the client wants to modify the event's Time to.
   */
  public void modifyEventInSched(String eventName, String name, ArrayList<User> invitees,
                                 Location location, Time time) {
    for (NUEvent e : this.events) {
      e.modifyEvent(eventName, name, invitees, location, time);
    }
  }

  /**
   * Returns the list of events in the current user's schedule.
   *
   * @return a list of events in the schedule
   */
  public ArrayList<NUEvent> eventsInSchedule() {
    return this.events;
  }

}