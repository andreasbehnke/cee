cee
===

Content Extraction Engine

Goal of this project is to build a research client for internet information sources.
At this time only RSS based news sites are tracked and can be read in parallel within
a working set. In future other internet information sources should be integrated, like
twitter tweets, facebook accounts etc.

Run the Software:

Download latest release: https://github.com/andreasbehnke/cee/releases/latest
run java -jar [downloaded file name] to start web application.
Point your browser to http://localhost:8888

Building Instructions:

Type "mvn install" to build the project. After that you should be able to run the
current stable client (webbased, GWT) with the following command:

java -jar webreader/target/webreader-1.7-SNAPSHOT.war
