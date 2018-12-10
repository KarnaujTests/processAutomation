package com.juancarloscasas.baseProject.baseTests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runners.Suite;

import com.juancarloscasas.baseProject.FrameworkTools.TestSuitesBase.CustomSuite;
import com.juancarloscasas.baseProject.baseTests.testsDefinition.ExampleTest;

@Suite.SuiteClasses({
	ExampleTest.class
})
public class ExampleSuite extends CustomSuite {
	
	@BeforeClass
	public static void assemblySetUp() {
		
	}
	
	@AfterClass
	public static void assemblyTearDown() {
		
	}
	
}
