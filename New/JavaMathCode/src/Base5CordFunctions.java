import java.util.ArrayList;

public class Base5CordFunctions {
	private static int[] valuesRemaning;
	private static int[][] valuesRemaningV2;
	private static int[][] tempCordFunction;
	private static long numFound;
	private static long numFoundOneResilient;
	private static long numFoundTwoResilient;
	private static ArrayList usableCordFunctions;
	private static long countForSBoxFromCordFunctions;
	private static long countForLBNGreaterThan2;
	private static long[] heatMap;
	private static long[] heatMapLBNOld;
	private static long[] heatMapNonLinearity;
	private static int[][] matrixInner = {{1,1},{4,1}};
	private static int[][] matrixOuter = {{0,3},{3,2}};
	private static int[] zeroVector = {0,0};
	private static int[] vector1 = {0,3};
	private static int[] vector2 = {4,3};
	private static int[] affineLBNHeatMap;
	private static int[] affineNonLinearityHeatMap;
	
	public static void main(String[] args)
	{
		valuesRemaning = new int[5];
		for(int i = 0; i < valuesRemaning.length; i++)
		{
			valuesRemaning[i] = 5;
		}
		
		tempCordFunction = new int [5][5];
		
		for(int i = 0; i < tempCordFunction.length; i++)
		{
			for(int j = 0; j < tempCordFunction[i].length; j++)
			{
				tempCordFunction[i][j] = -1;
			}
		}
		
		numFound = 0;
		numFoundOneResilient = 0;
		numFoundTwoResilient = 0;
		usableCordFunctions = new ArrayList<int[][]>();
		countForSBoxFromCordFunctions = 0;
		countForLBNGreaterThan2 = 0;
		//recursiveCall(0,0);
		
		valuesRemaningV2 = new int[5][5];
		for(int i = 0; i < valuesRemaningV2.length; i++)
		{
			for(int j = 0; j < valuesRemaningV2[i].length; j++)
			{
				valuesRemaningV2[i][j] = 1;
			}
		}
		
		heatMap = new long[20];
		heatMapLBNOld = new long[20];
		for(int i = 0;i < heatMap.length; i++)
		{
			heatMap[i] = 0;
			heatMapLBNOld[i] = 0;
		}
		heatMapNonLinearity = new long[100];
		for(int i = 0; i < heatMapNonLinearity.length; i++)
		{
			heatMapNonLinearity[i] = 0;
		}
		
		affineLBNHeatMap = new int[200];
		for(int i = 0; i < affineLBNHeatMap.length; i++)
		{
			affineLBNHeatMap[i] = 0;
		}
		
		affineNonLinearityHeatMap = new int[200];
		for(int i = 0; i < affineNonLinearityHeatMap.length; i++)
		{
			affineNonLinearityHeatMap[i] = 0;
		}
		
		//recursiveCallV2(0,0);
		//recursiveCallV3(0,0);
		recursiveCallV4(0,0);
		System.out.println("Total Found: " + numFound);
		
		System.out.println("\n\n\nProgram Over Printing final restults\n\n");
		System.out.println("Ammount of Cord Functions Found: " + numFound);
		System.out.println("Ammount of One Resilient Cord Functions Found: " + numFoundOneResilient);
		System.out.println("Ammount of Two Resilient Cord Functions Found: " + numFoundTwoResilient);
		System.out.println("Array List Size " + usableCordFunctions.size());
		System.out.println("countForSBoxFromCordFunctions " + countForSBoxFromCordFunctions);
		//System.out.println("S-Boxes with LBN > 2 found " + countForLBNGreaterThan2);
		printHeatMap();
		printHeatMapLBNOld();
		printHeatMapNonLinearity();
		printHeatMapLBNAffine();
	}
	
