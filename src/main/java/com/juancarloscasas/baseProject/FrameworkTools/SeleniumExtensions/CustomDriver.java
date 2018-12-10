package com.juancarloscasas.baseProject.FrameworkTools.SeleniumExtensions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.juancarloscasas.baseProject.FrameworkTools.Constants.FileFolderTypeConstants.FolderType;
import com.juancarloscasas.baseProject.FrameworkTools.InputDataReading.FilesAccessCRUD;
import com.juancarloscasas.baseProject.FrameworkTools.InputDataReading.FilesPropertiesAccess;

public class CustomDriver implements WebDriver {
	
	private WebDriver __driverImplementacion;
	public boolean isInstanced() {
		return (__driverImplementacion != null);
	}
	
	public WebDriver getDriver()
	{
		if (!isInstanced())
		{
			//Reseteamos el nombre de las ventanas almacenadas
			startApplicationWindowsStorage();
			
			String systemOS = System.getProperty("os.name");
			String osAppendix = "";
			if (systemOS.contains("Win")) {
				osAppendix = ".exe";
			}
			
			String driverType = FilesPropertiesAccess.getPropertyValue("driverType", "driverParameters.properties");
			if (driverType.compareTo("Chrome") == 0) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator + "chromedriver" + osAppendix);
//				System.setProperty("webdriver.chrome.driver", "/Users/jcasas/eclipse-workspace/baseProjectSelenium/chromedriver");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("start-maximized");
				options.addArguments("disable-infobars");
				__driverImplementacion = new ChromeDriver(options);			
			} else {
				Assert.fail("Must select one of the implemented drivers in the file propertiesTests.properties: Chrome.");
			}
			
//			__driverImplementacion.manage().window().maximize();
		}
		
		return __driverImplementacion;
	}
	
	/* *************
	 * Methods for faster window control between different applications
	 **************/
	private HashMap<String, String> __nombresVentanasAplicaciones;
	private void startApplicationWindowsStorage() {
		__nombresVentanasAplicaciones = new HashMap<String, String>();
	}
	
	/**
	 * Establish the pair window-application. The current driver window will be stored
	 * as window name for the application called
	 * @param application Name of the application which has the focus in the moment of call
	 */
	public void setWindowApplication(String application) {
		this.__nombresVentanasAplicaciones.put(application.toLowerCase(), this.getWindowHandle());
	}
	/**
	 * Recovers the pair application-window of the driver. Returns null if no match is found
	 * @param application: Application name
	 * @return Identifier of the driver window  or null if not found
	 */
	public String getWindowApplication(String application) {
		if (! this.__nombresVentanasAplicaciones.containsKey(application.toLowerCase())) {
			return null;
		} else {
			return this.__nombresVentanasAplicaciones.get(application.toLowerCase());
		}
	}
	private void cleanApplicationWindowsStored() {
		__nombresVentanasAplicaciones = null;
	}
	
	/* *************
	 * Methods for extraction of driver screenshots
	 **************/
	public void customTakeScreenshot(String nombreTestCaseActual, Throwable e)
	{
		if (getDriver() != null)
		{
			Set<String> windowsDriver = getDriver().getWindowHandles();
			
			// Se captura inicialmente la ventana presente ya que será la que origine el fallo y se elimina del resto
			File captura = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
			BufferedImage capturaModificada = writeInScreenshot(captura, e);
			FilesAccessCRUD.saveFile(FolderType.SCREENSHOTS, capturaModificada, nombreTestCaseActual);
			
			String windowInitial = getDriver().getWindowHandle();
			windowsDriver.remove(windowInitial);
			
			for (String window : windowsDriver) {
				if (this.getWindowHandle() != window) {
					this.switchTo().window(window);
				}
				
				String tipoDriver = FilesPropertiesAccess.getPropertyValue("tipoDeDriver", "propiedadesTests.properties");
				
				//TODO: Chrome complete page screenshot pending
				if (tipoDriver.equals("Chrome")) {
//					__driverImplementacion.manage().
//					width  = wd.execute_script("return Math.max(document.body.scrollWidth, document.body.offsetWidth, document.documentElement.clientWidth, document.documentElement.scrollWidth, document.documentElement.offsetWidth);")
//					height = wd.execute_script("return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);")

					captura = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
				} else {
					captura = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
				}
				
				FilesAccessCRUD.saveFile(FolderType.SCREENSHOTS, captura, nombreTestCaseActual);
			}
			
		}
	}
	
	public BufferedImage writeInScreenshot(File screenshotFile, Throwable failureException) {
		BufferedImage captura = null;
		try {
			captura = ImageIO.read(screenshotFile);
		} catch (IOException e) {
			System.out.println("IOException al tratar de convertir el File en BufferedImage.");
			return null;
		}
		
		Graphics g = captura.getGraphics();
		g.setFont(g.getFont().deriveFont(30f));
		g.setColor(new Color(208, 10, 10));
		g.drawString("Failed because of this exception!!", 100, 100); 
		g.drawString(failureException.getClass().getName(), 100, 135);
		g.dispose();
		
		return captura;
	}
	
	/*****************
	 * M�todos para la sobreescritura de Driver
	 *****************/
	public Options manage()
	{
		return getDriver().manage();
	}
	
	public WebElement findElement(By arg0) {
		return getDriver().findElement(arg0);
	}

	public List<WebElement> findElements(By arg0) {
		return getDriver().findElements(arg0);
	}

	public void get(String arg0) {
		getDriver().get(arg0);
	}

	public String getCurrentUrl() {
		return getDriver().getCurrentUrl();
	}

	public String getPageSource() {
		return getDriver().getPageSource();
	}

	public String getTitle() {
		return getDriver().getTitle();
	}

	public String getWindowHandle()
	{
		return getDriver().getWindowHandle();
	}

	public Set<String> getWindowHandles() {
		return getDriver().getWindowHandles();
	}

	public Navigation navigate() {
		return getDriver().navigate();
	}

	public void close() {
		getDriver().close();
	}
	
	public void scrollIntoView(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor)getDriver();
		executor.executeScript("arguments[0].scrollIntoView(true);", element);
	}
	
	public void javascriptClick(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor)getDriver();
		executor.executeScript("arguments[0].click();", element);
	}
	
	public void quit() {
		if (isInstanced()) {
			getDriver().quit();
			__driverImplementacion = null;
			cleanApplicationWindowsStored();
		}
	}

	public TargetLocator switchTo() {
		return getDriver().switchTo();
	}
}