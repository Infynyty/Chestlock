package de.kasyyy.chestlock.main;

import de.kasyyy.chestlock.commands.CMDChestLock;
import de.kasyyy.chestlock.events.LockedChestBreakEvent;
import de.kasyyy.chestlock.events.OpenLockedChestEvent;
import de.kasyyy.chestlock.events.PlaceChestNextToLockedChestEvent;
import de.kasyyy.chestlock.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class Chestlock extends JavaPlugin {

    private static Chestlock chestlock = null;

    public Chestlock()
    {
        super();
    }

    protected Chestlock(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        chestlock = this;
        this.saveDefaultConfig();
        setUpConfig();

        this.getServer().getPluginManager().registerEvents(new LockedChestBreakEvent(), this);
        this.getServer().getPluginManager().registerEvents(new OpenLockedChestEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlaceChestNextToLockedChestEvent(), this);

        this.getCommand("chestlock").setExecutor(new CMDChestLock());


        Bukkit.getConsoleSender().sendMessage(Util.prefix + "Chestlock enabled!");

    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Util.prefix + "Chestlock disabled!");
        chestlock = null;
    }

    private void setUpConfig() {
        this.getConfig().addDefault("Last-Counter-Used", 0);
        this.saveConfig();
    }

    public static Chestlock getInstance() {
        return chestlock;
    }
}
