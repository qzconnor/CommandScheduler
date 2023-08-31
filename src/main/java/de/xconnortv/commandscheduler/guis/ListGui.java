package de.xconnortv.commandscheduler.guis;

import de.xconnortv.commandscheduler.CommandScheduler;
import de.xconnortv.commandscheduler.ConfigHolder;
import de.xconnortv.commandscheduler.SkullsRegistry;
import de.xconnortv.commandscheduler.commands.SkullCreator;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class ListGui extends Gui {
    private final PaginationManager pagination = new PaginationManager(this);

    public ListGui(@NotNull Player player) {
        super(player, "list-gui", "List", 4);
        this.pagination.registerPageSlotsBetween(9, (9 * (4 - 1)) - 1);


        if (pagination.getCurrentPage() != 0) {
            addItem(0, new Icon(
                    SkullCreator.itemFromBase64(SkullsRegistry.LEFT_SKULL)
            ).setName("§6Previous").onClick(e -> {
                pagination.goPreviousPage();
                calculateAndUpdatePagination();
                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
            }));
        }
        if (!pagination.isLastPage()) {
            addItem(8, new Icon(
                    SkullCreator.itemFromBase64(SkullsRegistry.RIGHT_SKULL)
            ).setName("§6Next").onClick(e -> {
                pagination.goNextPage();
                calculateAndUpdatePagination();
                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
            }));
        }

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillRow(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName("§7"), 0);
        fillRow(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName("§7"), 4 - 1);
        calculateAndUpdatePagination();
        addItem(getSize() - 5, new Icon(
                SkullCreator.itemFromBase64(SkullsRegistry.PLUS_SKULL)
        ).setName("§6§lAdd Scheduler").onClick(e -> {
            new EditGui(player, null).open();
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
        }));


    }
    private void fetchSchedulers() {
        pagination.getItems().clear();
        for(ConfigHolder.ConfigCommand cmd : CommandScheduler.getInstance().getConfigHolder().getCommandHashMap().values()) {
            Icon icon = new Icon(Material.REPEATING_COMMAND_BLOCK);
            icon.setName("§6§l" + cmd.getName());

            if(cmd.isActive()) icon.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

            icon.setLore(cmd.isActive() ?
                            "§7» §a§lACTIVE" : "§7» §c§lINACTIVE",
                    "§7» Command: §6§l" + ConfigHolder.limitAndAddEllipsis(cmd.getCommand(), 20),
                    "§7» Interval: §6§l" + cmd.getInterval(),
                    "",
                    "§7» Toggle §a(Left Click)",
                    "§7» Edit §a(Right Click)"
            );
            icon.hideFlags();

            icon.onClick(event -> {
               if(event.isLeftClick()) {
                   Player player = (Player) event.getWhoClicked();
                   player.performCommand(cmd.isActive() ? "commandscheduler:scheduler stop " + cmd.getName() : "commandscheduler:scheduler start " + cmd.getName());
                   calculateAndUpdatePagination();
                   player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                   return;
               }
               if(event.isRightClick()) {
                   new EditGui(player, cmd).open();
                   player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
               }
            });

            pagination.addItem(icon);

        }
    }


    private void calculateAndUpdatePagination() {
        fetchSchedulers();
        this.pagination.update();
    }


}
