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

  private Map<Integer, Sentence> network;

  /**
   * Default Constructor
   * <p>
   *   Creates a new empty HashMap to contain the Sentences where
   *   the original sentence in the key and the Sentence object
   *   containing information on each sentence is the value.
   * </p>
   */
  protected Network() {
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

  protected void add(Integer sentenceNumber, Sentence sentence) {
    this.network.put(sentenceNumber, sentence);
  }

  protected Sentence get(final String sentence) {
    return this.network.get(sentence);
  }

  /**
   * Purging BSUs that have a confidence score of less than 1.000
   * out of every Sentence in the Network.
   *
   * @return a list of all removed BSUs
   */
  protected List<BSU> purgeBSUs() {
    List<BSU> removedBSUs = new ArrayList<BSU>();
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
   * Traversing every Sentence object and computing the semantic
   * relation amongst all Sentences.
   */
  protected void generateSemanticLinkNetwork() {

  }

  /**
   * Forces each Sentence to choose its top BSU
   */
  protected void chooseBSUs() {
    chooseBSUs(0);
  }

  /**
   * Print chosen BSUs
   *
   * @param printOriginalSentence - prints original sentence if true
   */
  protected void printBSUs(boolean printOriginalSentence) {
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      Sentence sentence = pair.getValue();
      if (printOriginalSentence) {
        System.out.println(sentence.getSentence());
      }
      System.out.println(sentence.getBSU());
    }
  }

  /**
   * Forces each Sentence to choose the BSU based on the index provided
   *
   * @param index - index of the collection that contains a BSU
   */
  protected void chooseBSUs(int index) {
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      pair.getValue().chooseBSU(index);
    }
  }

  /**
   * Forces each Sentence to choose the longest
   * BSU to be its representative BSU
   */
  protected void chooseLongestBSUs() {
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      pair.getValue().chooseLongestBSU();
    }
  }

  /**
   * Creates a list of all the compressed strings.
   *
   * @return text - a compressed, summarized representation of the original text
   */
  protected String getCompressedText() {
    String text = "";
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      text += pair.getValue().getCompressedSentence();
    }
    return text;
  }

  @Override
  public String toString() {
    String output = "";
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      output += pair.getValue() + "\n";
    }
    return output;
  }
}