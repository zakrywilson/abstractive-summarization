import java.lang.Integer;
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


  /** The network of sentence numbers and the Sentence object containing all its metadata */
  private Map<Integer, Sentence> network;


  /**
   * Default Constructor
   * <p>
   *   Creates a new empty HashMap to contain the Sentences where
   *   the original sentence in the key and the Sentence object
   *   containing information on each sentence is the value.
   * </p>
   */
  Network() {
    this.network = new ConcurrentHashMap<>();
  }


  /**
   * Constructor
   * <p>
   *   Creates a new collection based on the given parameter.
   *   The passed in collection should be of the following format:<br>
   *   Key: the original sentence.<br>
   *   Value: its Sentence object containing semantic information on sentence.
   * </p>
   *
   * @param network - a collection of all sentences in the body of text
   */
  protected Network(ConcurrentHashMap<Integer, Sentence> network) {
    this.network = network;
  }


  /**
   * Returns the network containing the sentence Strings with their
   * associated Sentence objects.
   *
   * @return network
   */
  protected Map<Integer, Sentence> getCollection() {
    return this.network;
  }


  /**
   * Adding a sentence number and a Sentence object to the network
   *
   * @param sentenceNumber - the sentence number
   * @param sentence - instance of Sentence
   */
  protected void add(Integer sentenceNumber, Sentence sentence) {
    this.network.put(sentenceNumber, sentence);
  }


  /**
   * Purging BSUs that have a confidence score of less than 1.000
   * out of every Sentence in the Network.
   *
   * @return a list of all removed BSUs
   */
  protected List<BSU> purgeBSUs() {
    List<BSU> removedBSUs = new ArrayList<>();
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      Sentence sentence = pair.getValue();
      removedBSUs.addAll(sentence.purge());
    }
    return removedBSUs;
  }


  /**
   * Purging Sentences that do not have any BSUs in them.
   *
   * @return list of removed sentences
   */
  protected List<String> purgeSentences() {
    List<String> removedSentences = new ArrayList<>();
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      Integer sentenceNumber = pair.getKey();
      Sentence sentence = pair.getValue();
      if (sentence.getAllBSUs().isEmpty()) {
        String removedSentence = sentence.getSentence();
        removedSentences.add(removedSentence);
        this.network.remove(sentenceNumber);
      }
    }
    return removedSentences;
  }


  /**
   * Forces each Sentence to choose the longest
   * BSU to be its representative BSU
   */
  void chooseLongestBSUs() {
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      pair.getValue().chooseLongestBSU();
    }
  }


  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      output.append(pair.getValue());
      output.append("\n");
    }
    return output.toString();
  }
}