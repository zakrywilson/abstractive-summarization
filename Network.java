import java.util.*;
import java.util.Map.Entry;


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
    this.network = new HashMap<>();
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
    List<Triple> removedTriples = new ArrayList<>();
    for (Entry<Integer, Sentence> pair: this.network.entrySet()) {
      Sentence sentence = pair.getValue();
      removedTriples.addAll(sentence.purge());
    }
    return removedTriples;
  }


  /**
   * Purging Sentences that do not have any Triples in them.
   * @return list of removed sentences
   */
  ArrayList<String> purgeSentences() {

    ArrayList<String> removedSentences = new ArrayList<>();
    Iterator<Entry<Integer, Sentence>> it;

    it = this.network.entrySet().iterator();
    while (it.hasNext()) {
      Sentence sentence = it.next().getValue();
      if (sentence.getAllTriples().isEmpty()) {
        removedSentences.add(sentence.getSentence());
        it.remove();
      }
    }
    return removedSentences;
  }


  /**
   * Forces each Sentence to choose longest Triples to be its
   * representative Triple.
   */
  void chooseLongestTriples() {
    for (Entry<Integer, Sentence> pair: this.network.entrySet()) {
      pair.getValue().chooseLongestTriples();
    }
  }


  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    for (Entry<Integer, Sentence> pair: this.network.entrySet()) {
      output.append(pair.getValue());
      output.append("\n");
    }
    return output.toString();
  }
}