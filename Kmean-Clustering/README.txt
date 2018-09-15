Project: K-Mean Clustering
-----------------------------------------------------

I. Basic Info
-----------------------------------------------------
Author: Penghao (Stanley) Chen
Date: 9/28/2017
Version: 1.0
E-mail: pc1819@nyu.edu
Development Environment: java 1.8.0_141

II. General Usage Notes
-----------------------------------------------------
The programs together does the K-mean clustering to a set of multi-dimensional points. The Kmean class employs Lloyd's Algorithm. The Kmean2 class is desgined to solve such problems that require clusters to be of fixed sizes. Theoretically, however, the Kmean2 method does not necessarily converge.

To see the results for the Homework Example, simply run the main() methods in Kmean and Kmean2. Their output path is predefined in the java files, so remember to rewrite the file path before running in a different environment.

III. File List
-----------------------------------------------------
(Major Classes)
Cluster.java
Kmean.java			Performs the Lloyd's Algorithm
Kmean2.java			Performs the "fixed cluster size" K-mean Algorithm
Point.java

(Utility Classes)
ComputeCentroid.java
Distance.java			Interface
DoubleEquals.java
DoubleCompare.java
Fitness.java			Contain metrics to eavaluate K-mean solutions
GeometricDistance.java
Pair.java
Stats.java

(Tests)
Test_Cluster.java
Test_ComputeCentroid.java
Test_DoubleCompare.java
Test_DoubleEquals.java
Test_Fitness.java
Test_GeometricDistance.java
Test_Kmean.java
Test_Kmean2.java
Test_Pair.java
Test_Point.java
Test_Stats.java

(Txt)
K-Mean results.txt		Clustering results for the Homework Example, using Kmean method
K-Mean2 resutls.txt		Clustering results for the Homework Example, using Kmean2 method
Comparison results.txt		Simulation results, comparing Kmean and Kmean2
README.txt

IV. Design
-----------------------------------------------------
A. Major Class Desgin

                       |------------|
                       |   Kmean    |
                       |   Kmean2   |\
                       |------------| \
                          |            \
                         has         |--------------|
                          |          |    Cluster   |
                          |          |--------------|
                |-----------------|       /
                |     Point       |<----has
                |-----------------|

Notice that there is no "Centroid" class here and it is designed so with a purpose. Under this framework, a centroid class is performing exactly the same functions as a Point class. The only possible difference is that centroid can have an ID field, which is not necessary if the Point collections under Kmean and Kmean2 are carefully managed. To cut the redundancy, centroid is not used here.

B. The Distance Interface
The design takes much reusability into account. The Distance interface is one good example for this.
The particular problem in the example is only two-dimensional and uses Euclidean distance, or Geometric distance. Yet in other cases, other distance metrics, such as Manhattan distance, are more proper.
To accomodate this situation, I designed the GeometricDistance class and grouped it under the Distance Interface.
To change the distance metric, use the static method of either Point or Kmean. The two setters interact with each other to ensure all points are under the same distance metric.

C. Other Reusable Classes
Other utility classes are also reusable, thought the scale is limited. The Stats class is a exception. All its methods are made static so that it can function as a counterpart to the Math class in statistics. It applies different statistical tools to arrays or Collections of numbers.

D. Tests
The tests are generally solid and considered many corner cases. Some results are examined by printing results rather than using assert statements. Yet the test mainly tests the algorithm. That is, it relatively lacks type-checking, compared with more comprehensive tests.

V. Kmean and Kmean2 Algorithms
-----------------------------------------------------
A. Kmean
Kmean uses the Lloyd's Algorithm, which is prooved to be convergent in most cases, but it is slow. The loop-stopping criteria used in this class is by convergence: take a list of centroids, use them to re-cluster the points and then calculate a new list of centroids. If before and after the change, the largest move within those centroids is very little, then it is considered reaching the convergence. Determining what qualifies as "very little" requires the static field, PRECISION, of the Point class.

B. Kmean2
Kmean2 modifies the Lloyd's Algorithm. In Lloyd's Algorithm, we assign points to different centroids. In Kmean2, we make centroids "take" a certain number of untaken points. If a centroid has already reached its limit to take any more point, we simply move to the next centroid and repeat the process.
Intuitively, this is not very "fair", because it is closely related to the order of the centroids -- front order centroids get more opportunities and can happily choose closer points.
One way to deal with this issue is to randomnize the order by which centroids takes the points. This is doable and its explanation is very ituitive. Possible improvement can be made here.
A more serious problem is that this method is not proved to be good, or convergent, either mathematically or empirically. So we may want to move with cautions when using this approach.

C. Comparison
To compare the two methods, utlize the Fitness class. One way to think of clustering problems is that we want to minimize the within-cluster distance variance. The intuition is that if a group of points have the same distance to an outstanding point, it is very likely that those points belong to the same group.
There are definitely other possible better metrics. Only that I found this one is very efficient and intuitive. This is also the same metric adopted in the Kmean2 algorithm.
Under the Fitness class, there is method getAvgVar that does this calculation. There is also a main method in the Fitness class, which is essentially a simulation over the same Homework Example.

Using the 100-time simulation result, on average, the Kmean algorithm achieves 0.39 unit-square variance, and the Kmean2 algorithm achieves 2.73 unit-square variance. Remember this is within-cluster distance variance and we want to minimize it, so clearly the Kmean method performs better than Kmean2.

Note: Don't use simuation over 100 times. Very time consuming.

VI. Limitations and Further Development
-----------------------------------------------------
1. For the Kmean2 algorithm, in each phase, randomnize the order that clusters take the closes points.
2. More type checking, input checking and exception handling.
3. Scalability is still limited. Will try to use more generic types in terms of the underlying data strucutre.
