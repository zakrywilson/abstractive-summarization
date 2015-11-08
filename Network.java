import java.util.*;

public class Network {

  private Map<String, Sentence> network;

  protected Network() {
    this.network = new HashMap<String, Sentence>();
  }
  
  protected Network(final Map<String, Sentence> network) {
    this.network = network;
  }

  protected void add(String sentence, Sentence data) {
    this.network.put(sentence, data);
  }

  protected Sentence get(final String sentence) {
    return this.network.get(sentence);
  }

  @Override
  public String toString() {
    String output = "";
    Iterator it = this.network.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry) it.next();
      output += pair.getValue() + "\n";
    }
    return output;
  }
}