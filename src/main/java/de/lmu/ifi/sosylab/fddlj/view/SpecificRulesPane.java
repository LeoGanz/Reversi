package de.lmu.ifi.sosylab.fddlj.view;

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

  // private ResourceBundle messages;

  private String gameStart =
      "In den ersten zwei Z�gen setzen beide Spieler"
          + " jeweils einen ihrer Steine auf die vier Mittelfelder."
          + " Es k�nnen deshalb zwei 'Startaufstellungen' entstehen,"
          + " wobei der zweite Spieler sich diese de facto aussuchen darf. "
          + "Danach ziehen die Spieler abwechselnd. \n "
          + "Im folgenden sehen Sie beide Startvariationen:";

  private String placementRules =
      "Man darf nur so setzen, dass ausgehend von dem gesetzten Stein "
          + "in beliebiger Richtung (senkrecht, waagerecht oder diagonal)"
          + " ein oder mehrere gegnerische Steine anschlie�en und danach"
          + " wieder ein eigener Stein liegt. Es muss also mindestens ein"
          + " gegnerischer Stein von dem gesetzten Stein und einem anderen"
          + " eigenen Stein in gerader Linie eingeschlossen werden. Dabei"
          + " m�ssen alle Felder zwischen den beiden eigenen Steinen von"
          + " gegnerischen Steinen besetzt sein. Im folgenden Bild ist Spieler"
          + " 'schwarz' am Zug und die gr�nen Punkte markieren die Zellen auf"
          + " die er seinen Stein platzieren darf:";

  private String possibleMovesExample =
      "Alle gegnerischen Steine, die so eingeschlossen"
          + " werden, wechseln die Farbe, indem sie umgedreht werden."
          + "Dies geschieht als Teil desselben Zuges, bevor der Gegne"
          + "r zum Zug kommt. Ein Zug kann mehrere Reihen gegnerischer"
          + " Steine gleichzeitig einschlie�en, die dann alle umgedreht"
          + " werden. Wenn aber ein gerade umgedrehter Stein weitere"
          + " gegnerische Steine einschlie�t, werden diese nicht umgedreht."
          + " Im folgenden Bild sieht man wie Spieler 'schwarz' aus dem"
          + " obigen Bild seinen Zug get�tigt hat, in dem er seinen schwarzen"
          + " Stein rechts oben platzierte:";

  private String gameEndingText =
      "Sobald ein Spieler passen muss, wenn er also keinen Stein mehr setzen"
          + " kann, ist das Spiel beendet. Andernfalls ist das Spiel beendet,"
          + " sobald alle Steine auf dem Spielfeld platziert sind. \n"
          + "Der Spieler, der am Ende die meisten Steine seiner Farbe auf dem"
          + " Brett hat, gewinnt. Haben beide die gleiche Zahl, ist das Spiel"
          + " unentschieden.";

  /** Constructor of this class initialises the pane. */
  public SpecificRulesPane(/*ResourceBundle messages*/) {
    super(50);
    // this.messages = messages;

    setAlignment(Pos.TOP_CENTER);
    getChildren().add(getGameFieldInformation());
    getChildren().add(getFirstMovesInformation());
    getChildren().add(getGameStartInformation());
    getChildren().add(getPlacementRulesInformation());
    getChildren().add(getGameEndingInformation());
  }

  private VBox getGameFieldInformation() {
    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label("Spielfeld");
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren()
        .add(getTextFlow(getText("Es wird auf einem Brett mit 8�8 Feldern gespielt.")));

    Image gameField =
        new Image(getClass().getClassLoader().getResourceAsStream("images/gameField.png"));
    ImageView imageView = new ImageView(gameField);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(IMAGE_HEIGHT);
    vbox.getChildren().add(imageView);

    return vbox;
  }

  private VBox getFirstMovesInformation() {

    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label("Steinanzahl");
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren()
        .add(
            getTextFlow(
                getText(
                    "Jeder Spieler hat am Anfang einen Vorrat von 32 Steinen. "
                        + "Dies wird w�hrend dem Spiel auf der linken Seite wie"
                        + " folgt angezeigt:")));

    Image diskIndicator =
        new Image(getClass().getClassLoader().getResourceAsStream("images/DiskIndicator.png"));
    ImageView imageView = new ImageView(diskIndicator);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(IMAGE_HEIGHT);
    vbox.getChildren().add(imageView);

    return vbox;
  }

  private VBox getGameStartInformation() {
    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label("Spielbeginn");
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren().add(getTextFlow(getText(gameStart)));

    HBox hbox = new HBox(20);
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
    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label("Platzierungsregeln");
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren().add(getTextFlow(getText(placementRules)));

    Image possibleMoves =
        new Image(getClass().getClassLoader().getResourceAsStream("images/possibleMoves.png"));
    ImageView imageView = new ImageView(possibleMoves);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(IMAGE_HEIGHT);
    vbox.getChildren().add(imageView);

    vbox.getChildren().add(getTextFlow(getText(possibleMovesExample)));

    Image moveDone =
        new Image(getClass().getClassLoader().getResourceAsStream("images/moveDone.png"));
    ImageView imageViewTwo = new ImageView(moveDone);
    imageViewTwo.setPreserveRatio(true);
    imageViewTwo.setFitHeight(IMAGE_HEIGHT);
    vbox.getChildren().add(imageViewTwo);

    return vbox;
  }

  private VBox getGameEndingInformation() {
    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label("Spielende");
    heading.setId("heading");
    vbox.getChildren().add(heading);

    vbox.getChildren().add(getTextFlow(getText(gameEndingText)));

    return vbox;
  }

  private TextFlow getTextFlow(Text text) {
    TextFlow textFlow = new TextFlow();
    textFlow.getChildren().add(text);
    textFlow.setLineSpacing(4);
    textFlow.setTextAlignment(TextAlignment.CENTER);

    return textFlow;
  }

  private Text getText(String input) {
    Text text = new Text(input);
    text.setFont(Font.font("Calibri", FontWeight.NORMAL, 18));
    text.setFill(Color.WHITE);

    return text;
  }
}
