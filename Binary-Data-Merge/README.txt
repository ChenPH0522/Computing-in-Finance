Project: Data Merge
-----------------------------------------------------


I. Basic Info
-----------------------------------------------------
Author: Penghao (Stanley) Chen
Date: 11/20/2017
Version: 1.0
Development Environment: Java 1.8.0_151


II. General Usage Notes
-----------------------------------------------------
The project builds a framework to merge multiple binary data sets of a certain format to one binary data set. One needs to note that the binary data sets to be read and merged are strictly restricted to a certain format, which will be introduced later. Basically, we are given some binary data set of some format, and what this project does is to 1) read the data sets and transform them to another format that we can easily read; 2) merge the transformed data sets to a single data set. The lesson behind this simple project is the Java I/O issue.


III. File List
-----------------------------------------------------
Package: dbReader
	PreProcessor.java
	DBReader.java			implements I_DBReader
	DBReaderProcessor.java		implements I_DBProcessor
	MergeAll.java

Tests for dbReader:
	Test_PreProcessor.java
	Test_DBReader.java
	Test_DBReaderProcessor.java
	Test_MergeAll.java
	Test_MergeAndJoin.java		tests taqDBReaders and dbReaderFramework packages

Package: dbReaderFramework
	DBManager.java
	I_DBReader.java
	I_DBProcessor.java
	I_DBClock.java
	MergeClock.java			implements I_DBClock
	TimeClock.java			implements I_DBClock

Package: taqDBReaders
	GZFileUtils.java
	ReadGZippedTAQQuotesFile.java
	ReadGZippedTAQTradesFile.java
	TAQQuotesDBReader.java		implements I_DBReader
	TAQTradesDBReader.java		implements I_DBReader

Other:
	README.txt
	DataFolder
          |-----outputFile
          |-----sampleTAQ
                   |-------quotes
                   |         |------20070620
                   |         |              IBM_quotes.binRQ		binary file
                   |         |              MSFT_quotes.binRQ		binary file
                   |         |------20070621
                   |                        IBM_quotes.binRQ		binary file
                   |                        MSFT_quotes.binRQ		binary file
                   |-------trades
                             |------20070620
                             |              IBM_quotes.binRT		binary file
                             |              MSFT_quotes.binRT		binary file
                             |------20070621
                                            IBM_quotes.binRT		binary file
                                            MSFT_quotes.binRT		binary file


IV. Design
-----------------------------------------------------
A. Major Class Design
                                                                 |---------------|
                                                          ---<---|  I_DBReader** |<--- input Binary Data
                                                         /       |---------------|
                                                        /
                       |-------------|    |-------------|       |------------------|
output binary data <---|  MergeAll*  |<---|  DBManager  |---<---|  I_DBProcessor** |
                       |-------------|    |-------------|       |------------------|
                                                        \
                                                         \       |--------------|
                                                          ---<---|  I_DBClock** |
                                                                 |--------------|
* Only for dbReader package
** In the following description, I_DBReader, I_DBProcessor and I_DBClock refer to both the interface and any class implementing them. DBReader specifically referes to the DBReader class in the dbReaders package.


B. Original Binary Data File
The original binary data is of the following format, starting from the 1st bit:
1 Integer, representing how many seconds from 1/1/1970 0:00 to 0:00 of the trading day
1 Integer, representing how many records are contained in the current binary data file
n Integers, each Integer representing how many milliseconds from 0:00 of the trading day to the trading time of the trading day
n Integers, each Integer representing the executed order size in one trade
n Floats, each Float representing the price at which the trade is executed

Graphically, the data is arranged as follows:
	|-1 int-|-1 int-|-------n ints---------|--------n ints--------|--------n floats--------|


C. Packages
There are three packages: dbReader, dbReaderFramework, taqDBReaders. Only the dbReader package is developed by the author. The other two packages were borrowed from other sources which will be provided in the references.

C.1 dbReaderFramework
The dbReaderFramework package builds the framework for binary data reading and processing. The class or interface names are pretty self-explanatary. Classes implementing I_DBReader read data. Classes implementing I_DBProcessor process data read by I_DBReader.

Two things need to be mentioned here. One is the I_DBClock interface and the two clock classes implementing it. The clock classes are used to generate the next sequenceNumber, which is the trading time converted into milliseconds in our current example, for readers to read. Without clocks, one needs to manually generate the next sequenceNumber to read, and peek all the first sequence number of many readers and decide whether or not to read them. The clock packs such functionality into one class and simplifies the code.

