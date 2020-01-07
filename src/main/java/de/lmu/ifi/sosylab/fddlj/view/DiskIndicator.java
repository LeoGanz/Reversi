package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Model;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DiskIndicator extends BorderPane {

  private static final int DISK_RADIUS = 120;
  private static final int PADDING = 10;
  private static final int DISK_BORDER_WIDTH = 2;
  private static final int OFFSET_SHADOW_X = 10;
  private static final int OFFSET_SHADOW_Y = 10;

  private static final Color SHADOW_COLOR = new Color(0, 0, 0, 1);

  private Canvas canvas;
  private Label label;

  private Model model;

  public DiskIndicator(Model model, String labelText) {

    this.model = model;

    initLabel(labelText);
    initCanvas();

  }

  private void initLabel(String labelText) {
    label = new Label(labelText);
    label.setFont(Font.font(25));
    label.setStyle("-fx-text-fill: #ffffff");
    setTop(label);
    BorderPane.setAlignment(label, Pos.CENTER);
  }

  private void initCanvas() {
    canvas = new Canvas(getWidth(), getHeight());
    setCenter(canvas);
    BorderPane.setAlignment(canvas, Pos.CENTER);

    widthProperty().addListener(e -> canvas.setWidth(getWidth()));
    heightProperty().addListener(e -> canvas.setHeight(getHeight() - label.getHeight()));

  }

  @Override
  protected void layoutChildren() {
    super.layoutChildren();

    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());


    int x = (int) ((canvas.getWidth() - DISK_RADIUS) / 2 - 3 * PADDING);
    int y = (int) ((canvas.getHeight() - DISK_RADIUS) / 2 - 3 * PADDING);

    drawDisk(gc, x, y);


  }

  private void drawDisk(GraphicsContext g, int x, int y) {

    g.setFill(model.getState().getPlayerManagement().getPlayerOne().getColor());
    g.fillOval(x + PADDING, y + PADDING, DISK_RADIUS - 2 * PADDING, DISK_RADIUS - 2 * PADDING);

    g.setStroke(Color.BLACK);
    g.setLineWidth(DISK_BORDER_WIDTH);
    g.strokeOval(x + PADDING, y + PADDING, DISK_RADIUS - 2 * PADDING, DISK_RADIUS - 2 * PADDING);

    g.applyEffect(
        new DropShadow(DISK_RADIUS - 2 * PADDING, OFFSET_SHADOW_X, OFFSET_SHADOW_Y, SHADOW_COLOR));

  }
}
