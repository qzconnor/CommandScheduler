package de.xconnortv.commandscheduler;

import de.xconnortv.commandscheduler.commands.SchedulerCommand;
import de.xconnortv.commandscheduler.commands.util.AbstractCommand;
import de.xconnortv.commandscheduler.exeptions.SchedulerException;
import lombok.Getter;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
@Getter
public final class CommandScheduler extends JavaPlugin {

    private static CommandScheduler INSTANCE;

    private ConfigHolder configHolder;
    @Override
    public void onEnable() {
        INSTANCE = this;
        new InventoryAPI(this).init();
        initConfig();
        configHolder = ConfigHolder.load(this);


        this.registerCommand(new SchedulerCommand(this), List.of("cs", "commandscheduler"));

        scheduleAll();


    }

    public void initConfig() {
        if(getConfig().getKeys(true).isEmpty()) {
            getConfig().set("prefix", "&6Prefix &7»");
            getConfig().set("commands.foo.command", "say hi");
            getConfig().set("commands.foo.interval", "10s");
            getConfig().set("commands.foo.active", false);
            saveConfig();
        }
    }

    public void scheduleAll() {
        for(ConfigHolder.ConfigCommand command : configHolder.getCommandHashMap().values()) {
            if(command.isActive()) scheduleCommand(command.getName());
        };
    }
    public void stopScheduleAll() {
        for(String name : configHolder.getCommandHashMap().keySet()) stopScheduleCommand(name);
    }

    public void scheduleCommand(String name) throws SchedulerException {
        ConfigHolder.ConfigCommand command = configHolder.getCommand(name);
        if(command == null)
            throw new SchedulerException("Provided command does not exists!");
        if(Bukkit.getScheduler().isCurrentlyRunning(command.getSchedulerId()))
            throw new SchedulerException("Scheduler already running!");

        command.setSchedulerId(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            getServer().dispatchCommand(getServer().getConsoleSender(), command.getCommand());
        }, 0, command.getRealInterval()));

    }

    public void stopScheduleCommand(String name) throws SchedulerException {
        ConfigHolder.ConfigCommand command = configHolder.getCommand(name);
        if(command == null)
            throw new SchedulerException("Provided command does not exists!");
        if(Bukkit.getScheduler().isCurrentlyRunning(command.getSchedulerId()))
            throw new SchedulerException("Scheduler is not running!");

        Bukkit.getScheduler().cancelTask(command.getSchedulerId());

    }


    @Override
    public void onDisable() {
        stopScheduleAll();
    }
    public void reload() {
        reload(false);
    }
    public void reload(boolean holderOnly) {
        reloadConfig();
        initConfig();
        if(holderOnly) {
            configHolder = ConfigHolder.load(this);
            return;
        }
        stopScheduleAll();
        configHolder = ConfigHolder.load(this);
        scheduleAll();
    }

    public void registerCommand(@NotNull AbstractCommand abstractCommand, @Nullable List<String> aliases) {
        this.registerCommand(abstractCommand.getName(), abstractCommand, aliases);
    }

    public void registerCommand(@NotNull String name, @NotNull TabExecutor executor, @Nullable List<String> aliases) {
        PluginCommand command = getCommand(name);
        if(command == null) return;
        command.setExecutor(executor);
        command.setTabCompleter(executor);
        if(aliases == null) return;
        command.setAliases(aliases);
    }

    public String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', configHolder.getPrefix() + " " + message);
    }


    public static CommandScheduler getInstance() {
        return INSTANCE;
    }

}
