public class TimeInfo {


  private static String[] months = { "January",   "February", "March",    "April",
                                     "May",       "June",     "July",     "August",
                                     "September", "October",  "November", "December" };

  /**
   * Checks the date format and provide the appropriate preposition.
   *
   * @param time - the named entity time information
   * @return the preposition to be appended to the sentence
   */
  protected static String getDateAndPreposition(String time) {
    String[] timeTokens = time.trim().split("\\s+");
    final int numberOfElements = timeTokens.length;
    final int FIRST = 0;
    final int SECOND = 1;
    final int THIRD = 2;

    switch (numberOfElements) {

      // One word
      case (1):

        // Only a simple year like "1990"
        if (timeTokens[FIRST].trim().matches("\\d{4}")) {
          return " in" + time;
        }

        // Only a year, but has an "s" like "1990's"
        if (timeTokens[FIRST].trim().matches("\\d{4}s")) {
          return " in the" + time;
        }

        break;

      // Two words
      case (2):
        break;

      case (3):
        for (String month : months) {
          if (timeTokens[FIRST].trim().compareTo(month) == 0) {
            return " on" + time;
          }
        }

    }

    return "";
  }
}