	private static void recursiveCall(int i, int j)
	{
		if(i == 5)
		{
			numFound++;
			if(numFound % 100000000 == 0)
			{
				System.out.println(numFound);
				printCordFunction(tempCordFunction);
			}
			return;
		}
		
		for(int k = 0; k < valuesRemaning.length; k++)
		{
			if(valuesRemaning[k] != 0)
			{
				tempCordFunction[i][j] = k;
				valuesRemaning[k]--;
				int tempj = j+1;
				int tempi = i;
				if(tempj == 5)
				{
					tempj = 0;
					tempi++;
				}
				recursiveCall(tempi,tempj);
				valuesRemaning[k]++;
				tempCordFunction[i][j] = -1;
			}
		}
	}
	/*
	 * Recusive call to create S-Boxes from 2-reslilent functions
	 * Uses recursion with global static data
	 */
	private static void recursiveCallV2(int i, int j)
	{
		if(i == 5)
		{
			numFound++;
			if (OneResilientBase5(tempCordFunction))
			{
				numFoundOneResilient++;
				if(TwoResilientBase5(tempCordFunction))
				{
					numFoundTwoResilient++;
					if( usableCordFunctions.size() > 0)
					{
						for(int index = 0; index < usableCordFunctions.size(); index++)
						{
							//System.out.println("hit");
							int[][] cordFunctionFromIndex = (int[][]) usableCordFunctions.get(index);
							if(isValidBase5Pow2SBox(tempCordFunction, cordFunctionFromIndex))
							{
								countForSBoxFromCordFunctions++;
								int tempLBNNumber = LinearBranch.calculateLinearBranchBase5Pow2(tempCordFunction, (int[][]) usableCordFunctions.get(index));
								//System.out.println("temp LBN Number " + tempLBNNumber);
								if(tempLBNNumber > 2)
								{
									countForLBNGreaterThan2++;
									//System.out.println("LBN value of " + tempLBNNumber + " found");
									heatMap[tempLBNNumber]++;
									//Below is affine transform code
									int[][][] affineTransform = AffineBase5Pow2.applyFullAffineTransform(tempCordFunction,(int[][]) usableCordFunctions.get(index), matrixInner, matrixOuter, vector1, vector2);
									System.out.println("Transform Successful");
									int tempLBNNumberAffine = LinearBranch.calculateLinearBranchBase5Pow2(affineToHighCordFunction(affineTransform), affineToLowCordFunction(affineTransform));
									affineLBNHeatMap[tempLBNNumberAffine]++;
									//System.out.println("High");
									//printCordFunction(affineToHighCordFunction(affineTransform));
									//System.out.println("Low");
									//printCordFunction(affineToLowCordFunction(affineTransform));
									int tempNonLinearityAffineNum = LinearBranch.calculateNonLineartyBase5Pow2(affineToHighCordFunction(affineTransform), affineToLowCordFunction(affineTransform));
									if(isValidBase5Pow2SBox(affineToHighCordFunction(affineTransform), affineToLowCordFunction(affineTransform)))
									{
										System.out.println("Affine transform resulted in valid S-Box");
									}
									
									//System.out.println("Printing Correlation Coeffiecnts Pre-Transform");
									//LinearBranch.printCorrelationCoefficentMatrixBase5Pow2(tempCordFunction, (int[][]) usableCordFunctions.get(index));
									//System.out.println("Printing Correltaiton Coefficents Post-Transform");
									//LinearBranch.printCorrelationCoefficentMatrixBase5Pow2(affineToHighCordFunction(affineTransform), affineToLowCordFunction(affineTransform));
									affineNonLinearityHeatMap[tempNonLinearityAffineNum]++;
								}
								int tempNonLinearityNum = LinearBranch.calculateNonLineartyBase5Pow2(tempCordFunction, (int[][]) usableCordFunctions.get(index));
								heatMapNonLinearity[tempNonLinearityNum]++;
								
							}
							//System.out.println("index " + index);
							//printCordFunction(cordFunctionFromIndex);
						}
						usableCordFunctions.add(copyCordFunction(tempCordFunction));
					}
					else
					{
						//countForSBoxFromCordFunctions++;
						usableCordFunctions.add(copyCordFunction(tempCordFunction));
					}
				}
			}
			if(numFound % 100000000 == 0)
			{
				System.out.println("\n\nNewCycle\n----------------------------------------------------------------------------\n");
				System.out.println("Ammount of Cord Functions Found: " + numFound);
				System.out.println("Ammount of One Resilient Cord Functions Found: " + numFoundOneResilient);
				System.out.println("Ammount of Two Resilient Cord Functions Found: " + numFoundTwoResilient);
				System.out.println("Array List Size " + usableCordFunctions.size());
				System.out.println("countForSBoxFromCordFunctions " + countForSBoxFromCordFunctions);
				//System.out.println("S-Boxes with LBN > 2 found " + countForLBNGreaterThan2);
				printHeatMap();
				printHeatMapNonLinearity();
				printHeatMapLBNAffine();
				printHeatMapNonLinearityAffine();
				//printCordFunction(tempCordFunction);
			}
			return;
		}
		for(int k = 0; k < valuesRemaningV2[i].length; k++)
		{
			if(valuesRemaningV2[i][k] != 0)
			{
				tempCordFunction[i][j] = k;
				valuesRemaningV2[i][k]--;
				int tempj = j+1;
				int tempi = i;
				if(tempj == 5)
				{
					tempj = 0;
					tempi++;
				}
				recursiveCallV2(tempi,tempj);
				valuesRemaningV2[i][k]++;
				tempCordFunction[i][j] = -1;
			}
		}
	}
	
