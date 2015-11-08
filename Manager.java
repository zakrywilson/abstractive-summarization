public class Manager {

  /**
   * Default constructor is not to be called.
   */
  private Manager() {}


  /**
   * Runs the entire program
   * @param args
   */
  public static void main(String[] args) {

    // Options
    String file = "tolstoy.txt";
    boolean writeToFile = true;

    Triples triples = new Triples(file, writeToFile);
    System.out.print(triples);
  }
}