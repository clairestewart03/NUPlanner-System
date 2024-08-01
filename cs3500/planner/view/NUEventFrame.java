package cs3500.planner.view;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;

import cs3500.planner.model.NUEvent;
import cs3500.planner.model.ReadOnlyPlannerModel;
import cs3500.planner.model.Schedule;
import cs3500.planner.model.User;

/**
 * This frame is how the client interacts with events, exposing all the relevant
 * information so that they can create/modify/or remove events in the System.
 */
public class NUEventFrame extends JFrame implements NUEventFrameView {
  private JLabel eventName;
  private JLabel location;
  private JLabel startDay;
  private JLabel startTime;
  private JLabel endDay;
  private JLabel endTime;
  private JButton modify;
  private JButton remove;
  private JButton create;
  private JComboBox<String> online;
  private JComboBox<String> startDayDropDown;
  private JComboBox<String> endDayDropDown;
  private JList users;
  private JTextField eventText;
  private JTextField locationText;
  private JTextField startTimeText;
  private JTextField endTimeText;
  private JPanel eventPanel;
  private JPanel locationPanel;
  private JPanel startDayPanel;
  private JPanel startTimePanel;
  private JPanel endDayPanel;
  private JPanel endTimePanel;
  private JPanel usersPanel;
  private JPanel buttonPanel;
  private final ReadOnlyPlannerModel model;
  private final User user;

  /**
   * Creates an NUEventFrame initializing the frame with all the relevant labels, buttons,
   * lists, texts in the panels.
   */
  public NUEventFrame(ReadOnlyPlannerModel model) {
    super();
    this.model = model;
    this.user = new User("", new Schedule("", new ArrayList<>()));
    setSize(400, 500);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);
    createEventPanel();
    createLocationPanel();
    createTimePanels();
    createUsersPanel();
    createButtonPanel();

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(eventPanel);
    mainPanel.add(locationPanel);
    mainPanel.add(startDayPanel);
    mainPanel.add(endDayPanel);
    mainPanel.add(startTimePanel);
    mainPanel.add(endTimePanel);
    mainPanel.add(usersPanel);
    mainPanel.add(buttonPanel);

