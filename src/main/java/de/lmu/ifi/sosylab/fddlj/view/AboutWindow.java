package de.lmu.ifi.sosylab.fddlj.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
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

  private ResourceBundle messages;

  /**
   * Public constructor of this class initialises stage and content.
   *
   * @param locale the Locale to use for output text
   */
  public AboutWindow(Locale locale) {
    super();

    messages = ResourceBundle.getBundle("MessagesBundle", locale);

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
    setTitle(messages.getString("aboutWindow_Title"));
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

    Tab tab = new Tab(messages.getString("aboutWindow_Tab_Licenses_Title"), scrollPane);
    tab.setClosable(false);

    return tab;
  }

  private Tab getRulesTab() {

    TextArea textArea = new TextArea();
    textArea.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 3);
    textArea.setWrapText(true);
    textArea.setFont(Font.font(16));
    textArea.setText(readFile("files/rules.txt"));

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(textArea);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefWidth(600);

    Tab tab = new Tab(messages.getString("aboutWindow_Tab_Rules_Title"), scrollPane);
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
}
