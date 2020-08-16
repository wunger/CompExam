import java.math.BigInteger;

public class DifferentalBranch {
	
	private static final int[] base3Valid = {0,1,2,4,5,6,8,9,0xa};
	private static int[] recursionSbox;
	private static boolean[] vacant;
	private static int counterOfDiffBranch;
	private static int total;
	private static int[] nonlinearitycount;
	private static int[] lbnCount;
	private static int[] nonLinearityCountDBN3;

	public static void main(String[] args) {
		
		int[] presentSBox = {0xc,0x5,0x6,0xb,0x9,0x0,0xa,0xd,0x3,0xe,0xf,0x8,0x4,0x7,0x1,0x2};
		int p = 2;
		int n = 4;
		System.out.println("PRESENT SBox Differental Branch Number: " + calculateDifferentalBranch(presentSBox));
		System.out.println("PRESENT SBox Differental Branch Number calculated generic: " + basepPowNDifferentalBranch(presentSBox,p,n));
		
		
		int[] giftSBox = {0x1,0xa,0x4,0xc,0x6,0xf,0x3,0x9,0x2,0xd,0xb,0x7,0x5,0x0,0x8,0xe};
		System.out.println("GIFT SBox Differental Branch Number: " + calculateDifferentalBranch(giftSBox));
		System.out.println("GIFT SBox Differental Branch Number calculated generic: " + basepPowNDifferentalBranch(giftSBox,p,n));
		
		
		
		int[] base3Sbox =  { 0xa, 4, 1, 0xf, 5, 2, 9, 0xf, 6, 0, 8, 0xf, 0xf, 0xf, 0xf, 0xf };
		int[] base3SboxGenericFormat = {8, 3, 1, 4, 2, 7, 5, 0, 6};
		System.out.println("Base three trial: " + baseThreeDifferentalBranch(base3Sbox));
		System.out.println("Base three generic trail: " + basepPowNDifferentalBranch(base3SboxGenericFormat,3,2));
		
		int[] base3SboxIdeal = { 0, 5, 0xa, 0xf, 6, 8, 1, 0xf, 9, 2, 4, 0xf, 0xf, 0xf, 0xf, 0xf };
		int[] base3SboxIdealGenericFormat = {0, 4, 8, 5, 6, 1, 7, 2, 3};
		System.out.println("Base three Ideal trial: " + baseThreeDifferentalBranch(base3SboxIdeal));
		System.out.println("Base three Ideal generic trail: " + basepPowNDifferentalBranch(base3SboxIdealGenericFormat,3,2));
		
		
		total = 0;
		
		nonlinearitycount = new int[20];
		lbnCount = new int[20];
		nonLinearityCountDBN3 = new int[20];
		
		for(int i = 0; i < nonlinearitycount.length; i++)
		{
			nonlinearitycount[i] = 0;
			lbnCount[i] = 0;
			nonLinearityCountDBN3[i] = 0;
		}
		vacant = new boolean[9];
		for(int i = 0; i < vacant.length; i++)
		{
			vacant[i] = true;
		}
		recursionSbox = new int[16];
		for(int i = 0; i < 16; i ++)
		{
			recursionSbox[i] = 0xf;
		}
		counterOfDiffBranch = 0;
		
		base3Pow2Recursion(0);
		
		System.out.println("Found = " + counterOfDiffBranch);
		System.out.println("Total Base 3 S-Box " + total);
		
		
		for(int i = 0; i < nonlinearitycount.length; i++)
		{
			System.out.println("non lineariy value " + i + " has count of " + nonlinearitycount[i]);
		}
		for(int i = 0; i < lbnCount.length; i++)
		{
			System.out.println("LBN value " + i + " has count of " + lbnCount[i]);
		}
		for(int i = 0; i < nonLinearityCountDBN3.length; i++)
		{
			System.out.println("Non-Lineary value " + i + "  of DBN = 3 S-Boxes has count of " + nonLinearityCountDBN3[i]);
		}
	}
	
	public static void base3Pow2Recursion(int index)
	{
		if(index == (vacant.length-1) )
		{
			for(int i = 0; i < vacant.length; i++)
			{
				if( vacant[i] == true)
				{
					recursionSbox[base3Valid[index]] = base3Valid[i];
					total++;
					
					nonlinearitycount[LinearBranch.calculateNonLinearityBase3(recursionSbox, 2)]++;
					lbnCount[LinearBranch.calculateLinearBranchBase3(recursionSbox, 2)]++;
					if(baseThreeDifferentalBranch(recursionSbox) > 2)
					{
						System.out.println("Found Differantal Branch of " + baseThreeDifferentalBranch(recursionSbox));
						//System.out.println("Non-Linearity of: " + LinearBranch.calculateNonLinearityBase3(recursionSbox, 2));
						System.out.println(sBoxBase3ToString(recursionSbox));
						counterOfDiffBranch++;
						nonLinearityCountDBN3[LinearBranch.calculateNonLinearityBase3(recursionSbox, 2)]++;
						return;
					}
				}
			}
			return;
		}
		else
		{
			for(int i = 0; i < vacant.length; i++)
			{
				if(vacant[i] == true)
				{
					recursionSbox[base3Valid[index]] = base3Valid[i];
					vacant[i] = false;
					base3Pow2Recursion(index + 1);
					vacant[i] = true;
				}
			}
		}
	}
	
