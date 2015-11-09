# Abstractive Summarization
Automatic abstractive summarization for articles

## How to use
* Download [CoreNLP](http://nlp.stanford.edu/software/corenlp.shtml#Download) 
and [OpenIE](http://nlp.stanford.edu/software/openie.shtml#Download) from Stanford.edu.  
Be sure to download both code and modules JARs for CoreNLP and OpenIE.
* Clone repo.
* Put all four JARs in repo.
* Run `Manager.java`.

## How it works
* Reads in file and extracts text.  
* Extracts information, displays it to screen, and writes it to file.
* Example:`"Bob likes puppies more than cats."`
  * Extracts triples: `[Bob | likes | puppies]`
  * Extracts named entities: `Bob -> Person`

###### Notes: 
* **This program is not finished** – so far it only extracts the triples from the text.
* If you want change the input file, that can be changed in `Manager.java`.
* Store input text files in `resources/`.
* By default, output is sent to standard out and to a file: `<input_filename>-bsu.txt`.
