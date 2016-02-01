# Abstractive Summarization
Automatic abstractive summarization for news articles.

## How to run
1. Download repository.
2. Install directory:
    1. `> cd /path/to/abstractive-summarization`
    2. `> ./setup [path/to/target/directory]`
        1. Project will moved to the target directory.
        If no target directory is specified, project is installed in working directory.
        2. Stanford JARs will be downloaded into `lib` directory.
3. Run demo: `> ./demo [arguments]`
    * If no arguments are specified, an excerpt on Tolstoy's biography 
    (`./resources/article-tolstoy.txt`) will be summarized for the demo.
    * If any arguments are specified for `demo`, the default text file will be ignored.

### Optional arguments

`-h  or  --help .......... Help`

`-f  or  --file [filename] Path to file, containing body of text to be summarized`

`-m .................... Write metadata to file`

`-s .................... Write summary to file`

## How it works
1. Program reads in file.  
2. Extracts important semantic information and writes it to file.
    1. Extracts *semantic triples*
        * Example:`"Bob likes puppies more than cats."`  
        System extracts multiple triples
            * `[Bob | likes | puppies]`
            * `[Bob | likes | puppies more than cats]`
    2. Extracts named entities: `Bob -> Person`
3. Removes semantic information with low confidence scores.
4. Removes other problematic extracted triples based on a series of rules.
5. Removes sentences that were not assigned triples or had all of their triples 
removed.
6. Generates new sentences off of the remaining information.
7. Adds back in the time named entity information.
8. Performs formatting.
9. Displays summary.

## Notes: 
* This program still needs work, but the system *does* summarize a body of text.
* Text files are provided inside `resources`.
* By default, the summarized text is sent to standard out. 
* The meta-data (i.e., named entity information and triples) is written to a 
file: `originalfilename-meta.txt`.
* The summary is written to a file: `originalfilename-summary.txt`.
