package com.juancarloscasas.baseProject.FrameworkTools.SeleniumExtensions;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ParameterizedWebElement {
	
	private ParameterizedLocator __locator;
	private WebDriver __localDriver;
	public ParameterizedWebElement(ParameterizedLocator locator, WebDriver driver) {
		__locator = locator;
		__localDriver = driver;
	}
	
	//TODO: Modificar comportamiento para que haya acceso a los metodos de esta clase pero el valor por defecto de la referencia sea al WebElement
	private WebElement getElement() {
		try {
			if (__localDriver == null) {
				Assert.fail("! Driver class instance not initialized");
			}
			return __localDriver.findElement(__locator.getByLocator());
		} catch (NoSuchElementException e) {
			return getElement();
		}
	}
	
	public WebElement getElement(String parametricReplace) {
		__locator.updateByLocator(parametricReplace);
		
		return getElement();
	}
	
	public ParameterizedWebElement setLocator(String parametricReplace) {
		__locator.updateByLocator(parametricReplace);
		
		return this;
	}
	
	public void clear() {
		getElement().clear();
	}

	public void click() {
		getElement().click();
	}

	public WebElement findElement(By arg0) {
		return getElement().findElement(arg0);
	}

	public List<WebElement> findElements(By arg0) {
		return getElement().findElements(arg0);
	}

	public String getAttribute(String arg0) {
		return getElement().getAttribute(arg0);
	}

	public String getCssValue(String arg0) {
		return getElement().getCssValue(arg0);
	}

	public Point getLocation() {
		return getElement().getLocation();
	}

	public Dimension getSize() {
		return getElement().getSize();
	}

	public String getTagName() {
		return getElement().getTagName();
	}

	public String getText() {
		return getElement().getText();
	}

	public boolean isDisplayed() {
		return getElement().isDisplayed();
	}

	public boolean isEnabled() {
		return getElement().isEnabled();
	}

	public boolean isSelected() {
		return getElement().isSelected();
	}

	public void sendKeys(CharSequence... arg0) {
		getElement().sendKeys(arg0);
	}
	
	public void submit() {
		getElement().submit();
	}

}