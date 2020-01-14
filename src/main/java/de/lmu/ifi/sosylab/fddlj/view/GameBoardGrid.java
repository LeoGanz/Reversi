package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameFieldImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameBoardGrid extends BorderPane {

  private Model model;
  private GameMode gameMode;
  private Controller controller;

  private SwitchButton switchButton;

  private char startLetter = 'A';

  public GameBoardGrid(Model model, GameMode gameMode, Controller controller, Stage stage) {
    super();

    this.model = model;
    this.gameMode = gameMode;
    this.controller = controller;

    initToggleSwitch();
    initGameBoard(stage);
  }

  private void initGameBoard(Stage stage) {

    GridPane grid = new GridPane();

    grid.setStyle("-fx-background-color: transparent;");
    grid.setGridLinesVisible(false);
    grid.setAlignment(Pos.CENTER);

    for (int column = 1; column < GameFieldImpl.SIZE + 1; column++) {
      for (int row = 1; row < GameFieldImpl.SIZE + 1; row++) {

        GraphicCell cell = new GraphicCell(column - 1, row - 1, this, model, gameMode, controller);
        grid.add(cell, column, row);
        switchButton.addListener(cell);
        model.addListener(cell);
      }
    }

    for (int i = 1; i < GameFieldImpl.SIZE + 1; i++) {
      Label letter = new Label(String.valueOf((char) (startLetter + i - 1)));
      letter.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: white");

      Label rowNumber = new Label(String.valueOf(i));
      rowNumber.setStyle("-fx-font-size: 30;-fx-font-weight: bold; -fx-text-fill: white");
      grid.add(letter, i, 0);
      grid.add(rowNumber, 0, i);

      GridPane.setHalignment(letter, HPos.CENTER);
      GridPane.setValignment(rowNumber, VPos.CENTER);
      GridPane.setMargin(letter, new Insets(5, 0, 20, 0));
      GridPane.setMargin(rowNumber, new Insets(0, 20, 0, 5));
    }

    setCenter(grid);
  }

  private void initToggleSwitch() {
    BorderPane borderPane = new BorderPane();

    Label title = getLabel("Hints anzeigen?");
    borderPane.setTop(title);
    BorderPane.setAlignment(title, Pos.CENTER);

    HBox hbox = new HBox(15);
    hbox.setAlignment(Pos.CENTER);

    Label no = getLabel("Nein");
    Label yes = getLabel("Ja");
    switchButton = new SwitchButton(100, 15);

    hbox.getChildren().addAll(no, switchButton, yes);
    borderPane.setCenter(hbox);
    BorderPane.setAlignment(hbox, Pos.CENTER);
    BorderPane.setMargin(hbox, new Insets(10, 0, 20, 0));

    setBottom(borderPane);
  }

  private Label getLabel(String text) {
    Label label = new Label(text);
    label.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
    return label;
  }
}
