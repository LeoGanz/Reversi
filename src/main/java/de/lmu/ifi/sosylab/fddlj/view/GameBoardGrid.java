package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameFieldImpl;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
  private Label numberPlayerOneDisks;
  private Label numberPlayerTwoDisks;

  private char startLetter = 'A';

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

    model.addListener(this);

    setStyle("-fx-background-color: #6e7175;");

    initToggleSwitch();
    initGameBoard(stage, view);
    initDiskCounter();
  }

  private void initGameBoard(Stage stage, View view) {

    GridPane grid = new GridPane();

    grid.setStyle("-fx-background-color: #6e7175;");
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
      letter.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white");

      Label rowNumber = new Label(String.valueOf(i));
      rowNumber.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: white");
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
    borderPane.setStyle("-fx-background-color: #6e7175;");

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
    BorderPane.setMargin(hbox, new Insets(10, 0, 0, 0));

    setBottom(borderPane);
    BorderPane.setMargin(borderPane, new Insets(30, 0, 0, 0));
  }

  private void initDiskCounter() {
    HBox hbox = new HBox(100);
    hbox.setAlignment(Pos.CENTER);
    hbox.setStyle("-fx-background-color: #6e7175;");

    if (model instanceof ModelImpl) {

      HBox playerOneInfo = new HBox(10);
      playerOneInfo.setAlignment(Pos.CENTER);
      playerOneInfo.setStyle("-fx-background-color: #6e7175;");

      ModelImpl mod = (ModelImpl) model;

      playerOneInfo
          .getChildren()
          .add(buildDiskTriangle(model.getState().getPlayerManagement().getPlayerOne().getColor()));
      numberPlayerOneDisks = new Label(String.valueOf(mod.getNumberOfDisksPlayerOne()));
      numberPlayerOneDisks.setFont(Font.font("Boulder", FontWeight.BOLD, 25));
      numberPlayerOneDisks.setStyle("-fx-text-fill: white;");
      playerOneInfo.getChildren().add(numberPlayerOneDisks);
      hbox.getChildren().add(playerOneInfo);

      HBox playerTwoInfo = new HBox(10);
      playerTwoInfo.setAlignment(Pos.CENTER);
      playerTwoInfo.setStyle("-fx-background-color: #6e7175;");

      numberPlayerTwoDisks = new Label(String.valueOf(mod.getNumberOfDisksPlayerTwo()));
      numberPlayerTwoDisks.setFont(Font.font("Boulder", FontWeight.BOLD, 25));
      playerTwoInfo.getChildren().add(numberPlayerTwoDisks);
      playerTwoInfo
          .getChildren()
          .add(buildDiskTriangle(model.getState().getPlayerManagement().getPlayerTwo().getColor()));
      hbox.getChildren().add(playerTwoInfo);
    }

    setTop(hbox);
    BorderPane.setMargin(hbox, new Insets(0, 0, 30, 0));
  }

  private GridPane buildDiskTriangle(Color color) {
    GridPane grid = new GridPane();

    grid.setStyle("-fx-background-color: transparent;");
    grid.setGridLinesVisible(false);
    grid.setAlignment(Pos.CENTER);

    GraphicDisk diskOne = new GraphicDisk(30, 30, 15, color);
    GraphicDisk diskTwo = new GraphicDisk(30, 30, 15, color);
    GraphicDisk diskThree = new GraphicDisk(30, 30, 15, color);
    GraphicDisk diskFour = new GraphicDisk(30, 30, 15, color);

    grid.add(diskOne, 1, 0);
    grid.add(diskTwo, 0, 1);
    grid.add(diskThree, 1, 1);
    grid.add(diskFour, 2, 1);

    return grid;
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
          if (model instanceof ModelImpl) {
            ModelImpl mod = (ModelImpl) model;

            numberPlayerOneDisks.setText(String.valueOf(mod.getNumberOfDisksPlayerOne()));

            numberPlayerTwoDisks.setText(String.valueOf(mod.getNumberOfDisksPlayerTwo()));
          }
        });
  }
}
