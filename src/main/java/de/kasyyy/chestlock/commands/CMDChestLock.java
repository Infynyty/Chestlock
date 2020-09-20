package de.kasyyy.chestlock.commands;

import de.kasyyy.chestlock.LockedChest;
import de.kasyyy.chestlock.main.Chestlock;
import de.kasyyy.chestlock.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CMDChestLock implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("chestlock")) {
            //Checks whether the targeted block is in reach and is eligible
            if(!(commandSender instanceof Player)) return true;
            Player p = (Player) commandSender;
            Block block = p.getTargetBlock(30);
            if(block == null) {
                p.sendMessage(Util.prefix + "The targeted block is not a chest. The chest must be within 30 blocks of the player!");
                return true;
            }
            if(!(block.getType().equals(Material.CHEST) ||
                    block.getType().equals(Material.ENDER_CHEST) ||
                    block.getType().equals(Material.SHULKER_BOX) ||
                    block.getType().equals(Material.FURNACE))) {
                p.sendMessage(Util.prefix + "The targeted block is not a chest. The chest must be within 30 blocks of the player!");
                return true;
            }






            //If there are no further arguments, the chest will be locked, if it isn't locked by anyone else
            if(strings.length == 0) {

                if(LockedChest.lockedChestExists(block)) {
                    LockedChest lockedChest = new LockedChest(block);
                    if(lockedChest.getOwner().equals(p.getUniqueId())) {
                        p.sendMessage(Util.prefix + "You have already locked this chest!");
                        return true;
                    }
                    p.sendMessage(Util.prefix + "This chest is already owned by someone else!");
                    return true;
                }

                //Checks if a chest is a double chest and if so both locations will be saved

                Block secondChest = Util.checkDoubleChest(block);
                if(secondChest != null) {
                    new LockedChest(block, p.getUniqueId());
                    new LockedChest(secondChest, p.getUniqueId());
                    p.sendMessage(Util.prefix + "The double chest has been locked! Give access permission to other players through the command </chestlock add <Player>>");
                    return true;
                }

                new LockedChest(block, p.getUniqueId());
                p.sendMessage(Util.prefix + "The chest has been locked! Give access permission to other players through the command </chestlock add <Player>>");

            }






            //Unlocks a locked chest
            if(strings.length == 1 && strings[0].equalsIgnoreCase("unlock")) {
                if(!(LockedChest.lockedChestExists(block))) {
                    p.sendMessage(Util.prefix + "This chest is not locked!");
                    return true;
                }

                LockedChest lockedChest = new LockedChest(block);
                if(!(p.getUniqueId().equals(lockedChest.getOwner())
                        || lockedChest.getAllowedPlayers().contains(p.getUniqueId().toString()))) {
                    p.sendMessage(Util.prefix + "You can't unlock chests that you don't own!");
                    return true;
                }

                Block secondChest = Util.checkDoubleChest(block);
                if(secondChest != null && LockedChest.lockedChestExists(secondChest)) {
                    LockedChest secondLockedChest = new LockedChest(secondChest);
                    secondLockedChest.removeChest();
                    lockedChest.removeChest();
                    p.sendMessage(Util.prefix + "Double chest unlocked!");
                    return true;
                }


                lockedChest.removeChest();
                p.sendMessage(Util.prefix + "Chest unlocked!");
            }








            //Tries to add a player to the list of allowed players
            if(strings.length == 2 && strings[0].equalsIgnoreCase("add")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(strings[1]);

                if(!(target.hasPlayedBefore())) {
                    p.sendMessage(Util.prefix + "This player has never been online!");
                    return true;
                }

                if(LockedChest.lockedChestExists(block)) {
                    LockedChest lockedChest = new LockedChest(block);
                    if (!(lockedChest.getOwner().equals(p.getUniqueId()))) {
                        p.sendMessage(Util.prefix + "You are not the owner of this chest!");
                        return true;
                    }

                    Block secondChest = Util.checkDoubleChest(block);
                    if(secondChest != null && LockedChest.lockedChestExists(secondChest)) {
                        LockedChest secondLockedChest = new LockedChest(secondChest);
                        secondLockedChest.addAllowedPlayer(p, target.getUniqueId());
                    }


                    lockedChest.addAllowedPlayer(p, target.getUniqueId());
                    p.sendMessage(Util.prefix + "Added player!");
                    return true;
                } else {
                    p.sendMessage(Util.prefix + "This chest is not locked!");
                }
            }





            //Removes a player from the list of allowed players
            if(strings.length == 2 && strings[0].equalsIgnoreCase("remove")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(strings[1]);
                if(LockedChest.lockedChestExists(block)) {
                    LockedChest lockedChest = new LockedChest(block);
                    if (!(lockedChest.getOwner().equals(p.getUniqueId()))) {
                        p.sendMessage(Util.prefix + "You are not the owner of this chest!");
                        return true;
                    }
                    if(!(target.hasPlayedBefore())) {
                        p.sendMessage(Util.prefix + "This player has never been online!");
                        return true;
                    }
                    Block secondChest = Util.checkDoubleChest(block);
                    if(secondChest != null && LockedChest.lockedChestExists(secondChest)) {
                        LockedChest secondLockedChest = new LockedChest(secondChest);
                        secondLockedChest.removeAllowedPlayer(p, target.getUniqueId());
                    }

                    lockedChest.removeAllowedPlayer(p, target.getUniqueId());
                    p.sendMessage(Util.prefix + "Removed player!");
                    return true;
                }
                else {
                    p.sendMessage(Util.prefix + "This chest is not locked!");
                    return true;
                }
            }
        }
        return true;
    }
}
