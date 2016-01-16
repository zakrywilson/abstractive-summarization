import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


/**
 * File manager utility class
 */
class FileManager {


  /**
   * Reads text from input file and returns it
   *
   * @param document - the document containing text to process
   * @return text - if there was an error, text will be equal to null
   */
  public static String getText(final String document, final String path) {

    // Check for resources directory
    File dir = new File(path);
    if (!dir.exists()) {
      if (!dir.mkdir()) {
        System.err.println("ERROR: failed to create 'resources' directory.");
      }
      return null;
    }

    // Create file for reading
    File file = new File(path + document);
    List<String> lines = null;

    // Read lines
    try {
      lines = Files.readAllLines(file.toPath());
    } catch (IOException e) {
      System.err.println("ERROR: unable to read file: " + document);
    }

    // Append lines to one String
    StringBuilder text = null;
    if (lines != null) {
      text = new StringBuilder();
      for (String line : lines) {

        // Remove all unnecessary whitespace
        line = line.trim();

        // Disregard whitespace lines and empty strings
        if (line.length() == 0) {
          continue;
        }

        // Guarding against bad punctuation
        if (line.matches(".*\\p{Punct}")) {
          text.append(line);
          text.append(" ");
        } else {
          text.append(line);
          text.append(". ");
        }
      }
    }
    return (text == null) ? null : text.toString();
  }


  /**
   * Creates file on disk by deleting any file with the same name.
   *
   * @param file - file to be created
   * @return true if creation was successful
   */
  static boolean thoroughlyCreate(File file) {
    if (file.exists()) {
      if (!file.delete()) {
        System.err.println("ERROR: unable to delete file " + file.getAbsolutePath());
        return false;
      }
    }
    try {
      if (!file.createNewFile()) {
        System.err.println("ERROR: unable to create file " + file.getName());
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * Takes a file name and text, and writes the text to the file.
   * If the file does not exist, it will be created.
   *
   * @param filename - name of the file to write to
   * @param text - text to be written to file
   */
  protected static void write(final String filename, final String text) {
    try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(createFile(filename)))) {
      writer.write(text);
    } catch (IOException e) {
      System.err.println("Failed to write to file.");
    }
  }


  /**
   * Creates a file, given a file name.
   *
   * @param filename - desired name for the file
   * @return file - a file that exists on disk
   * @throws IOException
   */
  private static File createFile(final String filename) throws IOException {

    File file = new File(filename);

    if (file.exists()) {
      if (!file.delete()) {
        throw new IOException("Failed to delete file.");
      }
    }

    if (!file.createNewFile()) {
      throw new IOException("Failed to create file: " + filename);
    }

    return file;
  }
}