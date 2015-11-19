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
   * @param index
   */
  protected void chooseBSU(int index) {
    setBSU(this.bsus.get(index));
  }

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

  @Override
  public String toString() {
    String output = this.sentence + "\n";
    for (BSU bsu : this.bsus) {
      output += "   " + bsu;
    }
    return output;
  }
}
