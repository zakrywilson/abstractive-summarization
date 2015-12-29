import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Sentence
 * <p>
 *   This data structure contains a sentence, it's BSUs,
 *   and its association amongst all other BSUs in the form of...
 * <p>
 *   (1) ASR: arguments semantic relatedness,
 *   (2) VSR: action-verbs semantic relatedness,
 *   (3) CSS: co-reference semantic relatedness.
 */
public class Sentence {


  /** The original sentence */
  private String sentence = null;

  /** The BSU chosen to represent the sentence */
  private BSU bsu = null;

  /** The entire list of BSUs generated off of the original sentence */
  private List<BSU> bsus = new ArrayList<BSU>();

  /** The sentence's associated named entities */
  private NamedEntities ner;


  /*
   * Constructor
   * <p>
   *   Takes a single sentence.
   * </p>
   *
   * @param sentence
   */
  protected Sentence(final String sentence) {
    this.sentence = sentence;
  }


  protected void addEntities(NamedEntities entities) {
    this.ner = entities;
  }


  protected String getTimeEntities() {
    return this.ner.getTimeEntity();
  }


  protected String getSentence() {
    return this.sentence;
  }


  protected BSU getBSU() {
    return this.bsu;
  }


  protected void setBSU(final BSU bsu) {
    this.bsu = bsu;
  }


  protected List<BSU> getAllBSUs() {
    return this.bsus;
  }


  protected void setAllBSUs(final List<BSU> bsus) {
    this.bsus = bsus;
  }


  /**
   * Purging BSUs that have a confidence score of less than 1.000
   *
   * @return list of removed BSUs
   */
  protected List<BSU> purge() {
    List<BSU> removedBSUs = new ArrayList<BSU>();
    Iterator<BSU> it = this.bsus.iterator();
    while (it.hasNext()) {
      BSU bsu = it.next();
      if (lowScore(bsu) || tooShort(bsu)) {
        removedBSUs.add(bsu);
        it.remove();
      }
    }
    return removedBSUs;
  }


  /**
   * Chooses BSU to represent sentence
   *
   * @param index - sets the BSU as whatever index was provided
   */
  protected void chooseBSU(int index) {
    setBSU(this.bsus.get(index));
  }


  /**
   * Chooses the longest BSU to represent the sentence
   */
  protected void chooseLongestBSU() {
    BSU longestBSU = null;
    for (BSU nextBSU : this.bsus) {
      if (longestBSU == null) {
        longestBSU = nextBSU;
      } else if (nextBSU.toString().length() > longestBSU.toString().length()) {
          longestBSU = nextBSU;
      }
    }
    setBSU(longestBSU);
  }


  /**
   * Takes the representative BSU and creates a sentence out of it.
   * @return compressed sentence - a sentence taken from a BSU
   */
  protected String getCompressedSentence() {
    return Formatter.formatSentence(this.bsu.getActor(),
                                    this.bsu.getAction(),
                                    this.bsu.getReceiver());
  }


  /**
   * Checks for a low confidence score
   * @param bsu - basic semantic unit to be checked
   * @return true if the BSU has a score lower than 1
   */
  private boolean lowScore(BSU bsu) {
    return !bsu.getScore().startsWith("1");
  }


  /**
   * Checks whether the BSU is composed of 3 or less words
   * @param bsu - basic semantic unit to be checked
   * @return true if the BSU is made up of 3 or less words
   */
  private boolean tooShort(BSU bsu) {
    return containsOneWord(bsu.getActor())  &&
           containsOneWord(bsu.getAction()) &&
           containsOneWord(bsu.getReceiver());
  }


  /**
   * Checks whether the string contains less than 1 word
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
    for (BSU bsu : this.bsus) {
      output.append("   ");
      output.append(bsu);
    }
    return output.toString();
  }
}