    this.add(mainPanel);
    this.setListeners(this);
    this.makeVisible();
  }

  /**
   * Constructor to create an event frame with a model and a user.
   * Helpful for public methods in this class to have access to the currently selected user
   * in the planner.
   * @param model the model to base the planner off of.
   * @param user the currently selected user.
   */
  public NUEventFrame(ReadOnlyPlannerModel model, User user) {
    super();
    this.model = model;
    this.user = user;
    setSize(400, 500);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);
    createEventPanel();
    createLocationPanel();
    createTimePanels();
    createUsersPanel();
    createButtonPanel();

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(eventPanel);
    mainPanel.add(locationPanel);
    mainPanel.add(startDayPanel);
    mainPanel.add(endDayPanel);
    mainPanel.add(startTimePanel);
    mainPanel.add(endTimePanel);
    mainPanel.add(usersPanel);
    mainPanel.add(buttonPanel);

    this.add(mainPanel);
    this.setListeners(this);
    this.makeVisible();
  }

  /**
   * Creates the panel for the 3 buttons at the bottom of the NUEventFrame.
   */
  private void createButtonPanel() {
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

    modify = new JButton("Modify Event");
    modify.setPreferredSize(new Dimension(100, 40));
    modify.setActionCommand("Modify Event");
    buttonPanel.add(modify);

    remove = new JButton("Remove Event");
    remove.setPreferredSize(new Dimension(100, 40));
    remove.setActionCommand("Remove Event");
    buttonPanel.add(remove);

    create = new JButton("Create Event");
    create.setPreferredSize(new Dimension(100, 40));
    create.setActionCommand("Create Event");
    buttonPanel.add(create);
  }

  /**
   * Creates the panel that contains the available users JLabel and JList to add to this
   * NUEventFrame.
   */
  private void createUsersPanel() {
    usersPanel = new JPanel();
    usersPanel.setPreferredSize(new Dimension(400, 100));
    usersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
    JLabel availableUsers = new JLabel("Available users");
    availableUsers.setAlignmentX(Component.LEFT_ALIGNMENT);
    usersPanel.add(availableUsers);
    usersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    this.initializeUsers();
    users.setAlignmentX(Component.LEFT_ALIGNMENT);
    usersPanel.add(users);
  }

  /**
   * Initializes the list of JList users to include only users uploaded to the model.
   */
  private void initializeUsers() {
    String[] usersList = new String[model.usersInSystem().size()];
    for (int index = 0; index < usersList.length; index++) {
      usersList[index] = model.usersInSystem().get(index).printName();
    }
    this.users = new JList<>(usersList);
  }

  /**
   * Creates the panel that allows the client to input the duration of the event to be added
   * to this NUEventFrame.
   */
  private void createTimePanels() {
    startDayPanel = new JPanel();
    startDayPanel.setLayout(new FlowLayout());
    startDay = new JLabel("Starting Day:");
    startDayPanel.add(startDay);

    String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
        "Saturday" };
    startDayDropDown = new JComboBox<>(days);
    startDayDropDown.setPreferredSize(new Dimension(300, 50));
    startDayPanel.add(startDayDropDown);

    startTimePanel = new JPanel();
    startTimePanel.setLayout(new FlowLayout());
    startTime = new JLabel("Starting time:");
    startTimePanel.add(startTime);
    startTimeText = new JTextField();
    startTimeText.setPreferredSize(new Dimension(300, 30));
    startTimePanel.add(startTimeText);

    endDayPanel = new JPanel();
    endDayPanel.setLayout(new FlowLayout());
    endDay = new JLabel("Ending Day:");
    endDayPanel.add(endDay);
    endDayDropDown = new JComboBox<>(days);
    endDayDropDown.setPreferredSize(new Dimension(300, 50));
    endDayPanel.add(endDayDropDown);

    endTimePanel = new JPanel();
    endTimePanel.setLayout(new FlowLayout());
    endTime = new JLabel("Ending time:");
    endTimePanel.add(endTime);
    endTimeText = new JTextField();
    endTimeText.setPreferredSize(new Dimension(300, 30));
    endTimePanel.add(endTimeText);
  }

  /**
   * Creates the panel that allows the client to input the location information to add to this
   * NUEventFrame.
   */
  private void createLocationPanel() {
    locationPanel = new JPanel();
    locationPanel.setLayout(new FlowLayout());
    String[] onlineDropDown = { "Is online", "Is not online" };
    online = new JComboBox<>(onlineDropDown);
    locationPanel.add(online);

    locationText = new JTextField();
    locationText.setPreferredSize(new Dimension(250, 50));
    locationPanel.add(locationText);
  }

  /**
   * Creates the panel that allows the user to input the event name to add to this NUEventFrame.
   */
  private void createEventPanel() {
    eventPanel = new JPanel();
    eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));

    //Adds the event label to the panel.
    eventName = new JLabel("Event name:");
    eventPanel.add(eventName);
    eventPanel.add(Box.createRigidArea(new Dimension(0, 5)));

    //Adds the text box to the panel.
    eventText = new JTextField();
    eventText.setPreferredSize(new Dimension(300, 50));
    eventPanel.add(eventText);
    eventPanel.add(Box.createRigidArea(new Dimension(0, 5)));

    //Adds the location label to the panel.
    location = new JLabel("Location:");
    eventPanel.add(location);
  }

  @Override
  public void setListeners(ActionListener clicks) {
    modify.addActionListener(clicks);
    remove.addActionListener(clicks);
    create.addActionListener(clicks);
  }

  @Override
  public void printEventContents() {
    System.out.println(eventName.getText() + " " + eventText.getText());
    System.out.println(location.getText() + " " + online.getSelectedItem() + ", "
            + locationText.getText());
    System.out.println(startDay.getText() + " " + startDayDropDown.getSelectedItem());
    System.out.println(startTime.getText() + " " + startTimeText.getText());
    System.out.println(endDay.getText() + " " + endDayDropDown.getSelectedItem());
    System.out.println(endTime.getText() + " " + endTimeText.getText());
  }

  @Override
  public void clearInputString() {
    //empty for now.
  }

  @Override
  public void resetFocus() {
    //empty for now.
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Remove Event")) {
      if (this.checkEmptyInputs()) {
        throw new IllegalArgumentException("Event does not have all information.");
      }
      System.out.println("Remove event:");
      this.printEventContents();
      System.out.println("Remove from: " + this.user.printName());
    } else if (e.getActionCommand().equals("Create Event")) {
      if (this.checkEmptyInputs()) {
        throw new IllegalArgumentException("Event does not have all information.");
      }
      System.out.println("Create event:");
      this.printEventContents();
    }
  }

  /**
   * Checks to see if any of the Event information is left blank.
   * @return true if a text box is blank, false if not.
   */
  private boolean checkEmptyInputs() {
    return eventText.getText().isBlank() || locationText.getText().isBlank()
      || startTimeText.getText().isBlank() || endTimeText.getText().isBlank();
  }

  /**
   * Fills the event frame of the clicked event with the event's information.
   * @param e the event clicked by the client.
   */
  public void fillEvent(NUEvent e) {
    this.eventText.setText(e.eventName());
    this.locationText.setText(e.locationName());
    if (e.isOnline()) {
      this.online.setSelectedIndex(0);
    } else {
      this.online.setSelectedIndex(1);
    }
    this.startDayDropDown.setSelectedIndex(e.startDayIndex());
    this.endDayDropDown.setSelectedIndex(e.endDayIndex());
    this.startTimeText.setText(e.startTimeText());
    this.endTimeText.setText(e.endTimeText());
    this.initializeUsers();
  }
}