package org.fsb.testAop;

import core.annotations.Component;

@Component
public class CommandImpl implements Command {
    private final String label = "yess !";
    @Override
    public void execute() {
        System.out.println(this.label);
    }

    @Override
    public void other() {
        System.out.println("Other !");
    }
}
