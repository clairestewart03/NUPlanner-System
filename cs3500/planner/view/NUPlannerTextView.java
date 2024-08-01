package cs3500.planner.view;

import cs3500.planner.model.CentralSystem;

/**
 * Represents the Text View for the NUPlanner system. Displays all the users
 * and their schedules in text form.
 */
public class NUPlannerTextView implements NUPlannerView {
  private final CentralSystem planner;

  /**
   * Creates a NUPlannerTextView with the given model and StringBuilder for output.
   * @param planner the model to visualize
   */
  public NUPlannerTextView(CentralSystem planner) {
    this.planner = planner;

  }

  /**
   * Returns the NUPlannerModel in string form listing all the users and their schedules.
   * @return a String representing the Planner system
   */
  public String toString() {
    return this.planner.toString();
  }
}
