package org.fsb.fakeControllers;

import core.annotations.Autowire;
import core.annotations.Component;
import core.annotations.FakeController;
import org.fsb.testAop.Command;

@FakeController
@Component
public class TestAOPController {

    @Autowire
    Command command;

    public void TestAOP() {
        command.execute();
        command.other();
    }

}
