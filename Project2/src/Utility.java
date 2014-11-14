import java.util.PriorityQueue;
import java.util.Random;


public class Utility {
	
	/*
	 * This function ramdomly provides true(1) or false(-1) 
	 */
	public static int randomBoolGenerator()
	{
		
		if(Math.random() > 0.5)
			return 1;
		else return -1;
	}
	
	/*
	 * This function returns true/false as per given probability
	 */
	public static boolean randomNumGeneratorProbability(double p)
	{
		if (Math.random() <= p)
			return true;
		else return false;
		
	}

	/*
	 * This function is used to print any Int 1Dimension array
	 */
	public static void printInt1DArray(int[] d1Array,int uBound)
	{
		for(int i =0;i<uBound;i++)
		{
			System.out.print(d1Array[i]+" ");
		}
		System.out.println();
		
	}
	/*
	 * This function is used to print any Int 2Dimension array
	 */
	public static void printInt2DArray(int[][] d1Array,int iBound,int jBound)
	{
		for(int i =0;i<iBound;i++)
		{
			for(int j =0;j<jBound;j++)
			{
				System.out.print(d1Array[i][j]+" ");
			}
			
			System.out.println();
		}
		System.out.println();
		
	}
	
	/*
	 * This function is used to print any Double 1Dimension array
	 */
	
	public static void printDouble1DArray(double[] d1Array,int uBound)
	{
		for(int i =0;i<uBound;i++)
		{
			System.out.print(d1Array[i]+" ");
		}
		System.out.println();
		
	}
	
	/*
	 * This function shuffles array
	 */
	 
	public static int[] randomizeArray(int[] iArray)
	{

		Random rnd = new Random();
	    for (int i = iArray.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = iArray[index];
	      iArray[index] = iArray[i];
	      iArray[i] = a;
	    }
	    
	    return iArray;
	}
	
	public static long[] heapSort(long[] runningTime,int uBound) 
	{
		PriorityQueue<Long> inputList = new PriorityQueue<Long>();
		
		//Insert into Heap
		for(int i=0;i<uBound;i++)
		{
			inputList.add(runningTime[i]);
		}
		
		long[] sortedArray = new long[uBound];
		
		for(int i=0;i<uBound;i++)
		{
			sortedArray[i]=inputList.remove();
		}
		
		return sortedArray;
		
	}
	
	public static void main(String[] args)
	{
		/*
		int[] shuffleBitSequence= new int[10];
		
		for(int i=0; i< 10; i++)
		{
			shuffleBitSequence[i]=i;
		}
		
		System.out.println("Before shuffling");
		Utility.printInt1DArray(shuffleBitSequence, 10);
		
		shuffleBitSequence= Utility.randomizeArray(shuffleBitSequence);
		System.out.println("After shuffling");
		Utility.printInt1DArray(shuffleBitSequence,10);
		*/
		
		int[] iArray={5,4,3,2,5,6,4,2,1,3,5,6};
		
		//int[] sortedArray = Utility.heapSort(iArray, iArray.length);
		
		//printInt1DArray(sortedArray, sortedArray.length);
	}
	
	
}
