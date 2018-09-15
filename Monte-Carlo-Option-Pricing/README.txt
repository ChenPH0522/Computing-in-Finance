Project: Monte Carlo Option Pricing
-----------------------------------------------------

I. Basic Info
-----------------------------------------------------
Author: Penghao (Stanley) Chen
Date: 10/25/2017
Version: 4.0
Development Environment: java 1.8.0_141


II.General Usage Notes
-----------------------------------------------------
This package outlines the framework of Monte Carlo option pricing method. In the package, the European call option and Asian call option classes are already given, but users are free to design their own options, including exotic types. The default stock price moves as a geometric Brownian motion process. Another process, Arithmetic Brownian motion is also given. But also, users are free to design their own stock processes.

Users should notice that Monte Carlo simulation, although converges in theory, might not always converge in practice. This is sometimes related to the input and the computing power of the hardware. For other cases, the Monte Carlo assumptions simple do not hold. That being said, if the user uses the same inputs twice, it's almost impossible that s/he will have identical prices. This is definitely worth noting if the user aims high precision.

As a complementary, the package also provides analytical solutions to European call and put options. This is for users convenient if the user wants high precision, low latency, or simply want to compare analytical solutions with simulation results.


III. File List
-----------------------------------------------------
(Major Classes)
MCPricing.java			Has a main method.
BSPricing.java
PayOut.java			Interface
EuropeanCall.java		Implements PayOut
AsianCall.java			Implements PayOut
StockPath.java			Interface
GeometricPath.java		Implements StockPath
ArithmeticPath.java		Implements StockPath
RandomVectorGenerator.java	Interface
GaussianGenerator.java		Implements RandomVectorGenerator
LogNormalGenerator.java		Implements RandomVectorGenerator
AntitheticGenerator.java	Implements RandomVectorGenerator

(Utility Classes)
Distribution.java		Interface
GaussianDistribution.java	Implements Distribution

(Tests)
Test_AntitheticGenerator.java
Test_ArithmeticPath.java
Test_AsianCall.java
Test_BSPricing.java
Test_European.java
Test_GaussianDistribution.java
Test_GaussianGenerator.java
Test_GeometricPath.java
Test_LogNormalGenerator.java
Test_MCPricing.java

(Txt & Others)
AsianCall1					Simulation results for AsianCall payouts
EuropeanCall1					Simulation results for EuropeanCall payouts
BM1 - Goes to zero				Simulation results for ArithmeticPath
BM2 - not going to zero
BM3 - increments with mean 0.1, std 4
Gaussian1 - standard normal			Simulation results for GaussianGenerator
Gaussian2 - normal with mean 10, std 10		Simulation results for GaussianGenerator
GBM1 - drift 0.0001, std 0.01			Simulation results for GeometricPath
GBM2 - ending values				Do multiple simulations using GeometriPath. Extract their ending values.
LogNormal1 - mean 5, std 0.5			Simulation results for LogNormalGenerator
MC_AsianCall					Use Monte Carlo Simulation pricing method to price an Asian call multiple times. Extract their prices.
MC_EuroCall					Use Monte Carlo Simulation pricing method to price an European call multiple times. Extract their prices.
Graphs.xls		Excel file that graphs simulation results. Also contains Black-Scholes solutions.
README.txt


IV. Design
-----------------------------------------------------
A. Major Class Design
                                           |------------|                 |-----------|
                                           | MCPricing  |                 | BSPricing |
                                           |------------|                 |-----------|
                                          /      |      \                     |
                                         /       |       \                    |
                            (Pseudo-input)    (input)    (depends)        (depends)
                            /                    |            \               |
                           /                     |             \              |
         |-----------------|            |-----------------|   |-----------------------|
         |    StockPath    |            |      PayOut     |   |  GaussianDistribution |
         | (ArithmeticPath)|--(input)-->| (European Call) |   |-----------------------|
         | (GeometricPath) |            |   (Asian Call)  |
         |-----------------|            |-----------------|
                 |
                 |
              (input)
                 |
                 |
      |-----------------------|
      | RandomVectorGenerator |
      |  (GaussianGenerator)  |
      |  (LogNormalGenerator) |
      |-----------------------|

The "peudo-input" between StockPath and MCPricing means that: although MCPricing takes StockPath as an input argument, what it really does is to pass the StockPath to the PayOut's method.

B. Distribution
The distribution interface is designed such that users can easily pull out distribution information including PDF, CDF and inverse CDF. The GaussianDistribution implemented here is one special case of distribution. The numerical method to calculate inverse Gaussian CDF provided in the HW assignment sheet is not very efficient, which is why it is not implemented here. The one currently implemented is much better. Reference for such method is listed at the end of this txt.

