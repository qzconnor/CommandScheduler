package de.xconnortv.commandscheduler.commands;

import de.xconnortv.commandscheduler.CommandScheduler;
import de.xconnortv.commandscheduler.commands.util.AbstractCommand;
import org.jetbrains.annotations.NotNull;

public class ChestShop extends AbstractCommand {
    public ChestShop(@NotNull CommandScheduler instance) {
        super("commandscheduler", instance);
    }
}
