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
    String file = "sample.txt";
    boolean writeToFile = true;

    // Running program
    Extractor extractor = new Extractor(file, writeToFile);

    // Gather NER data
    NamedEntities namedEntities = extractor.getNER();

    // Process the text
    Network network = extractor.getNetwork();
    network.purgeBSUs();
    network.purgeSentences();
    network.chooseLongestBSUs();

    // Display basic semantic units
    System.out.println("PRINTING BSUs");
    network.printBSUs(true);

    // Display summary
    System.out.println("\nCompressed text...\n");
    printSummary(network, namedEntities);

  }

  /**
   * Prints out the summary with NER information embedded inside
   *
   * @param network - the network of sentences
   * @param ner - the named entity information
   */
  private static void printSummary(Network network, NamedEntities ner) {
    System.out.println(network.getCompressedText());
  }
}
