package de.kasyyy.chestlock.events;

import de.kasyyy.chestlock.LockedChest;
import de.kasyyy.chestlock.main.Chestlock;
import de.kasyyy.chestlock.util.Util;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PlaceChestNextToLockedChestEvent implements Listener {

    Chestlock instance = Chestlock.getInstance();

    //Checks if a chest is placed next to a locked chest and if the player owns the locked chest
    @EventHandler
    public void onPlaceChest(BlockPlaceEvent e) {
        if(!(e.getBlock().getType().equals(Material.CHEST))) return;

        instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
            @Override
            public void run() {
                BlockState state = e.getBlock().getState();
                Chest chest = (Chest) state;
                InventoryHolder inventoryHolder = chest.getInventory().getHolder();

                if(inventoryHolder instanceof DoubleChest) {

                    DoubleChest doubleChest = ((DoubleChest) inventoryHolder);

                    Chest leftChest = (Chest) doubleChest.getLeftSide();
                    Chest rightChest = (Chest) doubleChest.getRightSide();

                    Block newChest;
                    Block oldChest;

                    if (leftChest.getBlock().equals(e.getBlockPlaced())) {
                        newChest = leftChest.getBlock();
                        oldChest = rightChest.getBlock();
                    } else {
                        newChest = rightChest.getBlock();
                        oldChest = leftChest.getBlock();
                    }

                    if(!(LockedChest.lockedChestExists(oldChest))) return;
                    LockedChest lockedChestOld = new LockedChest(oldChest);
                    if(!(lockedChestOld.getOwner().equals(e.getPlayer().getUniqueId()))) {
                        e.getPlayer().sendMessage(Util.prefix + "You can't place a chest next to a locked chest that you don't own!");
                        newChest.setType(Material.AIR);
                        e.getPlayer().getLocation().getWorld().dropItem(e.getPlayer().getLocation(), new ItemStack(Material.CHEST));
                        return;
                    }


                    new LockedChest(newChest, e.getPlayer().getUniqueId());
                    e.getPlayer().sendMessage(Util.prefix + "The double chest has been locked! Give access permission to other players through the command </chestlock add <Player>>");
                    return;
                }

            }
        }, 1);




    }


}
