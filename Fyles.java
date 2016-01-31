import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


/**
 * File manager utility class.
 */
class Fyles {


  /**
   * Reads text from input file and returns it.
   * @param document - document containing text to process
   * @return text or null if there was an error
   * @throws Exception
   */
  public static String getText(final File document) throws Exception {

    // Create file for reading
    List<String> lines;

    // Read lines
    try {
      lines = Files.readAllLines(document.toPath());
    } catch (IOException e) {
      throw new IOException("Unable to read file: " + document, e);
    }

    if (lines == null) {
      throw new Exception("Text extracted from '" + document + "' was null.");
    }

    // Append lines to one String
    StringBuilder text = new StringBuilder();
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
    return text.toString();
  }


  /**
   * Takes a file name and text, and writes the text to the file.
   * If the file does not exist, it will be created.
   * @param filename - name of the file to write to
   * @param text - text to be written to file
   * @throws IOException
   */
  static void write(final String filename, final String text) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(createFile(filename)))) {
      writer.write(text);
    } catch (IOException e) {
      throw new IOException("Failed to write to file.");
    }
  }


  /**
   * Creates a file, given a file name. If there is a naming conflict, the
   * original file is removed.
   * @param filename - desired name for the file
   * @return file that exists on disk
   * @throws IOException
   */
  private static File createFile(final String filename) throws IOException {

    File file = new File(filename);

    // If there is a naming conflict, remove the original
    if (file.exists()) {
      if (!file.delete()) {
        throw new IOException("Failed to delete file.");
      }
    }

    // Create the new file
    if (!file.createNewFile()) {
      throw new IOException("Failed to create file: " + filename);
    }

    return file;
  }


  /**
   * Gets the file extension from a file (e.g., 'file.txt' returns 'txt')
   * @param file - file to obtain file extension from
   * @param includeDot - whether the dot ('.') should be included
   * @return file extension
   */
  static String getFileExtension(final File file, final boolean includeDot) {
    int offset = (includeDot) ? 0 : 1;
    String filename = file.getName();
    return filename.substring(filename.indexOf(".") + offset);
  }


  /**
   * Gets the file name from a file without the file extension.
   * @param file - file to be removed of its extension
   * @return file without extension
   */
  static String removeFileExtension(final File file) {
    String path = file.getAbsolutePath();
    return path.substring(0, path.indexOf("."));
  }
}