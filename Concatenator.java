import java.util.Map;

public class Concatenator {

  protected static String fuse(Network network, NamedEntities ner) {

    StringBuilder summary = new StringBuilder();

    for (Map.Entry<Integer, Sentence> pair : network.getCollection().entrySet()) {
      Sentence sentence = pair.getValue();
      String compressedSentence = sentence.getCompressedSentence();
      summary.append(compressedSentence);
    }

    return summary.toString();
  }

}