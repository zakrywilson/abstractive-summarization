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
  private static void run(final String[] args) throws Exception {

    // Create command line option
    CommandLine commandline = new CommandLine();

    // File option
    Option fileOption = new Option();
    fileOption.addShortName("f");
    fileOption.addLongName("file");
    fileOption.addDescription("The body of text to be summarized.");
    fileOption.addExpectedArgCount(1);
    fileOption.isRequired(true);
    commandline.addOption(fileOption);

    // Write metadata to file option
    Option metaDataOption = new Option();
    metaDataOption.addShortName("m");
    metaDataOption.addDescription("Save meta data to file.");
    commandline.addOption(metaDataOption);

    // Write summary to file option
    Option summaryOption = new Option();
    summaryOption.addShortName("s");
    summaryOption.addDescription("Saves summary to file.");
    commandline.addOption(summaryOption);

    // Help option
    commandline.createHelp(getDisplayHelp());

    // Parse command line arguments
    commandline.parse(args);

    // Check for help
    if (commandline.needHelp()) {
      System.out.println(commandline.getHelp());
      return;
    }

    // Get file containing body of text
    File file = new File(fileOption.getArgument(0));
    if (!file.exists()) {
      throw new IllegalArgumentException("File '" + file + "' does not exist.");
    }

    // Get metadata option
    boolean writeMetadata = metaDataOption.isFound();

    // Get summary option
    boolean writeSummary = summaryOption.isFound();

    // Running program
    Extractor extractor = new Extractor(file);

    // Get the network and process it
    Network network = extractor.getNetwork();
    cleanupSentences(network);
    String summary = Concatenator.fuse(network);

    // Write metadata to file
    if (writeMetadata) {
      write(file, "-meta", network.toString() + extractor.getNER().toString());
    }

    // Write summary to file
    if (writeSummary) {
      write (file, "-summary", summary);
    }

    // Display summary
    clearConsole();
    printSummary(file.getName(), summary);
  }


  /**
   * Gets the help information.
   * @return help information
   */
  private static String getDisplayHelp() {
    String string = "\nAbstractive Summarization\n";
    string += "Automatic abstractive summarization for news articles.\n";
    string += "(https://github.com/zakrywilson/abstractive-summarization)\n\n";
    string += "usage: ./demo [arguments]\n\n";
    string += "arguments: \n";
    string += "   -h  or  --help            Help\n";
    string += "   -f  or  --file [filename] Body of text to be summarized\n";
    string += "   -m                        Write metadata to file\n";
    string += "   -s                        Write summary to file\n";
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
   * @param summary - summary of the file
   */
  private static void printSummary(final String filename, final String summary) {
    System.out.println("Summary of " + filename + ":");
    System.out.println(summary);
  }


  /**
   * Clears console for displaying summarized text.
   */
  private static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }


  /**
   * Writes information to file. File will be given new name based on the
   * extension: e.g., 'file.txt' & '-ext' ––> 'file-ext.txt'.
   * @param ext - new extension to file
   * @param string - string to be written to file
   * @throws Exception
   */
  static private void write(final File original,
                            final String ext,
                            final String string) throws Exception {

    // Create new file to write to
    String oldExtension = Fyles.getFileExtension(original, true);
    String name = Fyles.removeFileExtension(original);

    // Start writing
    Fyles.write(name + ext + oldExtension, string);
  }
}