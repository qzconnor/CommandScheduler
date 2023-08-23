package de.xconnortv.commandscheduler;

import com.sun.security.auth.UnixNumericUserPrincipal;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
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

            commandHashMap.put(key, new ConfigCommand(
                    key,
                    config.getString(cnfPre + ".command"),
                    config.getString(cnfPre + ".interval"),
                    config.getBoolean(cnfPre + ".active", false)
            ));
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

    @Getter
    @Setter
    public static class ConfigCommand {
        private String name;
        private String command;
        private String interval;
        private boolean active;
        private int schedulerId;

        public ConfigCommand(String name, String command, String interval, boolean active) {
            this.name = name;
            this.command = command;
            this.interval = interval;
            this.active = active;
            this.schedulerId = -1;
        }

        public long getRealInterval() {
            long ticks = 0;

            try {
                int value = Integer.parseInt(getInterval().substring(0, getInterval().length() - 1));
                char unit = getInterval().charAt(getInterval().length() - 1);

                switch (unit) {
                    case 'd':
                        ticks = (long) value * 24 * 60 * 60 * 20; // 1 Tag = 24 Stunden = 24 * 60 Minuten = 24 * 60 * 60 Sekunden = 24 * 60 * 60 * 20 Ticks
                        break;
                    case 's':
                        ticks = value * 20L; // 1 Sekunde = 20 Ticks
                        break;
                    default:
                        System.out.println("Ungültige Einheit.");
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                System.out.println("Ungültiges Eingabeformat.");
            }

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
