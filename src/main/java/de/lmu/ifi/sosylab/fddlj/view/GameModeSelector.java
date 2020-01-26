package de.lmu.ifi.sosylab.fddlj.view;

import java.util.ResourceBundle;
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

  private static final String CSS_CLOSELABEL_NORMAL = "-fx-text-fill: white; -fx-font-size: 35;";
  private static final String CSS_CLOSELABEL_SELECTED = "-fx-text-fill: red; -fx-font-size: 35;";

  private static final Insets TOP_INSETS = new Insets(15, 15, 0, 0);
  private static final int DISK_ADDITION = 15;
  private static final int DISK_SCALING = 10;

  private static final Font FONT_BUTTON = Font.font(18);
  private static final Cursor CURSOR_BUTTON = Cursor.HAND;
  private static final int MINWIDTH_BUTTON = 200;
  private static final int MAXWIDTH_BUTTON = 500;
  private static final int MINHEIGHT_BUTTON = 50;

  private Controller controller;
  private Scene scene;
  private Stage primaryStage;

  private BorderPane borderPane;
  private StartScreenDiskGrid startScreenDisks;

  private ResourceBundle messages;

  /**
   * Constructor of this class initialises variables and builds the stage.
   *
   * @param controller a reference to a controller instance
   * @param stage the stage created by the application thread
   * @param messages the ResourceBundle for the externalised strings
   */
  public GameModeSelector(Controller controller, Stage stage, ResourceBundle messages) {
    super();

    this.controller = controller;
    this.primaryStage = stage;

    this.messages = messages;
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
    setTitle(messages.getString("Game_Title"));
    initStyle(StageStyle.UNDECORATED);
    setMinWidth(2 * (Screen.getPrimary().getVisualBounds().getWidth() / 3));
    setMinHeight(2.1 * (Screen.getPrimary().getVisualBounds().getHeight() / 3));
    setWidth(getMinWidth());
    setHeight(getMinHeight());
    centerOnScreen();
    show();
  }

  private BorderPane buildTopPart() {

    BorderPane bp = new BorderPane();
    bp.getStyleClass().add("transparent");

    Label close = new Label("X");
    close.setCursor(Cursor.HAND);
    close.setStyle(CSS_CLOSELABEL_NORMAL);
    close.setOnMouseEntered(e -> close.setStyle(CSS_CLOSELABEL_SELECTED));
    close.setOnMouseExited(e -> close.setStyle(CSS_CLOSELABEL_NORMAL));
    close.setOnMouseClicked(e -> close());
    bp.setRight(close);
    BorderPane.setAlignment(close, Pos.TOP_RIGHT);
    BorderPane.setMargin(close, TOP_INSETS);

    HBox title = getTitleWithDisks();
    bp.setCenter(title);
    BorderPane.setAlignment(title, Pos.CENTER);
    BorderPane.setMargin(title, TOP_INSETS);

    return bp;
  }

  private HBox getTitleWithDisks() {
    HBox hbox = new HBox(10);
    hbox.setAlignment(Pos.CENTER);

    for (int i = 1; i < 4; i++) {
      hbox.getChildren()
          .add(
              getDisk(
                  i * DISK_SCALING + DISK_ADDITION,
                  i * DISK_SCALING + DISK_ADDITION,
                  (i * DISK_SCALING + DISK_ADDITION) / 2.0,
                  Color.WHITE));
    }

    Label title = new Label(messages.getString("Game_Title"));
    title.setId("title-label");
    hbox.getChildren().add(title);

    for (int i = 3; i > 0; i--) {
      hbox.getChildren()
          .add(
              getDisk(
                  i * DISK_SCALING + DISK_ADDITION,
                  i * DISK_SCALING + DISK_ADDITION,
                  (i * DISK_SCALING + DISK_ADDITION) / 2.0,
                  Color.BLACK));
    }

    return hbox;
  }

  private VBox buildSelectionPane() {

    VBox vbox = new VBox(35);
    vbox.setId("selection-pane");
    vbox.setAlignment(Pos.CENTER);
    vbox.setPrefWidth(1.3 * (Screen.getPrimary().getVisualBounds().getWidth() / 5));
    vbox.setMaxWidth(vbox.getPrefWidth());
    vbox.setFillWidth(true);

    Region top = new Region();
    VBox.setVgrow(top, Priority.ALWAYS);
    vbox.getChildren().add(top);

    PlayerCreation playerCreation = new PlayerCreation(controller, primaryStage, messages);

    Button singlePlayer = getButton(messages.getString("GameModeSelector_ButtonSingleplayer_Text"));
    singlePlayer.setOnAction(
        e -> {
          playerCreation.getSinglePlayerInformation(controller, this);
          borderPane.setCenter(playerCreation);
          borderPane.setRight(null);
        });

    Button hotseat = getButton(messages.getString("GameModeSelector_ButtonHotseat_Text"));
    hotseat.setOnAction(
        e -> {
          playerCreation.getMultiplePlayersInformation(this);
          borderPane.setCenter(playerCreation);
          borderPane.setRight(null);
        });

    Button multiPlayer = getButton(messages.getString("GameModeSelector_ButtonMultiplayer"));
    multiPlayer.setOnAction(
        e -> {
          playerCreation.getOnlinePlayerInformation(this);
          borderPane.setCenter(playerCreation);
          borderPane.setRight(null);
        });

    Button spectate = getButton(messages.getString("GameModeSelector_ButtonSpectate"));
    spectate.setOnAction(
        e -> {
          playerCreation.getSpectatorInformation(this);
          borderPane.setCenter(playerCreation);
          borderPane.setRight(null);
        });

    vbox.getChildren().addAll(singlePlayer, hotseat, multiPlayer, spectate);

    Region bottom = new Region();
    VBox.setVgrow(bottom, Priority.ALWAYS);
    vbox.getChildren().add(bottom);

    Button server = getButton(messages.getString("GameModeSelector_ButtonStartServer"));
    server.setOnAction(
        e -> {
          controller.startServer();
        });
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
    button.setMinHeight(MINHEIGHT_BUTTON);
    button.setMaxWidth(MAXWIDTH_BUTTON);
    button.setMinWidth(MINWIDTH_BUTTON);
    button.setCursor(CURSOR_BUTTON);
    button.setFont(FONT_BUTTON);
    button.setEffect(new DropShadow(4, Color.DARKGRAY));

    return button;
  }

  private GraphicDisk getDisk(double width, double height, double radius, Color color) {
    return new GraphicDisk(width, height, radius, color);
  }
}
