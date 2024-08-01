package cs3500.planner.view;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import cs3500.planner.model.Day;
import cs3500.planner.model.Location;
import cs3500.planner.model.NUEvent;
import cs3500.planner.model.NUPlannerModel;
import cs3500.planner.model.Schedule;
import cs3500.planner.model.Time;
import cs3500.planner.model.User;

import static java.lang.Math.floor;

/**
 * Class representing a panel in the NUPlanner.
 */
public class NUPlannerPanel extends JPanel {
  private User currentUser;
  private NUPlannerModel model;

  /**
   * Constructor.
   *
   * @param model the model to read from.
   */
  public NUPlannerPanel(NUPlannerModel model) {
    this.setSize(800, 800);
    this.currentUser = new User("", new Schedule("", new ArrayList<>()));
    this.model = model;
    this.addClickListener();
  }

  @Override
  protected void paintComponent(Graphics g) {
    int panelHeight = this.getHeight();
    int panelWidth = this.getWidth();
    int dayWidth = this.getWidth() / 7;
    int fourHourHeight = this.getHeight() / 6;
    int hourHeight = fourHourHeight / 4;

    Graphics2D g2d = (Graphics2D) g;

    //Draws the events as colored rectangles on the planner.
    if (!currentUser.printName().equals("")) {
      if (model.usersInSystem().contains(currentUser)) {
        for (NUEvent e : this.model.usersEvents(currentUser)) {
          Integer[] time = e.eventDuration();
          Integer[] startDayRow = this.dayRow(time[0]);
          Integer[] endDayRow = this.dayRow(time[2]);
          int startTimeCol = (int) this.timeCol(time[1]);
          int endTimeCol = (int) this.timeCol(time[3]);

          int rectWidth = Math.abs(endDayRow[1] - startDayRow[0]);
          g2d.setColor(Color.BLUE);
          if (e.startDayIndex() != e.endDayIndex()) {
            g2d.fillRect(startDayRow[0], startTimeCol, dayWidth, panelHeight);
            int daysInBetween = Math.abs(e.endDayIndex() - e.startDayIndex()) - 1;
            g2d.fillRect(startDayRow[1], 0, dayWidth * daysInBetween, panelHeight);
            if (e.endDayIndex() > e.startDayIndex()) {
              g2d.fillRect(endDayRow[0], 0, dayWidth, endTimeCol);
            }
          } else {
            g2d.fillRect(startDayRow[0], startTimeCol, rectWidth,
                    Math.abs(endTimeCol - startTimeCol));
          }
        }
      }
    }

    //These methods draw the lines onto the planner representing the hours and days.
    drawVerticalLines(g2d, dayWidth, panelHeight);
    drawHorizontalLines(g2d, hourHeight, panelWidth, fourHourHeight, panelHeight);
  }

  /**
   * Draws the horizontal lines representing the hours on the planner.
   *
   * @param g2d the Graphics2D to draw on.
   * @param hourHeight the increment separating each horizontal line, representing an hour on the
   *                   planner.
   * @param panelWidth the width of this planner panel.
   * @param fourHourHeight the increment separating each bold horizontal line, representing 4 hours
   *                  on the planner.
   * @param panelHeight the height of this planner panel.
   */
  private static void drawHorizontalLines(Graphics2D g2d, int hourHeight, int panelWidth,
                                          int fourHourHeight, int panelHeight) {
    for (int index = 1; index < 24; index++) {
      g2d.drawLine(0, index * hourHeight, panelWidth, index * hourHeight);
    }

    g2d.setStroke(new BasicStroke(4));
    for (int index = 0; index < 7; index++) {
      g2d.drawLine(0, index * fourHourHeight, panelWidth, index * fourHourHeight);
    }
  }

  /**
   * Draws the vertical lines separating the days on the planner.
   *
   * @param g2d the Graphics2D to draw on.
   * @param dayWidth the increment to separate each vertical lines, representing each day.
   * @param panelHeight the height of this planner panel.
   */
  private static void drawVerticalLines(Graphics2D g2d, int dayWidth, int panelHeight) {
    g2d.setColor(Color.BLACK);
    for (int index = 1; index < 7; index++) {
      g2d.drawLine(index * dayWidth, 0, index * dayWidth, panelHeight);
    }
  }

