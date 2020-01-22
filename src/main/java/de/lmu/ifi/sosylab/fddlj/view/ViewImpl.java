package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import de.lmu.ifi.sosylab.fddlj.model.Phase;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This class represents the game's main frame that holds all other GUI elements.
 *
 * @author Josef Feger
 */
public class ViewImpl implements View {

  static final String STAGE_RESIZED = "Stage resized";

  private Model model;
  private Controller controller;

  private Stage stage;
  private Scene scene;
  private BorderPane root;
  private GameBoardGrid gameBoard;

  private Label numberPlayerOneDisks;
  private Label numberPlayerTwoDisks;

  private PropertyChangeSupport support;

  /**
   * Constructor of this class initialises the main frame of the game.
   *
   * @param stage the stage created upon launching the application
   * @param model a reference to the model instance
   * @param controller a reference to the controller instance
   */
  public ViewImpl(Stage stage, Model model, Controller controller) {

    this.controller = controller;
    this.stage = stage;
    this.model = model;

    support = new PropertyChangeSupport(this);

    this.stage.setTitle("Reversi");
    this.stage.setMaximized(true);
    this.stage.setMinWidth(2 * Screen.getPrimary().getVisualBounds().getWidth() / 5.0);
    this.stage.setMinHeight(2 * Screen.getPrimary().getVisualBounds().getHeight() / 3);
    this.stage
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              support.firePropertyChange(
                  new PropertyChangeEvent(this, STAGE_RESIZED, null, support));
            });

    this.stage
        .heightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              support.firePropertyChange(
                  new PropertyChangeEvent(this, STAGE_RESIZED, null, support));
            });
  }

  @Override
  public void showGame(GameMode gameMode) {
    root = new BorderPane();
    root.getStylesheets().add("cssFiles/mainGame.css");
    root.setId("main-background");

    HBox spacerTop = new HBox();
    spacerTop.setMinHeight(40);
    spacerTop.setMaxHeight(80);
    spacerTop.setId("borderpane-spacer");
    root.setTop(spacerTop);

    HBox spacerBottom = new HBox();
    spacerBottom.setMinHeight(40);
    spacerBottom.setMaxHeight(80);
    spacerBottom.setId("borderpane-spacer");
    root.setBottom(spacerBottom);

    VBox left = getDiskIndicators(gameMode);
    root.setLeft(left);
    BorderPane.setAlignment(left, Pos.CENTER);

    gameBoard = new GameBoardGrid(model, controller, stage, this);
    root.setCenter(gameBoard);
    BorderPane.setAlignment(root, Pos.CENTER);
    BorderPane.setMargin(gameBoard, new Insets(30, 50, 30, 50));

    scene = new Scene(root);

    stage.setScene(scene);
    stage.show();
  }

  private VBox getDiskIndicators(GameMode gameMode) {

    VBox vbox = new VBox(30);
    vbox.setAlignment(Pos.TOP_CENTER);
    vbox.setPadding(new Insets(20));

    DiskIndicator currentPlayer = new DiskIndicator(model, "Current player:", this, controller);
    vbox.getChildren().add(currentPlayer);

    Button reset = getButton("Reset game");
    reset.setOnAction(
        e -> {
          controller.resetGame(
              gameMode,
              model.getState().getPlayerManagement().getPlayerOne(),
              model.getState().getPlayerManagement().getPlayerTwo());
        });

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    vbox.getChildren().addAll(new Separator(Orientation.HORIZONTAL), spacer, getDiskCounter());

    Region bottomSpacer = new Region();
    VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

    vbox.getChildren()
        .addAll(bottomSpacer, new Separator(Orientation.HORIZONTAL), getHelpButton(), reset);

    return vbox;
  }

  private VBox getDiskCounter() {
    VBox vbox = new VBox(30);
    vbox.setAlignment(Pos.TOP_CENTER);
    vbox.setStyle("-fx-background-color: #6e7175;");
    vbox.setFillWidth(true);
    vbox.setMaxWidth(Double.POSITIVE_INFINITY);

    if (model instanceof ModelImpl) {

      HBox playerOneInfo = new HBox(10);
      playerOneInfo.setAlignment(Pos.CENTER);
      playerOneInfo.setStyle("-fx-background-color: #6e7175;");
      playerOneInfo.setMaxWidth(Double.POSITIVE_INFINITY);

      ModelImpl mod = (ModelImpl) model;

      playerOneInfo
          .getChildren()
          .add(buildDiskTriangle(model.getState().getPlayerManagement().getPlayerOne().getColor()));
      numberPlayerOneDisks = new Label(String.valueOf(mod.getNumberOfDisksPlayerOne()));
      numberPlayerOneDisks.setFont(Font.font("Boulder", FontWeight.BOLD, 25));
      numberPlayerOneDisks.setStyle("-fx-text-fill: white;");
      playerOneInfo.getChildren().add(numberPlayerOneDisks);
      vbox.getChildren().add(playerOneInfo);

      HBox playerTwoInfo = new HBox(10);
      playerTwoInfo.setAlignment(Pos.CENTER);
      playerTwoInfo.setStyle("-fx-background-color: #6e7175;");

      playerTwoInfo
          .getChildren()
          .add(buildDiskTriangle(model.getState().getPlayerManagement().getPlayerTwo().getColor()));
      numberPlayerTwoDisks = new Label(String.valueOf(mod.getNumberOfDisksPlayerTwo()));
      numberPlayerTwoDisks.setFont(Font.font("Boulder", FontWeight.BOLD, 25));
      numberPlayerTwoDisks.setStyle("-fx-text-fill: white;");
      playerTwoInfo.getChildren().add(numberPlayerTwoDisks);

      vbox.getChildren().add(playerTwoInfo);
    }

    return vbox;
  }

  private Button getHelpButton() {
    Image help = new Image(getClass().getClassLoader().getResourceAsStream("images/help.png"));

    ImageView imageView = new ImageView(help);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(50);
    Button button = new Button("", imageView);
    button.setCursor(Cursor.HAND);
    button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    button.setOnAction(e -> new AboutWindow());
    Tooltip helper = new Tooltip();
    helper.setText(
        "Display a window with additional information about this reversi game,"
            + " such as the rules and licenses.");
    button.setTooltip(helper);

    return button;
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    Platform.runLater(
        new Runnable() {

          @Override
          public void run() {
            handleChangeEvent(event);
          }
        });
  }

  /**
   * The observable (= model) has just published that it has changed its state. The GUI needs to be
   * updated accordingly here.
   *
   * @param event The event that has been fired by the model.
   */
  private void handleChangeEvent(PropertyChangeEvent event) {

    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      support.firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());

      if (model instanceof ModelImpl) {
        ModelImpl mod = (ModelImpl) model;

        numberPlayerOneDisks.setText(String.valueOf(mod.getNumberOfDisksPlayerOne()));

        numberPlayerTwoDisks.setText(String.valueOf(mod.getNumberOfDisksPlayerTwo()));
      }

      if (model.getState().getCurrentPhase() == Phase.FINISHED) {

        root.setDisable(true);
        new GameFinishedScreen(controller, model, stage);
      }
    }

    if (event.getPropertyName().equals(Model.LISTENERS_CHANGED)) {

      if (event.getNewValue() instanceof Model) {
        if (root.isDisabled()) {
          root.setDisable(false);
        }

        this.model = (Model) event.getNewValue();

        support.firePropertyChange(event);
      }
    }
  }

  private Button getButton(String text) {
    Button button = new Button(text);
    button.setId("button");
    button.setMinHeight(50);
    button.setMaxWidth(250);
    button.setMinWidth(100);
    button.setCursor(Cursor.HAND);
    button.setFont(Font.font(18));

    return button;
  }

  @Override
  public void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void removeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
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
}
