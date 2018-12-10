package com.juancarloscasas.baseProject.baseTests.WrapperJUnit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.juancarloscasas.baseProject.baseTests.TestsBaseDefinition;

public class JUnitClassWrapperRule extends TestsBaseDefinition implements TestRule {

    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
 
            @Override
            public void evaluate() throws Throwable {
                try {
                	
                	pageInitializationMethod();
                	
                    base.evaluate();
                    
                } finally {
                	
                	driversClosure();
                	
                }
            }
            
        };
        
    }
}
