package com.juancarloscasas.baseProject.FrameworkTools.TestCaseRepositoryIntegration;

public interface ITCRepositoryTestMethod {
	
	public String getExecutionResult();
	
	public void testPass();
	public void testFail(String executionException);
	public void testBlock(String testcaseBlocking);
	
	public void reportResult();
}