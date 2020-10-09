package de.kasyyy.chestlock.events;

import de.kasyyy.chestlock.LockedChest;
import de.kasyyy.chestlock.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;


public class OpenLockedChestEvent implements Listener {

    @EventHandler
    public void onOpenChest(InventoryOpenEvent e) {
        if(!(e.getInventory().getType().equals(InventoryType.CHEST) || e.getInventory().getType().equals(InventoryType.FURNACE) || e.getInventory().getType().equals(InventoryType.SHULKER_BOX))) return;


        if(e.getInventory().getLocation() == null) {
            return;
        }

        if(!(LockedChest.lockedChestExists(e.getInventory().getLocation().getBlock()))) return;
        LockedChest lockedChest = new LockedChest(e.getInventory().getLocation().getBlock());

        if(lockedChest.getOwner().equals(e.getPlayer().getUniqueId())
                || lockedChest.getAllowedPlayers().contains(e.getPlayer().getUniqueId().toString())) {
            return;
        }

        e.getPlayer().sendMessage(Util.prefix + "This chest is locked!");
        e.setCancelled(true);
    }
}
