package cs3500.planner;

import cs3500.planner.controller.NUPlannerController;
import cs3500.planner.model.CentralSystem;
import cs3500.planner.model.NUPlannerModel;
import cs3500.planner.view.NUPlannerFrame;
import cs3500.planner.view.NUPlannerFrameView;

/**
 * Contains the main method to run the NUPlanner system.
 */
public class NUPlannerMain {
  /**
   * Main method to run the program.
   * @param args command-line arguments.
   */
  public static void main(String[] args) {
    NUPlannerModel model = new CentralSystem();
    NUPlannerFrameView view = new NUPlannerFrame(new CentralSystem());
    NUPlannerController controller = new NUPlannerController(model, view);
  }
}