
public class ANFPolynomials {
	private static int[] recursionOneDigitArr;
	private static int numPassed;
	private static int[][] fullRecursionArr;
	private static int[][] var2NeededValues;
	private static final int BASE = 5;
	private static boolean truthSave;

	
	public static int[][] cordFuncitonBase5Pow2ToPoly(int[][] cordFunction)
	{
		int[][] returnArr = new int[5][5];
		
		for(int i = 0; i < returnArr.length; i++)
		{
			for(int j = 0; j < returnArr[i].length; j++)
			{
				returnArr[i][j] = 0;
			}
		}
		
		//Adding MSD code below.
		
		int[] MSDNeeded = new int[5];
		for(int i = 0; i < cordFunction.length; i++)
		{
			MSDNeeded[i] = cordFunction[i][0];
			System.out.println("num needed " + MSDNeeded[i]);
		}
		
		//System.exit(0);
		
		int[] MSDPoly = SingleVarableComputaitonBase5(MSDNeeded);
		
		System.out.println("Printing out data for MSD");
		for(int i = 0; i < returnArr.length; i++)
		{
			System.out.println(MSDPoly[i]);
			returnArr[i][0] = MSDPoly[i];
		}
		
		int[] LSDNeeded = new int[5];
		for(int i = 0; i < cordFunction.length; i++)
		{
			LSDNeeded[i] = cordFunction[0][i];
			System.out.println("num needed " + LSDNeeded[i]);
		}
		
		//System.exit(0);
		
		int[] LSDPoly = SingleVarableComputaitonBase5(LSDNeeded);
		
		System.out.println("Printing out data for LSD");
		for(int i = 0; i < returnArr.length; i++)
		{
			System.out.println(LSDPoly[i]);
			returnArr[0][i] = LSDPoly[i];
		}
		
		System.out.println("Starting Comptuation for full resursion Arr");
		fullRecursionArr = new int[5][5];
		var2NeededValues = new int[5][5];
		for(int i = 0; i < fullRecursionArr.length; i++)
		{
			for(int j = 0; j < fullRecursionArr[i].length; j++)
			{
				fullRecursionArr[i][j] = -1;
				var2NeededValues[i][j] = cordFunction[i][j] % BASE;
			}
		}
		for(int i = 0; i < fullRecursionArr.length; i++)
		{
			fullRecursionArr[i][0] = returnArr[i][0];
			fullRecursionArr[0][i] = returnArr[0][i];
		}
		System.out.println("initlized full recursion array");
		
		System.out.println("Begenning 2Var Computation");
		
		truthSave = false;
		
		System.out.println("Computation complete, Truth Value: " + DoubleVarableComputaitonBase5());
		
		for(int i = 0; i < BASE; i++)
		{
			for(int j = 0; j < BASE; j++)
			{
				returnArr[i][j] = fullRecursionArr[i][j];
			}
		}
		
		System.out.println("Test within ANF code Begennign");
		
		System.out.println("Output of test is " + testPolyBase5Pow2(cordFunction,returnArr));
		
		
		return returnArr;
	}
	
