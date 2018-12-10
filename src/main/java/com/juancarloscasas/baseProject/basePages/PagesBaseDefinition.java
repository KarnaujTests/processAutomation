package com.juancarloscasas.baseProject.basePages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.juancarloscasas.baseProject.FrameworkTools.InputDataReading.FilesPropertiesAccess;
import com.juancarloscasas.baseProject.FrameworkTools.SeleniumExtensions.CustomDriver;

public abstract class PagesBaseDefinition {
	/*
	 * Common driver element for all pages reference
	 */
	protected CustomDriver driverTest;
	protected PagesBaseDefinition(CustomDriver driver) {
		this.driverTest = driver;
		PageFactory.initElements(this.driverTest, this);
	}
	
	/* *************************** 
	 * 
	 * Global constants for load time expectance
	 *
	 * ***************************/
	protected String timeExpectedPageLoad = FilesPropertiesAccess.getPropertyValue("timeLoadingPages", "timeLoadingTimes.properties");
	protected String timeExpectedElementLoad = FilesPropertiesAccess.getPropertyValue("timeLoadingElements", "timeLoadingTimes.properties");
	protected String timeExpectedAlertsLoad = FilesPropertiesAccess.getPropertyValue("timeLoadingAlerts", "timeLoadingTimes.properties");
	
