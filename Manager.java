import java.io.File;

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
    try {
      run(args);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("A fatal error occurred.");
      System.out.println(getDisplayHelp());
    }
  }


  /**
   * Runs the program after validating command line arguments.
   * @param args - command line arguments
   */
  private static void run(final String[] args) {

    // Create command line option
    CommandLine commandline = new CommandLine();

    // Body of text option
    Option textOption = new Option();
    textOption.addShortName("f");
    textOption.addLongName("file");
    textOption.addDescription("The body of text to be summarized.");
    textOption.addExpectedArgCount(1);
    textOption.isRequired(true);
    commandline.addOption(textOption);

    // Write metadata to file option
    Option metaDataOption = new Option();
    metaDataOption.addShortName("m");
    metaDataOption.addDescription("Save meta data to file.");

    // Write summary to file option
    Option summaryOption = new Option();
    summaryOption.addShortName("s");
    summaryOption.addDescription("Saves summary to file.");

    // Help option
    commandline.createHelp(getDisplayHelp());

    // Parse command line arguments
    commandline.parse(args);

    // Get body of text
    File file = new File(textOption.getArgument(0));
    if (!file.exists()) {
      throw new IllegalArgumentException("File '" + file + "' does not exist.");
    }

    // Get metadata option
    boolean writeMetaDataToFile = metaDataOption.isFound();

    // Get summary option
    boolean writeSummaryToFile = summaryOption.isFound();

    // Running program
    Extractor extractor = new Extractor(file.getName(), writeMetaDataToFile);

    // Get the network and process it
    Network network = extractor.getNetwork();
    cleanupSentences(network);

    // Display summary
    clearConsole();
    printSummary(file.getName(), network);
  }


  /**
   * Gets the help information.
   * @return help information
   */
  private static String getDisplayHelp() {
    String string = "Abstractive Summarization\n";
    string += "Automatic abstractive summarization for news articles.\n";
    string += "(https://github.com/zakrywilson/abstractive-summarization)\n";
    string += "usage: ./demo [arguments]\n\n";
    string += "arguments: \n";
    string += "   -h  or  --help            Help\n";
    string += "   -f  or  --file [filename] Body of text to be summarized\n";
    string += "   -m                        Write metadata to file\n";
    string += "   -s                        Write summary to file\n\n";
    return string;
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