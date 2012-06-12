This is a tool for annotating alignments by Soichiro Hayashi (modified by Graham Neubig).

----- Running ------

** 0. Prepare

In order to run the program, you must have Java installed.
If you do not have java, you can get it from http://www.java.com.

** 1. Run the program

From your file browser, double click on "AlignmentTool.jar".

Or, if you use the command line, run:
$ java -jar AlignmentTool.jar

** 2. Open the files

Click "Open Source" and select the source sentence file, "Open Target" and select the target sentence file.
If you already have some alignments, you can also click "Open Alignments" and select the alignment file.

Then click "Load" to load the sentences.

** 3. Annotate the files

For each pair of words that are aligned, click on the corresponding box.
When you have finished annotating a sentence, click on the orange arrow in the lower corner to go to the next sentence.

** 4. Save the annotations

When you are done annotating all the sentences, click the "Save" button and choose a file to save the alignments to.
If you cannot finish annotating all sentences in a single sitting, you can save half-way through the file, then resume where you left off by opening the saved alignments at step 2.

----- Building ------

To build the program, you must have Java and Apache Ant installed.
Once these are both prepared, run

$ ant

in the top directory, and the program should be built succesfully.
