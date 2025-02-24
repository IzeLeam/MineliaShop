package dev.minelia.mineliashop.utils.gui;

import org.bukkit.entity.Player;

public interface ButtonAction {

  default void onClick(final Player player) {
  }

  default void onRightClick(final Player player) {
  }

  default void onLeftClick(final Player player) {
  }

  default void onShiftClick(final Player player) {
  }

  default void onMiddleClick(final Player player) {
  }

}