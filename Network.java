import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A Network contains all Sentences from a body of text
 * which contain the original sentences and associated
 * Basic Semantic Units.
 */
public class Network {

  private Map<String, Sentence> network;

  /**
   * Default Constructor
   * <p>
   *   Creates a new empty HashMap to contain the Sentences where
   *   the original sentence in the key and the Sentence object
   *   containing information on each sentence is the value.
   * </p>
   */
  protected Network() {
    this.network = new HashMap<String, Sentence>();
  }

  /**
   * Constructor
   * <p>
   *   Creates a new collection based on the given parameter.
   *   The passed in collection should be of the following format:<br>
   *   Key: the original sentence.<br>
   *   Value: its Sentence object containing semantic information on sentence.
   * </p>
   * @param network
   */
  protected Network(final Map<String, Sentence> network) {
    this.network = network;
  }

  protected void add(String sentence, Sentence data) {
    this.network.put(sentence, data);
  }

  protected Sentence get(final String sentence) {
    return this.network.get(sentence);
  }

  @Override
  public String toString() {
    String output = "";
    Iterator it = this.network.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry) it.next();
      output += pair.getValue() + "\n";
    }
    return output;
  }
}