	public static boolean testPolyBase5Pow2(int[][] cordFunction, int[][] polynomial)
	{
		//boolean truthVal = true;
		
		for(int i = 0; i < polynomial.length; i++)
		{
			for(int j = 0; j < polynomial.length; j++)
			{
				int[] vector = {i,j};
				if(cordFunction[i][j] != applyPolyBase5Pow2(vector, polynomial))
				{
					System.err.println("Error i " + i + " and j " + j);
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static int[][] polyToLookUpBase5Pow2(int[][] poly)
	{
		int[][] returnArr = new int[BASE][BASE];
		
		for(int i = 0; i < poly.length; i++)
		{
			for(int j = 0; j < poly[i].length; j++)
			{
				int[] vector = {i,j};
				returnArr[i][j] = applyPolyBase5Pow2(vector, poly);
			}
		}
		
		return returnArr;
	}
	
	public static int applyPolyBase5Pow2(int[] vector, int[][] polynomial)
	{
		int returnVal = 0;
		for(int i = 0; i < polynomial.length; i++)
		{
			for(int j = 0; j < polynomial.length; j++)
			{
				int temp1 = (int) Math.pow((double) vector[0], (double) i);
				temp1 = temp1 % 5;
				int temp2 = (int) Math.pow((double) vector[1], (double) j);
				temp2 = temp2 % 5;
				int temp3 = temp1*temp2*polynomial[i][j];
				temp3 = temp3 % BASE;
				
				returnVal += temp3;
			}
			returnVal = returnVal % BASE;
		}
		
		
		return returnVal;
	}
	
	public static void printPolynomial(int[][] polynomial)
	{
		for(int i = 0; i < polynomial.length; i++)
		{
			for(int j = 0; j < polynomial[i].length; j++)
			{
				if(polynomial[i][j] != 0)
				{
					System.out.print(polynomial[i][j] + "x_1^" + i + "x_2^" + j + "\t");
				}
			}
		}
	}
	
	public static int[] SingleVarableComputaitonBase5(int[] neededValues)
	{
		System.out.println("entered single Variable");
		int[] returnArr = new int[5];
		
		numPassed = 0;
		
		recursionOneDigitArr = new int[5];
		for(int i = 0; i < recursionOneDigitArr.length; i++)
		{
			recursionOneDigitArr[i] = 0;
		}
		
		singleVarRecurions(neededValues,0);
		
		for(int i = 0; i < neededValues.length; i++)
		{
			returnArr[i] = recursionOneDigitArr[i];
		}
		
		return returnArr;
	}
	
	public static boolean DoubleVarableComputaitonBase5()
	{
		//We assume the single var compuation is done and does not need to be checked.
		//System.out.println("Entered Double Variable");
		boolean returnVal = true;
		
		
		doubleVarRecurions(4,4);
		
		return truthSave;
		
	}
	
	private static void singleVarRecurions(int[] neededValues, int debth)
	{
		for(int i = 0; i < BASE; i++)
		{
			recursionOneDigitArr[debth] = i; 
			
			if(debth == 4)//last number
			{
				if(testSingleVariable(neededValues))
				{
					return;
				}
			}
			else
			{
				singleVarRecurions(neededValues, debth+1);
				if(testSingleVariable(neededValues))
				{
					return;
				}
			}
		}
		
	}
	
	private static void doubleVarRecurions(int debthI, int debthJ)
	{
		if(debthI == 3 && debthJ == 1)
		{
			System.out.println("Status update");
		}
		
		for(int i = 0; i < BASE; i++)
		{
			
			fullRecursionArr[debthI][debthJ] = i;
			if( (debthI == 1) && (debthJ == 1)) //Final entry in array
			{
				//System.out.println("final entry in double recursion");
				//System.out.print("Truth val " + testDoubleVariableDebug());
				//System.exit(0);
				if(testDoubleVariable())
				{
					truthSave = true;
					return;
				}
			}
			else//is not the final entry
			{
				if(debthJ == 1)
				{
					doubleVarRecurions(debthI - 1, 4);
					if(truthSave == true)
					{
						return;
					}
				}
				else
				{
					doubleVarRecurions(debthI, debthJ - 1);
					if(truthSave == true)
					{
						return;
					}
				}
			}
		}
	}
	
	
	
	private static boolean testSingleVariable(int[] neededValues)
	{
		for(int i = 0; i < neededValues.length; i++)
		{
			int temp1 = recursionOneDigitArr[0];
			for(int j = 1; j < BASE; j++)
			{
				temp1 += recursionOneDigitArr[j] * ((int) Math.pow((double) i, (double) j));
				temp1 = temp1 % BASE;
			}
			if (temp1 != neededValues[i])
			{
				//System.out.println("Failed");
				//System.out.println("num passed ; " + numPassed);
				return false;
			}
			else
			{
				//System.out.println("Passed Itteration");
				numPassed++;
			}
		}
		//System.out.println("success");
		return true;
		
	}
	
	public static boolean testDoubleVariable()
	{
		//We assume single varaible has already been passed.
		for(int x1 = 1; x1 < BASE; x1++)
		{
			for(int x2 = 1; x2 < BASE; x2++)
			{
				int runningTotal = 0;
				for(int i = 0; i < BASE; i++)
				{
					for(int j = 0; j < BASE; j++)
					{
						if(fullRecursionArr[i][j] != 0)
						{
							runningTotal += fullRecursionArr[i][j] * ((int) Math.pow((double) x1, (double) i)) * ((int) Math.pow((double) x2, (double) j));
							runningTotal = runningTotal % BASE;
						}
					}
				}
				if(runningTotal != var2NeededValues[x1][x2])
				{
					return false;
				}
			}
		}
		return true;
		
	}
	
	public static boolean testDoubleVariableDebug()
	{
		//We assume single varaible has already been passed.
		for(int x1 = 1; x1 < BASE; x1++)
		{
			for(int x2 = 1; x2 < BASE; x2++)
			{
				int runningTotal = 0;
				for(int i = 0; i < BASE; i++)
				{
					for(int j = 0; j < BASE; j++)
					{
						if(fullRecursionArr[i][j] != 0)
						{
							runningTotal += fullRecursionArr[i][j] * ((int) Math.pow((double) x1, (double) i)) * ((int) Math.pow((double) x2, (double) j));
							runningTotal = runningTotal % BASE;
						}
					}
				}
				if(runningTotal != var2NeededValues[x1][x2])
				{
					System.out.println("Failed at x1 - "  + x1 + " and x2 at - " + x2);
					System.out.println("needed value " + var2NeededValues[x1][x2]);
					System.out.println("Running total  - " + runningTotal);
					return false;
				}
			}
		}
		return true;
		
	}
}
