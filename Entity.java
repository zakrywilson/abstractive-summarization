import java.util.ArrayList;
import java.util.List;

class Entity {

  private String entity;
  private List<Integer> sentenceNumbers;

  protected Entity(final String entity, final int sentenceNumber) {
    this.entity = entity;
    this.sentenceNumbers = new ArrayList<Integer>(1);
    this.sentenceNumbers.add(sentenceNumber);
  }

  protected String getEntity() {
    return entity;
  }

  protected List<Integer> getSentenceNumbers() {
    return sentenceNumbers;
  }
}
