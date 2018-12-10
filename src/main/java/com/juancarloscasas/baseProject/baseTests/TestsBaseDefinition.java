package com.juancarloscasas.baseProject.baseTests;

import java.util.ArrayList;

import com.juancarloscasas.baseProject.FrameworkTools.GeneradorLogs.Logger;
import com.juancarloscasas.baseProject.FrameworkTools.SeleniumExtensions.CustomDriver;
import com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration.ITCRepository;
import com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration.ITCRepositoryTestClass;
import com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration.NullConnection;
import com.juancarloscasas.baseProject.basePages.pageDefinition.LoginPage;
import com.juancarloscasas.baseProject.basePages.pageDefinition.NavigationHeaders;
import com.juancarloscasas.baseProject.basePages.pageDefinition.Billing.BillingControlPage;
import com.juancarloscasas.baseProject.basePages.pageDefinition.Billing.BillingItemsPage;
import com.juancarloscasas.baseProject.basePages.pageDefinition.Billing.RatePlansPage;
import com.juancarloscasas.baseProject.basePages.pageDefinition.ProductCatalog.AllowanceGroupPage;
import com.juancarloscasas.baseProject.basePages.pageDefinition.ProductCatalog.AllowancesPage;
import com.juancarloscasas.baseProject.basePages.pageDefinition.ProductCatalog.DiscountsPage;
import com.juancarloscasas.baseProject.basePages.pageDefinition.ProductCatalog.InstanceCharacteristicsPage;
import com.juancarloscasas.baseProject.basePages.pageDefinition.ProductCatalog.ProductOfferingsPage;
import com.juancarloscasas.baseProject.basePages.pageDefinition.ProductCatalog.TechnicalSpecificationsPage;

public abstract class TestsBaseDefinition {
	
	public TestsBaseDefinition() {
	}
	
	/****************
	 * Page and Driver initialization methods in PageFactory
	 ****************/
	protected static CustomDriver driver = new CustomDriver();
	
	protected static LoginPage LoginPagePO;
	protected static NavigationHeaders HeadersPO;
	protected static BillingControlPage BillingControlPO;
	protected static BillingItemsPage BillingItemsPO;
	protected static DiscountsPage DiscountsPagePO;
	protected static AllowancesPage AllowancesPagePO;
	protected static AllowanceGroupPage AllowanceGroupsPagePO;
	protected static RatePlansPage RatePlansPagePO;
	protected static InstanceCharacteristicsPage InstanceCharacteristicsPagePO;
	protected static TechnicalSpecificationsPage TechnicalSpecificationPagePO;
	protected static ProductOfferingsPage ProductOfferingPagePO;
	
	public static void pageInitializationMethod() {
		LoginPagePO = new LoginPage(driver);
		HeadersPO = new NavigationHeaders(driver);
		BillingControlPO = new BillingControlPage(driver);
		BillingItemsPO = new BillingItemsPage(driver);
		DiscountsPagePO = new DiscountsPage(driver);
		AllowancesPagePO = new AllowancesPage(driver);
		AllowanceGroupsPagePO = new AllowanceGroupPage(driver);
		RatePlansPagePO = new RatePlansPage(driver);
		InstanceCharacteristicsPagePO = new InstanceCharacteristicsPage(driver);
		TechnicalSpecificationPagePO = new TechnicalSpecificationsPage(driver);
		ProductOfferingPagePO = new ProductOfferingsPage(driver);
		
	}

	/********
	 * Methods for test closure
	 ********/
	public static void driverTakeGlobalScreenshot(String nombreCaptura, Throwable excepcionFallo)
	{
		if (driver.isInstanced()) {
			driver.customTakeScreenshot(nombreCaptura, excepcionFallo);
		}
	}
	
	public static void driversClosure()
	{
		if (driver.isInstanced()) {
			driver.quit();
		}
	}
	
	//TODO: Introduce Repository Mock for code uncommenting
	/* ********
	 * Defition of execution repository
	 *********/
	private ITCRepository interfaceRepositoryExec = NullConnection.getInstance();
	protected ITCRepositoryTestClass getExecReporter() {
		return interfaceRepositoryExec.getTestClass(this.getClass().getName());
	}
	protected void testReportResult(String nombreTest) {
		try {
			getExecReporter().setPassedTest(nombreTest);
		} catch (NullPointerException e) {
			Logger.raiseMinor("The test '" + nombreTest + "' could not be reported due to execution repository not being stablished.");
		}
	}
	
	/* ********
	 * Base for definition of associated testcases
	 *********/
	protected ArrayList<String> __listaCasosPrueba;
	public ArrayList<String> getListaCasosPruebaClase() {
		return this.__listaCasosPrueba;
	}

	

}