package dev.minelia.mineliashop.npc;

import com.comphenix.protocol.wrappers.EnumWrappers;

public enum NPCClickAction {
  INTERACT,
  ATTACK,
  INTERACT_AT;

  public static NPCClickAction fromProtocolLibAction(EnumWrappers.EntityUseAction action) {
    switch (action) {
      case INTERACT:
        return INTERACT;
      case ATTACK:
        return ATTACK;
      case INTERACT_AT:
        return INTERACT_AT;
      default:
        return null;
    }
  }
}