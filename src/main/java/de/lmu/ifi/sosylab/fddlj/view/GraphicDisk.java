package de.lmu.ifi.sosylab.fddlj.view;

import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GraphicDisk extends Circle {

  private static final float PADDING = 10;

  private Light.Spot light;

  public GraphicDisk(double width, double height, double radius, Color color) {

    initDisk(width, height, radius, color);
  }

  private void initDisk(double width, double height, double radius, Color color) {

    setCenterX(width / 2);
    setCenterY(height / 2);
    setRadius(radius);
    setFill(color);

    light = new Light.Spot();
    light.setColor(Color.WHITE);
    light.setX(getCenterX() - radius / 2 + 3 * PADDING);
    light.setY(getCenterY() - radius / 2 + 3 * PADDING);
    light.setZ(1.15 * height);

    Lighting lighting = new Lighting();
    lighting.setLight(light);
    setEffect(lighting);
  }

  void setLightX(double x) {
    light.setX(x);
  }

  void setLightY(double y) {
    light.setY(y);
  }

  void setLightZ(double z) {
    light.setZ(z);
  }
}
