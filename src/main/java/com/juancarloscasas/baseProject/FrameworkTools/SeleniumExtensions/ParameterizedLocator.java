package com.juancarloscasas.baseProject.FrameworkTools.SeleniumExtensions;

import org.junit.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.support.How;

public class ParameterizedLocator {
	
	private final How __how;
	public final String __using;
	public By byLocator;
	public ParameterizedLocator(How how, String using) {
		__how = how;
		__using = using;
	}
	
	private void setByLocator(String usingParameter) {
		switch (__how) {
		case ID:
			byLocator = By.id(usingParameter);
			break;
		case XPATH:
			byLocator = By.xpath(usingParameter);
			break;
		case NAME:
			byLocator = By.name(usingParameter);
			break;
		case CLASS_NAME:
			byLocator = By.className(usingParameter);
			break;
		case TAG_NAME:
			byLocator = By.tagName(usingParameter);
			break;
		case LINK_TEXT:
			byLocator = By.linkText(usingParameter);
			break;
		default:
			Assert.fail("! Parameterized locator not implemented");
			break;
		}
	}
	
	void updateByLocator(String... usingReplacements) {
		String tempUsing = __using;
		
		for (String replacement : usingReplacements) {
			String locatorReplacementFlag = captureUsingFirstReplacement();
			
			if (locatorReplacementFlag == null) {
				Assert.fail("! Parametric locator not found. Element locator= " + __using + "; Replacement: " + replacement);
			} else {
				tempUsing = tempUsing.replace(locatorReplacementFlag, replacement);
			}
		}

		setByLocator(tempUsing);
	}
	
	By getByLocator(String... usingReplacements) {
		return byLocator;
	}
	
	String captureUsingFirstReplacement() {
		int indexReplaceTextIni = __using.indexOf("#!");
		int indexReplaceTextEnd = __using.indexOf("#", indexReplaceTextIni + 1) + 1;

		if (indexReplaceTextIni == -1 || indexReplaceTextEnd == 0) {
			Assert.fail("The parametric identifier should be indicated by inserting an element starting by" +
					"#! and ending in # in the webelement's xpath to be able to replace it in execution time.");
		}
		return __using.substring(indexReplaceTextIni, indexReplaceTextEnd);
	}
	


}