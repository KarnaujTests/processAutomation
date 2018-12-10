package com.juancarloscasas.baseProject.baseTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.juancarloscasas.baseProject.FrameworkTools.GeneradorLogs.Logger;
import com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration.ITCRepository;
import com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration.NullConnection;
import com.juancarloscasas.baseProject.FrameworkTools.TestSuitesBase.CustomSuite;

public class TestsRunner {
	static String stackTrace;
	public static void main(String[] args) {
		//TODO: Repository is being defined twice (TestRunner and TestBase). Refactor to stablish an only point of definition
		ITCRepository applicationRepositoryExecutions = NullConnection.getInstance();
		
		CustomSuite suite = new ExampleSuite();
		suite.addCompleteExecutionTo(applicationRepositoryExecutions);
		
		Result result = JUnitCore.runClasses(
				suite.getClass()
		);
		
		for (Failure failure : result.getFailures()) {
			String nombreMetodoFallo = failure.getTestHeader();
			String mensajeFallo = failure.getMessage();
			stackTrace = failure.getTrace();
			
			String claseFallo = nombreMetodoFallo.substring(nombreMetodoFallo.lastIndexOf("(") + 1, nombreMetodoFallo.lastIndexOf(")"));
			
			applicationRepositoryExecutions.getTestClass(claseFallo).completeNotReportedTests(mensajeFallo);
			
			applicationRepositoryExecutions.getTestClass(claseFallo).notifyAllResultsFromClass();
		}
		
		if (! result.wasSuccessful())
		{
			Logger.write("********************************************************");
			Logger.write("* !! ERRORS/FAILURES APPEARED IN THE TEST EXECUTION !! *");
			Logger.write("********************************************************");
		}
		
		Logger.save();
		
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
	
}
