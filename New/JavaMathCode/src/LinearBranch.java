
public class LinearBranch {
	
	private static final int[] base3Valid = {0,1,2,4,5,6,8,9,0xa};

	public static void main(String[] args) {
		/*   //Example 1
		int[] sbox = {3,0,1,2};
		int[] v1 = intToVector(3, 2);
		int[] v2 = intToVector(1, 2);

		System.out.println(correlationCoefficent(v1,v2,sbox));
		*/
		
		//int[] sbox = {3,2,0,1};
		//int[] v1 = intToVector(0, 2);
		//int[] v2 = intToVector(1, 2);
		int[] sbox = {3,0,1,2};
		
		 int[] v1 = intToVector(0,2);
		 int[] v2 = intToVector(0,2);
		 
		 //int[] sbox16 = {0x4,0x0,0x8,0x2,0x3,0x5,0xb,0x7,0x1,0x9,0xa,0x6,0xc,0xd,0xe,0xf};
		 //int[] sbox16 = {0, 1, 2, 13, 4, 7, 15, 6, 8, 12, 9, 11, 10, 14, 5, 3};
		 //System.out.println(calculateLinearBranch(sbox16, 4));
		 
		 System.out.println("GIFT linear branch");
		 int[] giftSBox = {0x1,0xa,0x4,0xc,0x6,0xf,0x3,0x9,0x2,0xd,0xb,0x7,0x5,0x0,0x8,0xe};
		 int giftNonLinearityNum = calculateNonLinearity(giftSBox, 4);
		 System.out.println("GIFT linear branch num = " + giftNonLinearityNum);
		 
		 System.out.print("\n\n\n");
		 
		 System.out.println("PRESENT linear branch");
		 int[] presentSBox = {0xc,0x5,0x6,0xb,0x9,0x0,0xa,0xd,0x3,0xe,0xf,0x8,0x4,0x7,0x1,0x2};
		 int presentNonLinearityNum = calculateNonLinearity(presentSBox, 4);
		 System.out.println("PRESNT linear branch num = " + presentNonLinearityNum);
		 
		 
		 int[] v1Base3 = intToVectorBase3(0,2);
		 int[] v2Base3 = intToVectorBase3(0,2);
		 int[] sBoxBase3 = {0,5,10,-1,6,8,1,-1,9,2,4,-1,-1,-1,-1,-1};
		 
		 //System.out.println(correlationCoefficentBase3(v1Base3, v2Base3, sBoxBase3));
		 
		 System.out.println("Table1 Non-Linearity " + calculateNonLinearityBase3(sBoxBase3, 2));
		 
		 int [] sBoxBase3Num2 = {8,5,2,-1,6,0,9,-1,1,10,4,-1,-1,-1,-1,-1};
		 System.out.println("Table2 Non-Linearity " + calculateNonLinearityBase3(sBoxBase3Num2, 2));
		 
		 int [] sBoxBase3Num3 = {6,10,4,-1,1,8,2,-1,0,5,9,-1,-1,-1,-1,-1};
		 System.out.println("Table3 Non-Linearity " + calculateNonLinearityBase3(sBoxBase3Num3, 2));
		 
		 int [] sBoxBase3Num4 = { 0xa, 4, 1, -1, 5, 2, 9, -1, 6, 0, 8, -1, -1, -1, -1, -1 };
		 System.out.println("Table4 Non-Linearity " + calculateNonLinearityBase3(sBoxBase3Num4, 2));
		 
		 /*
		for(int i = 0; i < v1.length; i++)
		{
			System.out.print(v1[i] + " ");
		}
		System.out.println(" ");
		*/
		//System.out.println(correlationCoefficent(v1,v2,sbox));

		//System.out.println(calculateLinearBranch(sbox, 2));
	}
	
	
	public static int calculateNonLinearity(int [] sBox, int bitNum)
	{
		int[][] matrix = new int[sBox.length][sBox.length];
		int branchNumber = 0;
		
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix.length; j++)
			{
				int[] v1 = intToVector(i,bitNum);
				int[] v2 = intToVector(j,bitNum);
				matrix[i][j] = correlationCoefficent(v1,v2,sBox);
				
				if(matrix[i][j] != 0  && ((i != 0) && (j != 0)))
				{
					int possibleBranch = matrix[i][j];
					if (possibleBranch > branchNumber)
					{
						branchNumber = possibleBranch;
					}
				}
			}
		}
		System.out.println(matrixToString(matrix));
		
		return branchNumber;
	}
	
	public static int calculateNonLinearityBase3(int [] sBox, int pow)
	{
		int[][] matrix = new int[base3Valid.length][base3Valid.length];
		int branchNumber = 0;
		
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix.length; j++)
			{
				int[] v1 = intToVectorBase3(i,pow);
				int[] v2 = intToVectorBase3(j,pow);
				matrix[i][j] = correlationCoefficentBase3(v1,v2,sBox);
				
				if(matrix[i][j] != 0  && ((i != 0) && (j != 0)))
				{
					int possibleBranch = matrix[i][j];
					if (possibleBranch > branchNumber)
					{
						branchNumber = possibleBranch;
					}
				}
			}

		}
		//System.out.println(matrixToString(matrix));
		//System.out.println(matrixToString(matrix));
		
		return branchNumber;
	}
	
	public static int calculateLinearBranchBase5Pow2(int[][] cordFunction1, int[][] cordFunction2)
	{
		int[][] matrix = new int[25][25];
		int branchNumber = 99; //just an arbritaty high number
		
		for(int i = 0; i < 25; i++)
		{
			for(int j = 0; j < 25; j++)
			{
				int[] v1 = intToVectorBase5Pow2(i);
				int[] v2 = intToVectorBase5Pow2(j);
				
				matrix[i][j] = correlationCoefficentBase5(v1,v2, cordFunction1,cordFunction2);
				if(matrix[i][j] != 0  && ((i != 0) && (j != 0)))
				{
					int possibleBranch = hammingWeightBase5FromVector(v1) + hammingWeightBase5FromVector(v2);
					if (possibleBranch < branchNumber)
					{
						branchNumber = possibleBranch;
					}
				}
			}
		}
		return branchNumber;
	}
	
	public static int calculateLinearBranchBase5Pow2Old(int[][] cordFunction1, int[][] cordFunction2)
	{
		int[][] matrix = new int[25][25];
		int branchNumber = 99; //just an arbritaty high number
		
		for(int i = 0; i < 25; i++)
		{
			for(int j = 0; j < 25; j++)
			{
				int[] v1 = intToVectorBase5Pow2(i);
				int[] v2 = intToVectorBase5Pow2(j);
				
				matrix[i][j] = correlationCoefficentBase5(v1,v2, cordFunction1,cordFunction2);
				if(matrix[i][j] != 0  && ((i != 0) && (j != 0)))
				{
					int possibleBranch = hammingWeightBase5FromVectorOld(v1) + hammingWeightBase5FromVectorOld(v2);
					if (possibleBranch < branchNumber)
					{
						branchNumber = possibleBranch;
					}
				}
			}
		}
		return branchNumber;
	}
	
	public static void printCorrelationCoefficentMatrixBase5Pow2(int[][] cordFunction1, int[][] cordFunction2)
	{
		int[][] matrix = new int[25][25];
		//int branchNumber = 99; //just an arbritaty high number
		
		for(int i = 0; i < 25; i++)
		{
			for(int j = 0; j < 25; j++)
			{
				int[] v1 = intToVectorBase5Pow2(i);
				int[] v2 = intToVectorBase5Pow2(j);
				
				matrix[i][j] = correlationCoefficentBase5(v1,v2, cordFunction1,cordFunction2);
			}
		}
		//return branchNumber;
		
		for(int i = 0; i < 25; i++)
		{
			System.out.println("Row " + i);
			for(int j = 0; j < 25; j++)
			{
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.println("End Row " + i);
		}
	}
	
	public static int calculateNonLineartyBase5Pow2(int[][] cordFunction1, int[][] cordFunction2)
	{
		int[][] matrix = new int[25][25];
		int nonLinearityNum = 0; //just an arbritaty high number
		
		for(int i = 0; i < 25; i++)
		{
			for(int j = 0; j < 25; j++)
			{
				int[] v1 = intToVectorBase5Pow2(i);
				int[] v2 = intToVectorBase5Pow2(j);
				
				matrix[i][j] = correlationCoefficentBase5(v1,v2, cordFunction1,cordFunction2);
				if(matrix[i][j] != 0  && ((i != 0) && (j != 0)))
				{
					int possibleNonLinearity = matrix[i][j];
					if (possibleNonLinearity > nonLinearityNum)
					{
						nonLinearityNum = possibleNonLinearity;
					}
				}
			}
		}
		return nonLinearityNum;
	}
	
	public static int calculateLinearBranchBase3(int [] sBox, int pow)
	{
		int[][] matrix = new int[base3Valid.length][base3Valid.length];
		int branchNumber = 99;
		
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix.length; j++)
			{
				int[] v1 = intToVectorBase3(i,pow);
				int[] v2 = intToVectorBase3(j,pow);
				matrix[i][j] = correlationCoefficentBase3(v1,v2,sBox);
				
				if(matrix[i][j] != 0  && ((i != 0) && (j != 0)))
				{
					int possibleBranch = hammingWeightBase3Pow2(i) + hammingWeightBase3Pow2(j);
					if (possibleBranch < branchNumber)
					{
						branchNumber = possibleBranch;
					}
				}
			}

		}
		//System.out.println(matrixToString(matrix));
		
		return branchNumber;
	}
	
	public static int correlationCoefficent(int[] v1, int[] v2, int[] sBox)
	{
		int sum = -8;
		
		for(int x = 0; x < sBox.length; x++)
		{
			int[] xVector = intToVector(x, v1.length);
			int[] xVectorAfterPermutation = intToVector(sBox[x] , v1.length);
			
			int power  = dotProduct(v1,xVector) + dotProduct(v2,xVectorAfterPermutation);
			
			if( (power & 0b1) == 0 )
			{
				sum += 1;
			}
			else
			{
				//sum += -1;
			}
		}
		
		return sum;
	}
	
	public static int correlationCoefficentBase5(int[] v1, int[] v2, int[][] cordFunction1, int[][] cordFunction2)
	{
		int[] countArr = {-5,-5,-5,-5,-5};
		
		for(int x = 0; x < 25; x++)
		{
			int[] xVector = intToVectorBase5Pow2(x);
			int[] xVectorAfterSBox = { cordFunction1[xVector[0]][xVector[1]], cordFunction2[xVector[0]][xVector[1]]};
			
			int power = dotProduct(v1,xVector) + dotProduct(xVectorAfterSBox,v2);
			
			power = power % 5;
			if (power < 0)
				power += 5;
			countArr[(power % 5)]++;
		}
		
		int max = 0;
		
		for(int i = 0; i < countArr.length; i++)
		{
			int temp = countArr[i];
			if(temp < 0)
				temp = temp * (-1);
			if(temp > max)
				max = temp;
		}
		
		return max;
		
	}
	
	public static int correlationCoefficentBase3(int[] v1, int[] v2, int[] sBox)
	{
		//System.out.println("Entered correlation coefficent");
		int[] countArr = {-3,-3,-3};
		
		for(int x = 0; x < base3Valid.length; x++)
		{
			int[] xVector = intToVectorBase3(x, v1.length);
			int[] xVectorAfterPermutation = intToVectorBase3(sBox[base3Valid[x]] , v1.length);
			
			int power = dotProduct(v1,xVector) + dotProduct(v2,xVectorAfterPermutation);
			power = power % 3;
			if (power < 0)
				power += 3;
			countArr[(power % 3)]++;
		}
		
		int max = 0;
		
		for(int i = 0; i < countArr.length; i++)
		{
			int temp = countArr[i];
			if(temp < 0)
				temp = temp * (-1);
			if(temp > max)
				max = temp;
		}
		
		return max;
	}
	
	public static int[] intToVector(int number, int arrSize)
	{
		int[] arr = new int[arrSize];
		
		for(int i = 0; i < arrSize; i++)
		{
			arr[i] = ((number >> i) & 0b1);
		}
		
		return arr;
	}
	
	public static int[] intToVectorBaseP(int num, int p, int pow)
	{
		int temp = num;
		int[] arr = new int[pow];
		for(int i = 0; i < pow; i++)
		{
			arr[i] = temp % p;
			temp = (int) (temp / p);
		}
		return arr;
	}
	
	public static int[] intToVectorBase3(int number, int arrSize)
	{
		int[] arr = new int[arrSize];
		int temp = number;
		
		for(int i = 0; i < arrSize; i++)
		{
			arr[i] = temp % 3;
			
			temp = (int) (temp/3);
		}
		
		return arr;
	}
	
	public static int[] intToVectorBase5Pow2(int num)
	{
		int[] arr = new int[2];
		int temp = num;
		arr[1] = temp % 5;
		arr[0] = (int) temp/5;
		
		return arr;
	}
	
	public static int dotProduct(int[] vector1, int[] vector2)
	{
		int returnVal = 0;
		
		for(int i = 0; i < vector1.length; i++)
		{
			returnVal += (vector1[i] * vector2[i]);
		}
		return returnVal;
	}
	
	public static String matrixToString(int[][] arr)
	{
		String returnVal = "";
		
		returnVal = returnVal + "\t";
		for(int i = 0; i < arr.length; i++)
		{
			returnVal = returnVal + Integer.toString(i) + "\t";
		}
		returnVal = returnVal + "\n______________________________________________________________________________________\n";
		for(int i = 0; i < arr.length; i++)
		{
			for(int j = 0; j < arr.length; j++)
			{
				if(j == 0)
				{
					returnVal = returnVal + Integer.toString(i) + "|\t";
				}
				returnVal = returnVal + Integer.toString(arr[i][j]) + "\t";

			}
			returnVal = returnVal + "\n";
		}
		returnVal = returnVal + "\n";
		
		
		return returnVal;
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
	
	public static int hammingWeightBase3Pow2(int num)
	{
		if (num == 0)
			return 0;
		if ( (num ==1) || (num == 2) || (num == 3) || (num == 6))
			return 1;
		return 2;
	}
	
	public static int hammingWeightBase5SingleDigit(int num)
	{
		if(num == 0)
			return 0;
		return 1; //Short ciruct to use weight form code paper.
		/*
		if(num == 1 || num == 4)
			return 1;
		return 2;
		*/
	}
	
	public static int hammingWeightBase5SingleDigitOld(int num)
	{
		if(num == 0)
			return 0;
		if(num == 1 || num == 4)
			return 1;
		return 2;
	}
	
	public static int hammingWeightBase5FromVector(int[] vector)
	{
		int sum = 0;
		
		for( int i = 0; i < vector.length; i++)
		{
			sum += hammingWeightBase5SingleDigit(vector[i]);
		}
		return sum;
	}
	
	public static int hammingWeightBase5FromVectorOld(int[] vector)
	{
		int sum = 0;
		
		for( int i = 0; i < vector.length; i++)
		{
			sum += hammingWeightBase5SingleDigitOld(vector[i]);
		}
		return sum;
	}

}
