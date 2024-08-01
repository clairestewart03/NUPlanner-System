package cs3500.planner.model;

import java.util.ArrayList;

/**
 * Contains all the observation methods of the model.
 */
public interface ReadOnlyPlannerModel {

  /**
   * Checks if the given event has a time conflict with the given user's schedule.
   *
   * @param event the event to check for time conflicts
   * @param user  the user to check for time conflicts
   * @return true if there are conflicts, false if not.
   * @throws IllegalArgumentException if given user is not in the system
   */
  public boolean checkForTimeConflict(NUEvent event, User user);


  /**
   * Method to return the events in a given user's schedule.
   *
   * @param user the given user
   * @return a list of events in the given users schedule
   */
  public ArrayList<NUEvent> usersEvents(User user);

  /**
   * Returns the current list of users in the central system.
   * @return the List of users in the central system
   */
  public ArrayList<User> usersInSystem();

}