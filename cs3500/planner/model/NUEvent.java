package cs3500.planner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a single event in a schedule.
 */
public class NUEvent {
  private String name;
  private ArrayList<User> invitees;
  private Location location;
  private Time time;

  private final User host;

  /**
   * Creates an Event.
   *
   * @param name     the name of the Ev ent
   * @param invitees the invited users
   * @param location the location of the Event and whether it's online or not
   * @param time     the day and time of the Event
   * @param host     the host of the event
   * @throws IllegalArgumentException if any of the Event elements are null
   */
  public NUEvent(String name, ArrayList<User> invitees, Location location, Time time, User host) {
    if (name == null || invitees == null || location == null || time == null || host == null) {
      throw new IllegalArgumentException("Elements cannot be null");
    } else {
      this.name = name;
      this.invitees = invitees;
      this.location = location;
      this.time = time;
      this.host = host;
    }
  }

  /**
   * Checks if the given time overlaps with this event's time.
   *
   * @param e the event to check conflicting time
   * @return true if the two times overlap, false if not
   */
  public boolean checkForConflict(NUEvent e) {
    return !this.time.noTimeConflict(e.time);
  }

  /**
   * Determines if the given user is the host of this event.
   * Purpose: Used in removeEvent in CentralSystem to determine whether to remove the event
   * from all invitee's schedules (if the client removing the event is the host) or just client's.
   *
   * @param u the user to compare to this host.
   * @return true if the given user is the host, false if not.
   */
  public boolean removeFromHost(User u) {
    return this.host.equals(u);
  }

  /**
   * Modifies the name of the event to the given name.
   *
   * @param name the String to modify the name field with
   */
  public void modifyName(String name) {
    if (name != null) {
      this.name = name;
    }
  }

  /**
   * Modifies the invitees of the event to the given list of users.
   *
   * @param invitees the list of users to modify the invitees field with
   */
  public void modifyInvitees(ArrayList<User> invitees) {
    if (invitees != null) {
      this.invitees = invitees;
    }
  }

  /**
   * Modifies the location of the event to the given Location.
   *
   * @param location the Location to modify the location field with
   */
  public void modifyLocation(Location location) {
    if (location != null) {
      this.location = location;
    }
  }

  /**
   * Modifies the time of the event to the given Time.
   *
   * @param time the Time to modify the time field with
   */
  public void modifyTime(Time time) {
    if (time != null) {
      for (User user : this.invitees) {
        if (!this.time.noTimeConflict(time)) {
          user.removeEvent(this, user);
        }
      }
      this.time = time;
    }
  }

  /**
   * Chose to override equals because a NUEvent has multiple fields, all of which must be equal.
   * Therefore, the regular equals method would not ensure that two NUEvents are the exact same
   * object.
   *
   * @param other the event to compare to this event.
   * @return true if this and other are the same exact event, false if not.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof NUEvent) {
      NUEvent e = (NUEvent) other;
      return this.name.equals(e.name)
              && this.equalsInvitees(this.invitees, e.invitees)
              && this.location.equals(e.location)
              && this.time.equals(e.time)
              && this.host.equals(e.host);
    } else {
      return false;
    }
  }

  /**
   * Checks if the two given lists of invitees are the same exact lists.
   * Helps override the equals method in NUEvent.
   *
   * @param invitees one list of users to compare.
   * @param other the other list of users to compare
   * @return true if the two lists are the same, false if not.
   */
  private boolean equalsInvitees(ArrayList<User> invitees, ArrayList<User> other) {
    List<Boolean> booleans = new ArrayList<>();
    if (invitees.isEmpty() && other.isEmpty()) {
      return true;
    }
    for (int idx = 0; idx < invitees.size(); idx++) {
      booleans.add(invitees.get(idx).equals(other.get(idx)));
    }
    Boolean equals = true;
    for (Boolean bool : booleans) {
      if (bool.equals(false)) {
        equals = false;
        break;
      }
    }
    return equals;
  }

