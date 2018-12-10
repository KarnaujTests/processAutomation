package com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.juancarloscasas.baseProject.FrameworkTools.GeneradorLogs.Logger;

public class NullConnection implements ITCRepository {
	
	protected NullConnection() {
		
	}
	private static NullConnection __instance;
	public static NullConnection getInstance() {
		if (__instance == null) {
			__instance = new NullConnection();
		}
		return __instance;
	}

	public static void startRepositoryService() {
		
	}
	
	public static void stablishConnectionRepository()
	{
		return;
	}
	
	public void addTestClass(String nombreSuite) {
		return;
	}

	public ITCRepositoryTestClass getTestClass(String nombreTestClass) {
		return new ReportingTestClass();
	}
	
	private static HashMap<String, ReportingTestClass> listTestClass;
	public class ReportingTestClass implements ITCRepositoryTestClass
	{
		private LinkedHashMap<String, ReportingTestCase> listTestCases;
		public ReportingTestClass() {
			this.listTestCases = new LinkedHashMap<String, NullConnection.ReportingTestClass.ReportingTestCase>();
		}
		
		private class ReportingTestCase implements ITCRepositoryTestMethod
		{
			private String __testName;
			private int __testId;
			public ReportingTestCase(String nombreTest) {
				__testName = nombreTest;
				__testId = getTestCaseID(__testName, 0);
			}
			
			private String __testResult;
			private String __additionalNotesResult;
			public String getExecutionResult(){
				return __testResult;
			}

			public void testPass() {
//				__testResult = TestLinkAPIResults.TEST_PASSED;
				__testResult = null;
				__additionalNotesResult = "";
			}
			public void testFail(String excepcionEjecucion) {
//				__testResult = TestLinkAPIResults.TEST_FAILED;
				__testResult = null;
				__additionalNotesResult = "Failure on test. #Failure detail: " + excepcionEjecucion;
			}
			public void testBlock(String casoPruebaBloqueante) {
//				__testResult = TestLinkAPIResults.TEST_BLOCKED;
				__testResult = null;
				__additionalNotesResult = "TestCase blocked by failure in a previous testcase: " + casoPruebaBloqueante + ".";
			}
			
			private int getTestCaseID(String testCaseName, int testPlanId) {
				return 0;
			}
			
			public void reportResult() {
			}
		}
		
		public void addTestCase(String testcaseName) {
			if (! listTestCases.containsKey(testcaseName)) {
				listTestCases.put(testcaseName, new ReportingTestCase(testcaseName));
			}
		}
		
		public void addTestCase(ArrayList<String> testcaseList)
		{
			for (String casoPrueba : testcaseList) {
				listTestCases.put(casoPrueba, new ReportingTestCase(casoPrueba));
			}
		}

		public ITCRepositoryTestMethod getTestMethod(String testMethodName) {
			return listTestCases.get(testMethodName);
		}
		
		public void setPassedTest(String testName) {
			if (listTestCases.containsKey(testName)) {
				listTestCases.get(testName).testPass();
			} else {
				Logger.raiseMinor("The testcase name was not found in the !! No se encontr� el caso de prueba con identificador: " + testName);
			}
		}

		public void completeNotReportedTests(String exceptionMessage)
		{
			boolean testFailedDetected = false;
			String pruebaBloqueante = null;
			for (ReportingTestCase testcase : listTestCases.values())
			{
				if (testcase.getExecutionResult() == null && ! testFailedDetected) {
					testcase.testFail(exceptionMessage);
					pruebaBloqueante = testcase.__testName;
					testFailedDetected = true;
				} else if (testFailedDetected) {
					testcase.testBlock(pruebaBloqueante);
				}
			}
		}

		public void notifyAllResultsFromClass()
		{
			for (ReportingTestCase testcase : listTestCases.values()) {
				testcase.reportResult();
			}
		}
	}
	
	public void setEjecucionCorrecta(String suitePrueba, String casoPrueba) {
		
	}

	private static int getTestProyectID(String testProyectName) {
		return 0;
	}

	private static int getTestPlanID(String testPlanName, int testProyectId) {
		return 0;
	}
	
	private static int getTestBuildID(String buildName, int testPlanNameId) {
		return 0;
	}

}
