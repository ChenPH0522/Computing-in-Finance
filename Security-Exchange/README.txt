Project: Exchange and Order Books
-----------------------------------------------------

I. Basic Info
-----------------------------------------------------
Author: Penghao (Stanley) Chen
Date: 11/1/2017
Version: 1.0
Development Environment: 1.8.0_151


II. General Usage Notes
-----------------------------------------------------
The project builds a framwork for a security exchange. Several packages are included in the project, making the framework flexible to incorporate new features. Meanwhile, speed is the priority of an exchange. To promote speed, the project employes several data structures particularly suitable for such situation. There is also a trivial portion of hard coding, aiming to make the execution even faster.


III. File List
-----------------------------------------------------
(Major Classes)
Package: exchangeStructures
	Exchange
	Market
	Book				extends PriorityQueue, abstract
	BidBook				extends Book
	OfferBook			extends Book
Package: fills
	Comms
	Fill
Package: messages
	Cancel
	CancelRejected			extends Cancel
	Confirmation
	CancelationConfirmation		extends Confirmation
	RestingOrderConfirmation	extends Confirmation
Package: orderSpecs
	CliendId
	ClientOrder
	MarketId
	Price
	Quantity
	Side
Package: orderTypes
	Order
	RestingOrder			extends Order
	SweepingOrder			extends Order
	OrderQueue			extends LinkedHashMap
Package: util
	Pair

(Tests)
Package: exchangeStructures
	Test_Exchange
	Test_Market
	Test_BidBook
	Test_OfferBook
Package: messages
	Test_Cancel
Package: orderTypes
	Test_OrderQueue

(txt and others)
README.txt


IV. Design
-----------------------------------------------------
A. Major Class Design

                    |------------|
                    |  Exchange  |------pass-----------
                    |------------|                    |
                          |                           |
                         has                          |
                          |                           |                 |--------------------|
                    |------------|                |-------|             |       Fill         |
                    |   Market   |------pass------| Comms |-----has-----| Messages (package) |
                    |------------|                |-------|             |--------------------|
                          |                           |
                         has                          |
                          |                           |
                    |------------|                    |
                    |    Book    |------pass-----------
                    |------------|
                          |
                         has
                          |
                   |--------------|
                   |  OrderQueue  |
                   |--------------|
                          |
                         has
                          |
                    |------------|
                    |   Order    |
                    |------------|
                          |
                         has
                          |
                  |-----------------|
                  |   OrderSpecs    |
                  |    (package)    |
                  |-----------------|

B. Book
The Book class extends the PriorityQueue class in java in order to make the program efficient. To illustrate this, We can compare its performance with TreeMap. PriorityQueue has O(1) to peek the first element, O(logn) to add/remove an element, and O(n) to check if a particular element is present in the queue. As comparison, TreeMap takes O(logn) to get the first element, O(logn) to add/remove an element, and O(logn) to check if a particular element is present in the queue.

Both are very efficent, but PriorityQueue has a slight advantage in this situtation. When people place an order, the single most important thing is whether their order is executed (filled) or not. After it is filled or partially filled, the rest is not as important. Given this, it is extremely important to speed up the process when an order sweeps into the book at the market price.

When a sweeping order sweeps into the Book, the method peek() is called repeatedly utill the order is filled or becomes a resting order. It is exactly this repeated call of peek() lends PriorityQueue an advantage over TreeMap. LinkedList is another option, as it also takes O(1) to access to the first element, but being unmanaged and its disadvantage in searching make it not a good candidate for this project.

Admittedly, when a limit or stop order is placed in the market, PriorityQueue will potentially take more time than TreeMap, but in that case speed is not as important as for market order, therefore we can loose the requirement.

C. OrderQueue
The OrderQueue extends the LinkedHashMap class in java. LinkedHashMap is very efficient, taking O(1) to get/remove and check an element. However, this speed comes with a cost: memory. For this simple project, LinkedHashMap is probably a too expensive data structure to use. LinkedList might be a more proper choice.

The reason for such design is that LinkedHashMap makes cancel O(1) possible. For this project, cancelling an order happens at the Exchange level. In real application where internal memory is relatively cheap, LinkedHashMap may have an advantage: People managing the order book may want to directly cancel an order by its orderID. LinkedHashMap can reduce such operation to O(1).

V. Limitations and Further Development
-----------------------------------------------------
1. There are only two order types implemented in this project: sweeping order and resting order. Sweeping order can be viewed as paritially marketable order and limit order, depending on the price at which the order is placed. In real world, there are many other order types: fill-and-cancel, stop, block, etc. To incorporate those order types, we need to not only construct corresponding order classes, but also re-write the sweep methods in Bid/OfferBook.
2. As per this project, it's better to use LinkedList for OrderQueue rather than LinkedHashMap, which is as fast but takes more memory space.
3. This exchange is not synchronized. It is not thread safe. For an exchange, I definitely need to improve it in this aspect later.