  /**
   * We chose to use this name and list of invitees to override hashcode because this should ensure
   * a unique hash code for each event.
   *
   * @return a hashcode based on this name and list of invitees.
   */
  @Override
  public int hashCode() {
    return Objects.hash(name.length() + invitees.size());
  }

  /**
   * Converts the given event to a readable String to be used in the display of the schedule.
   *
   * @param day the day to determine if the event should be written or not.
   * @return a String representing the event.
   */
  public String toString2(Day day) {
    StringBuilder sb = new StringBuilder();
    if (this.time.sameDay(day)) {
      sb.append("\n\tname: ").append(this.name).append("\n");
      sb.append("\ttime: ").append(this.time.toString()).append("\n");
      sb.append("\tlocation: ").append(this.location.toString()).append("\n");
      sb.append("\tinvitees: ").append(this.inviteesString(this.invitees));
    } else {
      //do nothing;
    }
    return sb.toString();
  }

  /**
   * Converts the given list of users representing the event's list of invitees to a String.
   *
   * @param invitees list to convert to a String.
   * @return a String representing the given list of invitees.
   */
  private String inviteesString(ArrayList<User> invitees) {
    StringBuilder sb = new StringBuilder();
    for (User u : invitees) {
      sb.append(u.printName()).append("\t");
    }
    return sb.toString();
  }

  /**
   * Prints a string for the FileWriter to add to the XML file representing an event in the
   * schedule.
   *
   * @return String in XML format representing an event.
   */
  public String printEventXML() {
    StringBuilder sb = new StringBuilder();
    sb.append("<event>\n").append("<name>").append(this.name).append("</name>\n");
    sb.append("<time>\n").append(this.time.printTimeXML()).append("\n").append("</time>\n");
    sb.append("<location>\n").append(this.location.printLocationXML()).append("\n").append("</loca"
            + "tion>\n");
    sb.append("<users>\n").append(this.printUsersXML()).append("</users>\n");
    sb.append("</event>");
    return sb.toString();
  }

  /**
   * Helper for printEventXML that prints each invitee in XML format for the file write to add
   * to the XML file.
   *
   * @return String in XML format representing the list of invited Users
   */
  private String printUsersXML() {
    StringBuilder sb = new StringBuilder();
    for (User u : invitees) {
      sb.append(u.printUserXML());
    }
    return sb.toString();
  }

  /**
   * Removes the given event from the list of invitees.
   *
   * @param e the event to remove.
   */
  public void removeEventFromInvitee(NUEvent e) {
    for (User u : invitees) {
      u.removeEvent(e, u);
    }
  }

  /**
   * Modifies each field of this event if it matches the String representing the event the
   * client wishes to modify.
   *
   * @param eventName the event the client wants to modify.
   * @param name the name the client wants to change the event name to.
   * @param invitees the list of invitees the client wants the invitees to.
   * @param location the Location the client wants to change this location to.
   * @param time the Time the client wants to change this time to.
   */
  public void modifyEvent(String eventName, String name, ArrayList<User> invitees,
                          Location location, Time time) {
    if (this.name.equals(eventName)) {
      this.modifyName(name);
      this.modifyInvitees(invitees);
      this.modifyLocation(location);
      this.modifyTime(time);
    }
  }

  /**
   * Creates an Array of Integers to represent this event's time.
   * @return an Array of Integers.
   */
  public Integer[] eventDuration() {
    return this.time.timeInfo();
  }

  public String eventName() {
    return this.name;
  }

  public String locationName() {
    return this.location.locationName();
  }

  public boolean isOnline() {
    return this.location.isOnlineHelp();
  }

  public int startDayIndex() {
    return this.time.startDayIndexHelp();
  }

  public int endDayIndex() {
    return this.time.endDayIndexHelp();
  }

  public String startTimeText() {
    return this.time.startTimeTextHelp();
  }

  public String endTimeText() {
    return this.time.endTimeTextHelp();
  }

  /*
  public boolean timeInBetweenEvent(String time) {
    return this.time.timeInBetween(time);

  }

   */
}