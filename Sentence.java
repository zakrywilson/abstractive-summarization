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
      if (!bsu.getScore().startsWith("1")) {
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
   *
   * @return compressed sentence - a sentence taken from a BSU
   */
  protected String getCompressedSentence() {
    return formatSentence(this.bsu.getActor(),
                          this.bsu.getAction(),
                          this.bsu.getReceiver());
  }

  private String formatActor (String s) {
    if (!Character.isUpperCase(s.charAt(0))) {
      return capitalize(s);
    }
    return s;
  }


  private String formatAction (String s) {
    return s.toLowerCase();
  }

  private String formatReceiver (String s) {
    return s;
  }

  private String formatSentence(String actor, String action, String receiver) {
    // Make sure there isn't any spaces between a word and it's apostrophe
    String sentence = formatActor(actor) + " " + formatAction(action) + " " + formatReceiver(receiver) + ". ";
    if (sentence.contains(" 's")) {
      sentence = sentence.replaceAll(" 's ", "'s ");
    }
    return sentence;
  }

  /**
   * Capitalizing the first letter of a String
   *
   * @param s - String that will have its first character capitalized
   * @return capitalized string
   */
  private String capitalize(String s) {
    return Character.toUpperCase(s.charAt(0)) + s.substring(1);
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