	/* *************************** 
	 *
	 * Methods for interaction with simple elements
	 * 
	 ****************************/
	/**
	 * Clicks in a page and waits for the page to refresh
	 * @param element Element in which the click will be performed
	 */
	protected void clickAndRefresh(WebElement element)
	{
		WebElement htmlElement = driverTest.findElement(By.tagName("html"));
		
		waitsForElementToBeClicable(element);
		element.click();

		try {
			WebDriverWait waiter = new WebDriverWait(driverTest, TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(timeExpectedPageLoad)));
			waiter.ignoring(NoSuchElementException.class);
			waiter.until(ExpectedConditions.stalenessOf(htmlElement));
		} catch (TimeoutException e) {
			Assert.fail("The click in the element didn't trigger a page refresh as expected. See StackTrace");
		}
	}
	
	protected void changeText(WebElement element, String valueTo) {
		waitsForElementToBeVisible(element);
		element.clear();
		element.sendKeys(valueTo);
	}
	
	protected void clickAndChangeText(WebElement elementToClick, WebElement elementToChange) {
		String initialValue = elementToChange.getText();
		String extraValue = elementToChange.getAttribute("textContent");
		elementToClick.click();
		
		WebDriverWait waiter = new WebDriverWait(driverTest, TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(timeExpectedElementLoad)));
		waiter.ignoring(NoSuchElementException.class);
		try {
			waiter.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(elementToChange, initialValue)));
		} catch (TimeoutException e) {
			Assert.fail("The element did not produce a change in the other element value in the expected time.");
		}
	}
	
	protected void clickAndChangeTextContent(WebElement elementToClick, WebElement elementToChange) {
		String initialValue = elementToChange.getAttribute("textContent");

		elementToClick.click();
		
		WebDriverWait waiter = new WebDriverWait(driverTest, TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(timeExpectedElementLoad)));
		waiter.ignoring(NoSuchElementException.class);
		try {
			waiter.until(ExpectedConditions.not(ExpectedConditions.attributeContains(elementToChange, "textContent", initialValue)));
		} catch (TimeoutException e) {
			Assert.fail("The element did not produce a change in the other element value in the expected time.");
		}
	}
	
	protected void clickAndChangeVisibility(WebElement elementToClick, WebElement elementToChange) {
		waitForElementToBePresent(elementToClick);
		
		boolean initialVisibility = false;
		try {
			initialVisibility = elementToChange.isDisplayed();
		} catch (NoSuchElementException e) {
			initialVisibility = false;
		}
		
		elementToClick.click();
		
		WebDriverWait waiter = new WebDriverWait(driverTest, TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(timeExpectedElementLoad)));
		try {
			List<WebElement> listOfElementsToChange = new ArrayList<>();
			listOfElementsToChange.add(elementToChange);
			
			if (initialVisibility) {
				try {
					waiter.until(ExpectedConditions.not(ExpectedConditions.visibilityOfAllElements(listOfElementsToChange)));
				} catch (NoSuchElementException e) {
					//Element disappeared correctly
				}
			} else {
				waiter.ignoring(NoSuchElementException.class);
				waiter.until(ExpectedConditions.visibilityOfAllElements(listOfElementsToChange));
			}
		} catch (TimeoutException e) {
			Assert.fail("The element did not produce a change in the other element value in the expected time.");
		}
	}
	
	protected void clickAndChangePrecense(WebElement elementToClick, WebElement elementToChange) {
		boolean initialPresence = isElementPresent(elementToChange);
		
		waitsForElementToBeVisible(elementToClick);
		elementToClick.click();

		try {
			if (initialPresence) {
				waitForElementToBeNotPresent(elementToChange);
			} else {
				waitForElementToBePresent(elementToChange);
			}
		} catch (AssertionError e) {
			Assert.fail("The element did not produce a change in the other element value in the expected time.");
		}
	}
	
	protected void clickAndChangeSelection(WebElement elementToClick, WebElement elementToChange) {
		boolean initialSelection = elementToChange.isSelected();
		
		waitsForElementToBeVisible(elementToClick);
		elementToClick.click();

		WebDriverWait waiter = new WebDriverWait(driverTest, TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(timeExpectedElementLoad)));
		waiter.ignoring(NoSuchElementException.class);
		try {
			waiter.until(ExpectedConditions.elementToBeSelected(elementToChange));
		} catch (AssertionError e) {
			Assert.fail("The element did not produce a change in the other element value in the expected time.");
		}
	}
	
	protected boolean isElementPresent(WebElement element) {
		try {
			element.isDisplayed();
			
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	/**
	 * Clicks in an element and waits for an additional window to appear
	 * @param element Element in which the click will be performed
	 */
	protected void clickWithNewWindowCreation(WebElement element)
	{
		waitsForElementToBeVisible(element);
		
		Set<String> windowsInitial = driverTest.getWindowHandles();
//		String ventanaInicial = driverPruebas.getWindowHandle();
		
		element.click();

		Set<String> windowsPostClick = driverTest.getWindowHandles();
		long tiempoInicial = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		long tiempoActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		int tiempoEsperaCarga = Integer.parseInt(timeExpectedPageLoad);
		try
		{
			while (windowsPostClick.size() == windowsInitial.size())
			{
				windowsPostClick = driverTest.getWindowHandles();
				customSleep(200);
				
				tiempoActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
				if (tiempoActual > tiempoInicial + tiempoEsperaCarga)
				{
					Assert.fail("El click no provoco una apertura de ventana adicional como estaba esperado.");
				}
			}
			
			for (String ventana : windowsPostClick)
			{
				if (!windowsInitial.contains(ventana))
				{
					driverTest.switchTo().window(ventana);
					return;
				}
			}
		}
		catch (NoSuchWindowException activacionEstadoSucio) {
			Assert.fail("The window closed unexpectedly. Refactor the behavior as expected.");
		}
	}
	
	
	
	protected void clickViaJavascript(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor)driverTest;
		executor.executeScript("arguments[0].click();", element);
	}
	
	/**
	 * Clicks and verifies that the element was selected. Applicable for similar to 
	 * checkboxes elements
	 * @param elementSelectable Selectable elements that is expected to be selected
	 */
	protected void selectElement(WebElement elementSelectable)
	{
		waitsForElementToBeVisible(elementSelectable);
		
		if (!elementSelectable.isSelected())
		{
			elementSelectable.click();
		}
		
		waitsForElementToBeSelected(elementSelectable);
	}
	
	/**
	 * Changes the value of a text field, using explicit time waits
	 * @param element Text field element that allows for input values
	 * @param value Expected value at the end of the action
	 */
	protected void textFieldChangeValue(WebElement element, String value)
	{
		waitsForElementToBeVisible(element);
		element.click();
		element.clear();
		element.sendKeys(value);
	}

	/**
	 * Selects an element inside a select element that fits the expected value partially, the least
	 * @param elementSelect Select element which contains the possible values expected
	 * @param optionPartialValue Partial text of value that is held by the desired element
	 */
	protected void selectComboValueByPartialText(WebElement elementSelect, String optionPartialValue)
	{
		waitsForElementToBeVisible(elementSelect);
		Select selectElement = new Select(elementSelect);
		ArrayList<WebElement> valueList = new ArrayList<WebElement>(selectElement.getOptions());
		
		for (WebElement value : valueList)
		{
			if (value.getText().contains(optionPartialValue))
			{
				selectElement.selectByVisibleText(value.getText());
				return;
			}
		}
	}
	
	protected void selectComboValueByManualClickingLabel(WebElement elementSelect, String optionPartialValue)
	{
		waitsForElementToBeVisible(elementSelect);
		elementSelect.click();
		customSleep(100);
		
		WebElement optionSearched = elementSelect.findElement(By.xpath(".//*[@label='"+ optionPartialValue +"']"));
//		clickAndChangeVisibility(optionSearched, optionSearched);
	}
	
	/* ***************************
	 *
	 * Section for code execution stability
	 * 
	 ****************************/
	/**
	 * Method for an explicit wait on element, waiting for it to be visible to the user
	 * @param element Element that should appear to the user for the flux to continue
	 */
	protected void waitsForElementToBeVisible(WebElement element)
	{
		WebDriverWait waiter = new WebDriverWait(driverTest, TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(timeExpectedPageLoad)));

		waiter.ignoring(NoSuchElementException.class);
		try {
			waiter.until(ExpectedConditions.visibilityOf(element));
		} catch (TimeoutException e) {
			Assert.fail("The element (see StackTrace) didn't appear in the expected time.");
		}
	}

	protected void waitsForElementToBeClicable(WebElement element)
	{
		WebDriverWait waiter = new WebDriverWait(driverTest, TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(timeExpectedPageLoad)));

		waiter.ignoring(NoSuchElementException.class);
		try {
			waiter.until(ExpectedConditions.elementToBeClickable(element));
		} catch (TimeoutException e) {
			Assert.fail("The element (see StackTrace) didn't appear in the expected time.");
		}
	}

	/**
	 * Method for an explicit wait on an element, waiting for it to have a selected state
	 * @param element Element, of a selectable type, that is expected to be selected
	 */
	protected void waitsForElementToBeSelected(WebElement element)
	{
		WebDriverWait waiter = new WebDriverWait(driverTest, TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(timeExpectedPageLoad)));

		waiter.ignoring(NoSuchElementException.class);
		try {
			waiter.until(ExpectedConditions.elementSelectionStateToBe(element, true));
		} catch (TimeoutException e) {
			Assert.fail("The element (see StackTrace) was not selected as expected.");
		}
	}

	/**
	 * Method for an explicit wait on a list of elements which only one is expected to 
	 * appear towards the user
	 * @param elementsList List of elements that contains the possibilities that may be presented to the user
	 * @return true if an element is shown in the expected time
	 */
	protected boolean isAnElementInListLoadedVisible(List<WebElement> elementsList)
	{
		long timeInitial = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		long tiempoActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		while (tiempoActual < timeInitial + Long.parseLong(timeExpectedPageLoad))
		{
			for (WebElement element : elementsList)
			{
				try {
					if (element.isDisplayed())
					{
						return true;
					}
				} catch (NoSuchElementException e) { }
			}
			
			tiempoActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
			customSleep(500);
		}
		
		return false;
	}
	
	protected void waitForElementToBePresent(WebElement element)
	{
		long timeInitial = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		long timeActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		
		boolean elementPresent = false;
		while(!elementPresent)
		{
			try {
				element.isDisplayed();
				
				elementPresent = true;
			}
			catch(StaleElementReferenceException e) { }
			catch(NoSuchElementException e) {
				timeActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
				if (timeActual > timeInitial + Long.parseLong(timeExpectedElementLoad)) {
					Assert.fail("FAIL: The element was not cached in the DOM in the expected time.");
				}
			}
		}
	}
	
	protected void waitForElementToBeNotPresent(WebElement element)
	{
		long timeInitial = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		long timeActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		
		boolean elementPresent = true;
		while(elementPresent)
		{
			try {
				element.isDisplayed();
				
				timeActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
				if (timeActual > timeInitial + Long.parseLong(timeExpectedElementLoad)) {
					Assert.fail("FAIL: The element was not uncached from the DOM in the expected time.");
				}
			}
			catch(StaleElementReferenceException e) { }
			catch(NoSuchElementException e) {
				elementPresent = false;
			}
		}
	}
	
	protected void waitForAlertToAppear()
	{
		long timeInitial = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		long timeActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		
		boolean alertPresent = false;
		while(!alertPresent)
		{
			try
			{
				driverTest.findElement(By.tagName("html"));

				timeActual = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
				if (timeActual > timeInitial + Long.parseLong(timeExpectedElementLoad)) {
					Assert.fail("FAIL: The alert didn't appear in the expected time.");
				}
			}
			catch(UnhandledAlertException e)
			{
				System.out.print(driverTest.switchTo().alert().getText());
				alertPresent = true;
			}
		}
	}
	
	/**
	 * Function for execution temporal stop. Differs from Thread.sleep in the exception raised,
	 * since the automation code will never paralelize executions
	 * @param milisecondsToWait Time in miliseconds to freeze the execution
	 */
	protected void customSleep(int milisecondsToWait)
	{
		long timeInitial = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		long timePresent = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		while (timePresent < timeInitial + milisecondsToWait) {
			timePresent = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		}
	}
	
	/* *****************
	 *
	 * Download and Upload of files Section
	 * 
	 *******************/
	/**
	 * Method for the upload of a local file to the server via a button input. The file
	 * address should be included in the respective properties file.
	 * @param elementToUpload Element in the page that opens up the window for file selection
	 * @param fileName The name of the uploaded file, with extension included
	 */
	protected void uploadFile(WebElement elementToUpload, String fileName)
	{
		//TODO: Operation only available to Chrome Driver, other methods could be defined for other browsers
		customSleep(2000);
		
		String pathUploadFile = FilesPropertiesAccess.getPropertyValue("rutaFicherosSubida", "propiedadesTests.properties");
		
		elementToUpload.sendKeys(pathUploadFile + File.separator + fileName);
	}
	
}
