package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModelImplTest {

  private ModifiableGameState earlyGame_PlayerTwosTurn() {
    Player one = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
    Player two = new PlayerImpl("Rhea", Color.ALICEBLUE);
    ModifiablePlayerManagement manager = new PlayerManagementImpl(one, two);
    ModifiableGameField field = new GameFieldImpl();
    field.set(new CellImpl(3, 3), new DiskImpl(manager.getPlayerOne()));
    field.set(new CellImpl(3, 4), new DiskImpl(manager.getPlayerOne()));
    field.set(new CellImpl(3, 5), new DiskImpl(manager.getPlayerOne()));
    field.set(new CellImpl(4, 3), new DiskImpl(manager.getPlayerTwo()));
    field.set(new CellImpl(4, 4), new DiskImpl(manager.getPlayerTwo()));
    field.set(new CellImpl(4, 5), new DiskImpl(manager.getPlayerTwo()));

    manager.switchCurrentPlayer();

    ModifiableGameState state = new GameStateImpl();
    state.setGameField(field);
    state.setCurrentPhase(Phase.RUNNING);
    return state;
  }
}
