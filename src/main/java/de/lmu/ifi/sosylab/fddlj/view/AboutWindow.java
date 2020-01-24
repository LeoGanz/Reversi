package de.lmu.ifi.sosylab.fddlj.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This class displays a window in which the user can see additional, detailed information about
 * this game, such as the licenses and the game's rules.
 *
 * @author Josef Feger
 */
public class AboutWindow extends Stage {

  //private ResourceBundle messages;

  private String rulesGeneral =
      "Auf einem quadratischen Brett legen die Spieler "
          + "abwechselnd Spielsteine, deren Seiten unterschiedlich "
          + " (z.B. schwarz und weiß) gefärbt sind. Ein Spieler ('Weiß') legt seinen"
          + " Stein immer mit der weißen "
          + "Seite nach oben, der andere ('Schwarz') entsprechend mit der Schwarzen. "
          + "Ein Spieler muss seinen Stein auf ein leeres Feld legen, das horizontal, "
          + "vertikal oder diagonal"
          + " an ein bereits belegtes Feld angrenzt. Wird ein Stein gelegt, werden alle "
          + "gegnerischen Steine, die sich zwischen dem neuen Spielstein und einem bereits "
          + "gelegten Stein der eigenen Farbe befinden, umgedreht. Spielzüge, die zu keinem"
          + " Umdrehen von gegnerischen Steinen führen, sind nicht erlaubt. Das Ziel des "
          + "Spiels ist es, am Ende eine möglichst große Anzahl von Steinen der eigenen "
          + "Farbe auf dem Brett zu haben.";

  /**
   * Public constructor of this class initialises stage and content.
   *
   */
  public AboutWindow(/*ResourceBundle messages*/) {
    super();

    //this.messages = messages;

    initWindow();
  }

  private void initWindow() {
    TabPane tabPane = new TabPane();

    Tab tabRules = getRulesTab();
    Tab tabLicenses = getLicenseTab();

    tabPane.getTabs().add(tabRules);
    tabPane.getTabs().add(tabLicenses);

    BorderPane root = new BorderPane();
    root.setCenter(tabPane);
    root.getStylesheets().add("cssFiles/about.css");
    Scene scene = new Scene(root);

    setWidth(Screen.getPrimary().getVisualBounds().getWidth() / 4);
    setHeight(5 * Screen.getPrimary().getVisualBounds().getHeight() / 6);

    setScene(scene);
    setTitle("Über");
    centerOnScreen();
    show();
  }

  private Tab getLicenseTab() {
    TextArea textArea = new TextArea();
    textArea.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 3);
    textArea.setWrapText(true);
    textArea.setFont(Font.font(16));
    textArea.setText(readFile("files/NOTICE.md"));

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(textArea);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefWidth(600);

    Tab tab = new Tab("Lizenzen", scrollPane);
    tab.setClosable(false);

    return tab;
  }

  private Tab getRulesTab() {

    TextArea textArea = new TextArea();
    textArea.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 3);
    textArea.setWrapText(true);
    textArea.setFont(Font.font(16));
    textArea.setText(readFile("files/rules.txt"));

    VBox vbox = new VBox(15);
    vbox.setAlignment(Pos.CENTER);
    vbox.getChildren().add(getGeneralRules());
    vbox.getChildren().add(new SpecificRulesPane());

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(vbox);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefWidth(600);

    Tab tab = new Tab("Regeln", scrollPane);
    tab.setClosable(false);

    return tab;
  }

  private String readFile(String filePath) {
    BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath)));

    StringBuilder contentBuilder = new StringBuilder();

    Stream<String> stream = reader.lines();
    stream.forEach(s -> contentBuilder.append(s).append("\n"));

    return contentBuilder.toString();
  }

  private VBox getGeneralRules() {
    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.CENTER);

    Label heading = new Label("Allgemein");
    heading.setId("heading");
    vbox.getChildren().add(heading);

    TextArea textArea = new TextArea();
    textArea.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 3);
    textArea.setWrapText(true);
    textArea.setFont(Font.font("Calibri", 16));
    textArea.setText(rulesGeneral);
    vbox.getChildren().add(textArea);

    return vbox;
  }
}
