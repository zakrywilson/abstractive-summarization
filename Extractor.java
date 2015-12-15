
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

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


/**
 * Handles extracting information from a document:
 * <p>
 *   (1) Basic semantic units: stored in Network
 *   (2) Named entity information: stored in NamedEntities
 * </p>
 */
public class Extractor {


  /**
   * This contains the original sentence (key) and the BSU
   * objects containing information about each sentence.
   */
  private Network network = new Network();

  /**
   * Contains all of the named entity recognition information in LinkedHashMap.
   */
  private NamedEntities ner = new NamedEntities();

  /**
   * The input file containing the text to be processed.
   */
  private String inputFile = null;

  /**
   * Default path for input files: should be kept in ./resources/
   */
  private static final String PATH = "./resources/";

 /**
  * Constructor
  * <p>
  *   Reads in file, processes it, extracts triples, prints them to standard
  *   out and (optionally) writes data to new file (original-bsu.txt).
  * </p>
  * @param document - name of the file containing the input text
  * @param writeToFile - determines if output will be written to file
  */
  protected Extractor(final String document, final boolean writeToFile) {
    
    // Set original file
    this.inputFile = document;

    // Read and store file
    String text = getText(document);

    // Extract the triples
    boolean success = processText(text);

    // Write extracted information to file if successful
    if (success && writeToFile) writeToFile();
  }


 /**
  * Reads text from input file and returns it
  *
  * @param document - the document containing text to process
  * @return text - if there was an error, text will be equal to null
  */
  private static String getText(final String document) {

    // Check for resources directory
    File dir = new File(PATH);
    if (!dir.exists()) {
      if (!dir.mkdir()) {
        System.err.println("ERROR: failed to create 'resources' directory.");
      }
      return null;
    }

    // Create file for reading
    File file = new File(PATH + document);
    List<String> lines = null;

    // Read lines
    try {
      lines = Files.readAllLines(file.toPath());
    } catch (IOException e) {
      System.err.println("ERROR: unable to read file: " + document);
    }

    // Append lines to one String
    StringBuilder text = null;
    if (lines != null) {
      text = new StringBuilder();
      for (String line : lines) {

        // Remove all unnecessary whitespace
        line = line.trim();

        // Disregard whitespace lines and empty strings
        if (line.length() == 0) {
          continue;
        }

        // Guarding against bad punctuation
        if (line.matches(".*\\p{Punct}")) {
          text.append(line);
          text.append(" ");
        } else {
          text.append(line);
          text.append(". ");
        }
      }
    }
    return (text == null) ? null : text.toString();
  }


 /** 
  * Extracts triples and NER, stores the information in seperate maps, prints
  * the information to standard out and (optionally) writes data to file.
  *
  * @param text - text that is to be processed
  * @return true - if processing was successful
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

    // Store coreference resolution data
//    computeCoref(doc, props);

    // Get NER data, extract triples, and store the data
    extractData(doc, props);

    return true;
  }


 /**
  * Computes coreference resolution and substitues the anaphoras out
  *
  * @param doc - annotated document
  * @param props - properties
  */
  private void computeCoref(final Annotation doc, final Properties props) {

    // Create link graph for coreference resolution
    Map<Integer, CorefChain> graph = doc.get(CorefChainAnnotation.class);
    Collection<CorefChain> corefChains = graph.values();

    System.out.println("\n***** PRINTING COREFERENCE RESOLUTION INFORMATION *****\n");

    for (CorefChain corefChain : corefChains) {
      // System.out.println(corefChain.getMentionsWithSameHead().toString());
      System.out.println("--------------------------------------------");

      for (CorefChain.CorefMention corefMention : corefChain.getMentionsInTextualOrder()) {
        System.out.println("SENT NUM: " + corefMention.sentNum);
        System.out.println("TO STRING: " + corefMention.toString());
      }
      System.out.println("MENTIONS: " + corefChain.getMentionsInTextualOrder());
      // System.out.println(corefChain.getMentionMap());
      // System.out.println(corefChain.toString());
    }
    System.out.println("\n ***** END *****\n");
    // graph.toString();
    // System.out.println("GRAPH:\n" + graph);
  }


 /**
  * Extracts the triples, NER information, and stores it in bsus map
  *
  * @param doc - annotated document
  * @param props - properties
  */
  private void extractData(final Annotation doc, final Properties props) {
    
    // Loop over sentences in the document
    int sentenceNumber = 1;
    for (CoreMap sent : doc.get(CoreAnnotations.SentencesAnnotation.class)) {

      // Traversing the words in the current sentence
      for (CoreLabel token : sent.get(TokensAnnotation.class)) {
        // Text of the token
        String word = token.get(TextAnnotation.class);
        // POS tag of the token
        String pos = token.get(PartOfSpeechAnnotation.class);
        // NER label of the token
        String ne = token.get(NamedEntityTagAnnotation.class);
        // Store NER if NER exists (also omitting ',' -> date error)
        if ((ne.length() != 1) && !word.equals(",")) 
          this.ner.add(word, ne);
      }

      // This is the parse tree of the current sentence
      //Tree tree = sent.get(TreeAnnotation.class);

      // Get the OpenIE triples for the sentence
      Collection<RelationTriple> triples =
        sent.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

      // This is where we'll store the extracted triples
      List<BSU> bsus = new ArrayList<BSU>();

      // Create a sentence object
      Sentence sentence = new Sentence(sent.toString());

      // Store the triples
      for (RelationTriple triple : triples) {
        BSU bsu = new BSU(triple.subjectGloss(), 
                          triple.relationGloss(), 
                          triple.objectGloss(),
                          triple.confidenceGloss());
        bsus.add(bsu);
      }

      // Store sentences and associated BSUs inside triples object
      sentence.setAllBSUs(bsus);
      this.network.add(sentenceNumber, sentence);

      // Increment sentence number
      sentenceNumber++;

    } // Finished extracting information

    System.out.println("\ninformation extracted\n");
  }


  /**
   * Writes extracted data to file
   */
   private void writeToFile() {

     System.out.println("preparing to write to file...");
    
     // Check if the input file is valid
     if (inputFile == null || !inputFile.endsWith(".txt")) {
       System.err.println("ERROR: invalid file.");
       return;
     }

     // Create writer
     BufferedWriter writer = null;

     try {

       // Create new file to write to
       File file = new File(PATH + inputFile.replace(".txt", "-bsu.txt"));
       if (file.exists()) file.delete();
       file.createNewFile();
       writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
      
       // Write network and ner to file
       writer.write(this.network.toString());
       writer.write(this.ner.toString());

     } catch (IOException e) {

       System.err.println("ERROR: unable to write to file.");
       return;

     } finally {

       if (writer != null) {
         try {
           writer.close();
         } catch (IOException e) {
           System.err.println("ERROR: Unable to close writer resource for file");
         }
       }
     }

     System.out.println("\ninformation written to file\n");
 }


  /**
   * Getter for Network
   * @return network
   */
  protected Network getNetwork() {
    return this.network;
  }


  /**
   * Getter for named entity recognition
   * @return named entity recognition
   */
  protected NamedEntities getNER() {
    return this.ner;
  }


  /**
   * Returns a string that is a representation of the Network and NER objects
   * @return String representation of network and NER objects
   */
  @Override
  public String toString() {
    return this.network.toString() + this.ner.toString();
  }
}
