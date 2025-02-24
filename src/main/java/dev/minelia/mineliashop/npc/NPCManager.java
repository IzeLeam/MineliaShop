package dev.minelia.mineliashop.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.minelia.mineliashop.MineliaShop;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class NPCManager {

  private final Plugin plugin = MineliaShop.getInstance();

  public NPCManager(Plugin plugin) {
    ProtocolLibrary.getProtocolManager().addPacketListener(
        new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
          @Override
          public void onPacketReceiving(PacketEvent event) {
            EnumWrappers.EntityUseAction useAction = event.getPacket().getEntityUseActions().read(0);
            int entityId = event.getPacket().getIntegers().read(0);
            handleEntityInteract(event.getPlayer(), entityId, NPCClickAction.fromProtocolLibAction(useAction));
          }
        }
    );
  }

  private final Cache<Player, NPC> clickedNPCCache = CacheBuilder.newBuilder()
      .expireAfterWrite(1L, TimeUnit.SECONDS)
      .build();

  private void handleEntityInteract(Player player, int entityId, NPCClickAction action) {
    Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
      if (entityId != NPC.getNPC().getEntityId()) {
        return;
      }

      NPC previousClickedNPC = clickedNPCCache.getIfPresent(player);
      if (previousClickedNPC != null && previousClickedNPC.equals(NPC.getNPC())) {
        return;
      }
      clickedNPCCache.put(player, NPC.getNPC());

      NPCInteractionEvent event = new NPCInteractionEvent(NPC.getNPC(), player, action);
      Bukkit.getPluginManager().callEvent(event);
    }, 2);
  }
}