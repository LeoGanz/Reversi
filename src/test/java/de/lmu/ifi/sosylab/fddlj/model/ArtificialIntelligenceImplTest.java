package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArtificialIntelligenceImplTest {

  private final Player one = new PlayerImpl("Daenerys", Color.DARKRED);
  private final Player two = new PlayerImpl("WhiteWalker", Color.LIGHTBLUE);
  private final Disk diskOne = new DiskImpl(one);
  private final Disk diskTwo = new DiskImpl(two);

  @Test
  public void aiTest1() {

    Model game = new ModelImpl(GameMode.HOTSEAT, one, two);
    Assertions.assertTrue(game.placeDisk(new DiskImpl(one), new CellImpl(3, 3)));

    ArtificialIntelligence ai = new ArtificialIntelligenceImpl(1);
    Assertions.assertEquals(new CellImpl(3, 4), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(new DiskImpl(two), new CellImpl(3, 4)));
    Assertions.assertTrue(game.placeDisk(new DiskImpl(one), new CellImpl(4, 4)));

    Assertions.assertEquals(new CellImpl(4, 3), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(new DiskImpl(two), new CellImpl(4, 3)));

    Assertions.assertTrue(game.placeDisk(diskOne, new CellImpl(4, 2)));
    Assertions.assertEquals(new CellImpl(3, 2), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(diskTwo, new CellImpl(3, 2)));

    Assertions.assertTrue(game.placeDisk(diskOne, new CellImpl(2, 2)));
    Assertions.assertEquals(new CellImpl(3, 1), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(diskTwo, new CellImpl(3, 1)));

    Assertions.assertTrue(game.placeDisk(diskOne, new CellImpl(2, 3)));
    Assertions.assertEquals(new CellImpl(1, 2), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(diskTwo, new CellImpl(1, 2)));

    Assertions.assertTrue(game.placeDisk(diskOne, new CellImpl(0, 2)));
    Assertions.assertEquals(new CellImpl(5, 3), ai.calculateBestMove(game.getState()));
  }

  @Test
  public void aiTest2() {
    Model game = new ModelImpl(GameMode.HOTSEAT, one, two);
    ArtificialIntelligenceImpl ai = new ArtificialIntelligenceImpl(2);
    Assertions.assertTrue(game.placeDisk(diskOne, new CellImpl(3, 3)));
    Assertions.assertEquals(new CellImpl(3, 4), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(diskTwo, new CellImpl(3, 4)));
    Assertions.assertTrue(game.placeDisk(diskOne, new CellImpl(4, 3)));
    Assertions.assertEquals(new CellImpl(4, 4), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(diskTwo, new CellImpl(4, 4)));
    Assertions.assertTrue(game.placeDisk(diskOne, new CellImpl(4, 5)));
    Assertions.assertEquals(new CellImpl(5, 2), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(diskTwo, new CellImpl(5, 2)));
    Assertions.assertTrue(game.placeDisk(diskOne, new CellImpl(3, 5)));
    Assertions.assertEquals(new CellImpl(2, 5), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(diskTwo, new CellImpl(2, 5)));
    Assertions.assertTrue(game.placeDisk(diskOne, new CellImpl(5, 3)));
    Assertions.assertEquals(new CellImpl(5, 4), ai.calculateBestMove(game.getState()));
    Assertions.assertTrue(game.placeDisk(diskTwo, new CellImpl(5, 4)));
  }
}