	/*
	 * Uses recursion with global variables to create S-Boxes from 1-reslient coordanate fucntions.
	 */
	private static void recursiveCallV3(int i, int j)
	{
		if(i == 5)
		{
			numFound++;
			if (OneResilientBase5(tempCordFunction))
			{
				numFoundOneResilient++;
				if(usableCordFunctions.size() > 0)
				{
					for(int index = 0; index < usableCordFunctions.size(); index++)
					{
						int[][] cordFunctionFromIndex = (int[][]) usableCordFunctions.get(index);
						if(isValidBase5Pow2SBox(tempCordFunction, cordFunctionFromIndex))
						{
							countForSBoxFromCordFunctions++;
							int tempLBNNumber = LinearBranch.calculateLinearBranchBase5Pow2(tempCordFunction, (int[][]) usableCordFunctions.get(index));
							int tempLBNNumberOld = LinearBranch.calculateLinearBranchBase5Pow2Old(tempCordFunction, (int[][]) usableCordFunctions.get(index));
							//System.out.println("temp LBN Number " + tempLBNNumber);
							heatMap[tempLBNNumber]++;
							heatMapLBNOld[tempLBNNumberOld]++;
							int tempNonLinearityNum = LinearBranch.calculateNonLineartyBase5Pow2(tempCordFunction, (int[][]) usableCordFunctions.get(index));
							heatMapNonLinearity[tempNonLinearityNum]++;
						}
					}
				}
				usableCordFunctions.add(copyCordFunction(tempCordFunction));
				/*
				if(TwoResilientBase5(tempCordFunction))
				{
					numFoundTwoResilient++;
					if( usableCordFunctions.size() > 0)
					{
						for(int index = 0; index < usableCordFunctions.size(); index++)
						{
							//System.out.println("hit");
							int[][] cordFunctionFromIndex = (int[][]) usableCordFunctions.get(index);
							if(isValidBase5Pow2SBox(tempCordFunction, cordFunctionFromIndex))
							{
								countForSBoxFromCordFunctions++;
								int tempLBNNumber = LinearBranch.calculateLinearBranchBase5Pow2(tempCordFunction, (int[][]) usableCordFunctions.get(index));
								System.out.println("temp LBN Number " + tempLBNNumber);
								if(tempLBNNumber > 2)
								{
									countForLBNGreaterThan2++;
									System.out.println("LBN value of " + tempLBNNumber + " found");
									heatMap[tempLBNNumber]++;
								}
							}
							//System.out.println("index " + index);
							//printCordFunction(cordFunctionFromIndex);
						}
						usableCordFunctions.add(copyCordFunction(tempCordFunction));
					}
					else
					{
						//countForSBoxFromCordFunctions++;
						usableCordFunctions.add(copyCordFunction(tempCordFunction));
					}
				}
				*/
			}
			if(numFound % 100000000 == 0)
			{
				System.out.println("Ammount of Cord Functions Found: " + numFound);
				System.out.println("Ammount of One Resilient Cord Functions Found: " + numFoundOneResilient);
				//System.out.println("Ammount of Two Resilient Cord Functions Found: " + numFoundTwoResilient);
				System.out.println("Array List Size " + usableCordFunctions.size());
				System.out.println("countForSBoxFromCordFunctions " + countForSBoxFromCordFunctions);
				//System.out.println("S-Boxes with LBN > 2 found " + countForLBNGreaterThan2);
				printHeatMap();
				printHeatMapLBNOld();
				//printCordFunction(tempCordFunction);
				//printHeatMap();
				printHeatMapNonLinearity();
			}
			return;
		}
		for(int k = 0; k < valuesRemaningV2[i].length; k++)
		{
			if(valuesRemaningV2[i][k] != 0)
			{
				tempCordFunction[i][j] = k;
				valuesRemaningV2[i][k]--;
				int tempj = j+1;
				int tempi = i;
				if(tempj == 5)
				{
					tempj = 0;
					tempi++;
				}
				recursiveCallV3(tempi,tempj);
				valuesRemaningV2[i][k]++;
				tempCordFunction[i][j] = -1;
			}
		}
	}
	
