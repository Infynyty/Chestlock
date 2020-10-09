package de.kasyyy.chestlock;

import de.kasyyy.chestlock.main.Chestlock;
import de.kasyyy.chestlock.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LockedChest {
    private static Chestlock instance = Chestlock.getInstance();

    private int id;
    private Block block;
    private UUID owner;
    private ArrayList<String> allowedPlayers;
    private static ArrayList<Material> allowedMaterials = new ArrayList(Arrays.asList(Material.CHEST, Material.SHULKER_BOX, Material.FURNACE));

    /**
     * Loads a LockedChest from the configuration file
     * @param block
     */
    public LockedChest(Block block) {

        allowedPlayers = new ArrayList<>();
        for(String key : instance.getConfig().getKeys(false)) {
            if(key.equalsIgnoreCase("Last-Counter-Used")) continue;



            if(instance.getConfig().getInt(key + ".X") != block.getX()) continue;
            if(instance.getConfig().getInt(key + ".Y") != block.getY()) continue;
            if(instance.getConfig().getInt(key + ".Z") != block.getZ()) continue;



            this.id = Integer.parseInt(key);
            this.block = block;

            owner = UUID.fromString(instance.getConfig().getString(this.id + ".Owner"));

            if(instance.getConfig().getList(id + ".AllowedPlayers") == null) break;
            List<String> allowedPlayersString = (List<String>) instance.getConfig().getList(id + ".AllowedPlayers");
            if(allowedPlayersString.size() > 0) {
                allowedPlayers.addAll(allowedPlayersString);
            }
            break;
        }
    }

    /**
     * Create a new LockedChest
     * @param block
     * @param ownerUUID
     */
    public LockedChest(Block block, UUID ownerUUID) {
        allowedPlayers = new ArrayList<>();
        instance.getConfig().set("Last-Counter-Used", instance.getConfig().getInt("Last-Counter-Used") + 1);
        id = instance.getConfig().getInt("Last-Counter-Used");
        instance.getConfig().set(id + ".X", block.getX());
        instance.getConfig().set(id + ".Y", block.getY());
        instance.getConfig().set(id + ".Z", block.getZ());
        instance.getConfig().set(id + ".Owner", ownerUUID.toString());
        instance.saveConfig();
    }

    /**
     * Add a player to the list of players who are allowed to access a certain chest
     * @param targetUUID
     * @param sender
     */
    public void addAllowedPlayer(Player sender, UUID targetUUID) {
        if(sender.getUniqueId().equals(targetUUID)) {
            sender.sendMessage(Util.prefix + "You can't add yourself!");
            return;
        }
        if(allowedPlayers.contains(targetUUID.toString())) {
            sender.sendMessage(Util.prefix + "This player can already access this chest!");
            return;
        }
        allowedPlayers.add(targetUUID.toString());
        instance.getConfig().set(id + ".AllowedPlayers", allowedPlayers);
        instance.saveConfig();
    }

    /**
     * Removes a player from the list of allowed players
     * @param sender
     * @param targetUUID
     */
    public void removeAllowedPlayer(Player sender, UUID targetUUID) {
        if(!(allowedPlayers.contains(targetUUID.toString()))) {
            sender.sendMessage(Util.prefix + "This player is not listed as a player that can access this chest!");
            return;
        }

        allowedPlayers.remove(targetUUID.toString());
        instance.getConfig().set(id + ".AllowedPlayers", allowedPlayers);
        instance.saveConfig();
    }

    /**
     * Removes a LockedChest from the config
     */
    public void removeChest() {
        instance.getConfig().set(id + "", null);
        instance.saveConfig();
    }

    /**
     * Checks whether a given block is a LockedChest
     * @param block
     * @return True or false
     */
    public static boolean lockedChestExists(Block block) {
        LockedChest lockedChest = new LockedChest(block);
        return lockedChest.owner != null;
    }

    public int getId() {
        return id;
    }

    public Block getBlock() {
        return block;
    }

    public UUID getOwner() {
        return owner;
    }

    public ArrayList<String> getAllowedPlayers() {
        return allowedPlayers;
    }
}
