
public class TestANF {

	public static void main(String[] args) {
		
		int[][] arr1 = new int[5][5];
		
		for(int i = 0; i < arr1.length; i++)
		{
			for(int j = 0; j < arr1[i].length; j++)
			{
				arr1[i][j] = (i+j) % 5;
			}
		}
		
		int[][] poly1 = ANFPolynomials.cordFuncitonBase5Pow2ToPoly(arr1);
		
		System.out.println("TEST arr1");
		System.out.println(ANFPolynomials.testPolyBase5Pow2(arr1, poly1));
		
		ANFPolynomials.printPolynomial(poly1);
		
		
		
		//int[][] arr2 = new int[5][5];
		//int[][] arr2 = { {3,3,3,3,3}, {2,2,2,2,2},{3,3,3,3,3},{4,4,4,4,4},{0,0,0,0,0} };
		//arr2[0]  = {0,4,2,1,3};
		//arr2[1]  = {1,2,4,3,0};
		//arr2[2]  = {1,2,3,0,4};
		//arr2[3]  = {0,1,2,3,4};
		//arr2[4]  = {4,3,2,1,0};
		
		//System.out.println("TEST arr2");
		//int[][] poly2 = ANFPolynomials.cordFuncitonBase5Pow2ToPoly(arr2);
		//System.out.println(ANFPolynomials.testPolyBase5Pow2(arr2, poly2));
		
		//ANFPolynomials.printPolynomial(poly2);
		
		int[][] poly3 = { {0,0,0,0,0}, {1,1,1,1,1}, {2,1,0,0,0}, {0,0,0,0,0}, {0,0,0,0,0} };
		
		int[][] arr3 = ANFPolynomials.polyToLookUpBase5Pow2(poly3);
		int[][] poly3Rem = ANFPolynomials.cordFuncitonBase5Pow2ToPoly(arr3);
		ANFPolynomials.printPolynomial(poly3);
		System.out.println("\n\n");
		ANFPolynomials.printPolynomial(poly3Rem);

	}

}
