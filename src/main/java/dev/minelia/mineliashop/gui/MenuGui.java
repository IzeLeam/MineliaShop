package dev.minelia.mineliashop.gui;

import dev.minelia.mineliashop.MineliaShop;
import dev.minelia.mineliashop.utils.gui.BetterMenu;
import dev.minelia.mineliashop.utils.gui.ButtonAction;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuGui extends BetterMenu {

  private static final MenuGui menu = new MenuGui();
    private final FileConfiguration config = MineliaShop.getInstance().getConfig();

  public static MenuGui getMenu() {
    return menu;
  }

  public MenuGui() {
    super("Shop");

    this.addLine("d", "d", "", "", "", "", "", "d", "d");
    this.addLine("d", "", "", "", "", "", "", "", "d");
    this.addLine("", "", "shop", "", "", "", "trade", "", "");
    this.addLine("d", "", "", "", "", "", "", "", "d");
    this.addLine("d", "d", "", "", "close", "", "", "d", "d");

    this.setButton("d", itemManager.createItemStack(Material.STAINED_GLASS_PANE)
        .setName("§c")
        .setColor(DyeColor.MAGENTA));

    this.setButton("close", itemManager.createItemStack(Material.BARRIER)
        .setName("§cFermer")
        .asButton().setButtonAction(new ButtonAction() {
          @Override
          public void onClick(Player player) {
            player.closeInventory();
          }
        }));

    this.setButton("shop", itemManager.createItemStack(Material.CHEST)
        .setName(ChatColor.translateAlternateColorCodes('&', config.getString("menu.shop.name")))
        .setLore(config.getStringList("menu.shop.lore").stream()
                .map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList()))
        .asButton().setButtonAction(new ButtonAction() {
          @Override
          public void onClick(Player player) {
            ShopGui.getMenu().open(player);
          }
        }));

    this.setButton("trade", itemManager.createItemStack(Material.EMERALD)
        .setName(ChatColor.translateAlternateColorCodes('&', config.getString("menu.exchange.name")))
        .setLore(config.getStringList("menu.exchange.lore").stream()
                .map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList()))
        .asButton().setButtonAction(new ButtonAction() {
          @Override
          public void onClick(Player player) {

            Inventory inventory = player.getInventory();
            for (ItemStack item : inventory.getContents()) {
              if (item != null && item.getType() != null && item.getType().equals(Material.EMERALD)) {
                if (item.getAmount() == 64) {
                  inventory.removeItem(item);
                  MineliaShop.getInstance().getServer().dispatchCommand(MineliaShop.getInstance().getServer().getConsoleSender(),
                      MineliaShop.getInstance().getConfig().getString("minelia_piece_give_command").replace("%player%", player.getName()).replace("%amount%", "1"));
                  player.sendMessage("§aVous avez échangé 64 émeraudes contre 1 pièce Minelia");
                  return;
                }
              }
            }
            player.sendMessage("§cVous n'avez pas assez d'émeraudes");
          }
        }));
  }
}
