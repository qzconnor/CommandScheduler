package de.xconnortv.commandscheduler;

import com.sun.security.auth.UnixNumericUserPrincipal;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ConfigHolder {

    private CommandScheduler instance;

    private final String prefix;
    private final HashMap<String, ConfigCommand> commandHashMap;
    private final FileConfiguration config;

    public ConfigHolder(CommandScheduler instance, String prefix, HashMap<String, ConfigCommand> commandHashMap) {
        this.instance = instance;
        this.prefix = prefix;
        this.commandHashMap = commandHashMap;
        this.config = instance.getConfig();
    }

    public ConfigCommand getCommand(String name) {

        return getCommandHashMap().get(name);
    }

    public boolean removeCommand(String name) {
        if(!getCommandHashMap().containsKey(name)) return false;
        instance.stopScheduleCommand(name);
        commandHashMap.remove(name);
        return true;
    }

    public void save() {
        config.set("prefix", getPrefix());
        config.set("commands", null);
        for(ConfigCommand command : commandHashMap.values()) {
            String cnfPre = "commands." + command.getName();
            config.set(cnfPre + ".command", command.getCommand());
            config.set(cnfPre + ".interval", command.getInterval());
            config.set(cnfPre + ".active", command.isActive());
        }
        instance.saveConfig();
    }

    public static ConfigHolder load(CommandScheduler instance) {
        FileConfiguration config = instance.getConfig();
        HashMap<String, ConfigCommand> commandHashMap = new HashMap<>();
        if(!config.contains("commands") || config.getConfigurationSection("commands") == null) return null;
        for(String key : Objects.requireNonNull(
                config.getConfigurationSection("commands")
        ).getKeys(false)) {
            String cnfPre = "commands." + key;

            if(ConfigCommand.validDateInterval(config.getString(cnfPre + ".interval"))) {
                commandHashMap.put(key, new ConfigCommand(
                        key,
                        config.getString(cnfPre + ".command"),
                        config.getString(cnfPre + ".interval"),
                        config.getBoolean(cnfPre + ".active", false)
                ));
            }


        }

        return new ConfigHolder(
                instance,
                config.getString("prefix"),
                commandHashMap
        );
    }

    @Override
    public String toString() {
        return "ConfigHolder{" +
                "instance=" + instance +
                ", prefix='" + prefix + '\'' +
                ", commandHashMap=" + commandHashMap +
                '}';
    }

    public static String limitAndAddEllipsis(String input, int maxLength) {
        if (input.length() <= maxLength) {
            return input;
        } else {
            String truncated = input.substring(0, maxLength - 3); // -3 for the ellipsis
            return truncated + "...";
        }
    }


    @Getter
    @Setter
    public static class ConfigCommand {
        private String name;
        private String command;
        private String interval;
        private boolean active;
        private int schedulerId;


        public ConfigCommand() {
            this.schedulerId = -1;
        }

        public ConfigCommand(String name, String command, String interval, boolean active) {
            this.name = name;
            this.command = command;
            this.interval = interval;
            this.active = active;
            this.schedulerId = -1;
        }

        public static boolean validDateInterval(@Nullable String string) {
            if(string == null) return false;
            List<Character> validUnits = List.of('d', 's');
            try {
                Integer.parseInt(string.substring(0, string.length() - 1));
            } catch (NumberFormatException e) {
                return false;
            }
            char unit = string.charAt(string.length() - 1);
            System.out.println(unit);
            return validUnits.contains(unit);
        }

        public long getRealInterval() throws RuntimeException {
            long ticks;

            if(!validDateInterval(getInterval())) {
                return -1;
            }

            int value = Integer.parseInt(getInterval().substring(0, getInterval().length() - 1));
            char unit = getInterval().charAt(getInterval().length() - 1);

            ticks = switch (unit) {
                case 'd' -> value * 24L * 60L * 60L * 20L;
                case 's' -> value * 20L;
                default -> throw new RuntimeException("Invalid date format!");
            };
            return ticks;
        }

        @Override
        public String toString() {
            return "ConfigCommand{" +
                    "name='" + name + '\'' +
                    ", command='" + command + '\'' +
                    ", interval='" + interval + '\'' +
                    ", active=" + active +
                    '}';
        }
    }

}
