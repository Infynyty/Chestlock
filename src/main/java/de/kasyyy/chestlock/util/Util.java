package de.kasyyy.chestlock.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nullable;

public class Util {
    public static final String prefix = ChatColor.AQUA + "[Chestlock]>> " + ChatColor.DARK_GRAY;
    public static final String debug = ChatColor.RED + "[DEBUG]" + prefix;

    /**
     * Returns the other chest of double chest for a given double chest. If the block is not a double chest null is
     * returned.
     * @param block
     * @return
     */
    @Nullable
    public static Block checkDoubleChest(Block block) {
        if(!(block.getType().equals(Material.CHEST))) return null;

        BlockState state = block.getState();
        Chest chest = (Chest) state;
        InventoryHolder inventoryHolder = chest.getInventory().getHolder();

        if(inventoryHolder instanceof DoubleChest) {

            DoubleChest doubleChest = ((DoubleChest) inventoryHolder);

            Chest leftChest = (Chest) doubleChest.getLeftSide();
            Chest rightChest = (Chest) doubleChest.getRightSide();

            return leftChest.getBlock().equals(block) ? rightChest.getBlock() : leftChest.getBlock();
        }
        return null;
    }

}
