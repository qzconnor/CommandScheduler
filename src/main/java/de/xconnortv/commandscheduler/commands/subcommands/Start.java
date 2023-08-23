package de.xconnortv.commandscheduler.commands.subcommands;

import de.xconnortv.commandscheduler.ConfigHolder;
import de.xconnortv.commandscheduler.commands.util.AbstractCommand;
import de.xconnortv.commandscheduler.commands.util.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Start extends SubCommand {
    public Start(@NotNull AbstractCommand cmd) {
        super("start", cmd, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1) {
            commandSender.sendMessage(getInstance().formatMessage("&cPlease provide a name"));
            return true;
        }

        ConfigHolder.ConfigCommand cmd = getInstance().getConfigHolder().getCommand(args[0]);
        if(cmd == null) {
            commandSender.sendMessage(getInstance().formatMessage("&cScheduler with &6&l" + args[0] + " &cnot found!"));
            return true;
        }

        if(cmd.isActive()) {
            commandSender.sendMessage(getInstance().formatMessage("&cScheduler &6&l" + args[0] + " &calready running!"));
            return true;
        }

        cmd.setActive(true);
        getInstance().getConfigHolder().save();
        getInstance().scheduleCommand(args[0]);
        commandSender.sendMessage(getInstance().formatMessage("Started Scheduler: &6&l" + args[0]));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length > 1) return Collections.emptyList();

        List<String> list = new ArrayList<>();
        completeAction(list, args[0]);
        return list;
    }
    public void completeAction(List<String> list, String prefix){
        String text = prefix.toLowerCase();

        getInstance().getConfigHolder().getCommandHashMap().keySet().forEach(command -> {
            if(command.toLowerCase().startsWith(text)) {
                list.add(command.toLowerCase());
            }
        });
    }

}