  /**
   * Used when drawing the events onto the planner.
   * Determines the height of the rectangle representing an event on the planner.
   *
   * @param time The integer representing the start or end time of an event.
   * @return the coordinate at which to base the height of the event on.
   */
  private double timeCol(int time) {
    double minuteHeight = (double) this.getHeight() / 1440;
    double hours = floor((double) time / 100);
    double minutes = time % 100;

    return hours * 60 * minuteHeight + minutes * minuteHeight;
  }

  /**
   * Used when drawing the events onto the planner.
   * Determines the width of the rectangle representing an event on the planner.
   *
   * @param day The integer representing the start or end day of an event.
   * @return the coordinate at which to base the width of the event on.
   */
  private Integer[] dayRow(int day) {
    int dayWidth = this.getWidth() / 7;
    Integer[] dayCoord = new Integer[2];

    for (int index = 0; index < 7; index++) {
      if (day == index) {
        dayCoord[0] = index * dayWidth;
        dayCoord[1] = (index + 1) * dayWidth;
      }
    }
    return dayCoord;
  }

  /**
   * Changes this current user to the given user.
   * Helps to know which schedule to paint, based on who is selected in the JComboBox.
   *
   * @param u the user to switch the current user to.
   */
  public void changeUser(User u) {
    currentUser = u;
  }

  /**
   * Adds a click listener to this entire planner panel.
   * Purpose: to respond to the client's click when if they select an area on the planner
   * containing an event.
   */
  public void addClickListener() {
    this.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        NUPlannerPanel panel = NUPlannerPanel.this;
        panel.handleCellClick(e.getX(), e.getY());
      }

      @Override
      public void mousePressed(MouseEvent e) {
        //empty for now
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        //empty for now
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        //empty for now
      }

      @Override
      public void mouseExited(MouseEvent e) {
        //empty for now
      }
    });
  }

  /**
   * Handle an action in a based on where the user clicks on the panel.
   * Purpose: display the contents of the selected event on the planner.
   *
   * @param x the x coordinate of the click
   * @param y the y coordinate of the click
   */
  public void handleCellClick(int x, int y) {
    System.out.println(y);

    int totalMinutes = (int) Math.floor((double) (y * 24 * 60) / this.getHeight());
    int hours = totalMinutes / 60;
    System.out.println(hours);
    int minutes = totalMinutes % 60;
    System.out.println(minutes);

    String stringTime = this.addLeadingZero(Integer.toString(hours)
            + this.addLeadingZero(Integer.toString(minutes)));
    System.out.println(stringTime);

    //Draws the events as colored rectangles on the planner.
    if (!currentUser.printName().equals("")) {
      if (model.usersInSystem().contains(currentUser)) {
        for (NUEvent e : this.model.usersEvents(currentUser)) {
          if (e.checkForConflict(new NUEvent("Test", new ArrayList<>(),
                  new Location(true, "Test"),
                  new Time(this.indexToDay(e.startDayIndex()), this.indexToDay(e.endDayIndex()),
                          stringTime, stringTime),
                  new User("Test", new Schedule("Test", new ArrayList<>()))))) {
            NUEventFrame eventFrame = new NUEventFrame(model, this.currentUser);
            eventFrame.fillEvent(e);
            eventFrame.setVisible(true);
            eventFrame.setLocationRelativeTo(this);
          }
        }
      }
    }
    this.repaint();
  }

  private String addLeadingZero(String time) {
    if (time.length() == 1) {
      return ("0" + time);
    } else {
      return time;
    }
  }

  /**
   * Returns the day represented by the given integer.
   *
   * @param index the integer representing a day.
   * @return a day that corresponds to the given integer.
   */
  private Day indexToDay(int index) {
    Day[] dayList = Day.values();
    Day day = Day.MONDAY;
    for (int dayIdx = 0; dayIdx < 7; dayIdx++) {
      if (index == dayIdx) {
        day = dayList[index];
      }
    }
    return day;
  }

  /**
   * Returns this current user for the purpose of printing out the user of the currently
   * selected schedule.
   *
   * @return the user of the currently selected schedule.
   */
  public User scheduleOwner() {
    return this.currentUser;
  }
}