Another thing is the DBManager class. It's main functionality is to process data read by a list of I_DBReaders, using a list of I_DBProcessors, each processor correspond to one or more I_DBReaders. The order processing order is determined by a I_DBClock.

C.2 taqDBReaders
The taqDBReaders is one implementation of the dbReaderFramework and is relatively independent. This package reads binary data in the following manner:

         |-------------------|        |--------------------------|
         | TAQQuotesDBReader |        | ReadGZippedTAQQuotesFile |
         | TAQTradesDBReader |<-------| ReadGZippedTAQTradesFile |<-------- input binary data
         |-------------------|        |--------------------------|

Essentially, first the entire binary data is read into an intermediate class, ReadGZippedTAQQuotesFile or ReadGZippedTAQTradesFile. Then, whenever and I_DBReader needs data, it goes to the underlying ReadGZippedTAQQuotesFile or ReadGZippedTAQTradesFile to fetch the data. This is different from the implementation in the dbReader package, which we'll see later.

The GZFileUtils class is essentially a wraper class which wraps the decorating process of creating a DataInputStream and DataOutputStream.


D. dbReader package

D.1 PreProcessor
PreProcessor is created only to satisfy the requirement in the description of our example. This class takes in the original binary data sets, and transforms them to the following format, starting from the 1st bit:
1 Long, representing how many milliseconds from 1/1/1970 0:00 to the trading time
1 Short, the ID of the stock on which the trade is executed
1 Integer, the executed order size in the trade
1 Float, the price at which the trade is executed
... (repeats n times, n representing the number of records)

Graphically, the data is arranged as follows:
	| Long, Short, Integer, Float | Long, Short, Integer, Float | Long, Short, Integer, Float | ...

If the data is well-defined, and our reader class can directly read the data smoothly, this class is indeed not necessary.

D.2 DBReader
The DBReader class reads binary data defined in the above format. Apart from the data format, the DBReader class also differs from the TAQQuotesDBReader and TAQTradesDBReader in that the DBReader class reads binary file bit-by-bit. 

For example, for the taqDBReader package, as previously mentioned, the entire binary data is read into an intermediate class, ReadGZippedTAQQuotesFile or ReadGZippedTAQTradesFile. Then, whenever I_DBReader needs data, it goes to the underlying ReadGZippedTAQQuotesFile or ReadGZippedTAQTradesFile to fetch the data. In contrast, the DBReader class in the dbReader package opens an underlying DatasInputStream itself, and whenever it needs data, it utilize the DataInputStream to directly access the data on the hard drive. There is no intermediate class or memory involved.

D.3 DBReaderProcessor
The DBReaderProcessor merges data from different DBReaders together. One drawback of this class is that it is specifically devoted to the DBReader class. As a consequence, there is a not-so-elegant down casting in its processReader method.

D.4 MergeAll
The MergeAll class brings multiple parts together and executes the merging functionality. Basically it reads all files in a folder, find only files with the specified extension and generation number, and merge them group-wise using DBManager. Then loop again, do the reading and merging, until there is only one file with the specified extension left.

I must admit that this class does not have a very clean design. Its input and output folder is one same folder, which means that, after merging a bunch of binary data files to a single file, one needs to copy the output single file to the disired location, which is often annoying.

The reason why we opt for group-wise merging within each loop is that, for a very big database, reading all data sets together, even for each data set we only read a small number of bits, may cause overflow.


V. Java I/O
-----------------------------------------------------
Beneth the simple project is the Java I/O package, which is a very important topic in Java language. One may find useful to first get farmiliar with the Java I/O package and then work on the project.


VI. Limitations and Further Development
-----------------------------------------------------
There are many things can be improved in this project, especially for the dbReaders package. Specifically,
1. The structure of classes within the dbReader package is not very elegant. Many redundancy and hard coding.
2. The "same input and output folder" issue for the MergeAll class.
3. Not extendable. Classes are very narrowly defined for this specific project and cannot be reused anywhere else.


VII. References
-----------------------------------------------------
The dbReaderFramework package, the taqDBReaders package and the Test_MergeAndJoin unit test are credited to Professor Lee Maclin who teaches the Computing in Finance course at New York University in the fall of 2017.
