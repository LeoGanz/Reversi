package de.lmu.ifi.sosylab.fddlj;

import de.lmu.ifi.sosylab.fddlj.view.ControllerImpl;
import javax.swing.JOptionPane;

/**
 * Main class of the reversi application.
 *
 * @author Josef Feger.
 */
public class ReversiMain {

  /**
   * Invoke the actual starting method {@link ChessMain#showPawnGame()}.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    ControllerImpl.launch(args);
    ReversiMain.showGame(args);
  }

  /**
   * Parse input arguments and start the game accordingly.
   *
   * @param args the input arguments to parse
   */
  private static void showGame(String[] args) {
    // TODO initialize Controller and call start screen
    JOptionPane.showMessageDialog(
        null,
        "Reversi by FDDLJ will be coming soon!",
        "Coming soon",
        JOptionPane.INFORMATION_MESSAGE);
  }
}
