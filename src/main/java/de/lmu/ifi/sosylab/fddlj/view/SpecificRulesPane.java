package de.lmu.ifi.sosylab.fddlj.view;

import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 * This class offers a pane in which the rules of the reversi game are explained in specific way,
 * supported by graphical examples.
 *
 * @author Josef Feger
 */
public class SpecificRulesPane extends VBox {

  private static final int IMAGE_HEIGHT = 260;
  private final Font textFont = Font.font("Calibri", FontWeight.NORMAL, 18);
  private static final int VBOX_SPACING = 10;
  private static final int HBOX_SPACING = 20;
  private static final int LINESPACING_TEXTFLOW = 4;

  private ResourceBundle messages;

  /**
   * Constructor of this class initialises the pane.
   *
   * @param messages the ResourceBundle for the externalised strings
   */
  public SpecificRulesPane(ResourceBundle messages) {
    super(50);
    this.messages = messages;

    setAlignment(Pos.TOP_CENTER);
    getChildren().add(getGeneralRules());
    getChildren().add(getGameFieldInformation());
    getChildren().add(getFirstMovesInformation());
    getChildren().add(getGameStartInformation());
    getChildren().add(getPlacementRulesInformation());
    getChildren().add(getGameEndingInformation());
  }

  private VBox getGeneralRules() {
    VBox vbox = new VBox(VBOX_SPACING);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label(messages.getString("aboutWindow_Rules_General_Heading"));
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren().add(getTextFlow(getText(messages.getString("aboutWindow_Rules_General"))));

    return vbox;
  }

  private VBox getGameFieldInformation() {
    VBox vbox = new VBox(VBOX_SPACING);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label(messages.getString("aboutWindow_Rules_GameField"));
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren()
        .add(getTextFlow(getText(messages.getString("aboutWindow_Rules_GameField_Text"))));

    Image gameField =
        new Image(getClass().getClassLoader().getResourceAsStream("images/gameField.png"));
    ImageView imageView = new ImageView(gameField);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(IMAGE_HEIGHT);
    vbox.getChildren().add(imageView);

    return vbox;
  }

  private VBox getFirstMovesInformation() {

    VBox vbox = new VBox(VBOX_SPACING);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label(messages.getString("aboutWindow_Rules_DiskNumber"));
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren()
        .add(getTextFlow(getText(messages.getString("aboutWindow_Rules_DiskNumber_Text"))));

    Image diskIndicator =
        new Image(getClass().getClassLoader().getResourceAsStream("images/DiskIndicator.png"));
    ImageView imageView = new ImageView(diskIndicator);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(IMAGE_HEIGHT);
    vbox.getChildren().add(imageView);

    return vbox;
  }

  private VBox getGameStartInformation() {
    VBox vbox = new VBox(VBOX_SPACING);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label(messages.getString("aboutWindow_Rules_GameStart"));
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren().add(getTextFlow(getText(messages.getString("aboutWindow_Rules_GameStart"))));

    HBox hbox = new HBox(HBOX_SPACING);
    hbox.setAlignment(Pos.CENTER);

    Image versOne =
        new Image(getClass().getClassLoader().getResourceAsStream("images/start_vers1.png"));
    ImageView imageViewOne = new ImageView(versOne);
    imageViewOne.setPreserveRatio(true);
    imageViewOne.setFitHeight(IMAGE_HEIGHT);

    Image versTwo =
        new Image(getClass().getClassLoader().getResourceAsStream("images/start_vers2.png"));
    ImageView imageViewTwo = new ImageView(versTwo);
    imageViewTwo.setPreserveRatio(true);
    imageViewTwo.setFitHeight(IMAGE_HEIGHT);

    hbox.getChildren().addAll(imageViewOne, imageViewTwo);
    vbox.getChildren().add(hbox);

    return vbox;
  }

  private VBox getPlacementRulesInformation() {
    VBox vbox = new VBox(VBOX_SPACING);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label(messages.getString("aboutWindow_Rules_PossibleMoves"));
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren()
        .add(getTextFlow(getText(messages.getString("aboutWindow_Rules_PossibleMoves_Text"))));

    Image possibleMoves =
        new Image(getClass().getClassLoader().getResourceAsStream("images/possibleMoves.png"));
    ImageView imageView = new ImageView(possibleMoves);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(IMAGE_HEIGHT);
    vbox.getChildren().add(imageView);

    vbox.getChildren()
        .add(getTextFlow(getText(messages.getString("aboutWindow_Rules_PossibleMoves_Example"))));

    Image moveDone =
        new Image(getClass().getClassLoader().getResourceAsStream("images/moveDone.png"));
    ImageView imageViewTwo = new ImageView(moveDone);
    imageViewTwo.setPreserveRatio(true);
    imageViewTwo.setFitHeight(IMAGE_HEIGHT);
    vbox.getChildren().add(imageViewTwo);

    return vbox;
  }

  private VBox getGameEndingInformation() {
    VBox vbox = new VBox(VBOX_SPACING);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label(messages.getString("aboutWindow_Rules_GameEnding"));
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren()
        .add(getTextFlow(getText(messages.getString("aboutWindow_Rules_GameEnding_Text"))));

    return vbox;
  }

  private TextFlow getTextFlow(Text text) {
    TextFlow textFlow = new TextFlow();
    textFlow.getChildren().add(text);
    textFlow.setLineSpacing(LINESPACING_TEXTFLOW);
    textFlow.setTextAlignment(TextAlignment.CENTER);

    return textFlow;
  }

  private Text getText(String input) {
    Text text = new Text(input);
    text.setFont(textFont);
    text.setFill(Color.WHITE);

    return text;
  }
}
