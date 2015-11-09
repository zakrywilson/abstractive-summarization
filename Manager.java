public class Manager {

  /**
   * Runs the entire program
   * @param args
   */
  public static void main(String[] args) {

    // Options
    String file = "cat.txt";
    boolean writeToFile = true;

    Triples triples = new Triples(file, writeToFile);
    System.out.print(triples);
  }
}
