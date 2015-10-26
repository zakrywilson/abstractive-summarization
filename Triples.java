import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.OpenIE;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.DeterministicCorefAnnotator;
import edu.stanford.nlp.pipeline.ParserAnnotator;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.sequences.ColumnDocumentReaderAndWriter;

import edu.stanford.nlp.ie.*;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;


public class Triples {

  /*
   * This map is what we're storing all the extracted information in. It's
   * key is the sentence and it's value is a string list containing all
   * the triples produced from the given sentence.
   */
  private Map<String, List<String>> bsus = null;
  private String inputFile = null;


  protected Triples(String document, boolean writeToFile) {
    this.inputFile = document;                // Set original file
    String text = getText(document);          // Read and store file
    boolean success = getTriples(text);       // Extract the triples
    if (success) { 
      printTriples(writeToFile);
      done();
    }
    else failure();
  }


  private void doCoreferenceSubstitution(String document) {
    System.out.println("inside doCoreferenceSubstitution()");
  }


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


  private boolean getTriples(String text) {

    // If something went wrong with getting the text, stop processing
    if (text == null)
      return false;

    // Create the Stanford CoreNLP pipeline
    Properties props = new Properties();

    props.setProperty("annotators", 
                      "tokenize, ssplit, pos, parse, depparse, " + 
                      "lemma, ner, dcoref, natlog, openie");
                      // "lemma, natlog, openie");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    Annotation doc = new Annotation(text);
    pipeline.annotate(doc);

    // Initialize the Hashmap for storing triples
    bsus = new HashMap<String, List<String>>();

    // Loop over sentences in the document
    System.out.println("Extracting triples...");
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
        valueBSU.add("[" + triple.subjectLemmaGloss()  + " | " +
                           triple.relationLemmaGloss() + " | " +
                           triple.objectLemmaGloss()   + "]");
      }
      this.bsus.put(keySentence, valueBSU);
    }
    System.out.println("\ntriples successfully extracted.");
    return true;
  }


  private void printTriples(boolean writeToFile) {
    for (Map.Entry<String, List<String>> pair : this.bsus.entrySet()) {
      System.out.println("\n\nSENTENCE: " + pair.getKey());
      List<String> triples = pair.getValue();
      for (String triple : triples) {
        System.out.println("\tBSU: " + triple);
      }
    }
    if (writeToFile) writeToFile();
  }


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

    } catch (IOException e) {
      System.out.println("ERROR: caugh one of those cool IOExceptions.");
    } finally {
      if (writer != null) {
        try { writer.close(); } catch (IOException e) { /* who cares? */ }
      }
    }
  }


  private void done() {
    System.out.println("done.");
  }


  private void failure() {
    System.out.println("failure.");
  }


  public static void main(String[] args) throws Exception {
    String document = "tolstoy.txt";
    boolean writeToFile = true;
    Triples triples = new Triples(document, writeToFile);
  }
}
