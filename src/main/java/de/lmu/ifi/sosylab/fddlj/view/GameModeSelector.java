package de.lmu.ifi.sosylab.fddlj.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class offers the start window in which the user can select the game mode he wants to play.
 *
 * @author Josef Feger
 */
public class GameModeSelector extends Stage {

  private Controller controller;
  private Scene scene;
  private Stage primaryStage;

  private BorderPane borderPane;

  /**
   * Constructor of this class initialises variables and builds the stage.
   *
   * @param controller a reference to a controller instance
   * @param stage the stage created by the application thread
   */
  public GameModeSelector(Controller controller, Stage stage) {
    super();

    this.controller = controller;
    this.primaryStage = stage;
  }

  /** Displays a stage that allows the user to select which game mode he/she wants to play. */
  public void showGameModeSelection() {

    borderPane = new BorderPane();
    borderPane.getStylesheets().add("cssFiles/gameModeSelector.css");
    borderPane.setId("main-pane");

    borderPane.setTop(buildTopPart());
    borderPane.setRight(buildSelectionPane());

    scene = new Scene(borderPane);

    setScene(scene);
    setTitle("Reversi");
    initStyle(StageStyle.UNDECORATED);
    setMinWidth(2 * (Screen.getPrimary().getVisualBounds().getWidth() / 3));
    setMinHeight(2 * (Screen.getPrimary().getVisualBounds().getHeight() / 3));
    setWidth(getMinWidth());
    setHeight(getMinHeight());
    centerOnScreen();
    show();
  }

  private BorderPane buildTopPart() {

    BorderPane bp = new BorderPane();
    bp.setStyle("-fx-background-color: transparent;");

    HBox closeTop = new HBox();
    closeTop.setAlignment(Pos.CENTER_RIGHT);

    Label close = new Label("X");
    close.setCursor(Cursor.HAND);
    close.setStyle("-fx-text-fill: white; -fx-font-size: 35;");
    close.setOnMouseEntered(
        new EventHandler<MouseEvent>() {

          @Override
          public void handle(MouseEvent event) {

            close.setStyle("-fx-text-fill: red; -fx-font-size: 35;");
          }
        });
    close.setOnMouseExited(
        new EventHandler<MouseEvent>() {

          @Override
          public void handle(MouseEvent event) {

            close.setStyle("fx-text-fill: white; -fx-font-size: 35;");
          }
        });
    close.setOnMouseClicked(
        new EventHandler<MouseEvent>() {

          @Override
          public void handle(MouseEvent event) {

            close();
          }
        });
    closeTop.getChildren().add(close);
    bp.setRight(closeTop);
    BorderPane.setAlignment(closeTop, Pos.CENTER_RIGHT);
    BorderPane.setMargin(closeTop, new Insets(15, 30, 0, 0));

    Label title = new Label("Reversi");
    title.setFont(Font.font(25));
    title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 40;");
    bp.setCenter(title);
    BorderPane.setMargin(title, new Insets(15, 0, 0, 0));

    return bp;
  }

  private VBox buildSelectionPane() {

    VBox vbox = new VBox(15);
    vbox.setId("selection-pane");
    vbox.setAlignment(Pos.CENTER);
    vbox.setPrefWidth(1.3 * (Screen.getPrimary().getVisualBounds().getWidth() / 5) + 200);
    vbox.setMaxWidth(vbox.getPrefWidth());
    vbox.setFillWidth(true);

    Region top = new Region();
    VBox.setVgrow(top, Priority.ALWAYS);
    vbox.getChildren().add(top);

    Button singlePlayer = getButton("Singleplayer");
    singlePlayer.setOnAction(
        e -> {
          PlayerCreation playerCreation = new PlayerCreation(controller, primaryStage);
          playerCreation.getSinglePlayerInformation(controller, this, false);
          borderPane.setCenter(playerCreation);
          borderPane.setRight(null);
        });

    Button hotseat = getButton("Hotseat");
    hotseat.setOnAction(
        e -> {
          PlayerCreation playerCreation = new PlayerCreation(controller, primaryStage);
          playerCreation.getMultiplePlayersInformation(this);
          borderPane.setCenter(playerCreation);
          borderPane.setRight(null);
        });

    Button multiPlayer = getButton("Multiplayer");
    multiPlayer.setOnAction(
        e -> {
          PlayerCreation playerCreation = new PlayerCreation(controller, primaryStage);
          playerCreation.getSinglePlayerInformation(controller, this, true);
          borderPane.setCenter(playerCreation);
          borderPane.setRight(null);
        });

    vbox.getChildren().addAll(singlePlayer, hotseat, multiPlayer);

    Region bottom = new Region();
    VBox.setVgrow(bottom, Priority.ALWAYS);
    vbox.getChildren().add(bottom);

    return vbox;
  }

  void returnToMainScreen() {
    borderPane.setRight(buildSelectionPane());
    borderPane.setCenter(null);
  }

  private Button getButton(String text) {
    Button button = new Button(text);
    button.setId("button");
    button.setMinHeight(50);
    button.setMaxWidth(500);
    button.setMinWidth(200);
    button.setCursor(Cursor.HAND);
    button.setFont(Font.font(18));

    return button;
  }
}
