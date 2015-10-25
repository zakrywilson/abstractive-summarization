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

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.lang.StringBuilder;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Triples {

  /**
   *  Reads in the document, extracts the triples, and prints them
   */
  public static void main(String[] args) throws Exception {
    String document = "tolstoy.txt";
    String text = getText(document);
    boolean success = getTriples(text);

    if (success) 
      System.out.println("done."); 
    else 
      System.out.println("failure.");
  }

  /**
   *  Opens the file, reads the text, and returns it in a String
   */
  private static String getText(String document) {
    File file = new File(document);
    StringBuilder text = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      text.append(reader.readLine());
    } catch (IOException ioe) {
      System.out.println("ERROR: unable to read file: " + document);
    }

    return text.toString();
  }

  /**
   *  Extracts triples from text and prints them in [a | b | c] format
   */
  private static boolean getTriples(String text) {

    if (text == null)
      return false;

    // Create the Stanford CoreNLP pipeline
    Properties props = new Properties();

    props.setProperty("annotators", 
                      "tokenize, ssplit, pos, parse, depparse, lemma, " + 
                      "ner, dcoref, natlog, openie");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    Annotation doc = new Annotation(text);
    pipeline.annotate(doc);

    // Loop over sentences in the document
    for (CoreMap sent : doc.get(CoreAnnotations.SentencesAnnotation.class)) {

      // Get the OpenIE triples for the sentence
      Collection<RelationTriple> triples = 
        sent.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

      // Print the triples
      System.out.println("Printing triples...");
      for (RelationTriple triple : triples) {
        System.out.println( /* triple.confidence + "" + */ "[" + 
            triple.subjectLemmaGloss() + " ; " +
            triple.relationLemmaGloss() + " ; " +
            triple.objectLemmaGloss() + " ]" );
      }
    }
    return true;
  }
}
