package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import de.lmu.ifi.sosylab.fddlj.model.Phase;
import de.lmu.ifi.sosylab.fddlj.network.ClientCompatibleGui;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest.Response;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest.Response.ResponseType;
import de.lmu.ifi.sosylab.fddlj.network.communication.RejectedPlacement.Reason;
import de.lmu.ifi.sosylab.fddlj.network.communication.ServerNotification;
import de.lmu.ifi.sosylab.fddlj.network.communication.Spectators;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class represents the game's main frame that holds all other GUI elements.
 *
 * @author Josef Feger
 */
public class ViewImpl implements OnlineView, ClientCompatibleGui {

  private Model model;
  private Controller controller;

  private Stage stage;
  private Scene scene;
  private BorderPane root;
  private GameBoardGrid gameBoard;

  private Label numberPlayerOneDisks;
  private Label numberPlayerTwoDisks;
  private Label lobbyID;

  private PropertyChangeSupport support;

  private boolean playSound;
  private boolean volumeControlShowing;
  private boolean guiShowing;

  private float volume;
  private int lobbyId;

  private ResourceBundle messages;

  /**
   * Constructor of this class initialises the main frame of the game.
   *
   * @param stage the stage created upon launching the application
   * @param model a reference to the model instance
   * @param controller a reference to the controller instance
   * @param messages the ResourceBundle for the externalised strings
   */
  public ViewImpl(Stage stage, Model model, Controller controller, ResourceBundle messages) {

    this.controller = controller;
    this.stage = stage;
    this.model = model;

    this.messages = messages;

    support = new PropertyChangeSupport(this);
    playSound = true;
    volume = 0.6f;

    this.stage.setTitle(messages.getString("Game_Title"));
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
    if (guiShowing) {
      return;
    }

    guiShowing = true;
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

    gameBoard = new GameBoardGrid(model, controller, stage, this, messages);
    root.setCenter(gameBoard);
    BorderPane.setAlignment(root, Pos.CENTER);
    BorderPane.setMargin(gameBoard, new Insets(30, 50, 30, 50));

    VBox right = getMuteAndMainMenuButton();
    root.setRight(right);
    BorderPane.setMargin(right, new Insets(0, 15, 0, 0));

    scene = new Scene(root);
    stage.setMinWidth(
        left.getMinWidth()
            + gameBoard.getMinWidth()
            + BorderPane.getMargin(gameBoard).getLeft()
            + BorderPane.getMargin(gameBoard).getRight()
            + right.getMinWidth()
            + 2 * BorderPane.getMargin(right).getRight());

    double minHeightLeft =
        left.getMinHeight() + spacerBottom.getMinHeight() + spacerTop.getMinHeight();
    double minHeightBoard =
        gameBoard.getMinHeight() + spacerBottom.getMinHeight() + spacerTop.getMinHeight();

    if (minHeightLeft > minHeightBoard) {
      stage.setMinHeight(minHeightLeft);
    } else {
      stage.setMinHeight(minHeightBoard);
    }

    stage.setScene(scene);
    if (!stage.isShowing()) {
      stage.show();
    }
  }

