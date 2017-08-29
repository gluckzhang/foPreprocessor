package me.gluckzhang.main;


public class Test {
    public static void main(java.lang.String[] args) {
        int[] testArray = new int[]{ 100 , 200 , 300 , 400 , 500 };
        int index = 6;
        int tmp = (int)foArrayRead(testArray, 2) + 100;
        java.lang.System.out.println("init an int array: {100, 200, 300, 400, 500}");
        java.lang.System.out.println("local variable index: 6");
        java.lang.System.out.println("----array read test began----");
        java.lang.System.out.println(("local variable tmp = testArray[2] + 100: " + tmp));
        java.lang.System.out.println(("testArray[4]: " + foArrayRead(testArray, 4)));
        java.lang.System.out.println(("testArray[5]: (failure here) " + foArrayRead(testArray, 5)));
        java.lang.System.out.println(("testArray[5]: (failure here) " + foArrayRead(testArray, index)));
        try {
            java.lang.System.out.println(("testArray[index]: (failure here, because in a wrong try block) " + foArrayRead(testArray, index)));
        } catch (java.lang.IllegalArgumentException e) {
            java.lang.System.out.println("catch an IllegalArgumentException when print testArray[index]");
        }
        try {
            java.lang.System.out.println(testArray[index]);
        } catch (java.lang.IndexOutOfBoundsException e) {
            java.lang.System.out.println("IndexOutOfBoundsException catch block when print testArray[index]");
        }
        try {
            java.lang.System.out.println(testArray[index]);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            java.lang.System.out.println("ArrayIndexOutOfBoundsException catch block when print testArray[index]");
        }
        java.lang.System.out.println("----array read test ended----");
        java.lang.System.out.println("----array write test began----");
        java.lang.System.out.println("set testArray[4] = 555");
        foArrayWrite(testArray, 4, 555);
        java.lang.System.out.println("set testArray[5] = 666 + 777 (failure here)");
        foArrayWrite(testArray, 5, 666 + 777);
        java.lang.System.out.println("set testArray[6] = tmp + 666 (failure here)");
        foArrayWrite(testArray, 6, tmp + 666);
        java.lang.System.out.println("set testArray[index] = 888 (failure here)");
        foArrayWrite(testArray, index, 888);
        java.lang.System.out.println("----array read test began----");
    }

    public static java.lang.Object foArrayRead(java.lang.Object original, int index) {
        if ((index < 0) || (index > ((java.lang.reflect.Array.getLength(original)) - 1))) {
            java.lang.System.out.println((("Failure-oblivious mechanis triggered! invalid index: " + index) + ", return the first element"));
            return java.lang.reflect.Array.get(original, 0);
        }else {
            return java.lang.reflect.Array.get(original, index);
        }
    }

    public static void foArrayWrite(java.lang.Object original, int index, java.lang.Object newValue) {
        if ((index < 0) || (index > ((java.lang.reflect.Array.getLength(original)) - 1))) {
            java.lang.System.out.println((("Failure-oblivious mechanis triggered! invalid index: " + index) + ", ingore this write option"));
        }else {
            java.lang.reflect.Array.set(original, index, newValue);
        }
    }
}

