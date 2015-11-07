import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.OpenIE;

import edu.stanford.nlp.dcoref.CorefChain;
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
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author Zach Wilson
 * Handles extracting basic semantic units from a document
 */
public class Triples {


  /**
   * This map is what we're storing all the extracted BSU information;
   * its key is the sentence and it's value is a string list containing all
   * the triples produced from the given sentence.
   */
  private Map<String, List<String>> bsus = null;

  /**
   * This map contains the named entiy recognition information by the word
   * being the key and the associated NER tag being the value.
   */
  private Map<String, String> ner = null;

  /**
   * The input file containing the text to be processed.
   */
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
    
    // Set original file
    this.inputFile = document;

    // Read and store file
    String text = getText(document);

    // Extract the triples
    boolean success = processText(text);

    // Display results if successful
    if (success) {
      printTriples(writeToFile);
      done();
    } else {
      failure();
    }
  }


 /**
  * Reads text from input file and returns it
  * @param document
  * @return text - if there was an error, text will be equal to null
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
  * Extracts triples and NER, stores the information in seperate maps, prints
  * the information to standard out and (optionally) writes data to file.
  * @param text
  * @return true - if processing was successful
  */
  private boolean processText(String text) {

    // Ensure there is text to process
    if (text == null) {
      System.out.println("ERROR: text was null.");
      return false;
    }

    // Create the Stanford CoreNLP pipeline
    Properties props = new Properties();
    props.setProperty("annotators", 
                      "tokenize, ssplit, pos, parse, depparse, " + 
                      "lemma, ner, dcoref, natlog, openie");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    Annotation doc = new Annotation(text);
    pipeline.annotate(doc);

    computeCoref(doc);

    extractData(doc);
    return true;
  }


 /**
  * Computes coreference resolution and substitues the anaphoras out 
  */
  private void computeCoref(Annotation doc) {
    // Create link graph for coreference resolution
    Map<Integer, CorefChain> graph = doc.get(CorefChainAnnotation.class);
    Collection<CorefChain> corefChains = graph.values();
    for (CorefChain corefChain : corefChains) {
      // System.out.println(corefChain.getMentionsWithSameHead().toString());
      System.out.println(corefChain.getMentionMap().toString());
      // System.out.println(corefChain.toString());
    }
    // graph.toString();
    // System.out.println("GRAPH:\n" + graph);
  }


 /**
  * Extracts the triples, NER information, and stores it in bsus map
  */
  private void extractData(Annotation doc) {
    
    // Initialize the Hashmaps for storing triples and NER
    this.bsus = new HashMap<String, List<String>>();
    this.ner = new HashMap<String, String>();

    // Loop over sentences in the document
    for (CoreMap sent : doc.get(CoreAnnotations.SentencesAnnotation.class)) {

      // Traversing the words in the current sentence
      for (CoreLabel token: sent.get(TokensAnnotation.class)) {
        // Text of the token
        String word = token.get(TextAnnotation.class);
        // POS tag of the token
        String pos = token.get(PartOfSpeechAnnotation.class);
        // NER label of the token
        String ne = token.get(NamedEntityTagAnnotation.class);
        // Store NER if NER exists (also omitting ',' -> date error)
        if ((ne.length() != 1) && !word.equals(",")) 
          this.ner.put(word, ne);
      }

      // This is the parse tree of the current sentence
      // Tree tree = sent.get(TreeAnnotation.class);

      // Get the OpenIE triples for the sentence
      Collection<RelationTriple> triples = 
        sent.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

      // Using the clause splitter
      List<SentenceFragment> clauses = 
        new OpenIE(props).clausesInSentence(sent);

      // This is where we'll store the extracted triples
      List<String> valueBSUs = new ArrayList<>();

      // Store the triples
      String keySentence = sent.toString();
      for (RelationTriple triple : triples) {
        valueBSUs.add("[" + triple.subjectGloss()  + " | " +
                            triple.relationGloss() + " | " +
                            triple.objectGloss()   + "]");
      }

      // Store sentences and associated BSUs inside map
      this.bsus.put(keySentence, valueBSUs);

    } // Finished extracting information

    System.out.print("\ninformation extracted");
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
    
    // Check if the input file is valid
    if (inputFile == null || !inputFile.endsWith(".txt")) {
      System.out.println("ERROR: invalid file."); 
      return;
    }

    BufferedWriter writer = null;

    try {

      // Create new file to write to
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
    String document = "cr.txt";
    boolean writeToFile = true;
    Triples triples = new Triples(document, writeToFile);
  }

}
