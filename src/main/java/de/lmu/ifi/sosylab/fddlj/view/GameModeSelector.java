package de.lmu.ifi.sosylab.fddlj.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
  private StartScreenDiskGrid startScreenDisks;

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
    startScreenDisks = new StartScreenDiskGrid();
    borderPane.setCenter(startScreenDisks);

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

    Label close = new Label("X");
    close.setCursor(Cursor.HAND);
    close.setStyle("-fx-text-fill: white; -fx-font-size: 35;");
    close.setOnMouseEntered(e -> close.setStyle("-fx-text-fill: red; -fx-font-size: 35;"));
    close.setOnMouseExited(e -> close.setStyle("fx-text-fill: white; -fx-font-size: 35;"));
    close.setOnMouseClicked(e -> close());
    bp.setRight(close);
    BorderPane.setAlignment(close, Pos.TOP_RIGHT);
    BorderPane.setMargin(close, new Insets(15, 15, 0, 0));

    HBox title = getTitleWithDisks();
    bp.setCenter(title);
    BorderPane.setMargin(title, new Insets(15, 0, 0, 0));

    return bp;
  }

  private HBox getTitleWithDisks() {
    HBox hbox = new HBox(10);
    hbox.setAlignment(Pos.CENTER);

    for (int i = 1; i < 4; i++) {
      hbox.getChildren().add(getDisk(i * 10 + 15, i * 10 + 15, (i * 10 + 15) / 2, Color.WHITE));
    }

    Label title = new Label("Reversi");
    title.setFont(Font.font(25));
    title.setId("title-label");
    hbox.getChildren().add(title);

    for (int i = 3; i > 0; i--) {
      hbox.getChildren().add(getDisk(i * 10 + 15, i * 10 + 15, (i * 10 + 15) / 2, Color.BLACK));
    }

    return hbox;
  }

  private VBox buildSelectionPane() {

    VBox vbox = new VBox(35);
    vbox.setId("selection-pane");
    vbox.setAlignment(Pos.CENTER);
    vbox.setPrefWidth(1.3 * (Screen.getPrimary().getVisualBounds().getWidth() / 5) + 200);
    vbox.setMaxWidth(vbox.getPrefWidth());
    vbox.setFillWidth(true);
    vbox.setPadding(new Insets(0, 0, 20, 0));

    Region top = new Region();
    VBox.setVgrow(top, Priority.ALWAYS);
    vbox.getChildren().add(top);

    Button singlePlayer = getButton("Singleplayer");
    singlePlayer.setOnAction(
        e -> {
          PlayerCreation playerCreation = new PlayerCreation(controller, primaryStage);
          playerCreation.getSinglePlayerInformation(controller, this);
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
          playerCreation.getOnlinePlayerInformation(this);
          borderPane.setCenter(playerCreation);
          borderPane.setRight(null);
        });

    Button spectate = getButton("Spectate");
    spectate.setOnAction(
        e -> {
          PlayerCreation playerCreation = new PlayerCreation(controller, primaryStage);
          playerCreation.getSpectatorInformation(this);
          borderPane.setCenter(playerCreation);
          borderPane.setRight(null);
        });

    vbox.getChildren().addAll(singlePlayer, hotseat, multiPlayer, spectate);

    Region bottom = new Region();
    VBox.setVgrow(bottom, Priority.ALWAYS);
    vbox.getChildren().add(bottom);

    Button server = getButton("Start Server");
    server.setOnAction(e -> {});
    vbox.getChildren().add(server);

    return vbox;
  }

  void returnToMainScreen() {
    borderPane.setRight(buildSelectionPane());
    borderPane.setCenter(startScreenDisks);
  }

  private Button getButton(String text) {
    Button button = new Button(text);
    button.setId("button");
    button.setMinHeight(50);
    button.setMaxWidth(350);
    button.setMinWidth(150);
    button.setCursor(Cursor.HAND);
    button.setFont(Font.font(18));
    button.setEffect(new DropShadow(4, Color.DARKGRAY));

    return button;
  }

  private GraphicDisk getDisk(double width, double height, double radius, Color color) {
    return new GraphicDisk(width, height, radius, color);
  }
}
