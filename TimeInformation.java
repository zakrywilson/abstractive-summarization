import java.util.Arrays;
import java.util.List;

public class TimeInformation {


  /** A list of all possible months */
  private static String[] months = { "January",   "February", "March",    "April",
                                     "May",       "June",     "July",     "August",
                                     "September", "October",  "November", "December" };


  private static String[] removeExtraneousDateInfo(String[] timeTokens) {

    List<String> tokens = Arrays.asList(timeTokens);

    int monthCounter = 0;
    int tokenCounter = 0;

    for (String token : tokens) {
      for (String month : months) {
        if (token.trim().equalsIgnoreCase(month)) {
          if (++monthCounter > 1) {
            return tokens.subList(0, tokenCounter).toArray(new String[tokens.size()]);
          }
        }
      }
      ++tokenCounter;
    }
    return timeTokens;
  }


  /**
   * Checks the date format and provide the appropriate preposition.
   *
   * @param timeInformation - the named entity time information
   * @return the preposition to be appended to the sentence
   */
  protected static String getDateAndPreposition(final String timeInformation) {
    String[] tokens = timeInformation.trim().split("\\s+");
    final int numberOfElements = tokens.length;

    // Delete unnecessary month information
    String[] timeTokens = removeExtraneousDateInfo(tokens);

    switch (numberOfElements) {

      // One word
      case (1):

        // Only a simple year like "1990"
        if (timeTokens[0].trim().matches("\\d{4}")) {
          return " in" + createString(timeTokens);
        }

        // Only a year, but has an "s" like "1990's"
        if (timeTokens[0].trim().matches("\\d{4}s")) {
          return " in the" + createString(timeTokens);
        }

        break;

      // Three words
      case (3):

        for (String month : months) {
          if (timeTokens[0].trim().compareTo(month) == 0) {
            return " on" + createString(timeTokens);
          }
        }

        break;

    } // end of switch

    return "";
  }


  private static String createString(String[] array) {
    String time = "";
    for (String s : array) {
      if (s != null && s.equals("")) {
        time += " ";
        time += s;
      }
    }
    return time;
  }
}
