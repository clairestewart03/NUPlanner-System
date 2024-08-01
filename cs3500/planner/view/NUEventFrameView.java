package cs3500.planner.view;

import java.awt.event.ActionListener;

/**
 * Interface to describe what an NUEvent's frame is capable of in GUI form.
 */
public interface NUEventFrameView extends ActionListener {

  /**
   * Sets up the action listener so that the event frame can react to the user's click.
   *
   * @param clicks the ActionListener reacting to the user's click.
   */
  void setListeners(ActionListener clicks);

  /**
   * Get the string from the text field and return it.
   */
  void printEventContents();

  /**
   * Clear the text field. Note that a more general "setInputString" would work for this purpose but
   * would be incorrect. This is because the text field is not set programmatically in general but
   * input by the user.
   */

  void clearInputString();

  /**
   * Reset the focus on the appropriate part of the view that has the keyboard listener attached to
   * it, so that keyboard events will still flow through.
   */
  void resetFocus();

  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();


}
