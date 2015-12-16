import java.util.Map;

public class Concatenator {

  /**
   * Fuses the sentences and the time information from NER data
   *
   * @param network - summarized sentences
   * @param ner - named entity recognition information
   * @return fused sentences with NER time data
   */
  protected static String fuse(Network network, NamedEntities ner) {

    // The String that will be fused
    StringBuilder summary = new StringBuilder();

    // For each sentence, find all NER data in that same original sentence
    // If NER time data is found, append that to the sentence
    for (Map.Entry<Integer, Sentence> pair : network.getCollection().entrySet()) {

      Sentence sent = pair.getValue();
      String sentence = sent.getCompressedSentence();
      String time = sent.getTimeEntities();

      summary.append(appendData(sentence, time));
    }

    return summary.toString();
  }

  /**
   * Appends the time NER information to a given sentence
   *
   * @param sentence - any String sentence
   * @param time - the NER time data
   * @return time and sentence together in one string
   */
  private static String appendData(final String sentence, String time) {
    String fused = sentence;
    String endingString = sentence.substring(sentence.length() - 2, sentence.length());
    if (needsToBeAppended(sentence, time)) {
      String preposition = getPreposition(time);
      String newEndingString = preposition + time + endingString;
      fused = sentence.replace(endingString, newEndingString);
    }
    return fused;
  }

  /**
   * Checks whether the time string exists inside of the sentence already
   *
   * @param sentence - sentence that may or may not contain the time data
   * @param time - time NER data
   * @return true if the time data needs to be appended
   */
  private static boolean needsToBeAppended(String sentence, String time) {
    String[] tokens = time.split("\\s+");
    for (String token : tokens) {
      token = token.trim();
      if (token.length() != 0 && sentence.contains(token)) {
        return false;
      }
    }
    return true;
  }

  private static String getPreposition(String time) {
    String[] tokens = time.trim().split("\\s+");
    final int FIRST = 0;
    final int numberOfElements = tokens.length;

    // DEBUG
    System.err.println(java.util.Arrays.toString(tokens));

    switch (numberOfElements) {

      // One word
      case (1):

        // Only a simple year like "1990"
        if (tokens[FIRST].trim().matches("\\d{4}")) {
          return " in";
        }

        // Only a year, but has an "s" like "1990's"
        if (tokens[FIRST].trim().matches("\\d{4}s")) {
          return " in the";
        }

      // Two words
      case (2):
      }

    return "";
  }
}