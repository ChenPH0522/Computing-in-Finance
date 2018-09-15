Project: Middleware Implementation
-----------------------------------------------------

I. Basic Info
-----------------------------------------------------
Author: Penghao (Stanley) Chen
Version: 2.0
Development Environment: java 1.8.0_151
			 Apache ActiveMQ 5.15.2


II. General Usage Notes
-----------------------------------------------------
This pakcage implements the following function: The simulation manager (SimuManager) is created and needs to price an option. It broadcast the simulation request via an ActiveMQ topic to simulation slaves (SimuSlave). Simulation slaves receive the simulation requests, simulate stock paths, calculate the payoffs and then send the payoffs back to the simulation manager via an ActiveMQ queue. The simulation manager receives the payoffs, calculate the option price, then decide whether it needs more simulation results. If it does, then send another simulation request and does the loop.


III. File List
-----------------------------------------------------
(Major classes)
SimuManager.java	listener: ResultListener
ResultListener.java	serves to SimuManager
SimuSlave.java		listener: RequestListener
RequestListener.java	serves to SimuSlave

(Referenced Library)
activemq-all-5.15.2.jar

(Tests)
Test_SimuManager_ResultListener.java
Test_StringSplit.java

(Others)
README.txt


IV. Design
-----------------------------------------------------
A. Major Class Desgin
                                      (send requests)           |-----------------|
                              --------------------------------->| RequestListener |
                             /            (Topic)               |-----------------|
                            /                                            \         \
                           /                                              \         \
             |-------------|                                               \         |-----------|
             |             |                                                -------->|           |
             |             |                       Active MQ                (update) |           |
             | SimuManager |                    (Hidden: Broker)                     | SimuSlave |
             |             | (update)                                                |           |
             |             |<-------                                                 |           |
             |-------------|        \                                                |-----------|
                            \        \                                              /
                             \        \                                            /
                             |----------------|          (send results)           /
                             | ResultListener |<----------------------------------
                             |----------------|             (Queue)

B. Listeners
To promote code readability, Unlike examples given in class, listeners are distilled as separate classes instead of being embedded in Server/Client. Each listener has a private field that directly links the listener to the server/client that it serves to. In this way, as listener collects messages, it can directly modify fields under the server/client.

C. Synchronization
This task is multi-threading and therefore we need to ensure some of the processes are thread-safe. Taking SimuManager as an example. ResultListener initiate a saparate thread than the SimuManager. And while SimuManager is updating the prices, which relies on the _simuResults field that stores the simulation results, ResultListener can still access to _simuResults and make modifications. Such Simultaneous retrieval and modification would result an conflict. Therefore we need turn to synchronization.

D. Tests
Testing the project is not easy. My approach is to run the program and monitor the EnQueue and DeQueue processes on the ActiveMQ control platform. There are other ways to test the program, such as utilizing the Mockito package.


V. Active MQ
-----------------------------------------------------
A. Setting up, referencing and control
To run this program, one needs to download the ActiveMQ package and open the service. One needs also to add the ActiveMQ library to the referenced library of the java program. ActiveMQ has a platform to monitor the communication processes, whose link will be provided at the reference section.

B. Slow Consumer Problem
For this version of the program, a SimuManager sends one request, then a SimuSlave runs many simulation, packages those results into one message, and send it back. Previously, I tried one-message correspondence, meadning: a SimuManager sends one request, then a SimuSlave runs one simulation, and sends the result back. Theoretically, the one-message correspondence structure should work, though might be somewhat slow. In reallity, it does not.

The reason is due to something called Slow Consumer Problem, which ActiveMQ also identified and provide some solutions. Essentially, if there is a message consumer that runs very slow, and the producer keeps generating new messages, the ActiveMQ broker would freeze and fail to deliver new messages. In this particular problem, the SimuManager is the slow leg. When the number of requests is large, the message broker will freeze at some point.

Solutions suggested by ActiveMQ mainly focuses on detecting slow consumers and then drop them. This is not what we want here. Theoretically, when there is a Slow Consumer Problem, we can 1) speed up the consumer's speed to process messages, or 2) reduce the number of messages to be exchanged, or 3) improve the broker so that it can deal with such problem.

The speed of SimuManager is hardly improved, and we have relatively limited control over message brokers at this stage. So, an easy fix is to reduce the number of messages to be exchanged. That's how the current request-simulation mode is chosen. A rule of thumb is to run at least 100 simulations per message sent (simuPackageSize = 100).


VI. Limitations and Further Development
-----------------------------------------------------
1. Most of the fields are protected to ensure code clarity. Need to use private fields if code safety is important.
2. Slow consumer problem. The current fix of the slow consumer problem is temporary, meaning, even under the current structure, it is still possible that the SimuManager freezes the message broker.


VII. References
-----------------------------------------------------
ActiveMQ Homepage: http://activemq.apache.org/
ActiveMQ Control Platform: http://127.0.0.1:8161/admin/
	Username: admin
	Password: admin
Slow Consumer Problem: http://activemq.apache.org/slow-consumers.html
Slow Consumer Handling strategies: http://activemq.apache.org/slow-consumer-handling.html