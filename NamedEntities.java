import java.util.*;

public class NamedEntities {

  private Map<String, String> ner;

  protected NamedEntities() {
    this.ner = new LinkedHashMap();
  }

  protected void add(String noun, String ner) {
    this.ner.put(noun, ner);
  }

  protected String get(String noun) {
    return this.ner.get(noun);
  }

  @Override
  public String toString() {
    String output = "";
    for (Map.Entry<String, String> pair : this.ner.entrySet()) {
      output += pair.getKey() + " -> " + pair.getValue() + "\n";
    }
    return output;
  }
}