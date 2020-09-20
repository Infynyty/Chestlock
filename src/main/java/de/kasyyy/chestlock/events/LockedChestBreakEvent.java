package de.kasyyy.chestlock.events;

import de.kasyyy.chestlock.LockedChest;
import de.kasyyy.chestlock.util.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LockedChestBreakEvent implements Listener {

    @EventHandler
    public void onBreakLockedChest(BlockBreakEvent e) {
        if(!(e.getBlock().getType().equals(Material.CHEST) ||
                e.getBlock().getType().equals(Material.SHULKER_BOX) ||
                e.getBlock().getType().equals(Material.FURNACE))) {
            return;
        }
        if(!(LockedChest.lockedChestExists(e.getBlock()))) return;

        LockedChest lockedChest = new LockedChest(e.getBlock());
        if(!(e.getPlayer().getUniqueId().equals(lockedChest.getOwner()) || lockedChest.getAllowedPlayers().contains(e.getPlayer().getUniqueId().toString()))) {
            e.getPlayer().sendMessage(Util.prefix + "You can't destroy locked chests that you don't own!");
            e.setCancelled(true);
            return;
        }
        lockedChest.removeChest();
    }

}
