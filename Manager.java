/**
 * Manages entire program.
 * @author Zach Wilson
 */
public class Manager {

  /**
   * Main: Runs program by calling Triples to extract information from file.
   * @param args
   */
  public static void main(String[] args) {

    // Options
    String file = "sample.txt";
    boolean writeToFile = true;

    // Running program
    Triples triples = new Triples(file, writeToFile);
    Network network = triples.getNetwork();
    network.purgeBSUs();
    network.purgeSentences();
    System.out.print(network);
  }
}
