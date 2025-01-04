package dev.minelia.mineliashop.utils.gui;

import java.util.List;
import java.util.Map;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BetterItem extends ItemStack {

  public BetterItem(ItemStack itemStack) {
    super(itemStack);
  }

  public BetterItem(Material material) {
    super(material);
  }

  public BetterItem(Material material, int amount) {
    super(material, amount);
  }

  public BetterItem setName(String name) {
    ItemMeta meta = this.getItemMeta();
    assert meta != null;
    meta.setDisplayName(name);
    this.setItemMeta(meta);
    return this;
  }

  public String getName() {
    ItemMeta meta = this.getItemMeta();
    assert meta != null;
    if (meta.hasDisplayName()) {
      return meta.getDisplayName();
    }
    return this.getType().name();
  }

  public BetterItem setLore(List<String> lore) {
    ItemMeta meta = this.getItemMeta();
    assert meta != null;
    meta.setLore(lore);
    this.setItemMeta(meta);
    return this;
  }

  public List<String> getLore() {
    ItemMeta meta = this.getItemMeta();
    assert meta != null;
    return meta.getLore();
  }

  public BetterItem setQuantity(int amount) {
    this.setAmount(amount);
    return this;
  }

  public BetterItem setUnbreakable(boolean unbreakable) {
    ItemMeta meta = this.getItemMeta();
    assert meta != null;
    meta.spigot().setUnbreakable(unbreakable);
    return this;
  }

  public boolean isUnbreakable() {
    ItemMeta meta = this.getItemMeta();
    assert meta != null;
    return meta.spigot().isUnbreakable();
  }

  public BetterItem addEnchant(Enchantment enchant, int level) {
    this.addEnchantment(enchant, level);
    return this;
  }

  public BetterItem addEnchants(Map<Enchantment, Integer> enchants) {
    this.addEnchantments(enchants);
    return this;
  }

  public BetterItem addFlags(ItemFlag... flags) {
    ItemMeta meta = this.getItemMeta();
    assert meta != null;
    meta.addItemFlags(flags);
    this.setItemMeta(meta);
    return this;
  }

  public BetterItem removeFlags(ItemFlag... flags) {
    ItemMeta meta = this.getItemMeta();
    assert meta != null;
    meta.removeItemFlags(flags);
    this.setItemMeta(meta);
    return this;
  }

  public BetterItem setColor(DyeColor color) {
    this.setDurability(color.getData());
    return this;
  }

  public Button asButton() {
    return new Button(this);
  }
}