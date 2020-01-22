package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameFieldImpl;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

    VBox centerGrid = new VBox();
    centerGrid.setAlignment(Pos.CENTER);
    centerGrid.setEffect(new DropShadow(10, 5, 5, Color.BLACK));
    for (int row = 1; row < GameFieldImpl.SIZE + 1; row++) {

      HBox hboxRow = new HBox();
      hboxRow.setAlignment(Pos.CENTER);

      for (int column = 1; column < GameFieldImpl.SIZE + 1; column++) {

        GraphicCell cell = new GraphicCell(column - 1, row - 1, this, model, controller);
        hboxRow.getChildren().add(cell);
        switchButton.addListener(cell);
        view.addListener(cell);
      }

      centerGrid.getChildren().add(hboxRow);
    }

    setCenter(centerGrid);
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
