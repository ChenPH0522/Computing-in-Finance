# DataHandler
1st Github repo. From course project.

Project: Computing in Finance
-----------------------------------------------------

I. Basic Info
-----------------------------------------------------
Author: Penghao (Stanley) Chen
Date: 9/14/2017
Version: 1.0
E-mail: pc1819@nyu.edu
Development Environment: java 1.8.0_141

II. General Usage Notes
-----------------------------------------------------
This program reads financial data and performs different functions including sorting, inserting/deleting, and basic computing.

III. File List
-----------------------------------------------------
DataHandler.java	Reads data and perform different functions.
Record.java		Stores financial data entries.
DataHanderTest.java
RecordTest.java
prices.csv		Financial data
pricesBad.csv		For test-use only
pricesEmpty.csv		For test-use only
corrections.csv		Financial data that corrects corresponding data in prices.csv
correctionsTest.csv	For test-use only
retuls.txt		Results from the main method in DataHandler.java
README.txt		This file

IV. Design
-----------------------------------------------------
A. Class desgin

		|-----------------|		   |------------|
		|   DataHandler   |----- has ----->|   Record   |
		|-----------------|		   |------------|

B. DataHandler: to ensure data integrity, some of the fields and methods are made private.

                                        |-------------|
                                        | DataHandler |
                                        |-------------|
                                               |
           ----------------------------------------------------------------------------
           |                       |                    |           |                 |
    |-------------|        |---------------|    |---------------|   |          |------------------------|
    | removePrice |        | loadPriceData |    | correctPrices |   |          |   getPrices            |
    |    clear    |        |---------------|    |---------------|   |          |   computeMax           |
    |-------------|          /      \              /       \        |          |   computeAverage       |
                          call      call         call      call     |          |   computeMovingAverage |
                          /            \         /            \     |          |------------------------|
                     |------|         |----------|           |-------------|     /              |
                     | sort |         |   load   |           | insertPrice |    /              call
                     |------|         |----------|           |-------------|  call              |
                     /      \                                       \         /             |--------|
                  call      call                                   call      /              | toDate |
                  /            \                                      \     /               |--------|
           |-----------|   |------------|                         |--------------|
           | quickSort |   | bubbleSort |                         | isDateSorted |
           |-----------|   |------------|                         |--------------|
             /      \                |
           call     call             |
           /          \              |
    |-------------|  |-----------|   |
    | choosePivot |  | partition |  call
    |-------------|  |-----------|   |
                              |      |
                            call     |
                              |      |
                            |----------|
                            |   swap   |
                            |----------|

The following fields/methods in DataHandler.java are private:
	field:		data
	method:		quickSort
			choosePivot
			partition
			bubbleSort
			swap
			isDateSorted
			toDate

C. Record: contains only getters and setters. All fields are private to ensure data integrity.

V. Tests
-----------------------------------------------------
When testing private methods/field, change private methods/field to public/protected, do the tests, then change them back to private. 
The tests are based on the original prices.csv file (date ascendingly sorted) and other sample files created.
The paths are absolute. When running in a different environment, remember to change the paths accordingly.

VI. How To Run
-----------------------------------------------------
A. General Guidelines
There is a main method in DataHandler.java. Simply run the main method, and the results.txt will be created.
Several things to note:
	1. The financial data file must be strictly in the same format as the prices.csv file.
	2. The input and output file paths in the main method are absolute. When running in a different environment, remember to change them accordingly. Otherwise there will be an IOException.

B. Sample Result. Using the default financial data, the result should be:

The prices of SPY between 08/15/2004 and 08/20/2004 are:
94.42
94.95
95.92
95.64
96.32

The Average Price of SPY between 08/15/2004 and 09/15/2004 is: 97.09

The Maximum Price of SPY between 04/15/2004 and 06/15/2004 is: 99.77

The Moving Average of SPY between 08/15/2004 and 09/15/2004 for WindowSize 10 is:
96.04
96.23
96.43
96.54
96.79
96.93
97.17
97.36
97.48
97.65
97.82
98.10
98.24

VII. Limitations and Further development.
-----------------------------------------------------
1. This program will fail if the financial dataset is in any other format.
2. This program has only two sorting methods and two sorting keywords. It cannot sort by an unknown method or keyword.
3. Some minor code redundancy.
4. Didn't handle all exceptions.
5. Not well-designed. Not generic.
I will focus on improving those aspects in the future.