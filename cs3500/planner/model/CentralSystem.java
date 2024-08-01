package cs3500.planner.model;

import java.io.File;
import java.util.ArrayList;

import cs3500.planner.xmlbehavior.XMLHelper;

/**
 * Class representing a central system that keeps track of all users and their schedules.
 * Ensures all events between schedules are consistent.
 */
public class CentralSystem implements NUPlannerModel {
  private ArrayList<User> users;
  //INVARIANT: users != null.
  private ArrayList<File> files;
  private XMLHelper xmlHelper;

  /**
   * Creates a CentralSystem with a list of XML files.
   * The program will read the given files and perform operations on its contents.
   *
   * @param files the list of XML files to be uploaded
   */
  public CentralSystem(ArrayList<File> files) {
    this.files = files;
    this.users = new ArrayList<User>();
    this.xmlHelper = new XMLHelper();
  }

  /**
   * Empty constructor so CentralSystem can be created with no schedules or users.
   */
  public CentralSystem() {
    this.files = new ArrayList<File>();
    this.users = new ArrayList<User>();
    this.xmlHelper = new XMLHelper();
  }


  @Override
  public void createEvent(String name, ArrayList<User> invitees, Location location, Time time,
                          User host) {
    ArrayList<User> newInvitees = new ArrayList<>();
    newInvitees.add(host);
    newInvitees.addAll(invitees);
    for (int idx = 0; idx < this.users.size(); idx++) {
      if (this.users.get(idx).equals(host)) {
        users.get(idx).createEvent(name, newInvitees, location, time, host);
      }
    }
    for (User user : this.users) {
      for (User invitee : invitees) {
        if (user.equals(invitee)) {
          user.createEvent(name, newInvitees, location, time, host);
        }
      }
    }
  }

  @Override
  public void modifyEvent(String name, String editedName, ArrayList<User> invitees,
                          Location location, Time time) {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Event name cannot be empty");
    }
    for (User user : this.users) {
      user.modifyEvent(name, editedName, invitees, location, time);
    }
  }

  @Override
  public void removeEvent(NUEvent e, User u) {
    for (User user : this.users) {
      if (user.equals(u)) {
        if (e.removeFromHost(user)) {
          user.removeEvent(e, user);
        }
        for (User invitee : users) {
          invitee.removeEvent(e, invitee);
        }
      }
      else {
        user.removeEvent(e, user);
      }
    }
  }

  @Override
  public boolean checkForTimeConflict(NUEvent event, User user) {
    if (users.contains(user)) {
      return user.checkForTimeConflict(event, user);
    }
    else {
      throw new IllegalArgumentException("User is not in the system");
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (User u : users) {
      sb.append(u.toString());
    }
    return sb.toString();
  }

  @Override
  public void uploadUser() {
    for (File f : this.files) {
      this.users.add(this.xmlHelper.readXML(f));
    }
  }

  @Override
  public void addFile(File selectedFile) {
    this.files.add(selectedFile);
  }

  @Override
  public ArrayList<User> usersInSystem() {
    return this.users;
  }

  @Override
  public ArrayList<NUEvent> usersEvents(User user) {
    return user.usersEvents(user);
  }

  /**
   * Resets the user list in this model so that they can be re-uploaded based on the files.
   */
  public void resetUsers() {
    this.users = new ArrayList<>();
  }
}