import java.util.ArrayList;
import java.util.List;

  /**
   * Sentence
   * <p>
   *  This data structure contains a sentence, it's BSUs,
   *  and its association amongst all other BSUs in the form of...
   * </p>
   *   (1) ASR: arguments semantic relatedness,
   *   (2) VSR: action-verbs semantic relatedness,
   *   (3) CSS: coreference semantic relatedness.
   */
  public class Sentence {

    private String sentence, bsu = null;

    private List<String> asr, vsr, css = new ArrayList<String>();

    private List<BSU> bsus = null;
    
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

    protected String getBSU() {
      return this.bsu;
    }

    protected void setBSU(final String bsu) {
      this.bsu = bsu;
    }

    protected List<BSU> getAllBSUs() {
      return this.bsus;
    }

    protected void setAllBSUs(final List<BSU> bsus) {
      this.bsus = bsus;
    }

    protected List<String> getASR() {
      return this.asr;
    }

    protected void setASR(final String bsu) {
      this.asr.add(bsu);
    }

    protected List<String> getVSR() {
      return this.vsr;
    }

    protected void setVSR(final String bsu) {
      this.vsr.add(bsu);
    }

    protected List<String> getCSS() {
      return this.css;
    }

    protected void setCSS(final String bsu) {
      this.css.add(bsu);
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
