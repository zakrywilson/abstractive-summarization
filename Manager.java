/**
 * Manages entire program.
 * @author Zach Wilson
 */
class Manager {


  /**
   * Main: Runs program.
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
    clearConsole();
    printSummary(file, network);
  }


  /**
   * Purges unneeded triples and sentences, and the chooses
   * which triples should be used to represent a given sentence.
   * @param network - the network containing the data to clean up
   */
  private static void cleanupSentences(Network network) {
    network.purgeTriples();
    network.purgeSentences();
    network.chooseLongestTriples();
  }


  /**
   * Prints out the summary with NER information embedded inside.
   * @param filename - file being summarized
   * @param network - the network of sentences
   */
  private static void printSummary(String filename, Network network) {
    System.out.println("Summary of " + filename + ":");
    String summary = Concatenator.fuse(network);
    System.out.println(summary);
  }


  /**
   * Clears console for displaying summarized text.
   */
  private static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
}