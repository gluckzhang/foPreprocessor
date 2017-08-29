package me.gluckzhang.template;

import spoon.template.ExtensionTemplate;

public class ArrayWriteTemplate extends ExtensionTemplate {
	public static void foArrayWrite(Object original, int index, Object newValue) {
		if (index < 0 || index > java.lang.reflect.Array.getLength(original) - 1) {
			System.out.println("Failure-oblivious mechanis triggered! invalid index: " + index + ", ingore this write option");
		} else {
			java.lang.reflect.Array.set(original, index, newValue);
		}
	}
}
