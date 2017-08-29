package me.gluckzhang.main;

public class Test {

	public static void main(String[] args) {
		int[] testArray = {100, 200, 300, 400, 500};
		int index = 6;
		int tmp = testArray[2] + 100;
		System.out.println("init an int array: {100, 200, 300, 400, 500}");
		System.out.println("local variable index: 6");
		
		System.out.println("----array read test began----");
		
		System.out.println("local variable tmp = testArray[2] + 100: " + tmp);
		System.out.println("testArray[4]: " + testArray[4]);
		System.out.println("testArray[5]: (failure here) " + testArray[5]); // failure
		System.out.println("testArray[5]: (failure here) " + testArray[index]); // failure

		try {
			System.out.println("testArray[index]: (failure here, because in a wrong try block) " + testArray[index]);
		} catch (IllegalArgumentException e) {
			System.out.println("catch an IllegalArgumentException when print testArray[index]");
		}
		
		try {
			System.out.println(testArray[index]);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException catch block when print testArray[index]");
		}
		
		try {
			System.out.println(testArray[index]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException catch block when print testArray[index]");
		}
		
		System.out.println("----array read test ended----");
		
		System.out.println("----array write test began----");
		
		System.out.println("set testArray[4] = 555");
		testArray[4] = 555;
		System.out.println("set testArray[5] = 666 + 777 (failure here)");
		testArray[5] = 666 + 777; // failure
		System.out.println("set testArray[6] = tmp + 666 (failure here)");
		testArray[6] = tmp + 666; // failure
		System.out.println("set testArray[index] = 888 (failure here)");
		testArray[index] = 888; // failure
		
		System.out.println("----array read test began----");
	}
}