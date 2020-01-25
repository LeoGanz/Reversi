package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This class offers a grid of cells that represents a game board.
 *
 * @author Josef Feger
 */
public class GameBoardGrid extends BorderPane implements PropertyChangeListener {

  private Model model;
  private Controller controller;

  private SwitchButton switchButton;

  private boolean playSound;
  private float volume;

  private ResourceBundle messages;

  /**
   * Constructor of this class initialises variables and is responsible for building GUI elements.
   *
   * @param model a reference to a model instance
   * @param controller a reference to a controller instance
   * @param stage a reference to the game's main stage
   * @param view a reference to a view instance
   * @param messages the ResourceBundle for the externalised strings
   */
  public GameBoardGrid(
      Model model, Controller controller, Stage stage, View view, ResourceBundle messages) {
    super();

    this.model = model;
    this.controller = controller;

    this.playSound = true;
    this.volume = 0.6f;

    view.addListener(this);
    this.messages = messages;

    getStyleClass().add("transparent");
    initToggleSwitch();
    initGameBoard(stage, view);
    setMinWidth(model.getState().getField().getSize() * GraphicCell.MIN_WIDTH);
    setMinHeight(
        model.getState().getField().getSize() * GraphicCell.MIN_HEIGHT + switchButton.getHeight());

    new Thread(() -> initialiseAudioSystem()).start();
  }

  private void initGameBoard(Stage stage, View view) {

    VBox centerGrid = new VBox();
    centerGrid.setAlignment(Pos.CENTER);
    centerGrid.setEffect(new DropShadow(10, 5, 5, Color.BLACK));
    for (int row = 1; row < model.getState().getField().getSize() + 1; row++) {

      HBox hboxRow = new HBox();
      hboxRow.setAlignment(Pos.CENTER);

      for (int column = 1; column < model.getState().getField().getSize() + 1; column++) {

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

    Label hints = getLabel(messages.getString("GameBoardGrid_ShowHints_Label"));
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
    label.setId("gameBoardGrid-label");
    return label;
  }

  void playPlacementSound() {

    if (!playSound) {
      return;
    }

    new Thread(
        () -> {
          try {
            InputStream isAudioFile =
                getClass().getClassLoader().getResourceAsStream("audio/placement.wav");
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream =
                AudioSystem.getAudioInputStream(new BufferedInputStream(isAudioFile));
            clip.open(inputStream);
            FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
            clip.start();
          } catch (RuntimeException e) {
            throw e;
          } catch (Exception e) {
            e.printStackTrace();
            // throw new MissingResourceException(
            // "Missing a resource", "GameBoardGrid", "placement.wav");
          }
        })
        .start();
  }

  /**
   * This method seems to be needed in order to initialise the OS's audio system because otherwise
   * the first time playing the clip takes a really long time.
   */
  private void initialiseAudioSystem() {

    try {
      InputStream isAudioFile =
          getClass().getClassLoader().getResourceAsStream("audio/placement.wav");
      Clip clip = AudioSystem.getClip();
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(isAudioFile);
      clip.open(inputStream);
    } catch (LineUnavailableException e) {
      return;
    } catch (UnsupportedAudioFileException e) {
      return;
    } catch (IOException e) {
      return;
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Platform.runLater(
        () -> {
          if (evt.getPropertyName().equals(Model.LISTENERS_CHANGED)) {

            if (evt.getNewValue() instanceof Model) {

              this.model = (Model) evt.getNewValue();
            }
          } else if (evt.getPropertyName().equals(ViewImpl.SOUND_MODE_CHANGED)) {
            if (evt.getNewValue() instanceof Boolean) {
              this.playSound = (boolean) evt.getNewValue();
            }
          } else if (evt.getPropertyName().equals(ViewImpl.VOLUME_CHANGED)) {
            if (evt.getNewValue() instanceof Float) {
              this.volume = (float) evt.getNewValue();
            }
          }
        });
  }
}
