import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;


/**
 * File writer utility class
 */
public class FileWriter {

  private BufferedWriter writer = null;


  /**
   * Constructor
   * <p>
   *   Takes a file name String, creates the file, and creates the BufferedWriter
   * </p>
   * @param filename - file name that will be created if it doesn't already exist
   * @throws Exception
   */
  protected FileWriter(final String filename) throws Exception {
    this.writer = new BufferedWriter(new java.io.FileWriter(createFile(filename)));
  }


  /**
   * Creates a file, given a file name
   *
   * @param filename - desired name for the file
   * @return file - a file that exists on disk
   * @throws IOException
   */
  private File createFile(final String filename) throws IOException {

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


  /**
   * Takes a String and writes it to file
   *
   * @param text - text to be written to file
   * @throws IOException
   */
  protected void write(String text) throws IOException {
    this.writer.write(text);
  }


  /**
   * Closes BufferedWriter
   */
  protected void close() {
    if (this.writer != null) {
      try {
        this.writer.close();
      } catch (IOException e) {
        System.err.println("Failed to close BufferedWriter resource.");
      }
    }
  }

}