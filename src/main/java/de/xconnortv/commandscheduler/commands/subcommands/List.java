package de.xconnortv.commandscheduler.commands.subcommands;

import de.xconnortv.commandscheduler.ConfigHolder;
import de.xconnortv.commandscheduler.commands.util.AbstractCommand;
import de.xconnortv.commandscheduler.commands.util.SubCommand;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class List extends SubCommand {
    public List(@NotNull AbstractCommand cmd) {
        super("list", cmd, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        commandSender.sendMessage(getInstance().formatMessage("&6Here are all Schedulers:"));
        for(ConfigHolder.ConfigCommand cmd : getInstance().getConfigHolder().getCommandHashMap().values()) {
            TextComponent start = new TextComponent("§a§l[START]");
            start.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/commandscheduler:scheduler start " + cmd.getName()));
            TextComponent stop = new TextComponent("§c§l[STOP]");
            stop.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/commandscheduler:scheduler stop " + cmd.getName()));

            if(cmd.isActive()) {
                commandSender.spigot().sendMessage(new TextComponent(" §7» §6§l" + cmd.getName() + " §7is started! "), new TextComponent(" "), stop);
            }else {
                commandSender.spigot().sendMessage(new TextComponent(" §7» §6§l" + cmd.getName() + " §7is stopped! "), new TextComponent(" "), start);
            }


        }
        return true;
    }

    @Override
    public java.util.List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }

}
