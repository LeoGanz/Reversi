package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameFieldImpl;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This class offers a grid of cells that represents a game board.
 *
 * @author Josef Feger
 */
public class GameBoardGrid extends BorderPane implements PropertyChangeListener {

  private Model model;
  private Controller controller;

  private SwitchButton switchButton;

  private final String letterCss =
      "-fx-font-size: xx-large; -fx-font-weight: bold; -fx-text-fill: white";

  private final char startLetter = 'A';

  /**
   * Constructor of this class initialises variables and is responsible for building GUI elements.
   *
   * @param model a reference to a model instance
   * @param controller a reference to a controller instance
   * @param stage a reference to the game's main stage
   * @param view a reference to a view instance
   */
  public GameBoardGrid(Model model, Controller controller, Stage stage, View view) {
    super();

    this.model = model;
    this.controller = controller;

    view.addListener(this);

    setStyle("-fx-background-color: transparent;");
    initToggleSwitch();
    initGameBoard(stage, view);
  }

  private void initGameBoard(Stage stage, View view) {

    GridPane grid = new GridPane();

    grid.setGridLinesVisible(false);
    grid.setAlignment(Pos.CENTER);

    for (int column = 1; column < GameFieldImpl.SIZE + 1; column++) {
      for (int row = 1; row < GameFieldImpl.SIZE + 1; row++) {

        GraphicCell cell = new GraphicCell(column - 1, row - 1, this, model, controller);
        grid.add(cell, column, row);
        switchButton.addListener(cell);
        view.addListener(cell);
      }
    }

    for (int i = 1; i < GameFieldImpl.SIZE + 1; i++) {
      Label letter = new Label(String.valueOf((char) (startLetter + i - 1)));
      letter.setStyle(letterCss);

      Label rowNumber = new Label(String.valueOf(i));
      rowNumber.setStyle(letterCss);
      grid.add(letter, i, 0);
      grid.add(rowNumber, 0, i);

      GridPane.setHalignment(letter, HPos.CENTER);
      GridPane.setValignment(rowNumber, VPos.CENTER);
      GridPane.setMargin(letter, new Insets(5, 0, 20, 0));
      GridPane.setMargin(rowNumber, new Insets(0, 20, 0, 5));
    }

    double initValue = (Screen.getPrimary().getVisualBounds().getHeight() - GraphicCell.SPACING);
    grid.setMaxSize(initValue, initValue);

    setCenter(grid);
  }

  private void initToggleSwitch() {

    HBox hbox = new HBox(15);
    hbox.setAlignment(Pos.CENTER);

    Label hints = getLabel("Hints anzeigen?");
    switchButton = new SwitchButton(100, 15);

    hbox.getChildren().addAll(hints, switchButton);
    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(hbox);
    BorderPane.setAlignment(hbox, Pos.CENTER);

    setBottom(borderPane);
    BorderPane.setMargin(borderPane, new Insets(30, 0, 0, 0));
  }

  private Label getLabel(String text) {
    Label label = new Label(text);
    label.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
    return label;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Platform.runLater(
        () -> {
          if (evt.getPropertyName().equals(Model.LISTENERS_CHANGED)) {

            if (evt.getNewValue() instanceof Model) {

              this.model = (Model) evt.getNewValue();
            }
          }
        });
  }
}
