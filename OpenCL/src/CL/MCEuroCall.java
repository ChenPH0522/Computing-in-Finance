package CL;

import org.jocl.*;

import static org.jocl.CL.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import Stats.*;

public class MCEuroCall {

	private static int _simuSize = 50000;
	private static long _timeLimit = 5*60*1000;	// 5 minutes
	private static final int _MINLOOP = 10000;		//minimum number of loops
	
	public static void setSimuSize(int n) {_simuSize = n;}
	public static void setTimeLimit(long time) {_timeLimit = time;}
	
	/**
	 * @param S0	starting price of the underlying
	 * @param K		strike price
	 * @param r		continuous risk-free rate, per period
	 * @param q		continuous dividend yield, per period
	 * @param sigma	continuous volatility of the underlying return, per period
	 * @param T		number of periods to maturity
	 * @param precision		precision of the pricing result
	 * @param confidence	confidence level of the pricing result
	 * @return
	 */
	public static float pricing(float S0, float K, float r, float q, float sigma, int T, 
			float precision, float confidence) {
		
		float criticalValue = (float) Stats.Gauss.inv_cdf(0.5 + confidence/2, 0, 1);
		
		float cur_n = 1;
		float tot_n = 1;
		float[] payoffPV = getPayoff(S0, K, r, q, sigma, T);
		float var = 0;
		float new_mean = 0;
		float old_mean = 0;
		float threshold = Float.MAX_VALUE;
		
		long start_time = System.currentTimeMillis();
		
		while ((precision < threshold || tot_n < _MINLOOP) &&
				System.currentTimeMillis() - start_time < _timeLimit) {
			
			if (cur_n >= 2*_simuSize) {
				payoffPV = getPayoff(S0, K, r, q, sigma, T);
				cur_n = 1;
			}
			
			new_mean = old_mean + (payoffPV[(int)cur_n-1] - old_mean)/tot_n;
			if (tot_n > 1) {
				var = (tot_n-2)/(tot_n-1)*var + 
						(new_mean-old_mean)*(new_mean-old_mean)*tot_n;
			}
			
			old_mean = new_mean;
			threshold = (float) (criticalValue * Math.sqrt(var/tot_n));
			cur_n ++;
			tot_n ++;
		}
		
		if (precision < threshold) {
			System.out.println("Time out.");
			return -1;
		}
		
		return new_mean;
	}
	
	/**
	 * @param S0	starting price of the underlying
	 * @param K		strike price of the option
	 * @param D		discount factor. D = exp(-r*T)
	 * @param a_1	factor in SDE solution. a_1 = (r-q-0.5*sigma*sigma) * T
	 * @param a_2	factor in SDE solution. a_2 = sigma * sqrt(T)
	 * @return kernel string
	 */
	protected static String buildKernelString(float S0, float K, float D, float a_1, float a_2) {
		
		String kernelStr = "__kernel void payoff(__global const float* u1, __global const float* u2, __global float* payoff) \n"
                + "{\n"
                + "    int i = get_global_id(0);\n"
                + "    float g1 = sqrt(-2*log(u1[i])) * cos(2*M_PI*u2[i]);\n"
                + "    float g2 = sqrt(-2*log(u1[i])) * sin(2*M_PI*u2[i]);\n"
                + "    float price1 = " + S0 + "* exp(" + a_1 + "+" + a_2 +"*g1);\n"
                + "    float price2 = " + S0 + "* exp(" + a_1 + "+" + a_2 +"*g2);\n"
                + "    float pay1 = " + D + "*(price1-" + K + ");\n"
                + "    float pay2 = " + D + "*(price2-" + K + ");\n"
                + "    if (pay1 < 0) {pay1 = 0;};\n"
                + "    if (pay2 < 0) {pay2 = 0;};\n"
                + "    payoff[i] = pay1;\n"
                + "    payoff["+(2*_simuSize-1)+"-i] = pay2;\n"
                + "}\n";
		
		return kernelStr;
	}
	
	/**
	 * @param S0	starting price of the underlying
	 * @param K		strike price
	 * @param r		continuous risk-free rate, per period
	 * @param q		continuous dividend yield, per period
	 * @param sigma	continuous volatility of the underlying return, per period
	 * @param T		number of periods to maturity
	 * @return
	 */
	protected static float[] getPayoff(float S0, float K, float r, float q, float sigma, int T) {
		
        cl_platform_id[] platforms = new cl_platform_id[1];
        clGetPlatformIDs(1, platforms, null);
        cl_platform_id platform = platforms[0];
        cl_device_id[] devices = new cl_device_id[1];
        clGetDeviceIDs(platform,  CL_DEVICE_TYPE_GPU, 1, devices, null);
        cl_device_id device = devices[0];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Create a context for the selected device
        cl_context context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue for the selected device
        cl_command_queue commandQueue =
                clCreateCommandQueue(context, device, 0, null);

        // Read the program sources and compile them :
		float D = (float) Math.exp(-r * T);
		float a_1 = (float) (r-q-0.5*sigma*sigma)*T;
		float a_2 = (float) (sigma * Math.sqrt(T));
		String src = buildKernelString(S0, K, D, a_1, a_2);

        // Create the program from the source code
        cl_program program = clCreateProgramWithSource(context,
                1, new String[]{ src }, null, null);

        // Build the program
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        cl_kernel kernel = clCreateKernel(program, "payoff", null);
        Random rnd = new Random();
        float[] u1 = new float[_simuSize];
        float[] u2 = new float[_simuSize];
        for (int i = 0; i<_simuSize; i++) {
        	u1[i] = rnd.nextFloat();
        	u2[i] = rnd.nextFloat();
        }
        float[] payoff = new float[2*_simuSize];
        Pointer pu1 = Pointer.to(u1);
        Pointer pu2 = Pointer.to(u2);
        Pointer pp = Pointer.to(payoff);

        // Allocate the memory objects for the input and output data
        cl_mem[] memObjects = new cl_mem[3];
        memObjects[0] = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * _simuSize, pu1, null);
        memObjects[1] = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * _simuSize, pu2, null);
        memObjects[2] = clCreateBuffer(context, CL_MEM_READ_WRITE, Sizeof.cl_float *_simuSize*2, null, null);

        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memObjects[0]));
        clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memObjects[1]));
        clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memObjects[2]));

        // Set the work-item dimensions
        long[] global_work_size = new long[]{_simuSize};
        long[] local_work_size = new long[]{1};

        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        // Read the output data
        clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
                Sizeof.cl_float *2*_simuSize, pp, 0, null, null);
		
        // Release kernel, program, and memory objects
        clReleaseMemObject(memObjects[0]);
        clReleaseMemObject(memObjects[1]);
        clReleaseMemObject(memObjects[2]);
        
		clReleaseKernel(kernel);
        clReleaseProgram(program);
        
		return payoff;
	}
	
	public static void main(String[] args) 
			throws FileNotFoundException, UnsupportedEncodingException {
		
		float S = 152.35f;
		float K = 165f;
		float r = 0.0001f;
		float q = 0f;
		float sigma = 0.01f;
		int T = 252;
		float precision = 0.1f;
		float confidence = 0.96f;
		
		long startTime = System.currentTimeMillis();
		float price = MCEuroCall.pricing(S, K, r, q, sigma, T, precision, confidence);
		System.out.println("Simulation time is: " + (System.currentTimeMillis()-startTime)/1000. + " seconds.");
		System.out.println("European call option price is: " + price);
	}
}