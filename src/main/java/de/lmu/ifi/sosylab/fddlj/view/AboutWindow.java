package de.lmu.ifi.sosylab.fddlj.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This class displays a window in which the user can see additional, detailed information about
 * this game, such as the licenses and the game's rules.
 *
 * @author Josef Feger
 */
public class AboutWindow extends Stage {

  private ResourceBundle messages;

  private static final int PREFWIDTH_SCROLLPANE = 600;
  private static final int SPACING_VBOX_RULES = 15;
  private static final int LINESPACING_TEXTFLOW = 4;
  private static final Font FONT_TEXT_RULES = Font.font("Calibri", FontWeight.NORMAL, 18);

  /**
   * Public constructor of this class initialises stage and content.
   *
   * @param messages the ResourceBundle for the externalised strings
   */
  public AboutWindow(ResourceBundle messages) {
    super();

    this.messages = messages;

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

    setHeight(5 * Screen.getPrimary().getVisualBounds().getHeight() / 6);

    setScene(scene);
    setTitle(messages.getString("aboutWindow_Title"));
    centerOnScreen();
    show();
  }

  private Tab getLicenseTab() {

    Text text = new Text(readFile("files/NOTICE.md"));
    text.setFont(FONT_TEXT_RULES);
    text.setFill(Color.WHITE);

    TextFlow textFlow = new TextFlow();
    textFlow.getChildren().add(text);
    textFlow.setLineSpacing(LINESPACING_TEXTFLOW);
    textFlow.setTextAlignment(TextAlignment.CENTER);

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(textFlow);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefWidth(PREFWIDTH_SCROLLPANE);
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

    Tab tab = new Tab(messages.getString("aboutWindow_Tab_Licenses_Title"), scrollPane);
    tab.setClosable(false);

    return tab;
  }

  private Tab getRulesTab() {

    VBox vbox = new VBox(SPACING_VBOX_RULES);
    vbox.setAlignment(Pos.CENTER);
    vbox.getChildren().add(new SpecificRulesPane(messages));

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(vbox);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefWidth(PREFWIDTH_SCROLLPANE);
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

    Tab tab = new Tab(messages.getString("aboutWindow_Tab_Rules_Title"), scrollPane);
    tab.setClosable(false);

    return tab;
  }

  private String readFile(String filePath) {
    BufferedReader reader;
    try {
      reader =
          new BufferedReader(
              new InputStreamReader(
                  getClass().getClassLoader().getResourceAsStream(filePath), "UTF-8"));
      StringBuilder contentBuilder = new StringBuilder();

      Stream<String> stream = reader.lines();
      stream.forEach(s -> contentBuilder.append(s).append("\n"));

      reader.close();

      return contentBuilder.toString();
    } catch (UnsupportedEncodingException e1) {
      return messages.getString("aboutWindow_Rules_LicenseFile_ReadError");
    } catch (IOException e) {
      return messages.getString("aboutWindow_Rules_LicenseFile_ReadError");
    }
  }
}
