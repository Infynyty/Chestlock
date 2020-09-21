package de.kasyyy.chestlock.events;

import de.kasyyy.chestlock.LockedChest;
import de.kasyyy.chestlock.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

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

    @EventHandler
    public void onBurn(BlockBurnEvent e) {
        if(LockedChest.lockedChestExists(e.getBlock())) {
            new LockedChest(e.getBlock()).removeChest();
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        for(Block block : e.blockList()) {
            if(LockedChest.lockedChestExists(block)) {
                new LockedChest(block).removeChest();
            }
        }
    }
}
