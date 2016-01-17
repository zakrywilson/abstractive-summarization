import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * A Network contains all Sentences from a body of text which contain the
 * original sentences and associated Triples.
 */
class Network {


  /** Network of sentence numbers & Sentence object containing all its metadata */
  private Map<Integer, Sentence> network;


  /**
   * Default Constructor.
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
   * Returns the network containing the sentence Strings with their.
   * associated Sentence objects.
   * @return network
   */
  Map<Integer, Sentence> getCollection() {
    return this.network;
  }


  /**
   * Adding a sentence number and a Sentence object to the network.
   * @param sentenceNumber - the sentence number
   * @param sentence - instance of Sentence
   */
  void add(Integer sentenceNumber, Sentence sentence) {
    this.network.put(sentenceNumber, sentence);
  }


  /**
   * Purging Triples that have a confidence score of less than 1.000
   * out of every Sentence in the Network.
   * @return a list of all removed Triples
   */
  List<Triple> purgeTriples() {
    List<Triple> removedtriples = new ArrayList<>();
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      Sentence sentence = pair.getValue();
      removedtriples.addAll(sentence.purge());
    }
    return removedtriples;
  }


  /**
   * Purging Sentences that do not have any Triples in them.
   * @return list of removed sentences
   */
  List<String> purgeSentences() {
    List<String> removedSentences = new ArrayList<>();
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      Integer sentenceNumber = pair.getKey();
      Sentence sentence = pair.getValue();
      if (sentence.getAllTriples().isEmpty()) {
        String removedSentence = sentence.getSentence();
        removedSentences.add(removedSentence);
        this.network.remove(sentenceNumber);
      }
    }
    return removedSentences;
  }


  /**
   * Forces each Sentence to choose longest Triples to be its
   * representative Triple.
   */
  void chooseLongestTriples() {
    for (Map.Entry<Integer, Sentence> pair: this.network.entrySet()) {
      pair.getValue().chooseLongestTriples();
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