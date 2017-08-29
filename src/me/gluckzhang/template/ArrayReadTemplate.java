package me.gluckzhang.template;

import spoon.template.ExtensionTemplate;

public class ArrayReadTemplate extends ExtensionTemplate {
	public static Object foArrayRead(Object original, int index) {
		if (index < 0 || index > java.lang.reflect.Array.getLength(original) - 1) {
			System.out.println("Failure-oblivious mechanis triggered! invalid index: " + index + ", return the first element");
			return java.lang.reflect.Array.get(original, 0);
		} else {
			return java.lang.reflect.Array.get(original, index);
		}
	}
}
