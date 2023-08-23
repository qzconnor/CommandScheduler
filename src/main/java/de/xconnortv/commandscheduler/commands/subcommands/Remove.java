package de.xconnortv.commandscheduler.commands.subcommands;

import de.xconnortv.commandscheduler.commands.util.AbstractCommand;
import de.xconnortv.commandscheduler.commands.util.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Remove extends SubCommand {
    public Remove(@NotNull AbstractCommand cmd) {
        super("remove", cmd, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1) {
            commandSender.sendMessage(getInstance().formatMessage("&cPlease provide a name!"));
            return true;
        }
        if(getInstance().getConfigHolder().removeCommand(args[0])) {
            getInstance().getConfigHolder().save();
            commandSender.sendMessage(getInstance().formatMessage("Successfully removed: &6&l" + args[0]));
        }else {
            commandSender.sendMessage(getInstance().formatMessage("&cScheduler with &6&l" + args[0] + " &cnot found!"));
        }
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
