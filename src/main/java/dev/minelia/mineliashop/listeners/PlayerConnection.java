package dev.minelia.mineliashop.listeners;

import dev.minelia.mineliashop.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnection implements Listener {

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    NPC.getNPC().show(event.getPlayer());
  }
}