package me.gluckzhang.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;

import me.gluckzhang.template.ArrayReadTemplate;
import me.gluckzhang.template.ArrayWriteTemplate;
import spoon.Launcher;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.code.CtArrayAccess;
import spoon.reflect.code.CtArrayRead;
import spoon.reflect.code.CtArrayWrite;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtCatchVariable;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtTry;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.template.Substitution;



/**
 * Proof-of-concept Implementation of Failure-oblivious Preprocessor
 * @author gluckzhang
 *
 */
public class Analyze {
	private static Logger logger = Logger.getLogger(Analyze.class);

	public static void main(String[] args) {
		boolean needFoArrayRead = false; // whether need to add a failure-oblivious array read method
		boolean needFoArrayWrite = false; // whether need to add a failure-oblivious array write method
		Launcher spoon = new Launcher();
		spoon.addInputResource("src/me/gluckzhang/main/Test.java"); // use this file to test the preprocessor
		try {
			spoon.addTemplateResource(SpoonResourceHelper.createResource(new File("src/me/gluckzhang/template/ArrayReadTemplate.java")));
			spoon.addTemplateResource(SpoonResourceHelper.createResource(new File("src/me/gluckzhang/template/ArrayWriteTemplate.java")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		spoon.run();
		Factory factory = spoon.getFactory();
		
		for (CtType<?> s : factory.Class().getAll()) {
			logger.info("analyzing class: " + s.getQualifiedName());
			for (CtMethod<?> m : s.getMethods()) {
				// analyze every methods, find array read and write blocks
				List<CtArrayRead> readBlocks = m.getBody().getElements(new TypeFilter(CtArrayRead.class));
				logger.info("in method: " + m.getSimpleName() + ", " + readBlocks.size() + " arrayReadBlock(s) detected");
				for (CtArrayRead readBlock : readBlocks) {
					if (hasTryBlock(readBlock)) {
						// if this read block is wrapped by try block, check whether the author has noticed relative exceptions
						logger.info("readBlock at line " + readBlock.getPosition().getLine() + " has relative try-catch codes");
					} else {
						// translate this array read block into an failure-oblivious code
						logger.info("readBlock at line " + readBlock.getPosition().getLine() + " has been translated into failure-oblivious code");
						foBlock(readBlock, "read");
						needFoArrayRead = true;
					}
				}
				
				List<CtArrayWrite> writeBlocks = m.getBody().getElements(new TypeFilter(CtArrayWrite.class));
				logger.info("in method: " + m.getSimpleName() + ", " + writeBlocks.size() + " arrayWriteBlock(s) detected");
				for (CtArrayWrite wirteBlock : writeBlocks) {
					if (hasTryBlock(wirteBlock)) {
						logger.info("wirteBlock at line " + wirteBlock.getPosition().getLine() + " has relative try-catch codes");
					} else {
						// translate this array write block into an failure-oblivious code
						logger.info("wirteBlock at line " + wirteBlock.getPosition().getLine() + " has been translated into failure-oblivious code");
						foBlock(wirteBlock, "write");
						needFoArrayWrite = true;
					}
				}
			}
			
			if (needFoArrayRead) {
				Substitution.insertAll(s, new ArrayReadTemplate());
			}
			
			if (needFoArrayWrite) {
				Substitution.insertAll(s, new ArrayWriteTemplate());
			}
		}
		
		spoon.prettyprint();
	}
	
	public static boolean hasTryBlock(CtElement block) {
		boolean result = false;
		
		CtElement tryBlock = block.getParent(new TypeFilter(CtTry.class));
		if (tryBlock != null) {
			CtTry t = (CtTry)tryBlock;
			for (CtCatch catcher : t.getCatchers()) {
				CtCatchVariable<? extends Throwable> exceptionType = catcher.getParameter();
				String catcherType = exceptionType.getType().toString(); 
				if (catcherType.equals("java.lang.ArrayIndexOutOfBoundsException")
						|| catcherType.equals("java.lang.IndexOutOfBoundsException")
						|| catcherType.equals("java.lang.Exception")) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
	
	public static void foBlock(CtArrayAccess block, String type) {
		List<CtExpression> expression = null;
		String convertType = null;
		String arrayName = null;
		String indexValue = null;
		String newValue = null;
		String snippet = null;
		
		switch (type) {
		case "read":
			expression = block.getElements(new TypeFilter<>(CtExpression.class));
			CtElement variableElement = block.getParent(new TypeFilter(CtVariable.class));
			if (variableElement != null) {
				CtVariable variable = (CtVariable)variableElement;
				convertType = variable.getType().toString();
			}
			arrayName = expression.get(1).toString();
			indexValue = expression.get(expression.size() - 1).toString();
			if (convertType != null) {
				if (expression.size() == 3) {
					snippet = String.format("(%s)foArrayRead(%s, %s)", convertType, arrayName, indexValue);
				} else {
					String aN = expression.get(2).toString();
					String iV = expression.get(3).toString();
					snippet = String.format("(%s)foArrayRead(foArrayRead(%s, %s), %s)", convertType, aN, iV, indexValue);
				}
			} else {
				if (expression.size() == 3) {
					snippet = String.format("foArrayRead(%s, %s)", arrayName, indexValue);
				} else {
					String aN = expression.get(2).toString();
					String iV = expression.get(3).toString();
					snippet = String.format("foArrayRead(foArrayRead(%s, %s), %s)", aN, iV, indexValue);
				}
			}
			block.replace(block.getFactory().Code().createCodeSnippetExpression(snippet));
			break;
			
		case "write":
			expression = block.getParent().getElements(new TypeFilter<>(CtExpression.class));
			arrayName = expression.get(2).toString();
			indexValue = expression.get(3).toString();
			newValue = expression.get(4).toString();
			snippet = String.format("foArrayWrite(%s, %s, %s)", arrayName, indexValue, newValue);
			block.getParent().replace(block.getFactory().Code().createCodeSnippetStatement(snippet));
			break;
			
		default:
			break;
		}
	}
}
