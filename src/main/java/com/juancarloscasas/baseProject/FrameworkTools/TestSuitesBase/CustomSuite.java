package com.juancarloscasas.baseProject.FrameworkTools.TestSuitesBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.juancarloscasas.baseProject.FrameworkTools.GeneradorLogs.Logger;
import com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration.ITCRepository;

@RunWith(Suite.class)
public abstract class CustomSuite {
	/* *****************
	 * Methods to access Tests Properties on Runtime
	 ********************/
	/********
	 * Method for the complete storage of the classes that will be executed in the Suite, as well
	 * as the internal associated tests
	 * @param repositoryInstance Instance of the test repository that will host the list of executed testcases
	 */
	public void addCompleteExecutionTo(ITCRepository repositoryInstance) {
		SuiteClasses annotation = this.getClass().getAnnotation(SuiteClasses.class);
		
		for (Class<?> claseTests : annotation.value()) {
			String nombreClase = claseTests.getName();
			repositoryInstance.addTestClass(nombreClase);
			
			ArrayList<String> casosPruebaParcial = getTestCasesFromClass(claseTests);
			repositoryInstance.getTestClass(nombreClase).addTestCase(casosPruebaParcial);
		}
	}

	/**
	 * Recovers the list of testcases indicated in the List returned by getListaCasosPruebaClase
	 * for every test class
	 * @param testcasesClass Type of class for which the testcase list should be recovered
	 * @return List of testcases associated in the getListaCasosPruebaClase
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> getTestCasesFromClass(Class<?> testcasesClass) {
		// Generate an instance of the class that is actually in course, to get the unique method of that class
		Object instanceTestClass = null;
		try {
			instanceTestClass = testcasesClass.newInstance();
		} catch (InstantiationException e) { } catch (IllegalAccessException e) { }
		
		// Recollect the class method to pick the list of testcases
		Method reflectiveMethod = null;
		try {
			reflectiveMethod = testcasesClass.getMethod("getListaCasosPruebaClase");
		} catch (SecurityException e) {
			Logger.raiseMinor("Error in access to the tests of class '" + testcasesClass.getName() + "'." +
					"The method 'getListaCasosPruebaClase' shoud change it's visibility");
		} catch (NoSuchMethodException e) {
			Logger.raiseMinor("Error in access to the tests of class '" + testcasesClass.getName() + "'." +
					"The method 'getListaCasosPruebaClase' shoud be implemented");
		}
		
		// Call by reflection the method that serves as testcase names storage
		ArrayList<String> testcasesList = null;
		try {
			testcasesList = (ArrayList<String>) reflectiveMethod.invoke(instanceTestClass);
		} catch (IllegalArgumentException e) { // exception handling omitted for brevity
			e.printStackTrace();
		} catch (IllegalAccessException e) { // exception handling omitted for brevity
			e.printStackTrace();
		} catch (InvocationTargetException e) { // exception handling omitted for brevity
			e.printStackTrace();
		
		}
		
		return testcasesList;
	}
}
