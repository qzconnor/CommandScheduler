package de.xconnortv.commandscheduler.guis;

import de.xconnortv.commandscheduler.CommandScheduler;
import de.xconnortv.commandscheduler.ConfigHolder;
import de.xconnortv.commandscheduler.SkullsRegistry;
import de.xconnortv.commandscheduler.commands.SkullCreator;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class EditGui extends Gui {
    private final ConfigHolder.ConfigCommand command;
    private final boolean editMode;

    public EditGui(@NotNull Player player, @Nullable ConfigHolder.ConfigCommand command) {
        super(player, "edit-gui", command == null ? "New Scheduler" : "Edit Scheduler > §6§l" + command.getName(), 3);
        this.editMode = command != null;
        if(command == null) this.command = new ConfigHolder.ConfigCommand();
        else this.command = command;

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName("§7"));

        String commandName = !editMode ? "New Scheduler " + getRandomNumber(100, 999) : command.getName();
        String commandCommand = !editMode ? "-" : command.getCommand();
        String commandInterval = !editMode ? "-" : command.getInterval();


        addItem(8, new Icon(
                SkullCreator.itemFromBase64(SkullsRegistry.LEFT_SKULL)
        ).setName("§6Back").setLore("§7» All unsaved changes are lost.").onClick(e -> {
            player.closeInventory();
            new ListGui((Player) e.getWhoClicked()).open();
        }));

        addItem(10, new Icon(
                Material.NAME_TAG
        ).setName(editMode ? "§a§lEdit Name" : "§a§lName")
                .setLore("§7» Current: §6§l" + commandName)
                 .onClick(e -> {
                     player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                     new AnvilGUI.Builder().onClose(stateSnapshot -> {
                                 this.command.setName(stateSnapshot.getText());
                                 open();
                                 player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                             })
                             .onClick((slot, stateSnapshot) -> {
                                 if(slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();

                                 return List.of(AnvilGUI.ResponseAction.close());
                             })
                             .preventClose()
                             .title(editMode ? "Edit Name" : "Name")
                             .text(commandName)
                             .plugin(CommandScheduler.getInstance())
                             .open(player);





                }));

        addItem(12, new Icon(
                Material.COMMAND_BLOCK
        ).setName("§a§lEdit Command")
                .setLore("§7» Current: §6§l" + ConfigHolder.limitAndAddEllipsis(commandCommand, 20))
                .onClick(e -> {
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                    new AnvilGUI.Builder().onClose(stateSnapshot -> {
                                this.command.setCommand(stateSnapshot.getText());
                                open();
                                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                            })
                            .onClick((slot, stateSnapshot) -> {
                                if(slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();

                                return List.of(AnvilGUI.ResponseAction.close());
                            })
                            .preventClose()
                            .title(editMode ? "Edit Command" : "Command")
                            .text(commandCommand)
                            .plugin(CommandScheduler.getInstance())
                            .open(player);
                }));


        addItem(14, new Icon(
                Material.CLOCK
        ).setName("§a§lEdit Interval")
                .setLore("§7» Current: §6§l" + commandInterval)
                .onClick(e -> {
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                    new AnvilGUI.Builder().onClose(stateSnapshot -> {
                                this.command.setInterval(stateSnapshot.getText());
                                open();
                                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                            })
                            .onClick((slot, stateSnapshot) -> {
                                if(slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();

                                if(!ConfigHolder.ConfigCommand.validDateInterval(stateSnapshot.getText())){
                                    return List.of(
                                            AnvilGUI.ResponseAction.replaceInputText("Invalid Interval")
                                    );
                                }

                                return List.of(AnvilGUI.ResponseAction.close());
                            })
                            .preventClose()
                            .title(editMode ? "Edit Name" : "Name")
                            .text(commandInterval)
                            .plugin(CommandScheduler.getInstance())
                            .open(player);
                }));

        if(editMode) {
            addItem(getLastSlot() - 1, new Icon(
                    Material.BARRIER
            ).setName("§c§lRemove").setLore("§7» Removes scheduler from Config").hideFlags().onClick(e -> {
                if(CommandScheduler.getInstance().getConfigHolder().removeCommand(command.getName())) {
                    CommandScheduler.getInstance().getConfigHolder().save();
                    new ListGui((Player) e.getWhoClicked()).open();
                }
                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
            }));

        }


        addItem(getLastSlot(), new Icon(
                SkullCreator.itemFromBase64(SkullsRegistry.SAVE_SKULL)
        ).setName("§a§lSave")
                .setLore("§7» Save the scheduler to config")
                .onClick(e -> {
                    CommandScheduler.getInstance().getConfigHolder().save();
                    CommandScheduler.getInstance().reload();
                    new ListGui((Player) e.getWhoClicked()).open();
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                }));


    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
