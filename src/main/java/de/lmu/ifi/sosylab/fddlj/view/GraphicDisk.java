package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Disk;
import javafx.animation.FillTransition;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * This class is the graphical representation of the {@link Disk}.
 *
 * @author Josef Feger
 */
public class GraphicDisk extends Circle {

  private static final float PADDING = 10;

  private Light.Spot light;

  /**
   * Constructor of this class initialises variables and paints the disk.
   *
   * @param width the disk's parent's width in pixel
   * @param height the disk's parent's height in pixel
   * @param radius the disk's radius
   * @param color the disk's inital color
   */
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

  /**
   * Set the x value of the disk's spot light.
   *
   * @param x the spot light's x value
   */
  void setLightX(double x) {
    light.setX(x);
  }

  /**
   * Set the y value of the disk's spot light.
   *
   * @param y the spot light's y value
   */
  void setLightY(double y) {
    light.setY(y);
  }

  /**
   * Set the z value of the disk's spot light.
   *
   * @param z the spot light's z value
   */
  void setLightZ(double z) {
    light.setZ(z);
  }

  /**
   * Changes the disk's color with a {@link FillTransistion}.
   *
   * @param fill the disk's new color
   */
  void changeColor(Color fill) {
    if (!fill.equals(getFill())) {

      FillTransition ft = new FillTransition(Duration.millis(350), this, (Color) getFill(), fill);
      ft.setCycleCount(1);
      ft.setAutoReverse(false);

      ft.play();
      setFill(fill);
    }
  }
}
