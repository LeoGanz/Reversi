package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Optional;

/**
 * Testing utility for printing game states.
 *
 * @author Leonard Ganz
 */
public class Shell {

  private static final char SYMBOL_ONE = '1';
  private static final char SYMBOL_TWO = '2';
  private static final char SYMBOL_EMPTY = '|';

  /**
   * Utility for printing a {@link GameState} to the console.
   *
   * @param state GameState to print
   */
  public static void print(GameState state) {
    GameField gameField = state.getField();
    StringBuilder print = new StringBuilder();

    for (int row = gameField.getSize() - 1; row >= 0; row--) {
      print.append(row + " ");
      for (int column = 0; column < gameField.getSize(); column++) {
        Optional<Disk> pawn = gameField.get(new CellImpl(column, row));
        if (pawn.isPresent()) {
          if (pawn.get().getPlayer() == state.getPlayerManagement().getPlayerOne()) {
            print.append(SYMBOL_ONE);
          } else {
            print.append(SYMBOL_TWO);
          }
        } else {
          print.append(SYMBOL_EMPTY);
        }
      }
      print.append("\n");
    }
    print.append("  01234567\n");
    print.append("Player's turn: " + state.getPlayerManagement().getCurrentPlayer());

    System.out.println(print.toString());
    System.out.println();
  }
}
