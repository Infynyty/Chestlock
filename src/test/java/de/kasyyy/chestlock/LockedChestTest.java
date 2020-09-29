package de.kasyyy.chestlock;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.kasyyy.chestlock.main.Chestlock;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class LockedChestTest {

    private ServerMock server;
    private Chestlock plugin;
    private World mockWorld;
    private FileConfiguration fileConfiguration;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Chestlock.class);
        mockWorld = new WorldMock(Material.DIRT, 2);

        fileConfiguration = Mockito.mock(FileConfiguration.class);
        Mockito.when(plugin.getConfig()).thenReturn(fileConfiguration);
    }

    @Test
    public void testAddAllowedPlayer() {
        server.setPlayers(1);

        Block block = Mockito.mock(Block.class);
        PlayerMock playerMock = server.getPlayer(0);
        LockedChest lockedChest = new LockedChest(block, UUID.randomUUID());
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

}