  private VBox getDiskIndicators(GameMode gameMode) {

    VBox vbox = new VBox(30);
    vbox.setAlignment(Pos.TOP_CENTER);
    vbox.setPadding(new Insets(20));

    DiskIndicator currentPlayer =
        new DiskIndicator(model, messages.getString("ViewImpl_CurrentPlayer"), this, controller);
    vbox.getChildren().add(currentPlayer);
    stage.heightProperty().addListener(e -> currentPlayer.resizeDisk());
    stage.widthProperty().addListener(e -> currentPlayer.resizeDisk());

    Button reset = getButton(messages.getString("ViewImpl_ButtonResetGame"));
    reset.setOnAction(
        e -> {
          controller.resetGame(
              gameMode,
              model.getState().getPlayerManagement().getPlayerOne(),
              model.getState().getPlayerManagement().getPlayerTwo(),
              model.getState().getField().getSize());
          if (gameMode == GameMode.MULTIPLAYER) {
            gameBoard.setDisable(true);
            showAlert(
                AlertType.INFORMATION,
                messages.getString("ViewImpl_ResetRequest_Title"),
                messages.getString("ViewImpl_ResetRequest_Subtitle"),
                messages.getString("ViewImpl_ResetRequest_Info"));
          }
        });

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    VBox diskCounter = getDiskCounter();
    vbox.getChildren().addAll(new Separator(Orientation.HORIZONTAL), spacer, diskCounter);

    Region bottomSpacer = new Region();
    VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

    vbox.getChildren().addAll(bottomSpacer, new Separator(Orientation.HORIZONTAL), getHelpButton());
    if (gameMode != GameMode.SPECTATOR) {
      vbox.getChildren().addAll(reset);
    }
    vbox.setMinWidth(
        (diskCounter.getMinWidth() + vbox.getPadding().getLeft() + vbox.getPadding().getRight()));
    vbox.setMinHeight(
        vbox.getSpacing() * (vbox.getChildren().size() - 1)
            + currentPlayer.getMinHeight()
            + reset.getPrefHeight()
            + diskCounter.getMinHeight()
            + getHelpButton().getMinHeight()
            + vbox.getPadding().getTop()
            + vbox.getPadding().getBottom());

    return vbox;
  }

