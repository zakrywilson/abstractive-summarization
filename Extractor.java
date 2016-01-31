import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.util.*;


/**
 * Handles extracting information from a document:
 * Triples: stored in Network.
 * Named entity information: stored in EntitiesList.
 */
class Extractor {


  /** An instance of the Network containing all metadata for text */
  private Network network;

  /** Contains all of named entity recognition information in LinkedHashMap */
  private EntitiesList ner;


 /**
  * Constructor.
  * Reads in file, processes it, extracts triples, prints them to standard
  * out, and (optionally) writes metadata to new file (original-meta.txt) and
  * (optionally) writes summary to new file (original-summary.txt).
  * @param document - valid file containing the input text
  */
  Extractor(File document) throws Exception {

    this.network = new Network();
    this.ner = new EntitiesList();

    // Read and store file
    String text = Fyles.getText(document);

    // Extract the triples
    processText(text);
  }


 /**
  * Extracts triples and NER, stores the information in separate maps, prints
  * the information to standard out and (optionally) writes data to file.
  * @param text - text that is to be processed
  * @throws Exception
  */
  private void processText(final String text) throws Exception {

    // Ensure there is text to process
    if (text == null) {
      throw new Exception("Text was null.");
    }

    // Create the Stanford CoreNLP pipeline
    Properties props = new Properties();
    String pipe = "tokenize, ssplit, pos, parse, depparse, lemma, ner, dcoref, natlog, openie";
    props.setProperty("annotators", pipe);
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    Annotation doc = new Annotation(text);
    pipeline.annotate(doc);

    // Get NER data, extract triples, and store the data
    extractData(doc);
  }


 /**
  * Extracts the triples, NER information, and stores it in triples map.
  * @param doc - annotated document
  */
  private void extractData(final Annotation doc) {
    
    // Loop over sentences in the document
    int sentenceNumber = 1;
    for (CoreMap sent : doc.get(CoreAnnotations.SentencesAnnotation.class)) {

      EntitiesList entities = new EntitiesList();

      // Traversing the words in the current sentence
      for (CoreLabel token : sent.get(TokensAnnotation.class)) {

        String word = token.get(TextAnnotation.class);
        String namedEntity = token.get(NamedEntityTagAnnotation.class);

        // Store NER if NER exists (also omitting ',' -> date error)
        if ((namedEntity.length() != 1) && !word.equals(","))
          entities.add(word, namedEntity, sentenceNumber);
          this.ner.add(word, namedEntity, sentenceNumber);
      }

      // Get the OpenIE triples for the sentence
      Collection<RelationTriple> relationTriples =
        sent.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

      // This is where we'll store the extracted triples
      List<Triple> triples = new ArrayList<>();

      // Create a sentence object
      Sentence sentence = new Sentence(sent.toString());
      sentence.addEntities(entities);

      // Store the triples
      for (RelationTriple t : relationTriples) {
        Triple triple = new Triple(t.subjectGloss(),
                                   t.relationGloss(),
                                   t.objectGloss(),
                                   t.confidenceGloss());
        triples.add(triple);
      }

      // Store sentences and associated Triples inside triples object
      sentence.setAllTriples(triples);
      this.network.add(sentenceNumber, sentence);

      // Increment sentence number
      sentenceNumber++;
    }
  }


  /**
   * Getter for Network.
   * @return network
   */
  Network getNetwork() {
    return this.network;
  }


  /**
   * Getter for named entity recognition.
   * @return named entity recognition
   */
  EntitiesList getNER() {
    return this.ner;
  }


  /**
   * Returns a string that is a representation of the Network and NER objects.
   * @return String representation of network and NER objects
   */
  @Override
  public String toString() {
    return this.network.toString() + this.ner.toString();
  }
}