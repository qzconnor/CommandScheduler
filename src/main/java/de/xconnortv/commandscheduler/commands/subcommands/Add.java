package de.xconnortv.commandscheduler.commands.subcommands;

import de.xconnortv.commandscheduler.commands.util.AbstractCommand;
import de.xconnortv.commandscheduler.commands.util.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Add extends SubCommand {
    public Add(@NotNull AbstractCommand cmd) {
        super("add", cmd, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        commandSender.sendMessage(getInstance().formatMessage("&6Please edit the Config to add more commands!"));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }

}
