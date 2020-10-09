package de.kasyyy.chestlock;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import de.kasyyy.chestlock.main.Chestlock;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LockedChestTest {

    private ServerMock server;
    private Chestlock plugin;
    private World mockWorld;

    @Mock private Player senderMock;
    @Mock private Player targetMock;

    private UUID senderUUID = UUID.randomUUID();
    private UUID targetUUID = UUID.randomUUID();

    private LockedChest lockedChest;



    @Before
    public void setUp() {
        server = MockBukkit.mock();
        plugin = (Chestlock) MockBukkit.load(Chestlock.class);
        mockWorld = new WorldMock(Material.DIRT, 2);

        Mockito.when(senderMock.getUniqueId()).thenReturn(senderUUID);
        Mockito.when(targetMock.getUniqueId()).thenReturn(targetUUID);

        lockedChest = new LockedChest(mockWorld.getBlockAt(0,0,0), senderUUID);

    }


    @Test
    public void testAddAllowedPlayer() {
        server.setPlayers(2);

        lockedChest.addAllowedPlayer(senderMock, targetMock.getUniqueId());

        assertEquals(1, lockedChest.getAllowedPlayers().size());
        assertTrue(lockedChest.getAllowedPlayers().contains(targetMock.getUniqueId().toString()));
    }

    @Test
    public void testAddSamePlayer() {
        server.setPlayers(2);


        lockedChest.addAllowedPlayer(senderMock, senderMock.getUniqueId());

        assertEquals(0, lockedChest.getAllowedPlayers().size());
    }

    @Test
    public void testAddPlayerTwice() {
        server.setPlayers(2);

        lockedChest.addAllowedPlayer(senderMock, targetMock.getUniqueId());
        lockedChest.addAllowedPlayer(senderMock, targetMock.getUniqueId());

        assertEquals(1, lockedChest.getAllowedPlayers().size());
    }

    @Test
    public void testRemovePlayer() {
        server.setPlayers(2);

        lockedChest.addAllowedPlayer(senderMock, targetUUID);

        lockedChest.removeAllowedPlayer(senderMock, targetUUID);

        assertEquals(0, lockedChest.getAllowedPlayers().size());
        assertFalse(lockedChest.getAllowedPlayers().contains(targetUUID.toString()));
    }

    @Test
    public void testRemovePlayerNotInList() {
        lockedChest.removeAllowedPlayer(senderMock, targetUUID);

        assertEquals(0, lockedChest.getAllowedPlayers().size());
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

}