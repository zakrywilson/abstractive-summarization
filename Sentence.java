import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Sentence.
 * This data structure contains a sentence, it's Triples,
 * and its association amongst all other Triples in the form of...
 * (1) ASR: arguments semantic relatedness,
 * (2) VSR: action-verbs semantic relatedness,
 * (3) CSS: co-reference semantic relatedness.
 */
class Sentence {


  /** The original sentence */
  private String sentence = null;

  /** The Triple chosen to represent the sentence */
  private Triple triple = null;

  /** The entire list of Triples generated off of the original sentence */
  private List<Triple> triples = new ArrayList<>();

  /** The sentence's associated named entities */
  private NamedEntitiesList ner;


  /*
   * Constructor.
   * Takes a single sentence.
   * @param sentence
   */
  Sentence(final String sentence) {
    this.sentence = sentence;
  }


  /**
   * Add a named entities list object.
   * @param entities - list of named entities to add
   */
  void addEntities(NamedEntitiesList entities) {
    this.ner = entities;
  }


  /**
   * Getter for time entities.
   * @return time entities
   */
  String getTimeEntities() {
    return this.ner.getTimeEntity();
  }


  /**
   * Getter for the Sentence.
   * @return sentence
   */
  String getSentence() {
    return this.sentence;
  }


  /**
   * Getter for Triple.
   * @return triple
   */
  protected Triple getTriple() {
    return this.triple;
  }


  /**
   * Setter for the Triple.
   * @param triple - triple to be set as Sentence's representative triple
   */
  private void setTriple(final Triple triple) {
    this.triple = triple;
  }


  /**
   * Gets all Triple objects.
   * @return - all Triples
   */
  List<Triple> getAllTriples() {
    return this.triples;
  }


  /**
   * Sets all triples.
   * @param triples - list of triples to be set
   */
  void setAllTriples(final List<Triple> triples) {
    this.triples = triples;
  }


  /**
   * Purging Triples that have a confidence score of less than 1.000.
   * @return list of removed Triples
   */
  List<Triple> purge() {
    List<Triple> removedTriples = new ArrayList<>();
    Iterator<Triple> it = this.triples.iterator();
    while (it.hasNext()) {
      Triple triple = it.next();
      if (lowScore(triple) || tooShort(triple)) {
        removedTriples.add(triple);
        it.remove();
      }
    }
    return removedTriples;
  }


  /**
   * Chooses Triple to represent sentence.
   * @param index - sets the Triple as whatever index was provided
   */
  protected void chooseTriple(int index) {
    setTriple(this.triples.get(index));
  }


  /**
   * Chooses the longest Triple to represent the sentence.
   */
  void chooseLongestTriples() {
    Triple longestTriple = null;
    for (Triple nextTriple : this.triples) {
      if (longestTriple == null) {
        longestTriple = nextTriple;
      } else if (nextTriple.toString().length() > longestTriple.toString().length()) {
          longestTriple = nextTriple;
      }
    }
    setTriple(longestTriple);
  }


  /**
   * Takes the representative Triple and creates a sentence out of it.
   * @return compressed sentence
   */
  String getCompressedSentence() {
    return Formatter.formatSentence(this.triple.getActor(),
                                    this.triple.getAction(),
                                    this.triple.getReceiver());
  }


  /**
   * Checks for a low confidence score.
   * @param triple - semantic triple to be checked
   * @return true if the Triple has a score lower than 1
   */
  private boolean lowScore(Triple triple) {
    return !triple.getScore().startsWith("1");
  }


  /**
   * Checks whether the Triple is composed of 3 or less words.
   * @param triple - semantic triple to be checked
   * @return true if the Triple is made up of 3 or less words
   */
  private boolean tooShort(Triple triple) {
    return containsOneWord(triple.getActor())  &&
           containsOneWord(triple.getAction()) &&
           containsOneWord(triple.getReceiver());
  }


  /**
   * Checks whether the string contains less than 1 word.
   * @param string - string to be checked
   * @return true if string contains less than 1 word
   */
  private boolean containsOneWord(String string) {
    return !string.contains(" ") || string.equals("");
  }


  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    output.append(this.sentence);
    output.append("\n");
    for (Triple triple : this.triples) {
      output.append("   ");
      output.append(triple);
    }
    return output.toString();
  }
}