package cs3500.planner.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;

import cs3500.planner.xmlbehavior.XMLHelper;
import cs3500.planner.model.CentralSystem;
import cs3500.planner.model.NUPlannerModel;
import cs3500.planner.model.User;

/**
 * Class to represent what the NUPlanner's frame is capable of,
 * visualizes a planner system.
 */
public class NUPlannerFrame extends JFrame implements NUPlannerFrameView {
  private JButton addCalendar;
  private JButton saveCalendar;
  private JButton createEvent;
  private JButton scheduleEvent;
  private JPanel calendarPanel;
  private JPanel eventPanel;
  private JComboBox<String> users;
  private final NUPlannerPanel plannerPanel;
  private final NUPlannerModel model;
  private final XMLHelper xmlHelper;

  /**
   * Creates an NUPlannerFrame and initializes the buttons and grid.
   */
  public NUPlannerFrame(NUPlannerModel model) {
    super();
    this.model = model;
    this.xmlHelper = new XMLHelper();
    setSize(800, 800);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    createCalendarPanel();
    //createPlannerPanel();
    createEventPanel();
    plannerPanel = new NUPlannerPanel(this.model);
    plannerPanel.setPreferredSize(new Dimension(700, 700));
    this.setResizable(false);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(calendarPanel);
    mainPanel.add(plannerPanel);
    mainPanel.add(eventPanel);

    this.add(mainPanel);
    this.setListeners(this);
    makeVisible();
  }

  /**
   * Creates the panel that allows the user to add and save calandars as XMLs to add to
   * this NUPlannerFrame.
   */
  private void createEventPanel() {
    eventPanel = new JPanel();
    eventPanel.setPreferredSize(new Dimension(1000, 50));
    eventPanel.setLayout(new FlowLayout());

    addCalendar = new JButton("Add Calendar");
    addCalendar.setPreferredSize(new Dimension(200, 30));
    addCalendar.setActionCommand("Add Calendar");
    eventPanel.add(addCalendar);

    saveCalendar = new JButton("Save Calendar");
    saveCalendar.setPreferredSize(new Dimension(200, 30));
    saveCalendar.setActionCommand("Save Calendar");
    eventPanel.add(saveCalendar);
  }

  /**
   * Creates the panel at the top of this frame that allows the client to pick the user,
   * create a new event, and schedule an event.
   * This panel is then added to this NUPlannerFrame.
   */
  private void createCalendarPanel() {
    calendarPanel = new JPanel();
    calendarPanel.setPreferredSize(new Dimension(1000, 50));
    calendarPanel.setLayout(new FlowLayout());

    String[] u = { };
    users = new JComboBox<>(u);
    users.setPreferredSize(new Dimension(200, 30));
    users.setActionCommand("Switch User");
    calendarPanel.add(users);

    createEvent = new JButton("Create Event");
    createEvent.setPreferredSize(new Dimension(200, 30));
    createEvent.setActionCommand("Create Event");
    calendarPanel.add(createEvent);

    scheduleEvent = new JButton("Schedule Event");
    scheduleEvent.setPreferredSize(new Dimension(200, 30));
    scheduleEvent.setActionCommand("Schedule Event");
    calendarPanel.add(scheduleEvent);
  }

  @Override
  public void setListeners(ActionListener clicks) {
    this.createEvent.addActionListener(clicks);
    this.scheduleEvent.addActionListener(clicks);
    this.addCalendar.addActionListener(clicks);
    this.saveCalendar.addActionListener(clicks);
    this.users.addActionListener(clicks);
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Create Event":
        //Opens the Event Frame.
        NUEventFrame eventFrame = new NUEventFrame(model, this.plannerPanel.scheduleOwner());
        eventFrame.setVisible(true);
        eventFrame.setLocationRelativeTo(this);
        break;

      case "Add Calendar":
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(this);
        fileChooser.setVisible(true);
        System.out.println("Path: " + fileChooser.getSelectedFile().getAbsoluteFile());

        User u = this.xmlHelper.readXML(fileChooser.getSelectedFile());
        this.model.addFile(fileChooser.getSelectedFile());
        this.model.resetUsers();
        this.model.uploadUser();
        this.users.addItem(u.printName());
        this.plannerPanel.changeUser(this.convertToUser(users.getSelectedItem().toString()));
        break;

      case "Switch User":
        String userName = users.getSelectedItem().toString();
        this.plannerPanel.changeUser(this.convertToUser(userName));
        this.displayEvents(this.convertToUser(userName));
        break;

      case "Schedule Event":
        //Opens the Event Frame.
        NUEventFrame eventFrame2 = new NUEventFrame(new CentralSystem());
        eventFrame2.setVisible(true);
        eventFrame2.setLocationRelativeTo(this);
        break;

      case "Save Calendar":
        JFileChooser fileChooser2 = new JFileChooser();
        fileChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser2.showOpenDialog(this);
        fileChooser2.setVisible(true);
        System.out.println(fileChooser2.getSelectedFile().toString());
        break;

      default:
        //do nothing.
    }
  }

  /**
   * Associates the given string with a user in the system.
   * @param userName the name of the user
   * @return the User selected
   */
  private User convertToUser(String userName) {
    User user = null;
    for (User u : model.usersInSystem()) {
      if (u.printName().equals(userName)) {
        user = u;
      }
    }
    return user;
  }

  /**
   * Displays the events for the user in the GUI.
   * @param user the user to display events.
   */
  private void displayEvents(User user) {
    this.plannerPanel.repaint();
  }
}