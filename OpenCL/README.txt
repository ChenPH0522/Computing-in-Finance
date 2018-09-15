Project: Java OpenCL
-----------------------------------------------------


I. Basic Info
-----------------------------------------------------
Author: Penghao (Stanley) Chen
Date: 12/5/2017
Version: 1.0
Development Environment: Java 1.8.0_151
			JOCL 2.0.0


II. General Usage Notes
-----------------------------------------------------
The project utilizes GPU to perform steps with heavy methematical computations in Monte Carlo option pricing, including generating Gaussian numbers, calculating stock prices, and get option payoffs. To utilize the package, one needs to build path to the JOCL libray, whose link is provided at the end of the document.


III. File List
-----------------------------------------------------
Package: CL
	MCEuroCall.java
	Test_MCEuroCall.java

Package: Stats
README.txt


IV. Design
-----------------------------------------------------
A. Kernel
The Kernel is the essence of the whole program. It will be analyzed in the following aspects:

A.1 Float vs Double
To raise speed, the program uses floats, rather than double, because of memory space. A float takes 32 bits, while a double takes 64 bits.

A.2 Uniform Generation
This is one crucial link that can be further optimized. Currently, the program generates uniform random variable on the host instead of on the computing device (GPU). The host generates uniform random variable linearly, which dramatically slows down the algorithm.

Ideally, the uniform random numbers should be generated concurrently on the kernel level. Unfortunately, this is not an easy task. Since the main purpose of this project is simply to get some experience in utilizing GPU for scientific computing, I'm leaving this link for future improvement.

There are C alogrithms on the internet implementing this, whose links are provided at the end of the document. Generating random number is a broad topic. To extend a little bit, one of the most widely adopted algorithm is called Linear Congruential Generator. One of the referenced packages also utilizes another algorithm called Multiply-With-Carry.

A.3 Box Muller Transformation
After uniform random numbers are generated, they are passed on to the GPU memory space, where the uniform random numbers are used to generate independent standard Gaussian random numbers. This transformation method is called the Box Muller transformation. One can find the reference at the end of the document.

A.4 Branch Statements
A second link that can be further optimized are branch statements. Although there is a convention in kernel programming, "Branch statment is evil", I still chose to implement them because they are easy for developement and because they are anyway still faster than performing the Math.max function at the host level.


V. OpenCL
-----------------------------------------------------
OpenCL is a deep area and subtopic of parallel computing. One may find helpful to first get familiar with OpenCL concepts such as platform, execution, memory and programming models before working on the project.


VI. References
-----------------------------------------------------
JOCL: http://jocl.org/
The MWC64X Random Number Generator: http://cas.ee.ic.ac.uk/people/dt10/research/rngs-gpu-mwc64x.html
clMathLibraries: https://github.com/clMathLibraries/clRNG
Box Muller transformation: https://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
OpenCL Programming Guide: http://cg.inf.elte.hu/~gpgpu/opencl/2014-2015-2/01/OpenCL%20Programming%20Guide.pdf