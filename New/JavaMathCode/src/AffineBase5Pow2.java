
public class AffineBase5Pow2 {
	private static final int BASE = 5;
	private static final int POW = 2;
	
	public static int[] applyMatrix(int[][] affineMatrix, int[] vectorVal)
	{
		int[] returnVal = new int[2];
		
		for(int i = 0; i < affineMatrix.length; i++)
		{
			returnVal[i] = 0;
			for(int j = 0; j < affineMatrix[i].length; j++)
			{
				returnVal[i] += affineMatrix[i][j] * vectorVal[j];
			}
			returnVal[i] = returnVal[i] % BASE;
		}
		
		return returnVal;
	}
	
	public static int[] addVectors(int[] vector1, int[] vector2)
	{
		if( (vector1.length != POW) || (vector2.length != POW))
		{
			System.err.print("Error in vector size");
			System.exit(-1);
		}
		
		int[] returnVal = new int[POW];
		
		for(int i = 0; i < returnVal.length; i++)
		{
			returnVal[i] = vector1[i] + vector2[i];
			returnVal[i] = returnVal[i] % BASE;
		}
		
		return returnVal;
	}
	
	public static int[] applySBox(int[][] cordFunctionHigh, int[][] cordFunctionLow, int[] vectorInput)
	{
		int[] returnVal = new int[POW];
		
		returnVal[0] = cordFunctionHigh[vectorInput[0]][vectorInput[1]];
		returnVal[1] = cordFunctionLow[vectorInput[0]][vectorInput[1]];
		
		return returnVal;
	}
	
	public static int[] applyAffineTransformOnInput(int[][] cordFunctionHigh, int[][] cordFunctionLow, int[][] matrixInner, int[][] matrixOuter, int[] vectorInner, int[] vectorOuter, int[] vectorInput)
	{
		int[] returnVal = new int[POW];
		
		//printVector(vectorInput);
		int[] vectorAfterInnerMatrix = applyMatrix(matrixInner, vectorInput);
		//printVector(vectorAfterInnerMatrix);
		int[] vectorBeforeSBoxInput = addVectors(vectorAfterInnerMatrix,vectorInner);
		//printVector(vectorBeforeSBoxInput);
		int[] outputFromSBox = applySBox(cordFunctionHigh, cordFunctionLow, vectorBeforeSBoxInput);
		//printVector(outputFromSBox);
		int[] afterOuterMatrix = applyMatrix(matrixOuter,outputFromSBox);
		//printVector(afterOuterMatrix);
		int[] valueToBeReturned = addVectors(afterOuterMatrix,vectorOuter);
		//printVector(valueToBeReturned);
		
		return valueToBeReturned;
	}
	
	public static int[][][] applyFullAffineTransform(int[][] cordFunctionHigh, int[][] cordFunctionLow, int[][] matrixInner, int[][] matrixOuter, int[] vectorInner, int[] vectorOuter)
	{
		int[][][] sBoxReturnVal = new int[POW][BASE][BASE];
		
		//Initlization
		for(int i = 0; i < sBoxReturnVal.length ; i++)
		{
			for(int j = 0; j < sBoxReturnVal[i].length; j++)
			{
				for(int k = 0; k < sBoxReturnVal[i][j].length; k++)
				{
					sBoxReturnVal[i][j][k] = -1;
				}
			}
		}
		
		//ApplyingTransform
		for(int i = 0; i < BASE; i++)
		{
			for(int j = 0; j < BASE; j++)
			{
				int[] tempVector = {i,j};
				int[] afterTransform = applyAffineTransformOnInput(cordFunctionHigh, cordFunctionLow, matrixInner, matrixOuter, vectorInner, vectorOuter, tempVector);
				sBoxReturnVal[0][i][j] = afterTransform[0];
				sBoxReturnVal[1][i][j] = afterTransform[1];
				//System.out.println("after transform " + i + "," + j + " -> " + afterTransform[0] + "," + afterTransform[1]);
			}
		}
		
		//ErrorCheck
		for(int i = 0; i < sBoxReturnVal.length ; i++)
		{
			for(int j = 0; j < sBoxReturnVal[i].length; j++)
			{
				for(int k = 0; k < sBoxReturnVal[i][j].length; k++)
				{
					if (sBoxReturnVal[i][j][k] == -1)
					{
						System.err.println("Error in affine transfrom\nExiting program");
						System.exit(-1);
					}
				}
			}
		}
		
		return sBoxReturnVal;
	}

	public static void printVector(int[] vector)
	{
		for(int i = 0; i < vector.length; i++)
		{
			System.out.print(vector[i] + "\t");
		}
		System.out.println("\n");
	}
}
