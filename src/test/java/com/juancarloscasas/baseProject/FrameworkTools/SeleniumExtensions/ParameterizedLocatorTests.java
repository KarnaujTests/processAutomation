package com.juancarloscasas.baseProject.FrameworkTools.SeleniumExtensions;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.How;

public class ParameterizedLocatorTests {

	@Test
	public void locatorsShouldBeProperlyStreamed() {
		/*
		 * Precondiciones		
		 */
		String usingInitial = "//div[@class='listado-acciones'][#!REPLACENUMBERSOLICITUD#]//input[contains(@value,'Ver Acuse de Recibo') and not(@type='hidden')]";
		String stringExpected = "#!REPLACENUMBERSOLICITUD#";

		ParameterizedLocator locatorTest = new ParameterizedLocator(How.XPATH, usingInitial);

		String stringObtained = locatorTest.captureUsingFirstReplacement();

		Assert.assertEquals("Los valores encontrados fueron: Funcion:" + stringObtained + ", Esperado:" + stringExpected, stringObtained, stringExpected);
		
		String identifierUsed = "CASOP1/00004";
		By finalLocatorExpected = By.xpath("//div[@class='listado-acciones'][" + identifierUsed + "]//input[contains(@value,'Ver Acuse de Recibo') and not(@type='hidden')]");
		
		locatorTest.updateByLocator(identifierUsed);

		Assert.assertEquals("Los valores encontrados fueron:", locatorTest.byLocator, finalLocatorExpected);
	}

	@Test
	public void randomTest() {
		String am = "12/11/2018";
		
		System.out.println(am.substring(3, 5) + "/" + am.substring(0, 2) + "/" + am.substring(6));
	}
}
