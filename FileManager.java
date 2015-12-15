import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;


/**
 * File manager utility class
 */
public class FileManager {


  /**
   * Creates file on disk by deleting any file with the same name.
   *
   * @param file - file to be created
   * @return true if creation was successful
   */
  protected static boolean thoroughlyCreate(File file) {
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