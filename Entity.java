import java.util.ArrayList;
import java.util.List;


/**
 * Java object representing a single named entity.
 */
class Entity {


  /** The noun for the named entity */
  private String noun;

  /** The type of named entity */
  private String type;

  /** The corresponding sentence number */
  private List<Integer> sentenceNumbers;


  /**
   * Constructor.
   * @param type - the type of named entity
   * @param sentenceNumber - the sentence number in which the NER was found
   */
  Entity(final String noun, final String type, final int sentenceNumber) {
    this.noun = noun;
    this.type = type;
    this.sentenceNumbers = new ArrayList<>(1);
    this.sentenceNumbers.add(sentenceNumber);
  }


  /**
   * Gets the noun.
   * @return noun
   */
  String getNoun() {
    return this.noun;
  }


  /**
   * Gets the type of entity.
   * @return type of entity
   */
  String getType() {
    return this.type;
  }


  /**
   * Gets the sentence number.
   * @return sentence number
   */
  List<Integer> getSentenceNumbers() {
    return this.sentenceNumbers;
  }
}