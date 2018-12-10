package com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration;

public interface ITCRepository {
	
	public void addTestClass(String nombreSuite);
	
	public ITCRepositoryTestClass getTestClass(String nombreTestClass);
	
}

