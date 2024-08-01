package cs3500.planner.view;

import java.awt.event.ActionListener;

/**
 * Interface to describe what the main systems frame is capable of in GUI form.
 */
public interface NUPlannerFrameView extends ActionListener {

  /**
   * Sets up the action listener so that the event frame can react to the user's click.
   *
   * @param clicks the ActionListener reacting to the user's click.
   */
  void setListeners(ActionListener clicks);

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();

  /**
   * Reset the focus on the appropriate part of the view that has the keyboard listener attached to
   * it, so that keyboard events will still flow through.
   */
  void resetFocus();

  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();


}
