import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Contains all Named Entity Recognition information for a single body of text.
 */
public class NamedEntities {

  private Map<String, String> ner;

  /**
   * Constructor
   * <p>
   *  Initializes collection to hold NER information where
   *  the key is the entity and the value is the entity label.
   * </p>
   */
  protected NamedEntities() {
    this.ner = new LinkedHashMap();
  }

  protected void add(final String noun, final String ner) {
    this.ner.put(noun, ner);
  }

  protected String get(final String noun) {
    return this.ner.get(noun);
  }

  @Override
  public String toString() {
    String output = "";
    for (Map.Entry<String, String> pair : this.ner.entrySet()) {
      output += pair.getKey() + " -> " + pair.getValue() + "\n";
    }
    return output + "\n";
  }
}