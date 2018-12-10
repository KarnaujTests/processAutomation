package com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration;

import java.util.ArrayList;

public interface ITCRepositoryTestClass {
	
	public void addTestCase(String testName);
	public void addTestCase(ArrayList<String> listTestsNames);
	
	public void setPassedTest(String testName);
	public void completeNotReportedTests(String messageException);

	public void notifyAllResultsFromClass();
	
	public ITCRepositoryTestMethod getTestMethod(String testMethodName);

}