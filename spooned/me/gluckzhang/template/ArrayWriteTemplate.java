package me.gluckzhang.template;


public class ArrayWriteTemplate extends spoon.template.ExtensionTemplate {
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