	private static void recursiveCallV4(int i, int j)
	{
		if(i == 5)
		{
			//System.out.println("hit");
			numFound++;
			if(numFound % 10000 == 0)
			{
				if(usableCordFunctions.size() > 0)
				{
					for(int index = 0; index < usableCordFunctions.size(); index++)
					{
						int[][] cordFunctionFromIndex = (int[][]) usableCordFunctions.get(index);
						if(isValidBase5Pow2SBox(tempCordFunction, cordFunctionFromIndex))
						{
							countForSBoxFromCordFunctions++;
							int tempLBNNumber = LinearBranch.calculateLinearBranchBase5Pow2(tempCordFunction, (int[][]) usableCordFunctions.get(index));
							//System.out.println("temp LBN Number " + tempLBNNumber);
							heatMap[tempLBNNumber]++;
							int tempNonLinearityNum = LinearBranch.calculateNonLineartyBase5Pow2(tempCordFunction, (int[][]) usableCordFunctions.get(index));
							heatMapNonLinearity[tempNonLinearityNum]++;
							int tempLBNNumberOld = LinearBranch.calculateLinearBranchBase5Pow2Old(tempCordFunction, (int[][]) usableCordFunctions.get(index));
							heatMapLBNOld[tempLBNNumberOld]++;
						}
					}
				}
				usableCordFunctions.add(copyCordFunction(tempCordFunction));
			}
				/*
				if(TwoResilientBase5(tempCordFunction))
				{
					numFoundTwoResilient++;
					if( usableCordFunctions.size() > 0)
					{
						for(int index = 0; index < usableCordFunctions.size(); index++)
						{
							//System.out.println("hit");
							int[][] cordFunctionFromIndex = (int[][]) usableCordFunctions.get(index);
							if(isValidBase5Pow2SBox(tempCordFunction, cordFunctionFromIndex))
							{
								countForSBoxFromCordFunctions++;
								int tempLBNNumber = LinearBranch.calculateLinearBranchBase5Pow2(tempCordFunction, (int[][]) usableCordFunctions.get(index));
								System.out.println("temp LBN Number " + tempLBNNumber);
								if(tempLBNNumber > 2)
								{
									countForLBNGreaterThan2++;
									System.out.println("LBN value of " + tempLBNNumber + " found");
									heatMap[tempLBNNumber]++;
								}
							}
							//System.out.println("index " + index);
							//printCordFunction(cordFunctionFromIndex);
						}
						usableCordFunctions.add(copyCordFunction(tempCordFunction));
					}
					else
					{
					//countForSBoxFromCordFunctions++;
						usableCordFunctions.add(copyCordFunction(tempCordFunction));
					}
				}
				*/
			if(numFound % 10000000 == 0)
			{
				System.out.println("Ammount of Cord Functions Found: " + numFound);
				//System.out.println("Ammount of One Resilient Cord Functions Found: " + numFoundOneResilient);
				//System.out.println("Ammount of Two Resilient Cord Functions Found: " + numFoundTwoResilient);
				System.out.println("Array List Size " + usableCordFunctions.size());
				System.out.println("countForSBoxFromCordFunctions " + countForSBoxFromCordFunctions);
				//System.out.println("S-Boxes with LBN > 2 found " + countForLBNGreaterThan2);
				printHeatMap();
				printHeatMapLBNOld();
				printHeatMapNonLinearity();
				//printCordFunction(tempCordFunction);
			}
			return;
		}
		for(int k = 0; k < valuesRemaningV2[i].length; k++)
		{
			if(valuesRemaningV2[i][k] != 0)
			{
				tempCordFunction[i][j] = k;
				valuesRemaningV2[i][k]--;
				int tempj = j+1;
				int tempi = i;
				if(tempj == 5)
				{
					tempj = 0;
					tempi++;
				}
				recursiveCallV4(tempi,tempj);
				valuesRemaningV2[i][k]++;
				tempCordFunction[i][j] = -1;
			}
		}
	}
	
	
	private static void printCordFunction(int[][] cordFunction)
	{
		for(int i = 0; i < cordFunction.length; i++)
		{
			for(int j = 0; j < cordFunction[i].length; j++)
			{
				System.out.print(cordFunction[i][j] + "\t");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	private static boolean OneResilientBase5(int[][] cordFunction)
	{
		return testResilienceNewWt1(cordFunction);
		/*
		if(textAlphaBase5(cordFunction,1,0) == false)
			return false;
		if(textAlphaBase5(cordFunction,4,0) == false)
			return false;
		
		if(textAlphaBase5(cordFunction,0,1) == false)
			return false;
		if(textAlphaBase5(cordFunction,0,4) == false)
			return false;
		return true;
		*/
	}
	
	private static boolean TwoResilientBase5(int[][] cordFunction)
	{
		return testResilienceNewWt2(cordFunction);
		/*
		if(textAlphaBase5(cordFunction,2,0) == false)
			return false;
		if(textAlphaBase5(cordFunction,3,0) == false)
			return false;
		if(textAlphaBase5(cordFunction,0,2) == false)
			return false;
		if(textAlphaBase5(cordFunction,0,3) == false)
			return false;
		if(textAlphaBase5(cordFunction,1,1) == false)
			return false;
		if(textAlphaBase5(cordFunction,1,4) == false)
			return false;
		if(textAlphaBase5(cordFunction,4,1) == false)
			return false;
		if(textAlphaBase5(cordFunction,4,4) == false)
			return false;
		return true;
		*/
	}
	
	private static boolean testResilienceNewWt1(int[][] cordFunction)
	{
		for(int i = 0; i < cordFunction.length; i++)
		{
			if(textAlphaBase5(cordFunction,i,0) == false)
				return false;
			if(textAlphaBase5(cordFunction,0,i) == false)
				return false;
		}
		return true;
	}
	private static boolean testResilienceNewWt2(int[][] cordFunction)
	{
		for(int i = 0; i < cordFunction.length; i++)
		{
			for(int j = 0; j < cordFunction.length; j++)
			{
				if(textAlphaBase5(cordFunction,i,j) == false)
					return false;
			}
		}
		return true;
	}
	
	private static boolean textAlphaBase5(int[][] cordFunction, int alphaHigh,int alphaLow)
	{
		int[] valuesCount = new int[5];
		
		for(int i = 0; i < valuesCount.length; i++)
		{
			valuesCount[i] = 0;
		}
		
		for(int i = 0; i < cordFunction.length; i++)
		{
			for(int j = 0; j < cordFunction.length; j++)
			{
				int tempVal = expCalculationResilientBase5(cordFunction, i, j, alphaHigh, alphaLow);
				valuesCount[tempVal]++;
			}
		}
		
		int expectedVal = 5;
		
		for(int i = 0; i < valuesCount.length; i++)
		{
			if(expectedVal != valuesCount[i])
			{
				return false;
			}
		}
		return true;
	}
	
	private static int expCalculationResilientBase5(int[][] cordFunction, int xHigh, int xLow, int alphaHigh, int alphaLow)
	{
		int tempVal = cordFunction[xHigh][xLow];
		tempVal += xHigh*alphaHigh;
		tempVal += xLow*alphaLow;
		tempVal = tempVal % 5;
		
		return tempVal;
	}
	
	private static boolean isValidBase5Pow2SBox(int[][] cordFunction1, int[][] cordFunction2)
	{
		int[][] remaning = new int[5][5];
		
		for(int i = 0; i < remaning.length; i++)
		{
			for(int j = 0; j < remaning[i].length; j++)
			{
				remaning[i][j] = 1;
			}
		}
		for(int i = 0; i < remaning.length; i++)
		{
			for(int j = 0; j < remaning[i].length; j++)
			{
				remaning[cordFunction1[i][j]][cordFunction2[i][j]]--;
			}
		}
		
		for(int i = 0; i < remaning.length; i++)
		{		
			for(int j = 0; j < remaning[i].length; j++)
			{
				if( remaning[i][j] != 0)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	private static int[][] copyCordFunction(int[][] input)
	{
		int[][] arrOut = new int[input.length][input.length];
		
		for(int i = 0; i < input.length; i++)
		{
			for(int j = 0; j < input.length; j++)
			{
				arrOut[i][j] = input[i][j];
			}
		}
		
		return arrOut;
	}
	
	private static void printHeatMap()
	{
		System.out.println("Printing HeatMap for LBN:");
		for(int i = 0; i < heatMap.length; i++)
		{
			if(heatMap[i] != 0)
			{
				System.out.println("LBN value of " + i + " has a count of: " + heatMap[i]);
			}
		}
	}
	
	private static void printHeatMapLBNOld()
	{
		System.out.println("Printing HeatMap for LBN Old version:");
		for(int i = 0; i < heatMapLBNOld.length; i++)
		{
			if(heatMapLBNOld[i] != 0)
			{
				System.out.println("Old LBN value of " + i + " has a count of: " + heatMapLBNOld[i]);
			}
		}
	}
	
	private static void printHeatMapNonLinearity()
	{
		System.out.println("Printing HeatMap for NonLinearity:");
		for(int i = 0; i < heatMapNonLinearity.length; i++)
		{
			if(heatMapNonLinearity[i] != 0)
			{
				System.out.println("Non-Linearity value of " + i + " has a count of: " + heatMapNonLinearity[i]);
			}
		}
	}
	
	private static void printHeatMapLBNAffine()
	{
		System.out.println("Printing out Affine LBN heatmap");
		for(int i = 0; i < affineLBNHeatMap.length; i++)
		{
			if(affineLBNHeatMap[i] != 0)
			{
				System.out.println("After Affine Transform LBN of " + i + " has a count of " + affineLBNHeatMap[i]);
			}
		}
		
	}
	
	private static void printHeatMapNonLinearityAffine()
	{
		System.out.println("Printing out Affine Non-Lienarity heatmap");
		for(int i = 0; i < affineLBNHeatMap.length; i++)
		{
			if(affineNonLinearityHeatMap[i] != 0)
			{
				System.out.println("After Affine Transform non-linearity of " + i + " has a count of " + affineNonLinearityHeatMap[i]);
			}
		}
		
	}
	
	private static int[][] affineToHighCordFunction(int[][][] sBox)
	{
		int[][] returnVal = new int[5][5];
		for(int i = 0; i < returnVal.length; i++)
		{
			for(int j = 0; j < returnVal.length; j++)
			{
				returnVal[i][j] = sBox[0][i][j];
			}
		}
		return returnVal;
	}
	
	private static int[][] affineToLowCordFunction(int[][][] sBox)
	{
		int[][] returnVal = new int[5][5];
		for(int i = 0; i < returnVal.length; i++)
		{
			for(int j = 0; j < returnVal.length; j++)
			{
				returnVal[i][j] = sBox[1][i][j];
			}
		}
		return returnVal;
	}

}
