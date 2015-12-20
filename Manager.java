/**
 * Manages entire program.
 *
 * @author Zach Wilson
 */
public class Manager {


  /**
   * Main: Runs program by calling Triples to extract information from file.
   *
   * @param args - no arguments are being used
   */
  public static void main(String[] args) {

    // Options
    String file = "characters.txt";
    boolean writeToFile = true;

    // Running program
    Extractor extractor = new Extractor(file, writeToFile);

    // Gather NER data
    NamedEntities namedEntities = extractor.getNER();

    // Get the network and process it
    Network network = extractor.getNetwork();
    cleanupSentences(network);

    // Display basic semantic units
//    System.out.println("PRINTING BSUs");
//    network.printBSUs(true);

    // Display summary
    printSummary(file, network, namedEntities);
  }


  /**
   * Purges unneeded triples and sentences, and the chooses
   * which tiples should be used to represent a given sentence
   *
   * @param network - the network containing the data to clean up
   */
  private static void cleanupSentences(Network network) {
    network.purgeBSUs();
    network.purgeSentences();
    network.chooseLongestBSUs();
  }


  /**
   * Prints out the summary with NER information embedded inside
   *
   * @param filename - file being summarized
   * @param network - the network of sentences
   * @param ner - the named entity information
   */
  private static void printSummary(String filename, Network network, NamedEntities ner) {
    System.out.println("Summary of " + filename + ":");
    String summary = Concatenator.fuse(network, ner);
    System.out.println(summary);
  }
}
