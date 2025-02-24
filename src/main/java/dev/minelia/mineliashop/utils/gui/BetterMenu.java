package dev.minelia.mineliashop.utils.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.minelia.mineliashop.MineliaShop;
import dev.minelia.mineliashop.utils.managers.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class BetterMenu implements Listener {

  private Inventory inventory;
  private final String name;
  private final Map<Integer, Button> buttons = new HashMap<>();
  private final List<List<String>> lines = new ArrayList<>();
  protected final ItemManager itemManager = new ItemManager();
  private boolean isListenerRegistered = false;

  public BetterMenu(String name) {
    this.name = name;
  }

  public BetterMenu(String name, int lines) {
    this.name = name;
    int i = 0;
    while (i++ < lines) {
      this.addLine();
    }
  }

  public void addLine(List<String> symbols) {
    assert symbols.size() == 9;
    assert lines.size() < 6;
    lines.add(symbols);
    this.inventory = Bukkit.createInventory(null, this.lines.size() * 9, this.name);
  }

  public void addLine(String... line) {
    this.addLine(Arrays.asList(line));
  }

  public void addLine() {
    this.addLine(Arrays.asList("", "", "", "", "", "", "", "", ""));
  }

  public void setItemStack(String symbol, BetterItem item) {
    int index = 0;
    for (List<String> line : this.lines) {
      for (String reference : line) {
        if (reference.equals(symbol)) {
          inventory.setItem(index, item);
        }
        index++;
      }
    }
  }

  public void setButton(String symbol, Button button) {
    int index = 0;
    for (List<String> line : this.lines) {
      for (String reference : line) {
        if (reference.equals(symbol)) {
          inventory.setItem(index, button.getItemStack());
          buttons.put(index, button);
        }
        index++;
      }
    }
  }

  public void setButton(String symbol, BetterItem item) {
    setButton(symbol, item.asButton());
  }

  public void setButtonList(String symbol, List<Button> buttons) {
    int index = 0;
    int buttonIndex = 0;
    for (List<String> line : this.lines) {
      for (String reference : line) {
        if (reference.equals(symbol)) {
          inventory.setItem(index, buttons.get(buttonIndex).getItemStack());
          this.buttons.put(index, buttons.get(buttonIndex));
          if (buttonIndex + 1 < buttons.size()) {
            buttonIndex++;
          } else {
            return;
          }
        }
        index++;
      }
    }
  }

  public void open(Player player) {
    assert !this.lines.isEmpty();
    if (!isListenerRegistered) {
      Bukkit.getPluginManager().registerEvents(this, MineliaShop.getInstance());
      player.openInventory(this.inventory);
    }
    isListenerRegistered = true;
  }

  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    if (this.inventory.getViewers().contains(event.getPlayer()) && event.getInventory().getViewers().size() < 2) {
      HandlerList.unregisterAll(this);
      isListenerRegistered = false;
    }
  }

  @EventHandler
  public void onInventoryDrag(InventoryDragEvent event) {
    event.getWhoClicked().sendMessage("§8Drag !");
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!event.getInventory().getViewers().contains(event.getWhoClicked())) {
      return;
    }
    if (event.getClickedInventory() == null || event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
      event.setCancelled(true);
      return;
    }
    if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
      return;
    }

    switch (event.getAction()) {
      case MOVE_TO_OTHER_INVENTORY:
      case COLLECT_TO_CURSOR:
        event.setCancelled(true);
        break;
      default:
        event.setCancelled(true);
        final Button button = buttons.get(event.getSlot());
        if (button == null) {
          return;
        }
        final ButtonAction action = button.getButtonAction();

        Player player = (Player) event.getWhoClicked();
        switch (event.getClick()) {
          case LEFT:
            action.onLeftClick(player);
            action.onClick(player);
            break;
          case RIGHT:
            action.onRightClick(player);
            action.onClick(player);
            break;
          case SHIFT_LEFT:
          case SHIFT_RIGHT:
            action.onShiftClick(player);
            action.onClick(player);
            break;
          case MIDDLE:
            action.onMiddleClick(player);
            break;
          default:
        }
    }
  }
}