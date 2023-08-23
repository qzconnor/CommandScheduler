package de.xconnortv.commandscheduler.commands;

import de.xconnortv.commandscheduler.CommandScheduler;
import de.xconnortv.commandscheduler.commands.subcommands.*;
import de.xconnortv.commandscheduler.commands.util.AbstractCommand;
import org.jetbrains.annotations.NotNull;

public class SchedulerCommand extends AbstractCommand {
    public SchedulerCommand(@NotNull CommandScheduler instance) {
        super("scheduler", instance);
        this.register(new Add(this));
        this.register(new Remove(this));
        this.register(new Reload(this));
        this.register(new Stop(this));
        this.register(new Start(this));
        this.register(new List(this));
    }
}
