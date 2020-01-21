package de.lmu.ifi.sosylab.fddlj.view;

import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class offers a grid of cells in which each cell holds a disk.
 *
 * @author Josef Feger
 */
public class StartScreenDiskGrid extends BorderPane {

  private Color colorOne = Color.WHITE;
  private Color colorTwo = Color.BLACK;

  private ArrayList<GraphicDisk> animationStarter;

  private final int animationCycle = 800;
  private final int gridSize = 3;

  /** Public constructor of this class build a 2x2 grid with one disk placed on each cell. */
  public StartScreenDiskGrid() {
    animationStarter = new ArrayList<>();

    initGui();
    setEffect(new DropShadow(15, Color.BLACK));
  }

  private void initGui() {
    VBox container = new VBox();
    container.setAlignment(Pos.CENTER);

    for (int i = 0; i < gridSize; i++) {
      container.getChildren().add(buildRow((i % 2) == 0));
    }

    setCenter(container);

    Thread animationThread =
        new Thread(
            () -> {
              for (GraphicDisk disk : animationStarter) {
                if (((Color) disk.getFill()).equals(colorOne)) {
                  disk.changeColorInfinitely(colorTwo, animationStarter.size() * animationCycle);
                } else {
                  disk.changeColorInfinitely(colorOne, animationStarter.size() * animationCycle);
                }
                try {
                  Thread.sleep(animationCycle);
                } catch (InterruptedException e) {
                  continue;
                }
              }
            });

    animationThread.start();
  }

  private HBox buildRow(boolean top) {

    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER);

    for (int i = 0; i < gridSize; i++) {
      GraphicCell cell = new GraphicCell();
      Color color;
      if (top) {
        color = (i % 2 == 0) ? colorOne : colorTwo;
      } else {
        color = (i % 2 == 0) ? colorTwo : colorOne;
      }

      GraphicDisk disk =
          new GraphicDisk(
              cell.getPrefWidth(), cell.getPrefHeight(), (cell.getPrefHeight() - 20) / 2, color);
      cell.placeDisk(disk);
      animationStarter.add(disk);

      hbox.getChildren().add(cell);
    }

    return hbox;
  }
}
