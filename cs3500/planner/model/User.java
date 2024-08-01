package cs3500.planner.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class representing a User who is the owner of a Schedule.
 */
public class User implements NUPlannerModel {
  private final String uId;

  private Schedule schedule;

  /**
   * Creates a User with id and schedule.
   *
   * @param uId      the user's unique id
   * @param schedule the user's schedule
   * @throws IllegalArgumentException if uId or Schedule is null
   */
  public User(String uId, Schedule schedule) {
    if (uId == null || schedule == null) {
      throw new IllegalArgumentException("ID and schedule cannot be null");
    } else {
      this.uId = uId;
      this.schedule = schedule;
    }
  }

  @Override
  public void createEvent(String name, ArrayList<User> invitees, Location location, Time time,
                          User host) {
    this.schedule.addEvent(name, invitees, location, time, host);
  }

  @Override
  public void modifyEvent(String eventName, String name, ArrayList<User> invitees,
                          Location location, Time time) {
    this.schedule.modifyEventInSched(eventName, name, invitees, location, time);

  }

  @Override
  public void removeEvent(NUEvent e, User u) {
    if (this.schedule.containsEvent(e)) {
      this.schedule.removeEvent(e);
      u.schedule.removeEvent(e);
    }
  }

  @Override
  public void uploadUser() {
    //empty for now.
  }

  @Override
  public void addFile(File selectedFile) {
    //empty for now.
  }

  @Override
  public void resetUsers() {
    //empty for now.
  }

  @Override
  public boolean checkForTimeConflict(NUEvent event, User user) {
    return this.schedule.checkEventsForConflicts(event);
  }

  @Override
  public ArrayList<NUEvent> usersEvents(User user) {
    return this.schedule.eventsInSchedule();
  }

  /**
   * Method should never be called on a user.
   * Used in CentralSystem to get the list of users in the system.
   *
   * @return empty arrayList.
   */
  public ArrayList<User> usersInSystem() {
    return new ArrayList<>();
  }

  /**
   * Chose to override equals because a User has multiple fields, all of which must be equal.
   * Therefore, the regular equals method would not ensure that two Users are the exact same
   * object.
   *
   * @param other the User to compare to this User.
   * @return true if this and other are the same exact User, false if not.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof User) {
      User u = (User) other;
      return this.uId.equals(u.uId)
              && this.schedule.equals(u.schedule);
    } else {
      return false;
    }
  }

  /**
   * We chose to use the uId to override hashcode because this should ensure a unique
   * hash code for each User.
   *
   * @return a hashcode based on this uId.
   */
  @Override
  public int hashCode() {
    return Objects.hash(uId.length() * uId.length() / 2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("User: ").append(this.uId).append("\n").append(this.schedule.toString());
    return sb.toString();
  }

  /**
   * Returns the uId of this User representing their name.
   *
   * @return this user's uId.
   */
  public String printName() {
    return this.uId;
  }

  /**
   * Returns a string for the File writer to write into the XML file representing a user invited
   * to the event.
   *
   * @return String in XML format representing a user invited of the event.
   */
  public String printUserXML() {
    StringBuilder sb = new StringBuilder();
    sb.append("<uid>").append(this.uId).append("</uid>\n");
    return sb.toString();
  }

  public boolean containsEvent(NUEvent e) {
    return this.schedule.containsEvent(e);
  }

}