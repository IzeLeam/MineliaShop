package dev.minelia.mineliashop.listeners;

import dev.minelia.mineliashop.MineliaShop;
import dev.minelia.mineliashop.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashSet;
import java.util.Set;

public class PlayerMoveListener implements Listener {

    private final Set<Player> playerCanSeeFakeEntity = new HashSet<>();

    private void handleMove(final Player player) {
        final Location NPCLocation = NPC.getNPC().getLocation();
        final Location playerLocation = player.getLocation();
        NPCLocation.setWorld(playerLocation.getWorld());
        if (playerLocation.distanceSquared(NPCLocation) <= Math.pow(30.0, 2.0)) {
            if (!this.playerCanSeeFakeEntity.contains(player)) {
                this.playerCanSeeFakeEntity.add(player);
                NPC.getNPC().show(player);
            }
        }
        else if (this.playerCanSeeFakeEntity.contains(player)) {
            this.playerCanSeeFakeEntity.remove(player);
            NPC.getNPC().hide(player);
        }
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        this.handleMove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.playerCanSeeFakeEntity.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerSpawn(final EntitySpawnEvent event) {
        if (event.getEntity() instanceof Player) {
            this.handleMove((Player) event.getEntity());
        }
    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        this.handleMove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        Bukkit.getScheduler().runTaskLater(MineliaShop.getInstance(), () -> {
            handleMove(event.getPlayer());
        }, 10);
    }
}