Compiling and running
---------------------

We programmend in Java 8 and used Maven as our build system. To download all dependencies and compile, run:
mvn package

Then to run, execute:
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar

This will print the available command line arguments and how to use them, which we hope are mostly self explanatory.

An important thing to note is we run the algorithms in a 2 step process: first we use "sort" to generate lists of files to process, then we run the algorithms using these file lists. The motivation behind this is explained in our report. An example workflow might look like this:

# generate file lists in "sort" folder
mkdir sort
cd sort
java -jar ../target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar sort /path/to/files application/octet-stream text/html image/gif application/xml
cd ..
# generate bfa.json
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar bfa sort
# generate bfd-I-a.json, bfd-I-b.json, bfd-I-c.json
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar bfd sort
# generate bfc.json
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar bfc sort
# generate fht.json based on first 4 bytes
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar fht sort 4
# generate fht.json based on first 8 bytes
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar fht sort 8
# generate fht.json based on first 16 bytes
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar fht sort
# now we want to try classifying unknown files
mkdir octet-stream-only
cp sort/application\;octet-stream octet-stream-only
# determine whether unknown files are most like text/html, image/gif, or application/xml according to BFA
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar bfaDetect octet-stream-only
# determine which unknown types are most similar to each other according to FHT
# first 4 bytes, assurance cutoff=0.9
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar fhtDetect octet-stream-only 0.9 4
# first 8 bytes, assurance cutoff=0.9
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar fhtDetect octet-stream-only 0.9 8
# first 16 bytes, assurance cutoff=0.9
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar fhtDetect octet-stream-only 0.9 16
# generate diversity.json
java -jar target/mime-diversity-1.0-SNAPSHOT-jar-with-dependencies.jar diversity /path/to/files

Important files and folders
---------------------------

tika-mimetypes.xml - our modified Tika fingerprint file
tika-mimetypes.diff - diff of our tika-mimetypes.xml compared to the one in Tika 1.11
vis - Contains d3 visualizations. Hover over the pie slice, line, etc to see tooltips.
vis-extra-credit - Extra credit visualization. Must open in Firefox, not Chrome.
json - Contains the JSON files, most of which are used by visualizations. We tried to use d3.json() to load them but ran into cross-site restriction issues, so we ended up copy-pasting the JSON into the visualization HTML, unfortunately.
	bfa.json - used by vis/bfa.htm (Part 4)
	bfd-I-a.json, bfd-I-b.json, bfd-I-c.json - used by vis/bfd1.htm (Part 5a, 5b for MIME types other than application/octet-stream)
	bfaDetect/Detected_Mime_type_count.json - used by vis/bfd2.htm (Part 5a for application/octet-stream)
	bfaDetect/File_and_mimetype.json - matches application/octet-stream files to the most similar of 14 MIME types, according to BFA (Part 5a)
	bfaDetect/File_and_alevel.json - contains assurance level of those MIME type matches (Part 5a)
	bfc.json - used by vis/bfc.htm (Part 5c)	
	fht.json - used by vis/fht.htm (Part 6)
	fhtDetect/*.json - Groups similar application/octet-stream files together using FHT. First number is assurance cutoff and second number is number of bytes per file to compare. Used to create the final tika-mimetypes.xml. (Part 7a)
	diversity.json - Used by diversity-after.htm and diversity-after-bar.htm (Part 7d)
	Extra_credit_part_1/EditValue.json - Contains Edit Distance
	Extra_credit_part_1/similarity_scores.txt - Contains Jaccard similarity 
	Extra_credit_part_1/Cosine22.json - Contains cosine distances
