package dev.minelia.mineliashop.gui;

import dev.minelia.mineliashop.MineliaShop;
import dev.minelia.mineliashop.utils.gui.BetterMenu;
import dev.minelia.mineliashop.utils.gui.ButtonAction;
import dev.minelia.mineliashop.utils.managers.ItemManager;

import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ShopGui extends BetterMenu {

  private static final ShopGui menu = new ShopGui();
  private final FileConfiguration config = MineliaShop.getInstance().getConfig();

  public static ShopGui getMenu() {
    return menu;
  }

  public ShopGui() {
    super("Shop");

    this.addLine("d", "d", "", "", "", "", "", "d", "d");
    this.addLine("d", "", "item", "", "item", "", "item", "", "d");
    this.addLine("", "", "", "", "", "", "", "", "");
    this.addLine("", "", "item", "", "item", "", "item", "", "");
    this.addLine("d", "", "", "", "", "", "", "", "d");
    this.addLine("d", "d", "", "", "back", "", "", "d", "d");

    this.setButton("back", itemManager.createItemStack(Material.BARRIER)
        .setName("§cRetour")
        .asButton().setButtonAction(new ButtonAction() {
          @Override
          public void onClick(Player player) {
            MenuGui.getMenu().open(player);
          }
        }));

    this.setButton("d", itemManager.createItemStack(Material.STAINED_GLASS_PANE)
        .setName("§c")
        .setColor(DyeColor.MAGENTA));

    this.setButtonList("item", config.getConfigurationSection("items").getKeys(false).stream().map(key ->
      itemManager.createItemStack(getMaterial(key))
          .setName(ChatColor.translateAlternateColorCodes('&', config.getString("items." + key + ".name")))
          .setLore(config.getStringList("items." + key + ".lore").stream()
                  .map(lore -> {
                    lore = lore.replace("%price%", String.valueOf(config.getInt("items." + key + ".price")));
                    return ChatColor.translateAlternateColorCodes('&', lore);
                  }).collect(Collectors.toList()))
          .addFlags(ItemFlag.HIDE_ATTRIBUTES)
          .asButton().setButtonAction(new ButtonAction() {
            @Override
            public void onClick(Player player) {
              if (!hasEnoughMoney(player, config.getInt("items." + key + ".price"))) {
                player.sendMessage("§cVous n'avez pas assez d'argent pour acheter cet item.");
                return;
              }
              removeMoney(player, config.getInt("items." + key + ".price"));
              if (key.contains("spawner")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    config.getString("spawner_command").replace("%player%", player.getName()).replace("%type%", key.substring(0, key.indexOf("_"))));
              } else {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    config.getString("minelia_item_command").replace("%player%", player.getName()).replace("%item%", key));
              }
            }
          })
    ).collect(Collectors.toList()));
  }

  private boolean hasEnoughMoney(Player player, int price) {
    int count = 0;
    for (ItemStack item : player.getInventory().getContents()) {
      if(item == null) {
        continue;
      }
      final net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
      if (!nmsItem.hasTag() || !nmsItem.getTag().hasKey("minelia-money") || !nmsItem.getTag().getBoolean("minelia-money")) {
        continue;
      }

      count += item.getAmount();
    }

    return count >= price;
  }

  private void removeMoney(Player player, int amount) {
    int remaining = amount;
    if (remaining == 0) {
      return;
    }

    int slot = 0;
    for (final ItemStack item : player.getInventory().getContents()) {
      final net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
      if (!nmsItem.hasTag() || !nmsItem.getTag().hasKey("minelia-money") || !nmsItem.getTag().getBoolean("minelia-money")) {
        continue;
      }
      if (item.getAmount() > remaining) {
        final ItemStack clone = itemManager.createItemStack(item);
        clone.setAmount(item.getAmount() - remaining);
        player.getInventory().setItem(slot, clone);
        return;
      } else {
        remaining -= item.getAmount();
        player.getInventory().setItem(slot, null);
      }
      slot++;
    }
  }

  private Material getMaterial(String material) {
    if (material.contains("spawner")) {
      return Material.MOB_SPAWNER;
    } else if (material.contains("pickaxe")) {
      return Material.DIAMOND_PICKAXE;
    } else if (material.contains("sceptre")) {
      return Material.BLAZE_ROD;
    } else if (material.contains("dagger")) {
      return Material.IRON_SWORD;
    } else {
      return Material.STICK;
    }
  }
}
