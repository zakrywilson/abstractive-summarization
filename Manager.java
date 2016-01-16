/**
 * Manages entire program.
 *
 * @author Zach Wilson
 */
class Manager {


  /**
   * Main: Runs program by calling Triples to extract information from file.
   *
   * @param args - no arguments are being used
   */
  public static void main(String[] args) {

    // Options
    String file = "sample.txt";
    boolean writeToFile = true;

    // Running program
    Extractor extractor = new Extractor(file, writeToFile);

    // Get the network and process it
    Network network = extractor.getNetwork();
    cleanupSentences(network);

    // Display summary
    printSummary(file, network);
  }


  /**
   * Purges unneeded triples and sentences, and the chooses
   * which triples should be used to represent a given sentence
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
   */
  private static void printSummary(String filename, Network network) {
    System.out.println("\nSummary of " + filename + ":");
    String summary = Concatenator.fuse(network);
    System.out.println(summary);
  }
}