	public static int calculateDifferentalBranch(int [] arr)
	{
		int n = arr.length;
		int branchNumber = arr.length;
		
		for(int i = 0; i < n; i++)
		{
			int initalIn = i;
			int initalOut = arr[i];
			
			for(int j = 0; j < n; j++)
			{
				if(i != j)
				{
					int afterIn = j;
					int afterOut = arr[j];
					
					int inXor = initalIn ^ afterIn;
					int outXor = initalOut ^ afterOut;
					
					int tempDiffNumber = hammingWeight(inXor) + hammingWeight(outXor);
					
					if(tempDiffNumber < branchNumber)
					{
						branchNumber = tempDiffNumber;
					}
				}
				
			}
		}
		return branchNumber;
	}
	
	public static int baseThreeDifferentalBranch(int[] Sbox)
	{
		
		int min = Sbox.length;
		
		for(int i = 0; i < base3Valid.length; i++)
		{
			for(int j = 0; j < base3Valid.length; j++)
			{
				if(i != j)
				{
					int count = 0;
					int[] iVector = numToVectorBase3Pow2(base3Valid[i]);
					int[] jVector = numToVectorBase3Pow2(base3Valid[j]);
					
					int[] iOutVector = numToVectorBase3Pow2(Sbox[base3Valid[i]]);
					int[] jOutVector = numToVectorBase3Pow2(Sbox[base3Valid[j]]);
					
					count += baseThreeWeightVecotr(iVector, jVector);
					count += baseThreeWeightVecotr(iOutVector, jOutVector);
					
					if(count < min)
						min = count;
				}
			}
		}
		
		
		return min;
	}
	
	public static int basepPowNDifferentalBranch(int[] Sbox, int p, int n)
	{
		BigInteger pBig = new BigInteger(Integer.toString(p));
		BigInteger compLengthBig = pBig.pow(n);
		int compLength = compLengthBig.intValue();
		
		if(Sbox.length != compLength)
		{
			System.err.println("Size Mis-Match\n CompLength: " + compLength + "\n ArrayLength: " + Sbox.length);
			System.exit(-1);
		}
		int min = Sbox.length;
		
		for(int i = 0; i < compLength; i++)
		{
			for(int j = 0; j < compLength; j++)
			{
				if(i != j)
				{
					int count = 0;
					
					int[] iVector = numToVectorBasepPown(i, p, n);
					int[] jVector = numToVectorBasepPown(j, p, n);
					
					int[] iOutVector = numToVectorBasepPown(Sbox[i], p, n);
					int[] jOutVector = numToVectorBasepPown(Sbox[j], p, n);
					
					count += basepWeightVector(iVector,jVector,p);
					count += basepWeightVector(iOutVector,jOutVector,p);
					
					if(count < min)
					{
						min = count;
					}
				}
			}
		}
		
		return min;
	}
	
	public static int hammingWeight(int num)
	{
		int count = 0;
		
		for(int i = 0; i < 32; i++)
		{
			if ( ((num >> i)& 1) == 1)
			{
				count++;
			}
		}
		return count;
	}
	
	public static int baseThreeWeight(int num1, int num2)
	{
		if (num1 == num2)
			return 0;
		else
			return 1;
	}
	
	public static int basepWeight(int num1,int num2, int p)
	{
		int diff = (num1 - num2) % p;
		
		if (diff < 0)//Check for negetive num
		{
			diff += p;
		}
		
		int diff2 = (diff * (-1)) + p;//Check for modular difference.
		
		if(diff > diff2)//diff becomes min(diff,diff2)
		{
			diff = diff2;
		}
		
		return diff;
	}
	
	public static int baseThreeWeightVecotr(int[] arr1,int[] arr2)
	{
		int count = 0;
		
		for(int i = 0; i < arr1.length; i++)
		{
			count += baseThreeWeight(arr1[i] , arr2[i]);
		}
		
		return count;
	}
	
	public static int basepWeightVector(int[] arr1,int[] arr2, int p)
	{
		int count = 0;
		
		for(int i = 0; i < arr1.length; i++)
		{
			count += basepWeight(arr1[i], arr2[i], p);
		}
		
		return count;
	}
	
	public static int[] numToVectorBase3Pow2(int num)
	{
		int[] returnVal = new int[2];
		returnVal[0] = (num & 0b11);
		returnVal[1] = ( (num >> 2) & 0b11);
		
		return returnVal;
	}
	
	public static int[] numToVectorBasepPown(int num, int p, int n)
	{
		int[] returnVal = new int[n];
		int temp = num;
		
		for(int i = 0; i < n; i++)
		{
			returnVal[i] = temp % p;
			temp = (int) (temp/p);
		}
		
		return returnVal;
	}
	
	static String sBoxBase3ToString(int[] sBox)
	{
		String returnVal = "";
		for(int i = 0; i < base3Valid.length; i++)
		{
			returnVal = returnVal + base3Valid[i] + " -> " + sBox[base3Valid[i]] + "    ";
		}
		return returnVal;
	}

}
