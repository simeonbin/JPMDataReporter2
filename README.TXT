========================
BUILD OUTPUT DESCRIPTION
========================

When you build an Java application project that has a main class, the IDE
automatically copies all of the JAR
files on the projects classpath to your projects dist/lib folder. The IDE
also adds each of the JAR files to the Class-Path element in the application
JAR files manifest file (MANIFEST.MF).

To run the project from the command line, go to the 
\JpmDataReporter\out\artifacts\JpmDataReporter_jar folder and type the following:

java -jar "JPMDataReporter.jar" 

To distribute this project, zip up the dist folder (including the lib folder)
and distribute the ZIP file.


I created a rudimentary User Interface. Two Buttons, 6 TextAreas and a couple other minor Java Swing widgets.
CSV type File jpmdata_input.txt contains the Financial Data like the one that follows.

foo, B, 0.50, SGP, 01 Mar 2018, 02 Mar 2018, 200, 100.25
bar, S, 0.22, AED, 02 Mar 2018, 03 Mar 2018, 450, 150.5
abc, S, 0.75, AUD, 02 Mar 2018, 03 Mar 2018, 150, 50.5
def, S, 1.22, GBP, 02 Mar 2018, 03 Mar 2018, 50, 25.4
jpm, B, 1.0,  USD,  03 Mar 2018, 06 Mar 2018, 400, 90.2
jpn, S, 0.14, CNY, 06 Mar 2018, 07 Mar 2018, 600, 30.3
jpl, B, 0.55, BGN, 09 Mar 2018, 10 Mar 2018, 700, 20.3
jpk, B, 0.32, SAR, 09 Mar 2018, 10 Mar 2018, 600, 30.3
jpo, S, 0.22, AED, 09 Mar 2018, 10 Mar 2018, 900, 40.4
jpk, S, 0.32, SAR, 09 Mar 2018, 10 Mar 2018, 800, 50.5
ghi, B, 1.07, EUR, 10 Mar 2018, 11 Mar 2018, 110, 25.7
abd, S, 0.017, RUB, 11 Mar 2018, 12 Mar 2018, 228, 10.5
abe, S, 0.75, AUD, 13 Mar 2018, 14 Mar 2018, 128, 110.5
abf, S, 0.74, CAD, 14 Mar 2018, 15 Mar 2018, 138, 120.5
abg, S, 0.14, CNY, 15 Mar 2018, 16 Mar 2018, 148, 130.5
abh, S, 0.99, CHF, 16 Mar 2018, 17 Mar 2018, 158, 140.5
abi, S, 0.04, CZK, 17 Mar 2018, 18 Mar 2018, 168, 150.5
abj, B, 1.22, GBP, 18 Mar 2018, 19 Mar 2018, 178, 160.5
abk, S, 0.27, ILS, 19 Mar 2018, 20 Mar 2018, 188, 170.5
abl, B, 0.015, INR, 20 Mar 2018, 21 Mar 2018, 198, 200.5
abm, S, 0.051, MXN, 21 Mar 2018, 22 Mar 2018, 218, 300.5
abn, S, 0.12, NOK, 22 Mar 2018, 23 Mar 2018, 318, 140.5
abo, B, 0.076, ZAR, 23 Mar 2018, 24 Mar 2018, 118, 199.5
abp, S, 0.27, TRY, 24 Mar 2018, 25 Mar 2018, 118, 10.7
abq, B, 0.69, NZD, 25 Mar 2018, 26 Mar 2018, 118, 133.6
abr, S, 0.32, BRL, 26 Mar 2018, 27 Mar 2018, 118, 288.0
abs, B, 0.0015, CLP, 27 Mar 2018, 28 Mar 2018, 118, 350.5
abt, S, 0.14, DKK, 28 Mar 2018, 29 Mar 2018, 118, 80.9
abu, B, 0.11, SEK, 29 Mar 2018, 30 Mar 2018, 118, 73.4
abv, S, 0.25, PLN, 30 Mar 2018, 31 Mar 2018, 118, 69.6
abw, B, 0.32, SAR, 30 Mar 2018, 31 Mar 2018, 118, 74.8
abx, S, 0.22, AED, 30 Mar 2018, 31 Mar 2018, 118, 30.3

When button 'View Data File' is pressed the contents are shown in the Text Area below the button.
When button 'Create Report' is pressed the rest of the TextAreas get filled.

I define a class for the Financial Data Object, with Columns:
Entity, BuySell, AgreedFx, Currency, InstructionSet, SettlementDate, Units, PricePerUnit
The class implements Comparable and contains, the getters and setters for above-mentioned fields.

I also read the File to a List of 'JPMDataObject' Objects.
I change the 'SettlementDate' to account for "Banking Holidays", for now only weekends.
I use Collections.sort to sort (descending order) the List of the DataObjects according to 'SettlementDate' column.
I compare Dates within the Objects in the List.

As just mentioned above, when button 'Create Report' is pressed the rest of the TextAreas get filled.
And all the info, Daily Totals, Rankings of Entities, gets logged in the Output.

Incoming USD$ Amount settled
Outgoing USD$ Amount settled
Incoming Ranking of Entities
Outgoing Ranking of Entities
Column Names from Financial Data CSV File {exactly same as fields of Class name 'JPMDataObject'}

The Ranking of Entities according to (Incoming or Outgoing) Amount required Sorting a Map by its Values. 
The Map is of type <String, Double> with String being the Entity, Double being the Total Incoming or Outgoing 
Value of said Entity.

I have also included a few sample JUnit Tests for JPMDataReporter. 
The Tests for JPMDataObject class are too obvious, for economy of space and time I omitted them.
They all can be found in the 'Test Packages' directory.
I also used the CSV file for lots of the debugging and testing of various cases.

To Summarize I would consider the following as the two highlights of the Project :

1st, Sorting (according to Settlement Dates) and Iterating of the List of JPMDataObject objects , 
so that one can total Incoming and Outgoing USD$ Amounts per Day.

2nd, Sorting (according to Value of the <K,V> pair) and Iterating of the Map of <String, Double> Entries, 
String being the Entity, Double being the Total Amount value for that Entity.
This accomplishes the Ranking of Entities based on Total Incoming and Outgoing Amounts.


Notes:

* If two JAR files on the project classpath have the same name, only the first
JAR file is copied to the lib folder.
* Only JAR files are copied to the lib folder.
If the classpath contains other types of files or folders, these files (folders)
are not copied.
* If a library on the projects classpath also has a Class-Path element
specified in the manifest,the content of the Class-Path element has to be on
the projects runtime path.
* To set a main class in a standard Java project, right-click the project node
in the Projects window and choose Properties. Then click Run and enter the
class name in the Main Class field. Alternatively, you can manually type the
class name in the manifest Main-Class element.
