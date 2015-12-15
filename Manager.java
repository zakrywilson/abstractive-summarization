/**
 * Manages entire program.
 *
 * @author Zach Wilson
 */
public class Manager {


  /**
   * Main: Runs program by calling Triples to extract information from file.
   *
   * @param args
   */
  public static void main(String[] args) {

    // Options
    String file = "sample.txt";
    boolean writeToFile = true;

    // Running program
    Extractor extractor = new Extractor(file, writeToFile);

    // Process the text
    Network network = extractor.getNetwork();
    network.purgeBSUs();
    network.purgeSentences();
    network.chooseLongestBSUs();

    // Display results
    System.out.println("PRINTING BSUs");
    network.printBSUs(true);
    System.out.println("\nCompressed text...\n" + network.getCompressedText());

  }
}
