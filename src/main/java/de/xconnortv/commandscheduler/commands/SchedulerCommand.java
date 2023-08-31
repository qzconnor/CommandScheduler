package de.xconnortv.commandscheduler.commands;

import de.xconnortv.commandscheduler.CommandScheduler;
import de.xconnortv.commandscheduler.guis.ListGui;
import de.xconnortv.commandscheduler.commands.subcommands.*;
import de.xconnortv.commandscheduler.commands.util.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SchedulerCommand extends AbstractCommand {
    public SchedulerCommand(@NotNull CommandScheduler instance) {
        super("scheduler", instance);
        this.register(new Reload(this));
        this.register(new Stop(this));
        this.register(new Start(this));
    }

    @Override
    public boolean onRootCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String[] args) {
        new ListGui((Player) commandSender).open();
        return true;
    }
}
