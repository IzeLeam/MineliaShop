package dev.minelia.mineliashop;

import dev.minelia.mineliashop.listeners.PlayerConnection;
import dev.minelia.mineliashop.listeners.PlayerMoveListener;
import dev.minelia.mineliashop.npc.NPC;
import dev.minelia.mineliashop.npc.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineliaShop extends JavaPlugin {

  private static MineliaShop instance;

  public static MineliaShop getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;

    saveDefaultConfig();

    // Register the player connection listener
    Bukkit.getPluginManager().registerEvents(new PlayerConnection(), this);
    Bukkit.getPluginManager().registerEvents(NPC.getNPC(), this);
    Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);

    NPCManager npcManager = new NPCManager(this);
    NPC.getNPC().spawn();
  }
}