  private VBox getDiskCounter() {
    VBox vbox = new VBox(30);
    vbox.setAlignment(Pos.TOP_CENTER);
    vbox.getStyleClass().add("transparent");
    vbox.setMaxWidth(Double.POSITIVE_INFINITY);

    if (model instanceof ModelImpl) {

      HBox playerOneInfo = new HBox(10);
      playerOneInfo.setAlignment(Pos.CENTER);
      playerOneInfo.getStyleClass().add("transparent");
      playerOneInfo.setMaxWidth(Double.POSITIVE_INFINITY);

      ModelImpl mod = (ModelImpl) model;
      Font labelFont = Font.font("Boulder", FontWeight.BOLD, 25);

      VBox diskTriangle =
          buildDiskTriangle(model.getState().getPlayerManagement().getPlayerOne().getColor());
      playerOneInfo.getChildren().add(diskTriangle);
      numberPlayerOneDisks = new Label(String.valueOf(mod.getNumberOfDisksPlayerOne()));
      numberPlayerOneDisks.setFont(labelFont);
      numberPlayerOneDisks.getStyleClass().add("white-label");
      playerOneInfo.getChildren().add(numberPlayerOneDisks);
      vbox.getChildren().add(playerOneInfo);

      HBox playerTwoInfo = new HBox(10);
      playerTwoInfo.setAlignment(Pos.CENTER);
      playerTwoInfo.getStyleClass().add("transparent");

      playerTwoInfo
          .getChildren()
          .add(buildDiskTriangle(model.getState().getPlayerManagement().getPlayerTwo().getColor()));
      numberPlayerTwoDisks = new Label(String.valueOf(mod.getNumberOfDisksPlayerTwo()));
      numberPlayerTwoDisks.setFont(labelFont);
      numberPlayerTwoDisks.getStyleClass().add("white-label");
      playerTwoInfo.getChildren().add(numberPlayerTwoDisks);

      vbox.getChildren().add(playerTwoInfo);

      vbox.setMinHeight((vbox.getSpacing() + diskTriangle.getMinHeight() * 2));
      vbox.setMinWidth(
          (diskTriangle.getMinWidth() + playerOneInfo.getSpacing() + labelFont.getSize() * 2));
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
    button.setOnAction(e -> new AboutWindow(messages));
    Tooltip helper = new Tooltip();
    helper.setText(messages.getString("ViewImpl_ButtonHelp_Tooltip"));
    button.setTooltip(helper);
    button.setMinHeight(50);

    return button;
  }

  private VBox getMuteAndMainMenuButton() {
    VBox vbox = new VBox(30);
    vbox.setAlignment(Pos.TOP_CENTER);
    vbox.setPadding(new Insets(20));

    if (controller.getCurrentGameMode() == GameMode.MULTIPLAYER) {
      SpectatorList spectatorList = new SpectatorList(messages, lobbyId);
      addListener(spectatorList);

      vbox.getChildren().add(spectatorList);
    }

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    vbox.getChildren().add(spacer);

    if (controller.getCurrentGameMode() == GameMode.MULTIPLAYER) {
      vbox.getChildren().add(new Separator(Orientation.HORIZONTAL));
    }

    final Image play =
        new Image(getClass().getClassLoader().getResourceAsStream("images/loudspeaker.png"));
    final Image mute =
        new Image(getClass().getClassLoader().getResourceAsStream("images/loudspeaker_mute.png"));

    ImageView imageView = new ImageView(play);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(30);
    Button button = new Button("", imageView);
    button.setCursor(Cursor.HAND);
    button.setId("mute-button");
    button.setPrefSize(50, 50);
    button.setOnAction(
        e -> {
          if (playSound) {
            playSound = false;

            imageView.setImage(mute);
            button.setGraphic(imageView);
            support.firePropertyChange(SOUND_MODE_CHANGED, !playSound, playSound);
          } else {
            playSound = true;

            imageView.setImage(play);
            button.setGraphic(imageView);
            support.firePropertyChange(SOUND_MODE_CHANGED, !playSound, playSound);
          }
        });
    button.setOnMouseClicked(
        e -> {
          if (e.getButton() == MouseButton.SECONDARY) {
            showVolumeControl(button);
            return;
          }
        });
    button.setTooltip(new Tooltip(messages.getString("ViewImpl_ButtonSound_Tooltip")));

    vbox.getChildren().add(button);

    Button back = getButton(messages.getString("ViewImpl_ButtonBack_Text"));
    back.setOnAction(e -> returnToMainMenu());
    vbox.getChildren().add(back);

    vbox.setMinWidth(
        back.getPrefWidth() + vbox.getPadding().getRight() + vbox.getPadding().getLeft());
    return vbox;
  }

  private void showVolumeControl(Button parent) {
    if (!volumeControlShowing) {
      volumeControlShowing = true;
      final Stage dialog = new Stage();
      dialog.initModality(Modality.NONE);

      HBox dialogBox = new HBox(15);
      dialogBox.setStyle("-fx-background-color: #dddddd");
      dialogBox.setPadding(new Insets(10));

      Slider slider = new Slider();
      slider.setMin(0);
      slider.setMax(100);
      slider.setValue(volume * 100);
      slider.setShowTickLabels(true);
      slider.setShowTickMarks(true);
      slider.setMajorTickUnit(50);
      slider.setMinorTickCount(5);
      slider.setBlockIncrement(5);
      slider.setMinWidth(200);
      slider.setMinHeight(35);
      slider.setCursor(Cursor.HAND);

      Label label = new Label(String.valueOf(volume * 100));
      label.setStyle("-fx-text-file: #000000; -fx-font-size: x-large;");
      slider
          .valueProperty()
          .addListener(
              (ov, oldVal, newVal) -> {
                volume = (float) (newVal.intValue() / 100.0);
                label.setText(String.valueOf(newVal.intValue()));

                support.firePropertyChange(VOLUME_CHANGED, null, volume);
              });

      dialogBox.getChildren().addAll(slider, label);
      Scene dialogScene = new Scene(dialogBox, 280, 50);
      dialog.setScene(dialogScene);
      dialog.setX(
          parent.localToScreen(parent.getBoundsInLocal()).getMinX() - dialogScene.getWidth() - 10);
      dialog.setY(parent.localToScreen(parent.getBoundsInLocal()).getMinY());
      dialog.initStyle(StageStyle.UNDECORATED);
      dialog.show();
      dialog
          .focusedProperty()
          .addListener(
              (ov, oldVal, newVal) -> {
                if (!newVal) {
                  dialog.close();
                  volumeControlShowing = false;
                }
              });
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    Platform.runLater(() -> handleChangeEvent(event));
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
        new GameFinishedScreen(controller, model, stage, messages);
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
    button.setMinHeight(40);
    button.setPrefHeight(50);
    button.setMaxWidth(250);
    button.setMinWidth(100);
    button.setPrefWidth(170);
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

  private VBox buildDiskTriangle(Color color) {
    GridPane grid = new GridPane();
    VBox vbox = new VBox(3);

    vbox.getStyleClass().add("transparent");
    grid.setGridLinesVisible(false);
    vbox.setAlignment(Pos.CENTER);

    GraphicDisk diskOne = new GraphicDisk(30, 30, 15, color);

    HBox topRow = new HBox();
    topRow.setAlignment(Pos.CENTER);
    topRow.getChildren().add(diskOne);
    GraphicDisk diskTwo = new GraphicDisk(30, 30, 15, color);
    GraphicDisk diskThree = new GraphicDisk(30, 30, 15, color);

    HBox bottomRow = new HBox(3);
    bottomRow.setAlignment(Pos.CENTER);
    bottomRow.getChildren().addAll(diskTwo, diskThree);

    vbox.setMinHeight(80);
    vbox.setMinWidth((3 * 30));

    vbox.getChildren().addAll(topRow, bottomRow);

    return vbox;
  }

  @Override
  public void receivedJoinRequestResponse(Response response) {
    Platform.runLater(() -> handleJoinRequestResponse(response));
  }

  private void handleJoinRequestResponse(Response response) {
    if (response.getType() == ResponseType.JOIN_SUCCESSFUL) {
      lobbyId = response.getLobbyID();

      if (lobbyID != null) {
        lobbyID.setText(lobbyID.getText() + " " + response.getLobbyID());
      }
    } else if (response.getType() == ResponseType.LOBBY_NOT_FOUND) {
      showAlert(
          AlertType.ERROR,
          messages.getString("ViewImpl_JoinError_LobbyNotFound_Title"),
          messages.getString("ViewImpl_JoinError_LobbyNotFound_Subtitle"),
          messages.getString("ViewImpl_JoinError_LobbyNotFound_Info"));
      returnToMainMenu();
    } else if (response.getType() == ResponseType.NO_PLAYERS_NEEDED) {

      showAlert(
          AlertType.ERROR,
          messages.getString("ViewImpl_JoinError_NoPlayerNeeded_Title"),
          messages.getString("ViewImpl_JoinError_NoPlayerNeeded_Subtitle"),
          messages.getString("ViewImpl_JoinError_NoPlayerNeeded_Info"));
      returnToMainMenu();
    }
  }

  private void returnToMainMenu() {
    stage.close();
    if (controller instanceof ControllerImpl) {
      ((ControllerImpl) controller).showGameModeSelector(new Stage());
    } else {
      controller = new ControllerImpl(messages);
      ((ControllerImpl) controller).showGameModeSelector(new Stage());

      stage.close();
    }
  }

  @Override
  public void receivedRejectedPlacementReason(Reason rejectedPlacement) {
    System.out.println(rejectedPlacement);
    Platform.runLater(
        () ->
            showAlert(
                AlertType.ERROR,
                messages.getString("ViewImpl_DiskError_Title"),
                messages.getString("ViewImpl_DiskError_Subtitle"),
                messages.getString("ViewImpl_DiskError_Info")));
  }

  @Override
  public void receivedServerNotification(ServerNotification serverNotification) {
    Platform.runLater(() -> handleServerNotification(serverNotification));
  }

  private void handleServerNotification(ServerNotification serverNotification) {
    switch (serverNotification) {
      case SERVER_SHUTTING_DOWN:
        handleServerShuttingDown();
        break;
      case RESTARTING:
        break;
      case RECEIVED_INVALID_DATA:
        break;
      case PARTNER_ACCEPTED_RESTART:
        break;
      case PARTNER_DENIED_RESTART:
        showAlert(
            AlertType.INFORMATION,
            messages.getString("ViewImpl_ResetRequest_Denied_Title"),
            messages.getString("ViewImpl_ResetRequest_Denied_Subtitle"),
            messages.getString("ViewImpl_ResetRequest_Denied_Info"));
        root.setDisable(false);
        break;
      case PARTNER_REQUESTED_RESTART:
        handleRestartRequest();
        break;
      case PLAYER_ONE_LEFT:
      case PLAYER_TWO_LEFT:
        handleOnePlayerLeft();
        break;
      case LOBBY_CLOSED:
        handleLobbyClosed();
        break;
      default:
        break;
    }
  }

  private void handleServerShuttingDown() {
    if (!stage.isShowing()) {
      return;
    }
    ButtonType backToMainMenu = new ButtonType(messages.getString("ViewImpl_ButtonBack_Text"));
    ButtonType continueAgainstAi =
        new ButtonType(messages.getString("ViewImpl_ButtonContinueAgainstAI_Text"));
    ButtonType exit = new ButtonType(messages.getString("ViewImpl_ButtonExit_Text"));

    ArrayList<ButtonType> buttonTypes = new ArrayList<>();
    buttonTypes.add(backToMainMenu);
    if (controller.getCurrentGameMode() != GameMode.SPECTATOR) {
      buttonTypes.add(continueAgainstAi);
    }
    buttonTypes.add(exit);

    Alert alert =
        showOptionDialog(
            AlertType.INFORMATION,
            messages.getString("ViewImpl_ServerShutdown_Title"),
            messages.getString("ViewImpl_ServerShutdown_Subtitle"),
            messages.getString("ViewImpl_ServerShutdown_Info"),
            buttonTypes);

    Optional<ButtonType> optional = alert.showAndWait();
    if (optional.get().equals(backToMainMenu)) {
      returnToMainMenu();
    } else if (optional.get().equals(continueAgainstAi)) {
      controller.continueAgainstAi(model);
    } else if (optional.get().equals(exit)) {
      Platform.exit();
    } else {
      returnToMainMenu();
    }
  }

  private void handleRestartRequest() {
    if (!stage.isShowing()) {
      return;
    }
    ButtonType accept = new ButtonType(messages.getString("ViewImpl_ResetRequest_Accept"));
    ButtonType deny = new ButtonType(messages.getString("ViewImpl_ResetRequest_Deny"));

    ArrayList<ButtonType> buttonTypes = new ArrayList<>();
    buttonTypes.add(accept);
    buttonTypes.add(deny);

    root.setDisable(true);

    Alert alert =
        showOptionDialog(
            AlertType.INFORMATION,
            messages.getString("ViewImpl_ResetRequest_Received_Title"),
            messages.getString("ViewImpl_ResetRequest_Received_Subtitle"),
            messages.getString("ViewImpl_ResetRequest_Received_Info"),
            buttonTypes);

    Optional<ButtonType> optional = alert.showAndWait();
    if (optional.get().equals(accept)) {
      if (controller instanceof MultiplayerController) {
        ((MultiplayerController) controller).acceptGameRestart();
      }
    } else if (optional.get().equals(deny)) {
      if (controller instanceof MultiplayerController) {
        ((MultiplayerController) controller).denyGameRestart();
      }
    }

    root.setDisable(false);
  }

  private void handleLobbyClosed() {
    if (!stage.isShowing()) {
      return;
    }

    ButtonType backToMainMenu = new ButtonType(messages.getString("ViewImpl_ButtonBack_Text"));
    ButtonType continueAgainstAi =
        new ButtonType(messages.getString("ViewImpl_ButtonContinueAgainstAI_Text"));
    ButtonType exit = new ButtonType(messages.getString("ViewImpl_ButtonExit_Text"));

    ArrayList<ButtonType> buttonTypes = new ArrayList<>();
    buttonTypes.add(backToMainMenu);
    if (controller.getCurrentGameMode() != GameMode.SPECTATOR) {
      buttonTypes.add(continueAgainstAi);
    }
    buttonTypes.add(exit);

    Alert alert =
        showOptionDialog(
            AlertType.INFORMATION,
            messages.getString("ViewImpl_ServerShutdown_Title"),
            messages.getString("ViewImpl_ServerShutdown_Subtitle"),
            messages.getString("ViewImpl_ServerShutdown_Info"),
            buttonTypes);

    Optional<ButtonType> optional = alert.showAndWait();
    if (optional.get().equals(backToMainMenu)) {
      returnToMainMenu();
    } else if (optional.get().equals(continueAgainstAi)) {
      controller.continueAgainstAi(model);
    } else if (optional.get().equals(exit)) {
      Platform.exit();
    } else {
      returnToMainMenu();
    }
  }

  private void handleOnePlayerLeft() {
    if (!stage.isShowing()) {
      return;
    }
    Model copyModel = model;
    ButtonType backToMainMenu = new ButtonType(messages.getString("ViewImpl_ButtonBack_Text"));
    ButtonType continueAgainstAi =
        new ButtonType(messages.getString("ViewImpl_ButtonContinueAgainstAI_Text"));
    ButtonType remainAndWait =
        new ButtonType(messages.getString("ViewImpl_OpponentDisconnected_Remain"));

    ArrayList<ButtonType> buttonTypes = new ArrayList<>();
    buttonTypes.add(backToMainMenu);
    if (controller.getCurrentGameMode() != GameMode.SPECTATOR) {
      buttonTypes.add(continueAgainstAi);
    }
    buttonTypes.add(remainAndWait);
    ButtonType exit = new ButtonType(messages.getString("ViewImpl_ButtonExit_Text"));
    buttonTypes.add(exit);

    Alert alert =
        showOptionDialog(
            AlertType.INFORMATION,
            messages.getString("ViewImpl_OpponentDisconnected_Title"),
            messages.getString("ViewImpl_OpponentDisconnected_Subtitle"),
            messages.getString("ViewImpl_OpponentDisconnected_Info"),
            buttonTypes);

    Optional<ButtonType> optional = alert.showAndWait();
    if (optional.get().equals(backToMainMenu)) {
      returnToMainMenu();
    } else if (optional.get().equals(continueAgainstAi)) {
      controller.continueAgainstAi(copyModel);
    } else if (optional.get().equals(exit)) {
      Platform.exit();
    } else if (optional.get().equals(remainAndWait)) {
      return;
    } else {
      returnToMainMenu();
    }
  }

  @Override
  public void modelExchanged(Model model) {
    this.model = model;
    Platform.runLater(
        () -> {
          if (root.isDisable()) {
            root.setDisable(false);
          }
          support.firePropertyChange(Model.LISTENERS_CHANGED, null, model);

          showGame(controller.getCurrentGameMode());
        });
  }

  @Override
  public void receivedSpectator(Spectators spectators) {
    Platform.runLater(() -> support.firePropertyChange(View.SPECTATORS_CHANGED, null, spectators));
  }

  @Override
  public void showWaitingScreen() {
    root = new BorderPane();
    root.getStylesheets().add("cssFiles/mainGame.css");
    root.setId("main-background");
    root.setPadding(new Insets(50));

    Image loadingGif =
        new Image(getClass().getClassLoader().getResourceAsStream("images/LoadingGif.gif"));
    ImageView imageView = new ImageView(loadingGif);
    root.setCenter(imageView);

    Label waiting = new Label(messages.getString("ViewImpl_WaitForOpponent"));
    waiting.setFont(Font.font(20));
    waiting.setStyle("-fx-text-fill: white");
    root.setTop(waiting);
    BorderPane.setAlignment(waiting, Pos.CENTER);

    lobbyID = new Label(messages.getString("ViewImpl_LabelLobbyID"));
    lobbyID.setStyle("-fx-text-fill: white");
    root.setBottom(lobbyID);
    BorderPane.setAlignment(lobbyID, Pos.CENTER);

    scene = new Scene(root);
    stage.setScene(scene);
    if (!stage.isShowing()) {
      stage.show();
    }
  }

  private void showAlert(AlertType alertType, String title, String header, String content) {
    if (!stage.isShowing()) {
      return;
    }

    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.getDialogPane().setContent(new Label(content));
    alert.initOwner(stage);
    alert.showAndWait();
  }

  private Alert showOptionDialog(
      AlertType alertType,
      String title,
      String header,
      String content,
      ArrayList<ButtonType> buttonTypes) {

    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.getDialogPane().setContent(new Label(content));
    alert.initOwner(stage);
    alert.getButtonTypes().setAll(buttonTypes);

    return alert;
  }

  @Override
  public void handleConnectionError() {
    Platform.runLater(
        () -> {
          showAlert(
              AlertType.ERROR,
              messages.getString("ViewImpl_ConnectionError_Title"),
              messages.getString("ViewImpl_ConnectionError_Subtitle"),
              messages.getString("ViewImpl_ConnectionError_Info"));

          returnToMainMenu();
        });
  }
}
