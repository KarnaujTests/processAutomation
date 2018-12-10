package com.juancarloscasas.baseProject.baseTests.testsDefinition;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.juancarloscasas.baseProject.FrameworkTools.GeneradorLogs.Logger;
import com.juancarloscasas.baseProject.FrameworkTools.InputDataReading.FilesPropertiesAccess;
import com.juancarloscasas.baseProject.basePages.pageDefinition.NavigationHeaders.HEADERS;
import com.juancarloscasas.baseProject.baseTests.TestsBaseDefinition;

public class ExampleTest extends TestsBaseDefinition{
	
	public ExampleTest() { 
		__listaCasosPrueba = new ArrayList<String>(Arrays.asList(
				"TEST-ID"));
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestsBaseDefinition.pageInitializationMethod();
		
		LoginPagePO.goToLandingPage();
		
		LoginPagePO.introduceLoginData(
				FilesPropertiesAccess.getPropertyValue("username", "driverParameters.properties"), 
				FilesPropertiesAccess.getPropertyValue("password", "driverParameters.properties"));
		
		LoginPagePO.validateCurrentVersion("master_0.0.1");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		TestsBaseDefinition.driversClosure();
		
		Logger.save();
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void configureAccountTypes() {
		HeadersPO.goToArea(HEADERS.BILLING);
		
		BillingControlPO.configureAccountTypes();
	}
	
	@Test
	public void configureAccounts() {
		HeadersPO.goToArea(HEADERS.BILLING);
		
		BillingControlPO.configureAccounts();
	}
	
	@Test
	public void configureBillingGroups() {
		HeadersPO.goToArea(HEADERS.BILLING);
		
		BillingItemsPO.configureBillingItemGroups();
	}

	@Test
	public void configureDiscountPlans() {
		HeadersPO.goToArea(HEADERS.BILLING);
		
		DiscountsPagePO.configureDiscountPlans();
	}

	@Test
	public void configureAllowances() {
		HeadersPO.goToArea(HEADERS.PRODUCTCATALOG);
		
		AllowancesPagePO.configureAllowances(); 
	}

	@Test
	public void editAllowances() {
		HeadersPO.goToArea(HEADERS.PRODUCTCATALOG);

		AllowancesPagePO.editAllowances();
	}

	@Test
	public void configureAllowanceGroups() {
		HeadersPO.goToArea(HEADERS.PRODUCTCATALOG);

		AllowanceGroupsPagePO.configureAllowanceGroups();
	}

	@Test
	public void configurePriceLists() {
		HeadersPO.goToArea(HEADERS.BILLING);

		RatePlansPagePO.configurePriceLists();
	}

	@Test
	public void configureRatePlans() {
		HeadersPO.goToArea(HEADERS.BILLING);

		RatePlansPagePO.configureRatePlans();
	}

	@Test
	public void configureRatePlanPriceListRelation() {
		HeadersPO.goToArea(HEADERS.BILLING);

		RatePlansPagePO.configureRatePlanPriceListRelations();
	}

	@Test
	public void configureInstanceCharacteristicRatePlan() {
		HeadersPO.goToArea(HEADERS.PRODUCTCATALOG);

		InstanceCharacteristicsPagePO.configureRatePlans();
	}

	@Test
	public void configureInputCharacteristicInvoiceEU() {
		HeadersPO.goToArea(HEADERS.PRODUCTCATALOG);

		InstanceCharacteristicsPagePO.configureInvoiceNameES();
	}

	@Test
	public void editProductSpecifications() {
		HeadersPO.goToArea(HEADERS.PRODUCTCATALOG);

		TechnicalSpecificationPagePO.editRatePlanPriceListRelations();
	}

	@Test
	public void editProductOfferingsEnrichment() {
		HeadersPO.goToArea(HEADERS.PRODUCTCATALOG);

		ProductOfferingPagePO.editProductOfferingCommercialEnrichment();
	}

}