C. Other Reusable Classes
Other than the Distribution interface and the GaussianDistribution class, everything else is much limited to the Monte Carlo simulation problem, with limited scalability. The RandomVectorGenerator interface and those classes implmenting it can be useful every now and then in other situations.

D. Tests
Although there is a unit test for each actual class, the package is not fully tested. The biggest weakness for this package is that it assumes good input. That is, there is no crazy inputs such as negative variance or astronomical returns. Type checking is also poor. The reason why it is tested so is because otherwise it will consume enormous development energe and time. This package can handle most cases for normal usage. But for actual deployment, it requires more type-checking and exception handling.

Also, simulation results are not easy to test in Java unit test. The simple method here is to run the code in the test case to prove it works, then visualize results in the Excel file to check whether it produces the desired results. This is admittedly not very rigorous, but suffices to spot most errors for this simple task. Note that the data in Excel file might not be the same in txt files, as I'm changing them constantly when I run new simulations.


V. Monte Carlo option pricing method
-----------------------------------------------------
1. speed
The Monte Carlo simulation alogorithm implemented here is not the most efficient one, since there is much construcing, calling involved in it. And, it's not multi-threaded. It is designed so to promot code readability and scalability. Generally, it takes around 2 seconds to price an option if the precision is no under 0.01 and significance level is below 98%. The relationship among time, precision, and significance level is dynamic, but should follow a pattern. If one is interested, one can actually run the simulation at multiple times, and construct the time surface over the precision-significance plane.

Another thing worth noting here is that in the MCPricing class, there is a _MINLOOP and _MAXLOOP restriction. We need a _MAXLOOP to force the program to stop if it takes too much time to push the result to the desired significance level. We need a _MINLOOP to ensure the simulation generates enough data points, and we can reasonable estimate the theoretical variance using the generated data points.

2. convergence
Theoretically, if we run Monte Carlo for infinitely many times, we will eventually get the theoretical price of the option. In practice, this is not feasible. One consequence is that, if you use exactly the same inputs and run the Monte Carlo pricing twice, you will get two different prices. This is a problem for tasks requiring high precision. But for general usage, people might live with it.

If we run Monte Carlo pricing for many times, that is, we are similating Monte Carlo simulation, we'll eventually get a distribution of prices. This is what the main method in MCPricing aims to do. The result is plotted in the Excel file. Visually, it is similar to normal distribution. And theoretically it should be, by central limit theorem.

NOTE: DO NOT RUN THE SIMULATION OVER 100 TIMES. VERY TIME-COSUMING!

3. divergence
Monte Carlo pricing is not a universal solution. There are cases where Monte Carlo simulation is not suitable. Such cases include but not limited to cases where volatility is very large. In such cases, Monte Carlo pricing results will go to 0, while Black-Scholes solution will converge to the stock price. The two methods diverge. Theoretically, importance sampling method could be implemented to correct such situation. But still, very often, when some parameter has extreme behavior, the underlying assumptions for Monte Carlo and Black-Scholes, such as geometric stock price movement, stochastic integrals, optimal exercising strategies, etc., simply do not hold any more.

4. limitations
There is a flaw in this algorithm. Currently the _MINLOOP level is set at 10000. In general, this is enought to ensure a reasonable estimate for the standard deviation of simulated option prices. However, if the simga is very low, drift is mild even negative, K is too high (or too low) relative to current price so that it is very not likely to be in-the-money, it's possible that for the 10000 simulations, we all get 0 payout. And that means we have a 0 standard deviation, and the Monte Carlo simulation will immediately exit loops after reaching the _MINLOOP requirement. The consequence is that, we'll give the option price equal to 0. But this is not correct: its theoretical value is still positive, though might be small.

Such corner cases can happen at a very low frequency, maybe once in several years, if not longer. But it's still a flaw. I will try to improve it in the future.


VI. Limitations and Further Development
-----------------------------------------------------
1. More type-checking and Exception handling. Only assuming good inputs is definitely a weakness in the program. Users may well carelessly give a negative standard deviation to the methods. I will address such issues in the furture.
2. Monte Carlo pricing method has many drawbacks, as mentioned in the above section. There is definitely room to improve in this field.
3. In classes implementing RandomVectorGenerator, it's better to incorporate a seed field, because in testing or other cases, we might want control over what they generates. Improvements are needed here.


VII. References
-----------------------------------------------------
1. https://quant.stackexchange.com/questions/21764/stopping-monte-carlo-simulation-once-certain-convergence-level-is-reached
2. http://www.quantopia.net/inverse-normal-cdf/
3. http://www.quantopia.net/cumulative-normal-distribution/