import java.util.ArrayList;
import java.util.List;


class Entity {


  private String noun;
  private String type;
  private List<Integer> sentenceNumbers;


  /**
   * Constructor
   *
   * @param type - the type of named entity
   * @param sentenceNumber - the sentence number in which the NER was found
   */
  protected Entity(final String noun, final String type, final int sentenceNumber) {
    this.noun = noun;
    this.type = type;
    this.sentenceNumbers = new ArrayList<Integer>(1);
    this.sentenceNumbers.add(sentenceNumber);
  }


  protected String getNoun() {
    return this.noun;
  }


  protected String getType() {
    return this.type;
  }


  protected List<Integer> getSentenceNumbers() {
    return this.sentenceNumbers;
  }
}