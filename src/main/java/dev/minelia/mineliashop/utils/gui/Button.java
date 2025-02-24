package dev.minelia.mineliashop.utils.gui;

import java.util.Set;
import org.bukkit.inventory.ItemStack;

public class Button {

  private final ItemStack itemStack;
  private ButtonAction buttonAction;
  private Set<Integer> slots;

  public Button(ItemStack itemStack) {
    this.itemStack = itemStack;
    this.buttonAction = null;
  }

  public Button setButtonAction(ButtonAction buttonAction) {
    this.buttonAction = buttonAction;
    return this;
  }

  public ItemStack getItemStack() {
    return this.itemStack;
  }

  public ButtonAction getButtonAction() {
    return this.buttonAction;
  }

  public Set<Integer> getSlots() {
    return this.slots;
  }

}

