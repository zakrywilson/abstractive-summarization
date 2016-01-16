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
  Entity(final String noun, final String type, final int sentenceNumber) {
    this.noun = noun;
    this.type = type;
    this.sentenceNumbers = new ArrayList<>(1);
    this.sentenceNumbers.add(sentenceNumber);
  }


  String getNoun() {
    return this.noun;
  }


  String getType() {
    return this.type;
  }


  List<Integer> getSentenceNumbers() {
    return this.sentenceNumbers;
  }
}