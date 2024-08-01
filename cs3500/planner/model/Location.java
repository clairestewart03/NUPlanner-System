package cs3500.planner.model;

import java.util.Objects;

/**
 * Class representing the Location of an Event.
 */
public class Location {
  private boolean isOnline;
  private String place;

  /**
   * Creates a Location for an Event.
   *
   * @param isOnline determines if the Event is online or not
   * @param place    the place where the Event is occurring
   */
  public Location(boolean isOnline, String place) {
    if (place == null) {
      throw new IllegalArgumentException("place cannot be null");
    } else {
      this.isOnline = isOnline;
      this.place = place;
    }
  }

  /**
   * Chose to override equals because a Location has multiple fields, all of which must be equal.
   * Therefore, the regular equals method would not ensure that two Locations are the exact same
   * object.
   *
   * @param other the Location to compare to this Location.
   * @return true if this and other are the same exact Location, false if not.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof Location) {
      Location loc = (Location) other;
      return this.isOnline == loc.isOnline
              && this.place.equals(loc.place);
    } else {
      return false;
    }
  }

  /**
   * We chose to use the place to override hashcode because this should ensure a unique
   * hash code for each Location.
   *
   * @return a hashcode based on this place.
   */
  @Override
  public int hashCode() {
    return Objects.hash(place.length());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.place).append("\n").append("\tonline: ").append(this.isOnline);
    return sb.toString();
  }

  /**
   * Returns a string for the File writer to write into the XML file representing the location
   * of the event.
   *
   * @return String in XML format representing the location of an event.
   */
  public String printLocationXML() {
    StringBuilder sb = new StringBuilder();
    sb.append("<online>").append(this.isOnline).append("</online>\n");
    sb.append("<place>").append(this.place).append("</place>");
    return sb.toString();
  }

  public String locationName() {
    return this.place;
  }

  public boolean isOnlineHelp() {
    return this.isOnline;
  }
}