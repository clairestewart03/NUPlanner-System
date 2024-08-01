package cs3500.planner.xmlbehavior;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cs3500.planner.model.Day;
import cs3500.planner.model.Location;
import cs3500.planner.model.NUEvent;
import cs3500.planner.model.Schedule;
import cs3500.planner.model.Time;
import cs3500.planner.model.User;

/**
 * Class to deal with reading and writing XML files to create and save schedules.
 */
public class XMLHelper {
  Schedule schedule;

  /**
   * Creates an XMLHelper based on the given schedule.
   * Purpose: the given schedule will be converted to an XML in saveSchedule().
   *
   * @param schedule the schedule the client wants to save to an XML file.
   */
  public XMLHelper(Schedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Creates an XMLHelper with no parameters.
   * Purpose: used to create XMLHelpers to read files.
   */
  public XMLHelper() {
    //empty constructor so that the xmlHelper can be initialized in the Central System
  }

  /**
   * Creates schedule based on the given XML file.
   *
   * @return a new Schedule for the file's user and content
   */
  //TODO: CHANGE NAME TO UPLOADSCHEDULE
  public User readXML(File file) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(file);
      doc.getDocumentElement().normalize();

      ArrayList<NUEvent> events = new ArrayList<>();
      String uId = "";
      Schedule s = null;
      Node scheduleNode = doc.getDocumentElement();
      if (scheduleNode.getNodeType() == Node.ELEMENT_NODE) {
        Element scheduleElem = (Element) scheduleNode;
        uId = scheduleElem.getAttribute("id");
        s = new Schedule(uId, events);
      }
      NodeList nodeList = doc.getElementsByTagName("event");
      for (int item = 0; item < nodeList.getLength(); item++) {
        Node event = nodeList.item(item);
        if (event.getNodeType() == Node.ELEMENT_NODE) {
          Element eventElem = (Element) event;
          events.add(this.createEventFromXML(eventElem));
        }
      }
      return new User(uId, s);
    } catch (ParserConfigurationException ex) {
      throw new IllegalStateException("Error in creating the builder");
    } catch (IOException e) {
      throw new IllegalStateException("Error in opening the file");
    } catch (SAXException e) {
      throw new IllegalStateException("Error in parsing the file");
    }
  }

  /**
   * Creates NUEvent based on the given XML Element.
   * Purpose: The NUEvent created will be added to the user's schedule in readXML
   * @param event the Element to convert to an NUEvent.
   * @return the NUEvent to be added to the User's schedule.
   */
  private NUEvent createEventFromXML(Element event) {
    String name = "";
    Time time = null;
    Location location = null;
    ArrayList<User> invitees = new ArrayList<>();
    User host = null;

    name = event.getElementsByTagName("name").item(0).getTextContent();
    time = this.createTime(event.getElementsByTagName("time").item(0));
    location = this.createLocation(event.getElementsByTagName("location").item(0));
    invitees = this.createUsers(event.getElementsByTagName("users").item(0));
    host = this.getHost(event.getElementsByTagName("users").item(0));
    if (event.getTagName().equals("name")) {
      name = event.getAttribute("name");
    }
    if (event.getTagName().equals("time")) {
      time = this.createTime(event);
    }
    if (event.getTagName().equals("location")) {
      location = this.createLocation(event);
    }
    if (event.getTagName().equals("users")) {
      invitees = this.createUsers(event);
      host = this.getHost(event);
    }
    return new NUEvent(name, invitees, location, time, host);
  }

  /**
   * Retrieves the host from the given Node and converts it from an XML Element to a User.
   * Purpose: The created User will be added to the event in createEventFromXML.
   *
   * @param users the Node that contains the host.
   * @return a User representing the host of the event.
   */
  private User getHost(Node users) {
    String id = "";
    if (users.getNodeType() == Node.ELEMENT_NODE) {
      Element userElem = (Element) users;
      id = userElem.getElementsByTagName("uid").item(0).getTextContent();
    }
    return new User(id, new Schedule(id, new ArrayList<>()));
  }

  /**
   * Creates a list of users from the given XML Node representing the invitees of the event.
   * Purpose: the created list of users will be added to the event in createEventFromXML.
   *
   * @param users the Node containing the list of invitees.
   * @return a list of users representing the invitees to the event.
   */
  private ArrayList<User> createUsers(Node users) {
    ArrayList<User> invitees = new ArrayList<>();
    if (users.getNodeType() == Node.ELEMENT_NODE) {
      Element userElem = (Element) users;
      NodeList userList = userElem.getElementsByTagName("uid");
      for (int idx = 0; idx < userList.getLength(); idx++) {
        Node user = userList.item(idx);
        if (user.getNodeType() == Node.ELEMENT_NODE) {
          Element idElem = (Element) user;
          String id = idElem.getTextContent();
          User u = new User(id, new Schedule(id, new ArrayList<>()));
          invitees.add(u);
        }
      }
    }
    return invitees;
  }

  /**
   * Creates a Location based on the given XML Node.
   * Purpose: the created Location will be added to the event in createEventFromXML.
   *
   * @param loc the Node to read in order to create the Location.
   * @return the Location of the event based on the XML Node.
   */
  private Location createLocation(Node loc) {
    NodeList locChildren = loc.getChildNodes();
    boolean online = false;
    String place = "";

    if (loc.getNodeType() == Node.ELEMENT_NODE) {
      Element locElem = (Element) loc;
      online = Boolean.parseBoolean(locElem.getElementsByTagName("on"
              + "line").item(0).getTextContent());
      place = locElem.getElementsByTagName("place").item(0).getTextContent();
    }
    return new Location(online, place);
  }

  /**
   * Creates a Time based on the given XML Node.
   * Purpose: the created Time will be added to the event in createEventFromXML.
   *
   * @param time the Node to convert to a Time type.
   * @return a Time representing the given XML Node.
   */
  private Time createTime(Node time) {
    Day startDay = Day.MONDAY;
    Day endDay = Day.MONDAY;
    String startTime = "";
    String endTime = "";
    if (time.getNodeType() == Node.ELEMENT_NODE) {
      Element timeElem = (Element) time;
      startDay = this.stringToDay(timeElem.getElementsByTagName("start-"
              + "day").item(0).getTextContent());
      endDay = this.stringToDay(timeElem.getElementsByTagName("end-"
              + "day").item(0).getTextContent());
      startTime = timeElem.getElementsByTagName("start").item(0).getTextContent();
      endTime = timeElem.getElementsByTagName("end").item(0).getTextContent();
    }
    return new Time(startDay, endDay, startTime, endTime);
  }

  /**
   * Converts the given String to a value of the Day enum.
   *
   * @param day the String to convert to a day.
   * @return the Day represented by the given String.
   */
  private Day stringToDay(String day) {
    if (day.equals("Sunday")) {
      return Day.SUNDAY;
    }
    if (day.equals("Monday")) {
      return Day.MONDAY;
    }
    if (day.equals("Tuesday")) {
      return Day.TUESDAY;
    }
    if (day.equals("Wednesday")) {
      return Day.WEDNESDAY;
    }
    if (day.equals("Thursday")) {
      return Day.THURSDAY;
    }
    if (day.equals("Friday")) {
      return Day.FRIDAY;
    } else {
      return Day.SATURDAY;
    }
  }

  /**
   * Converts the given Schedule into an XML File.
   */
  public void saveSchedule() {
    try {
      Writer file = new FileWriter(schedule.fileName() + "-schedule.xml");
      file.write("<?xml version=\"1.0\"?>\n");
      file.write(schedule.printXML());
      file.close();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}