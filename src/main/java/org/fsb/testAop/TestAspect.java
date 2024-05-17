package org.fsb.testAop;

import core.annotations.After;
import core.annotations.Aspect;
import core.annotations.Before;

@Aspect
public class TestAspect {
    @Before("org.fsb.testAop.Command.execute()")
    public void beforeTreatment(){
        System.out.println("Treatment Before for execute");
    }

    @After("org.fsb.testAop.Command.others()")
    public void afterTreatment(){
        System.out.println("Treatment After for other");
    }
}
