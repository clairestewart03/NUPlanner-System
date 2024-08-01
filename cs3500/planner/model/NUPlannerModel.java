package cs3500.planner.model;

import java.io.File;
import java.util.ArrayList;

/**
 * Interface representing a weekly planner system for multiple users.
 * Contains the operations and mutator methods necessary for users to interact with the planner.
 */
public interface NUPlannerModel extends ReadOnlyPlannerModel {
  /**
   * Creates an Event at the given time and location for the client (host) and all invitees.
   *
   * @param name     the name of the event
   * @param invitees the users invited to the event
   * @param location the location of the event and whether it is online
   * @param time     the duration of the event
   * @param host     the host of the event
   * @throws IllegalArgumentException if the Event conflicts with the host's Schedule
   */
  public void createEvent(String name, ArrayList<User> invitees, Location location, Time time,
                          User host);

  /**
   * Modifies the attributes of the given event.
   * These modifications are consistent across all users invited to the event.
   *
   * @param name the name of the event the client wants to modify.
   * @param editedName the name the client wants to modify the event's name to.
   * @param invitees the list of users the client wants to modify the event's invitees to.
   * @param location the location the client wants to modify the event's location to.
   * @param time the time the client wants to modify the event's time to.
   */
  public void modifyEvent(String name, String editedName, ArrayList<User> invitees,
                          Location location, Time time);

  /**
   * Removes an event from the given users schedule and updates the event's list of invitees.
   * If the user is the host of the event, then it removes the event from all invitees schedules.
   *
   * @param e the event that is to be removed
   */
  public void removeEvent(NUEvent e, User u);

  /**
   * Uploads each XML file in the system's list of Files
   * Each File represents a single User’s schedule.
   */
  public void uploadUser();


  /**
   * Adds the given file to the List of Files.
   * @param selectedFile the file to add
   */
  void addFile(File selectedFile);

  /**
   * Resets the model's list of users to allow the view to re-upload users based on the files
   * in the system.
   */
  void resetUsers();
}