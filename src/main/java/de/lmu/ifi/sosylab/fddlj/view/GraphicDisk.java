package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Disk;
import javafx.animation.Animation;
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

  static final float PADDING = 10;
  static final float SCALE = 1.2f;

  private Light.Spot light;

  /**
   * Constructor of this class initialises variables and paints the disk.
   *
   * @param width the disk's parent's width in pixel
   * @param height the disk's parent's height in pixel
   * @param radius the disk's radius
   * @param color the disk's initial color
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
    light.setX(getCenterX() - radius / 2 + 1.5 * PADDING);
    light.setY(getCenterY() - radius / 2 + 1.5 * PADDING);
    light.setZ(SCALE * height);
    light.setPointsAtX(getCenterX());
    light.setPointsAtY(getCenterY());
    light.setPointsAtZ(0);
    light.setSpecularExponent(2.0);

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

  /**
   * Changes the disk's color from the current fill to the new fill infinitely with a {@link
   * FillTransistion}.
   *
   * @param fill the new color for the fill transition.
   * @param duration time in milliseconds how long the transition should play
   */
  void changeColorInfinitely(Color fill, long duration) {
    if (!fill.equals(getFill())) {

      FillTransition ft =
          new FillTransition(Duration.millis(duration), this, (Color) getFill(), fill);
      ft.setCycleCount(Animation.INDEFINITE);
      ft.setAutoReverse(true);

      ft.play();
    }
  }

  /**
   * Resizes the disk to the new coordinates.
   *
   * @param width the parent's new width
   * @param height the parent's new height
   * @param radius the disk's new radius
   */
  void resizeDisk(double width, double height, double radius) {
    initDisk(width, height, radius, (Color) getFill());
  }
}
