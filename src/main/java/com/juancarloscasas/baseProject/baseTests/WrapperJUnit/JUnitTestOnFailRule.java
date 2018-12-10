package com.juancarloscasas.baseProject.baseTests.WrapperJUnit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.juancarloscasas.baseProject.FrameworkTools.GeneradorLogs.Logger;
import com.juancarloscasas.baseProject.baseTests.TestsBaseDefinition;

public class JUnitTestOnFailRule extends TestsBaseDefinition implements TestRule {

    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
 
            @Override
            public void evaluate() throws Throwable {
                try {
            		Logger.write("*******************************");
            		Logger.write("*Beginning execution test :" + this.getClass().getName() + "*");
            		Logger.write("*******************************");

                    base.evaluate();
                    
            		Logger.write("************************");
            		Logger.write("*End of test: " + this.getClass().getName() + "*");
            		Logger.write("************************");

                } catch (AssertionError e) {
                	
                	Logger.raiseTestFailure(e.getMessage());

                	driverTakeGlobalScreenshot("FAIL_" + description.getMethodName(), e);
                	
                    throw e;
                    
                } catch (Exception e) {
                	
                	Logger.raiseTestFailure(e.toString());

                	try {
                		driverTakeGlobalScreenshot("ERROR_" + description.getMethodName(), e);
                	} finally {
                		driversClosure();
                	}

                	throw e;
                	
                } finally {
                	
//                	afterMethod();
                	
                }
            }
            
        };
        
    }
}
