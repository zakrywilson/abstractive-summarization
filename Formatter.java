/**
 * Handles all the sentence string formatting
 */
public class Formatter {


  /**
   * Formats the entire sentence
   * @param actor - actor of the triple
   * @param action - action of the triple
   * @param receiver - receiver of the triple
   * @return formatted sentence
   */
  protected static String formatSentence(String actor, String action, String receiver) {
    String sentence = formatActor(actor) + " " + formatAction(action) + " " + formatReceiver(receiver) + ". ";
    return removeExtraSpaces(sentence);
  }


  /**
   * Removes weird spaces created by Stanford's Open Information Extractor
   *
   * @param sentence - the sentence to be formatted
   * @return the formatted sentence
   */
  private static String removeExtraSpaces(String sentence) {
    sentence = sentence.replaceAll("\\s's\\s", "'s ");
    sentence = sentence.replaceAll("\\s'd\\s", "'d ");
    sentence = sentence.replaceAll("\\s'ed\\s", "'ed ");
    sentence = sentence.replaceAll("\\s're\\s", "'re ");
    sentence = sentence.replaceAll("\\s,\\s", ", ");
    sentence = sentence.replaceAll("\\s;\\s", "; ");
    sentence = sentence.replaceAll("\\ws\\s'\\s", "s' ");
    sentence = sentence.replaceAll("\\Q$\\E\\s", "\\$");
    sentence = sentence.replaceAll("\\s%\\s", "% ");
    return sentence;
  }


  /**
   * Formats the actor of the triple
   * @param string - string to be capitalized
   * @return string capitalized
   */
  private static String formatActor (String string) {
    if (!Character.isUpperCase(string.charAt(0))) {
      return capitalize(string);
    }
    return string;
  }


  /**
   * Formats the action of the triple
   * @param string - text to be converted to lowercase
   * @return string - string converted to lowercase
   */
  private static String formatAction (String string) {
    return string.toLowerCase();
  }


  /**
   * Formats the receiver of the triple
   * @param string - text to be formatted
   * @return formatted string
   */
  private static String formatReceiver (String string) {
    return string;
  }


  /**
   * Capitalizing the first letter of a String
   *
   * @param string - String that will have its first character capitalized
   * @return capitalized string
   */
  private static String capitalize(String string) {
    return Character.toUpperCase(string.charAt(0)) + string.substring(1);
  }
}