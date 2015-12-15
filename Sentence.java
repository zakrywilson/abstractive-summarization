import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

  // The sentence and it's associated BSUs
  private String sentence = null;
  private BSU bsu = null;
  private List<BSU> bsus = new ArrayList<BSU>();

  // The data comprising its external relationships
  private List<String> asr = new ArrayList<String>();
  private List<String> vsr = new ArrayList<String>();
  private List<String> css = new ArrayList<String>();

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

  protected List<String> getASRs() {
    return this.asr;
  }

  protected void addASR(final String bsu) {
    this.asr.add(bsu);
  }

  protected List<String> getVSRs() {
    return this.vsr;
  }

  protected void addVSRs(final String bsu) {
    this.vsr.add(bsu);
  }

  protected List<String> getCSSs() {
    return this.css;
  }

  protected void addCSS(final String bsu) {
    this.css.add(bsu);
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
//      if (lowScore(bsu) || tooShort(bsu)) {
      if (lowScore(bsu)) {
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
    return formatSentence(this.bsu.getActor(),
                          this.bsu.getAction(),
                          this.bsu.getReceiver());
  }

  /**
   * Formats the actor of the triple
   * @param string - string to be capitalized
   * @return string capitalized
   */
  private String formatActor (String string) {
    if (!Character.isUpperCase(string.charAt(0))) {
      return capitalize(string);
    }
    return string;
  }

  /**
   * Formats the action of the triple
   * @param string - text to be converted to lowercase
   * @return string - string converted to lowercase
   */
  private String formatAction (String string) {
    return string.toLowerCase();
  }

  /**
   * Formats the receiver of the triple
   * @param string - text to be formatted
   * @return formatted string
   */
  private String formatReceiver (String string) {
    return string;
  }

  /**
   * Formats the entire sentence
   * @param actor - actor of the triple
   * @param action - action of the triple
   * @param receiver - receiver of the triple
   * @return formatted sentence
   */
  private String formatSentence(String actor, String action, String receiver) {
    // Make sure there isn't any spaces between a word and it's apostrophe
    String sentence = formatActor(actor) + " " + formatAction(action) + " " + formatReceiver(receiver) + ". ";
    sentence = sentence.replaceAll("\\w's\\w", "'s ");
    return sentence;
  }

  /**
   * Capitalizing the first letter of a String
   *
   * @param string - String that will have its first character capitalized
   * @return capitalized string
   */
  private String capitalize(String string) {
    return Character.toUpperCase(string.charAt(0)) + string.substring(1);
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
    String output = this.sentence + "\n";
    for (BSU bsu : this.bsus) {
      output += "   " + bsu;
    }
    return output;
  }
}
