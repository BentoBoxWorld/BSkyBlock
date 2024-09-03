package dev.viaduct.factories.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@Getter
public class ItemBuilder {

    private final ItemStack item;
    private Material material;
    private int amount;
    private final ItemMeta meta;
    private List<String> lore = new ArrayList<>();
    private final Map<Enchantment, Integer> enchants = new HashMap<>();

    public ItemBuilder(Material material) {
        this.material = material;
        amount = 1;
        item = new ItemStack(this.material, amount);
        meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.material = material;
        this.amount = amount;
        item = new ItemStack(this.material, amount);
        meta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.material = item.getType();
        this.meta = item.getItemMeta();
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            lore.addAll(item.getItemMeta().getLore());
        }
    }

    public ItemBuilder setMaterial(Material material) {
        item.setType(material);
        this.material = material;
        return this;
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(Chat.colorize(name));
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        lore.add(Chat.colorize("&7" + line));
        return this;
    }

    public ItemBuilder addLoreLines(String... lines) {
        Arrays.stream(lines).forEach(line -> {
            line = "&7" + line;
            lore.add(Chat.colorize(line));
        });
        return this;
    }

    public ItemBuilder addLoreLines(List<String> lines) {
        lines.forEach(line -> {
            line = "&7" + line;
            lore.add(Chat.colorize(line));
        });
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = Chat.colorizeList(lore);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment type, int level) {
        enchants.put(type, level);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        item.setAmount(this.amount);
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        meta.setCustomModelData(data);
        return this;
    }

    public ItemBuilder setItemFlags(ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flag) {
        meta.addItemFlags(flag);
        return this;
    }

    public ItemBuilder setPersistentData(NamespacedKey key, String value) {
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        return this;
    }

    public ItemBuilder unbreakable(boolean hideUnbreakable) {
        if (hideUnbreakable) {
            setItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        meta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder glowing() {
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemStack build() {
        meta.setLore(lore);
        enchants.forEach(item::addUnsafeEnchantment);
        item.setItemMeta(meta);
        return item;
    }

}
