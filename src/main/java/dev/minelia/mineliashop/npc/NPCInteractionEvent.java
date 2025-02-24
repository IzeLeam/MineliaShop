package dev.minelia.mineliashop.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCInteractionEvent extends Event {

  private static final HandlerList handlers = new HandlerList();
  private final NPC npc;
  private final Player player;
  private final NPCClickAction action;

  public NPCInteractionEvent(NPC npc, Player player, NPCClickAction action) {
    this.npc = npc;
    this.player = player;
    this.action = action;
  }

  public NPC getNPC() {
    return npc;
  }

  public Player getPlayer() {
    return player;
  }

  public NPCClickAction getAction() {
    return action;
  }

  @Override
  public HandlerList getHandlers() {
    return getHandlerList();
  }

  private static HandlerList getHandlerList() {
    return handlers;
  }
}
