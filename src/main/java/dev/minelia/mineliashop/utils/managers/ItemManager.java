package dev.minelia.mineliashop.utils.managers;

import dev.minelia.mineliashop.utils.gui.BetterItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemManager {

  public BetterItem createItemStack(ItemStack itemStack) {
    return new BetterItem(itemStack);
  }

  public BetterItem createItemStack(Material material) {
    return new BetterItem(material);
  }

  public BetterItem createItemStack(Material material, int amount) {
    return new BetterItem(material, amount);
  }

}