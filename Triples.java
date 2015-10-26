import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.OpenIE;

import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.DeterministicCorefAnnotator;
import edu.stanford.nlp.pipeline.HybridCorefAnnotator;
import edu.stanford.nlp.pipeline.ParserAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ie.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sequences.ColumnDocumentReaderAndWriter;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;


/**
 * Handles extracting basic semantic units from a document
 */
public class Triples {


  /**
   * This map is what we're storing all the extracted BSU information in. It's
   * key is the sentence and it's value is a string list containing all
   * the triples produced from the given sentence.
   */
  private Map<String, List<String>> bsus = null;
  private Map<String, String> ner = null;
  private String inputFile = null;


 /**
  * Default Constructor
  */
  private Triples() {
    // not used
  }


 /**
  * Constructor
  * Reads in file, processes it, extracts triples, prints them to standard
  * out and (optionally) writes data to new file (original-bsu.txt).
  * 
  * @param document - name of the file containing the input text
  * @param writeToFile - determines if output will be written to file
  */
  protected Triples(String document, boolean writeToFile) {
    this.inputFile = document;                // Set original file
    String text = getText(document);          // Read and store file
    doCoreferenceSubstitution(text);          // Do coreference substitution
    boolean success = getTriples(text);       // Extract the triples
    if (success) { 
      printTriples(writeToFile);
      done();
    }
    else failure();
  }


 /**
  * Reads text from input file and returns it
  * @param document
  * @return text
  */
  private static String getText(String document) {
    File file = new File(document);
    String text = null;

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      text = reader.readLine();
    } catch (IOException e) {
      System.out.println("ERROR: unable to read file: " + document);
    }
    return text;
  }


 /**
  * Performs coreference resolution and substitution
  * @param text
  */
  private void doCoreferenceSubstitution(String text) {
    System.out.println("inside doCoreferenceSubstitution()");
  }


 /**
  * Extracts triples from text, prints them to standard out, and (optionally)
  * writes them back to file
  * @param text
  * @return true - if processing was successful
  */
  private boolean getTriples(String text) {

    // If something went wrong with getting the text, stop processing
    if (text == null)
      return false;

    // Create the Stanford CoreNLP pipeline
    Properties props = new Properties();
    props.setProperty("annotators", 
                      "tokenize, ssplit, pos, parse, depparse, " + 
                      "lemma, ner, dcoref, natlog, openie");
                      // "tokenize, ssplit, pos, depparse, 
                      // lemma, natlog, openie");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    Annotation doc = new Annotation(text);
    pipeline.annotate(doc);

    //////////////////////////////////////////////////////////////////////////
    // COREFERENCE RESOLUTION ////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    List<CoreMap> sentences = doc.get(SentencesAnnotation.class);
    this.ner = new HashMap<String, String>();
    for(CoreMap sentence: sentences) {
      // traversing the words in the current sentence
      // a CoreLabel is a CoreMap with additional token-specific methods
      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
        // this is the text of the token
        String word = token.get(TextAnnotation.class);
        // this is the POS tag of the token
        String pos = token.get(PartOfSpeechAnnotation.class);
        // this is the NER label of the token
        String ne = token.get(NamedEntityTagAnnotation.class);
        if (ne.length() != 1) {
          ner.put(word, ne);
        }
      }

      // this is the parse tree of the current sentence
      // Tree tree = sentence.get(TreeAnnotation.class);

      // this is the Stanford dependency graph of the current sentence
      // SemanticGraph dependencies = sentence.get(
        // CollapsedCCProcessedDependenciesAnnotation.class);
    }

    //////////////////////////////////////////////////////////////////////////
    // EXTRACTING TRIPLES ////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    // Initialize the Hashmap for storing triples
    this.bsus = new HashMap<String, List<String>>();

    // Loop over sentences in the document
    for (CoreMap sent : doc.get(CoreAnnotations.SentencesAnnotation.class)) {

      // Get the OpenIE triples for the sentence
      Collection<RelationTriple> triples = 
        sent.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

      // Using the clause splitter
      List<SentenceFragment> clauses = 
        new OpenIE(props).clausesInSentence(sent);

      // This is where we'll store the extracted triples
      List<String> valueBSU = new ArrayList<>();

      // Print the triples
      String keySentence = clauses.toString();
      for (RelationTriple triple : triples) {
        valueBSU.add("[" + triple.subjectGloss()  + " | " +
                           triple.relationGloss() + " | " +
                           triple.objectGloss()   + "]");
      }
      this.bsus.put(keySentence, valueBSU);
    }
    System.out.print("\n---------------------\n");
    System.out.print("\ninformation extracted");
    return true;
  }


 /**
  * Prints triples to standard out; writes them to file if writeToFile is true
  * @param writeToFile - determinds whether data should be written to file
  */
  private void printTriples(boolean writeToFile) {
    // Print out sentences and BSUs
    for (Map.Entry<String, List<String>> pair : this.bsus.entrySet()) {
      System.out.println("\n\nSENTENCE: " + pair.getKey());
      List<String> triples = pair.getValue();
      for (String triple : triples) {
        System.out.println("\tBSU: " + triple);
      }
    }
    // Print out NER
    for (Map.Entry<String, String> pair : this.ner.entrySet()) {
      System.out.println("NER: " + pair.getKey() + " -> " + pair.getValue());
    }
    // Write this all to file
    if (writeToFile) writeToFile();
  }


 /**
  * Writes extracted data to file
  */
  private void writeToFile() {
    
    if (inputFile == null || !inputFile.endsWith(".txt")) {
      System.out.println("ERROR: invalid file."); 
      return;
    }

    BufferedWriter writer = null;

    try {

      File file = new File(inputFile.replace(".txt", "-bsu.txt"));
      if (file.exists()) file.delete();
      file.createNewFile();
      writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
      
      // Write sentences and BSUs to file
      for (Map.Entry<String, List<String>> pair : this.bsus.entrySet()) {
        writer.write("SENTENCE: " + pair.getKey()); 
        writer.newLine();
        List<String> triples = pair.getValue();
        for (String triple : triples) {
          writer.write("     BSU: " + triple);
          writer.newLine();
        }
        writer.newLine();
      }

      // Write NER information to file
      for (Map.Entry<String, String> pair : this.ner.entrySet()) {
        System.out.println("NER: " + pair.getKey() + " -> " + pair.getValue());
        writer.write("NER: " + pair.getKey() + " -> " + pair.getValue());
        writer.newLine();
      }

    } catch (IOException e) {
      System.out.println("ERROR: unable to write to file."); return;
    } finally {
      if (writer != null) {
        try { writer.close(); } catch (IOException e) { /* who cares? */ }
      }
    }
    System.out.println("\ninformation written to file");
  }


 /**
  * Prints "done"
  */
  private void done() {
    System.out.println("\ndone.");
  }


 /**
  * Prints "failure"
  */
  private void failure() {
    System.out.println("\nfailure.");
  }


 /**
  * Main
  * Two configurations: file name and whether output data should be
  * written to file
  */
  public static void main(String[] args) throws Exception {
    String document = "nautilus.txt";
    boolean writeToFile = true;
    Triples triples = new Triples(document, writeToFile);
  }

}
