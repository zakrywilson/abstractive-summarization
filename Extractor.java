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
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;


/**
 * Handles extracting information from a document:
 * Triples: stored in Network.
 * Named entity information: stored in EntitiesList.
 */
class Extractor {


  /** An instance of the Network containing all metadata for text */
  private Network network = new Network();


  /** Contains all of named entity recognition information in LinkedHashMap */
  private EntitiesList ner = new EntitiesList();


  /** The input file containing the text to be processed */
  private String inputFile = null;


  /** Default path for input files: should be kept in ./resources/ */
  private static final String PATH = "./resources/";


 /**
  * Constructor.
  * Reads in file, processes it, extracts triples, prints them to standard
  * out and (optionally) writes data to new file (original-meta.txt).
  * @param document - file containing the input text
  * @param writeToFile - determines if output will be written to file
  */
  Extractor(final String document, final boolean writeToFile) {

    // Set original file
    this.inputFile = document;

    // Read and store file
    String text = FileManager.getText(document, PATH);

    // Extract the triples
    boolean success = processText(text);

    // Write extracted information to file if successful
    if (success && writeToFile) writeToFile();
  }


 /**
  * Extracts triples and NER, stores the information in separate maps, prints
  * the information to standard out and (optionally) writes data to file.
  * @param text - text that is to be processed
  * @return true if processing was successful
  */
  private boolean processText(final String text) {

    // Ensure there is text to process
    if (text == null) {
      System.err.println("ERROR: text was null.");
      return false;
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

    return true;
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

    } // Finished extracting information

    System.out.println("Information extracted");
  }


  /**
   * Writes extracted data to file.
   */
   private void writeToFile() {

     // Check if the input file is valid
     if (inputFile == null || !inputFile.endsWith(".txt")) {
       System.err.println("ERROR: invalid file.");
       return;
     }

     // Create new file to write to
     File file = new File(PATH + inputFile.replace(".txt", "-meta.txt"));
     if (!FileManager.thoroughlyCreate(file)) return;

     // Start writing
     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {

       // Write network and ner to file
       writer.write(this.network.toString());
       writer.write(this.ner.toString());

     } catch (IOException e) {
       System.err.println("ERROR: unable to write to file.");
       return;
     }

     System.out.println("Information written to file");
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