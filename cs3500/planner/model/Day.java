package cs3500.planner.model;

/**
 * Enum representing the days of the week.
 */
public enum Day {
  SUNDAY("Sunday"),
  MONDAY("Monday"),
  TUESDAY("Tuesday"),
  WEDNESDAY("Wednesday"),
  THURSDAY("Thursday"),
  FRIDAY("Friday"),
  SATURDAY("Saturday");

  private final String day;

  /**
   * Creates a day.
   *
   * @param day the given day of the week
   */
  Day(String day) {
    this.day = day;
  }

  /**
   * Returns the Day as a Sring.
   *
   * @return a String of the day
   */
  public String toString() {
    return this.day;
  }

  /**
   * Converts the day into an integer to compare to other days.
   *
   * @return number that corresponds with this day in the Day enumeration
   */
  public int dayIndex() {
    Day[] list = Day.values();
    int i = 0;
    for (int index = 0; index < 7; index++) {
      if (this == list[index]) {
        i = index;
      }
    }
    return i;
  }
}