import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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
    this.network = new ConcurrentHashMap<String, Sentence>();
  }

  /**
   * Constructor
   * <p>
   *   Creates a new collection based on the given parameter.
   *   The passed in collection should be of the following format:<br>
   *   Key: the original sentence.<br>
   *   Value: its Sentence object containing semantic information on sentence.
   * </p>
   * @param network - a collection of all sentences in the body of text
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

  /**
   * Purging BSUs that have a confidence score of less than 1.000
   * out of every Sentence in the Network.
   * @return a list of all removed BSUs
   */
  protected List<BSU> purgeBSUs() {
    List<BSU> removedBSUs = new ArrayList<BSU>();
    for (Map.Entry<String, Sentence> pair: this.network.entrySet()) {
      Sentence sentence = pair.getValue();
      removedBSUs.addAll(sentence.purge());
    }
    return removedBSUs;
  }

  /**
   * Purging Sentences that do not have any BSUs in them.
   * @return list of removed sentences
   */
  protected List<String> purgeSentences() {
    List<String> removedSentences = new ArrayList<String>();
    for (Map.Entry<String, Sentence> pair: this.network.entrySet()) {
      Sentence sentence = pair.getValue();
      if (sentence.getAllBSUs().isEmpty()) {
        String removed = sentence.getSentence();
        removedSentences.add(removed);
        this.network.remove(removed);
      }
    }
    return removedSentences;
  }

  @Override
  public String toString() {
    String output = "";
    for (Map.Entry<String, Sentence> pair: this.network.entrySet()) {
      output += pair.getValue() + "\n";
    }
    return output;
  }
}