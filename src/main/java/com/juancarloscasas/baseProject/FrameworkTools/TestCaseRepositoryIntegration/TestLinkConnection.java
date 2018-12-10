//package com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import testlink.api.java.client.TestLinkAPIClient;
//import testlink.api.java.client.TestLinkAPIException;
//import testlink.api.java.client.TestLinkAPIResults;
//
//import com.google.common.base.Objects;
//import com.juancarloscasas.baseProject.FrameworkTools.GeneradorLogs.Logger;
//import com.juancarloscasas.baseProject.FrameworkTools.InputDataReading.FilesPropertiesAccess;
//
//public class TestLinkConnection implements ITCRepository {
//	
//	protected TestLinkConnection() {
//		startRepositoryService();
//	}
//	private static TestLinkConnection __instance;
//	public static TestLinkConnection getInstance() {
//		if (__instance == null) {
//			__instance = new TestLinkConnection();
//		}
//		return __instance;
//	}
//	
//	public static void startRepositoryService() {
//		stablishTestLinkConnection();
//		
//		listTestClass = new LinkedHashMap<String, TestLinkConnection.ReportingTestClass>();
//		
//		__testsProjectReporte = FilesPropertiesAccess.getPropertyValue("TestsProject", "testlinkParameters.properties");
//		__testsProjectReporteId = getTestProyectID(__testsProjectReporte);
//		__executionPlanReporte = FilesPropertiesAccess.getPropertyValue("ExecutionPlan", "testlinkParameters.properties");
//		__executionPlanReportId = getTestPlanID(__executionPlanReporte, __testsProjectReporteId);
//		__executionBuildReporte = FilesPropertiesAccess.getPropertyValue("ExecutionBuild", "testlinkParameters.properties");
//		__executionBuildReporteId = getTestBuildID(__executionBuildReporte, __executionPlanReportId);
//	}
//	
//	public static void stablishTestLinkConnection()
//	{
//		if (repositoryAPIURL == null) { repositoryAPIURL = getAPIKEY(); }
//		if (repositoryAPIKEY == null) { repositoryAPIKEY = getAPIURL(); }
//		
//		if (repositoryAPIKEY == null || repositoryAPIURL == null) {
//			Logger.raiseMinor("Failure on TestLink report; It's mandatory to define a URL and Key to access platform XRCP interface (InterfazTestLink.properties)");
//		}
//		
//		//TODO: Exception control on repository connection failure
//		setTcClient(repositoryAPIURL, repositoryAPIKEY);
//	}
//	
//	/*
//	 * Propiedades de conexi�n a la API de TestLink
//	 */
//	private static String repositoryAPIURL;
//	private static String repositoryAPIKEY;
//	private static TestLinkAPIClient repositoryAPI;
//	public static String getAPIKEY() {
//		return FilesPropertiesAccess.getPropertyValue("APIKEY", "testlinkParameters.properties");
//	}
//	public static String getAPIURL() {
//		return FilesPropertiesAccess.getPropertyValue("APIURL", "testlinkParameters.properties");
//	}
//	public static TestLinkAPIClient setTcClient(String APIKEY, String APIURL) {
//		if (repositoryAPI == null) {
//			repositoryAPI  = new TestLinkAPIClient(APIKEY, APIURL);
//			if (! repositoryAPI.isConnected) {
//				Logger.raiseMinor("Failure on TestLink communication; Didn't stablish correct communication with its' API.");
//			}
//		}
//		
//		return repositoryAPI;
//	}
//	
//	private static String __testsProjectReporte;
//	private static int __testsProjectReporteId;
//	private static String __executionPlanReporte;
//	private static int __executionPlanReportId;
//	private static String __executionBuildReporte;
//	private static int __executionBuildReporteId;
//	
//	/*
//	 * Objects to storage the execution history in memory
//	 */
//	private static HashMap<String, ReportingTestClass> listTestClass;
//	public class ReportingTestClass implements ITCRepositoryTestClass
//	{
//		private LinkedHashMap<String, ReportingTestCase> listTestCases;
//		public ReportingTestClass() {
//			this.listTestCases = new LinkedHashMap<String, TestLinkConnection.ReportingTestClass.ReportingTestCase>();
//		}
//		
//		/*
//		 * Construcci�n para almac�n de Casos de Prueba
//		 */
//		private class ReportingTestCase implements ITCRepositoryTestMethod
//		{
//			private String __testName;
//			private int __testId;
//			public ReportingTestCase(String nombreTest) {
//				__testName = nombreTest;
//				__testId = getTestCaseID(__testName, __executionPlanReportId);
//			}
//			
//			private String __testResult;
//			private String __additionalNotesResult;
//			public String getExecutionResult(){
//				return __testResult;
//			}
//
//			public void testPass() {
//				__testResult = TestLinkAPIResults.TEST_PASSED;
//				__additionalNotesResult = "";
//			}
//			public void testFail(String excepcionEjecucion) {
//				__testResult = TestLinkAPIResults.TEST_FAILED;
//				__additionalNotesResult = "Failure on test. #Failure detail: " + excepcionEjecucion;
//			}
//			public void testBlock(String casoPruebaBloqueante) {
//				__testResult = TestLinkAPIResults.TEST_BLOCKED;
//				__additionalNotesResult = "TestCase blocked by failure in a previous testcase: " + casoPruebaBloqueante + ".";
//			}
//			
//			private int getTestCaseID(String testCaseName, int testPlanId) {
//				try {
//					String testcaseExternalID = testCaseName.substring(testCaseName.indexOf("-")+1);
//					
//					// Organizaci�n de los resultados para tests de forma diferente al resto
//					TestLinkAPIResults dataIntermediate = repositoryAPI.getCasesForTestPlan(testPlanId);
//					@SuppressWarnings("unchecked")
//					HashMap<String,?> testCasesTestLink = (HashMap<String, ?>) dataIntermediate.getData(0);
//					
//					//Comparamos cada entrada para ver si es el proyecto buscado y si lo es, guardamos el valor y le devolvemos
//					for (String internalId : testCasesTestLink.keySet())
//					{
//						Object[] dataIntermediateTestI = (Object[]) testCasesTestLink.get(internalId);
//						@SuppressWarnings("unchecked")
//						HashMap<String,String> dataTestI = (HashMap<String,String>) dataIntermediateTestI[0];
//
//						if (Objects.equal(dataTestI.get("external_id").toString(), testcaseExternalID)) {
//							String idTestCase = dataTestI.get("tcase_id").toString();
////							AccesoFicherosPropiedades.setPropertyValue("TestCase_" + testCaseName, idTestCase, "InterfazTestLink.properties");
//
//							return Integer.parseInt(idTestCase);
//						}
//					}
//					Logger.raiseMinor("Could not locate a test by name '" + testCaseName + "' in the TestLink repository.");
//				} catch (TestLinkAPIException errorConexi�n) {
//					Logger.raiseMinor("The testcase could not be recovered due to an error with TestLink communication.");
//					return 0;
//				}
//				return 0;
//			}
//			
//			public void reportResult()
//			{
//				try {
//					Logger.save();
//					repositoryAPI.reportTestCaseResult(__executionPlanReportId, __testId, __executionBuildReporteId, __additionalNotesResult, __testResult);
//				} catch (TestLinkAPIException errorconexion) {
//					Logger.raiseMinor("The testcase result could not be reported due to an error with TestLink communication");
//				}
//			}
//		}
//		
//		/*
//		 * M�todos para la interacci�n con Casos de Prueba
//		 */
//		public void addTestCase(String testcaseName) {
//			if (! listTestCases.containsKey(testcaseName)) {
//				listTestCases.put(testcaseName, new ReportingTestCase(testcaseName));
//			}
//		}
//		
//		public void addTestCase(ArrayList<String> testcaseList)
//		{
//			for (String casoPrueba : testcaseList) {
//				listTestCases.put(casoPrueba, new ReportingTestCase(casoPrueba));
//			}
//		}
//
//		public ITCRepositoryTestMethod getTestMethod(String testMethodName) {
//			return listTestCases.get(testMethodName);
//		}
//		
//		public void setPassedTest(String testName) {
//			if (listTestCases.containsKey(testName)) {
//				listTestCases.get(testName).testPass();
//			} else {
//				Logger.raiseMinor("The testcase name was not found in the !! No se encontr� el caso de prueba con identificador: " + testName);
//			}
//		}
//
//		public void completeNotReportedTests(String exceptionMessage)
//		{
//			boolean testFailedDetected = false;
//			String pruebaBloqueante = null;
//			for (ReportingTestCase testcase : listTestCases.values())
//			{
//				if (testcase.getExecutionResult() == null && ! testFailedDetected) {
//					testcase.testFail(exceptionMessage);
//					pruebaBloqueante = testcase.__testName;
//					testFailedDetected = true;
//				} else if (testFailedDetected) {
//					testcase.testBlock(pruebaBloqueante);
//				}
//			}
//		}
//
//		public void notifyAllResultsFromClass()
//		{
//			for (ReportingTestCase testcase : listTestCases.values()) {
//				testcase.reportResult();
//			}
//		}
//	}
//	
//	public void addTestClass(String nombreSuite) {
//		if (!listTestClass.containsKey(nombreSuite)) {
//			listTestClass.put(nombreSuite, new ReportingTestClass());
//		}
//	}
//
//	public ITCRepositoryTestClass getTestClass(String nombreTestClass) {
//		return listTestClass.get(nombreTestClass);
//	}
//
//	public void setEjecucionCorrecta(String suitePrueba, String casoPrueba) {
//		getTestClass(suitePrueba).getTestMethod(casoPrueba).testPass();
//	}
//
//	private static int getTestProyectID(String testProyectName) {
//		try {
//			TestLinkAPIResults repositoryProjects = repositoryAPI.getProjects();
//			
//			int projectTotalNumber = repositoryProjects.size();
//			//Comparamos cada entrada para ver si es el proyecto buscado y si lo es, guardamos el valor y le devolvemos
//			for (int i = 0; i < projectTotalNumber; i++)
//			{
//				Map<?,?> dataProjectI = repositoryProjects.getData(i);
//				if (Objects.equal(dataProjectI.get("name").toString(),testProyectName)) {
//					String idProject = dataProjectI.get("id").toString();
//					return Integer.parseInt(idProject);
//				}
//			}
//			Logger.raiseMinor("The testProject with name '" + testProyectName + "' could not be located.");
//		} catch (TestLinkAPIException errorConexi�n) {
//			Logger.raiseMinor("The list of TestLink's testProjects could not be recovered due to a problem with it's API communication.");
//			return 0;
//		}
//		return 0;
//	}
//
//	private static int getTestPlanID(String testPlanName, int testProyectId) {
//		try {
//			TestLinkAPIResults repositoryPlans = repositoryAPI.getProjectTestPlans(testProyectId);
//			
//			int numeroProyectos = repositoryPlans.size();
//			//Comparamos cada entrada para ver si es el proyecto buscado
//			for (int i = 0; i<numeroProyectos; i++)
//			{
//				Map<?,?> dataPlanI = repositoryPlans.getData(i);
//				if (Objects.equal(dataPlanI.get("name").toString(),testPlanName)) {
//					String idPlan = dataPlanI.get("id").toString();
////						AccesoFicherosPropiedades.setPropertyValue("Plan_" + testPlanName, idPlan, "InterfazTestLink.properties");
//					return Integer.parseInt(idPlan);
//				}
//			}
//			Logger.raiseMinor("The testPlan with name '" + testPlanName + "' could not be located.");
//		} catch (TestLinkAPIException errorConexi�n) {
//			Logger.raiseMinor("The list of TestLink's testPlans could not be recovered due to a problem with it's API communication.");
//			return 0;
//		}
//		return 0;
//	}
//	
//	private static int getTestBuildID(String buildName, int testPlanNameId) {
//		try {
//			TestLinkAPIResults repositoryBuilds = repositoryAPI.getBuildsForTestPlan(testPlanNameId);
//			
//			int numeroProyectos = repositoryBuilds.size();
//			//Comparamos cada entrada para ver si es el proyecto buscado y si lo es, guardamos el valor y le devolvemos
//			for (int i = 0; i<numeroProyectos; i++)
//			{
//				Map<?,?> dataBuildI = repositoryBuilds.getData(i);
//				if (Objects.equal(dataBuildI.get("name").toString(),buildName)) {
//					String idBuild = dataBuildI.get("id").toString();
////						AccesoFicherosPropiedades.setPropertyValue("Build_" + buildName, idBuild, "InterfazTestLink.properties");
//					return Integer.parseInt(idBuild);
//				}
//			}
//			Logger.raiseMinor("The testBuild with name '" + buildName + "' could not be located.");
//		} catch (TestLinkAPIException errorConexi�n) {
//			Logger.raiseMinor("The list of TestLink's builds could not be recovered due to a problem with it's API communication.");
//			return 0;
//		}
//		return 0;
//	}
//	
//	
//
//}
