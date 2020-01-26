package de.lmu.ifi.sosylab.fddlj.view;

import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class offers util methods needed upon creating the player objects needed for the game.
 *
 * @author Josef Feger
 */
public class PlayerCreationUtils {

  static final int[] gameFieldSizes = new int[] {8, 10, 12, 14, 16};
  static final String[] gameFieldSizeOptions =
      new String[] {"8x8", "10x10", "12x12", "14x14", "16x16"};

  private static final int PADDING_VBOX_TEXTFIELD = 15;
  private static final int MINWIDTH_TEXTFIELD = 200;
  private static final String CSS_TEXTFIELD_NORMAL =
      "-fx-border-color: transparent transparent #ffffff transparent;" + " -fx-border-width: 3";
  private static final String CSS_TEXTFIELD_SELECTED =
      "-fx-border-color: transparent transparent rgb(0,0,255) transparent; "
          + "-fx-border-width: 3";
  private static final int MAXWIDTH_TEXTFIELD_LABEL = 300;

  private static final int PREFHEIGHT_COLORPANE = 50;
  private static final int PREFWIDTH_COLORPANE = 50;

  private static final Font FONT_BUTTON = Font.font(18);
  private static final Cursor CURSOR_BUTTON = Cursor.HAND;
  private static final int MINWIDTH_BUTTON = 100;
  private static final int MAXWIDTH_BUTTON = 250;
  private static final int MINHEIGHT_BUTTON = 30;
  private static final int PREFWIDTH_BUTTON = 150;

  private static final double SIMILARITY_DISTANCE = 0.25;
  private static final int MAXHEIGHT_SEPARATOR = 450;

  /**
   * Returns a VBox containing a text field and a label with the given text.
   *
   * @param textField the text field object to use
   * @param labelText the text to display in the label
   * @return a vbox object containing the text field and a label
   */
  static VBox getInputField(TextField textField, String labelText) {
    VBox vbox = new VBox();
    vbox.setPadding(new Insets(PADDING_VBOX_TEXTFIELD));
    vbox.setAlignment(Pos.CENTER);

    textField.setMinWidth(MINWIDTH_TEXTFIELD);
    textField.setId("text-field");
    textField.setPrefWidth(200);
    textField.setMaxWidth(400);
    textField
        .focusedProperty()
        .addListener(
            new ChangeListener<Boolean>() {

              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {

                if (newValue) {
                  textField.setStyle(CSS_TEXTFIELD_SELECTED);
                } else {
                  textField.setStyle(CSS_TEXTFIELD_NORMAL);
                }
              }
            });

    Label lbl = new Label(labelText);
    lbl.setId("normal-text");
    lbl.setWrapText(true);
    lbl.setMaxWidth(MAXWIDTH_TEXTFIELD_LABEL);
    vbox.getChildren().addAll(lbl, textField);
    return vbox;
  }

  /**
   * Creates a pane in which the user can select and color and see the selected color.
   *
   * @param colorPicker the color picker object to use
   * @param messages the resource bundle object to use for predefined messages
   * @return a vbox containing the color picker and a label to show the selected color
   */
  static VBox getColorPickerPane(ColorPicker colorPicker, ResourceBundle messages) {
    VBox vbox = new VBox(5);
    vbox.setAlignment(Pos.CENTER);

    Pane color = new Pane();
    color.setId("color-pane");
    color.setStyle("-fx-background-color: " + toRgbCode(colorPicker.getValue()) + ";");
    color.setPrefHeight(PREFHEIGHT_COLORPANE);
    color.setMinHeight(PREFHEIGHT_COLORPANE);
    color.setMaxHeight(PREFHEIGHT_COLORPANE);
    color.setPrefWidth(PREFWIDTH_COLORPANE);
    color.setMinWidth(PREFWIDTH_COLORPANE);
    color.setMaxWidth(PREFWIDTH_COLORPANE);
    colorPicker.setOnAction(
        (ActionEvent t) -> {
          color.setStyle("-fx-background-color: " + toRgbCode(colorPicker.getValue()) + ";");
        });

    Label label = new Label(messages.getString("PlayerCreation_ChooseColor"));
    label.setId("normal-text");
    vbox.getChildren().addAll(label, color, colorPicker);

    return vbox;
  }

  /**
   * Converts a color to a rgb code display in web format.
   *
   * @param color the color to convert
   * @return a string representing the color in hex format.
   */
  static String toRgbCode(Color color) {

    return String.format(
        "#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }

  /**
   * Returns a vbox object containing a label and a choice box for selecting the game field size.
   *
   * @param choiceBox the choice box to use for selecting the game field size.
   * @param messages the resource bundle object to use for predefined messages
   * @return a vbox with a label and a choice box
   */
  static VBox getGameFieldSizeSelection(ChoiceBox<String> choiceBox, ResourceBundle messages) {
    VBox vbox = new VBox(5);
    vbox.setAlignment(Pos.CENTER);

    Label label = new Label(messages.getString("PlayerCreation_ChooseGameFieldSize"));
    label.setId("normal-text");
    vbox.getChildren().add(label);

    choiceBox.getSelectionModel().select(0);
    vbox.getChildren().add(choiceBox);

    return vbox;
  }

  /**
   * Returns an already styled button with the given text.
   *
   * @param text the button's text
   * @return a styled button
   */
  static Button getButton(String text) {
    Button button = new Button(text);
    button.setId("button");
    button.setMinHeight(MINHEIGHT_BUTTON);
    button.setMaxWidth(MAXWIDTH_BUTTON);
    button.setMinWidth(MINWIDTH_BUTTON);
    button.setPrefWidth(PREFWIDTH_BUTTON);
    button.setCursor(CURSOR_BUTTON);
    button.setFont(FONT_BUTTON);
    button.setEffect(new DropShadow(4, Color.DARKGRAY));

    return button;
  }

  /**
   * Checks if the entered string is a valid input.
   *
   * @param input the input to verify
   * @return {@code true} if the input is valid, otherwise {@code false}
   */
  static boolean isTextFieldInputValid(String input) {
    return input != null && !input.equals("");
  }

  /**
   * Checks whether two colors are similar to each other.
   *
   * @param c the first color
   * @param v the second color
   * @return {@code true} if they are similar to each other, otherwise {@code false}
   */
  static boolean similarTo(Color c, Color v) {
    double distance =
        Math.sqrt(
            (c.getRed() - v.getRed()) * (c.getRed() - v.getRed())
                + (c.getGreen() - v.getGreen()) * (c.getGreen() - v.getGreen())
                + (c.getBlue() - v.getBlue()) * (c.getBlue() - v.getBlue()));

    if (distance < SIMILARITY_DISTANCE) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns a random color.
   *
   * @return a random color
   */
  static Color getRandomColor() {
    return new Color(Math.random(), Math.random(), Math.random(), 1.0);
  }

  /**
   * Builds a new separator object.
   *
   * @return a separator object.
   */
  static Separator getSeparator() {
    Separator sep = new Separator(Orientation.VERTICAL);
    sep.setMaxHeight(MAXHEIGHT_SEPARATOR);

    return sep;
  }
}
