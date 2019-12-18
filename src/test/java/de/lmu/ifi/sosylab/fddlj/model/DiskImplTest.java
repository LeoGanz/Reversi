package de.lmu.ifi.sosylab.fddlj.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link DiskImpl}.
 *
 * @author Leonard Ganz
 */
public class DiskImplTest {

  private Player playerCreator() {
    Player player = new PlayerImpl();

    return player;
  }

  @Test
  public void testGetPlayer() {
    Player player = this.playerCreator();

    Disk disk = new DiskImpl(player);

    Assertions.assertEquals(
            player,
            disk.getPlayer(),
            "GetPlayer() returned " + disk.getPlayer() + " instead of expected " + player);
  }

  @Test
  public void testEquals_Equal() {
    Player player = this.playerCreator();
    Disk disk1 = new DiskImpl(player);
    Disk disk2 = new DiskImpl(player);

    Assertions.assertTrue(disk1.equals(disk2), disk1 + " should be equal to " + disk2);
  }

  @Test
  public void testEquals_Unequal() {
    Player player = this.playerCreator();
    Disk disk1 = new DiskImpl(player);
    Disk disk2 = new DiskImpl(player);

    Assertions.assertFalse(disk1.equals(disk2), disk1 + " should not be equal to " + disk2);
  }

  @Test
  public void testHashCode_Equal() {
    Player player = this.playerCreator();
    Disk disk1 = new DiskImpl(player);
    Disk disk2 = new DiskImpl(player);

    Assertions.assertTrue(disk1.equals(disk2), disk1 + " should be equal to " + disk2);
    Assertions.assertEquals(disk1.hashCode(), disk2.hashCode(), "HashCode should be equal for equal cells");
  }

  @Test
  public void testHashCode_Unequal() {
    Player player = this.playerCreator();
    Disk disk1 = new DiskImpl(player);
    Disk disk2 = new DiskImpl(player);

    Assertions.assertFalse(disk1.equals(disk2), disk1 + " should not be equal to " + disk2);
    Assertions.assertNotEquals(
            disk1.hashCode(), disk2.hashCode(), "HashCode should not be equal for unequal cells");
  }

  @Test
  public void testToString_Regular() {
    Assertions.assertEquals(
        "Disk with player #", new DiskImpl(this.playerCreator()).toString(), "ToString did not display cell correctly");
  }

}
