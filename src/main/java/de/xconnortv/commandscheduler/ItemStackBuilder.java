package de.xconnortv.commandscheduler;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;

public class ItemStackBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemStackBuilder(Material material) {
        itemStack = new ItemStack(material, 1);
        itemMeta = itemStack.getItemMeta();
    }
    public ItemStackBuilder setDisplayName(String displayName) {
        itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemStackBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }
    public ItemStackBuilder setLore(String[] lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }
    public ItemStackBuilder setEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }
    public ItemStackBuilder setArmorColor(Color color) {
        ((LeatherArmorMeta) itemMeta).setColor(color);
        return this;
    }
    public ItemStackBuilder setItemFlags(ItemFlag itemFlags) {
        itemMeta.addItemFlags(itemFlags);
        return this;
    }
    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }
    public ItemStackBuilder addGlow() {
        //itemStack.addUnsafeEnchantment(new GlowEnchantment(), 1);
        return this; //setItemFlags(ItemFlag.HIDE_ENCHANTS)
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
