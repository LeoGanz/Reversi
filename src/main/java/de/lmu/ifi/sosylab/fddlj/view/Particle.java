package de.lmu.ifi.sosylab.fddlj.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * This class represents a single Particle used for the fireworks animation in the {@link
 * GameFinishedScreen}.
 */
public class Particle {
  private static final double GRAVITY = 0.06;
  // properties for animation
  // and colouring
  double alpha;
  final double easing;
  double fade;
  double posX;
  double posY;
  double velX;
  double velY;
  final double targetX;
  final double targetY;
  final Paint color;
  final int size;
  final boolean usePhysics;
  final boolean shouldExplodeChildren;
  final boolean hasTail;
  double lastPosX;
  double lastPosY;

  /**
   * Constructor of this class initilaises this particle.
   *
   * @param posX the particle's x position
   * @param posY the particle's y position
   * @param velX the particle's x velocity
   * @param velY the particle's y velocity
   * @param targetX the particle's target x position
   * @param targetY the particle's target y position
   * @param color the particle's color
   * @param size the particle's size
   * @param usePhysics indicates whether this particle should simulate physics
   * @param shouldExplodeChildren indicates whether this particle should explode other particles it
   *     comes into contact with
   * @param hasTail indicated whether the particle has a tail
   */
  public Particle(
      double posX,
      double posY,
      double velX,
      double velY,
      double targetX,
      double targetY,
      Paint color,
      int size,
      boolean usePhysics,
      boolean shouldExplodeChildren,
      boolean hasTail) {
    this.posX = posX;
    this.posY = posY;
    this.velX = velX;
    this.velY = velY;
    this.targetX = targetX;
    this.targetY = targetY;
    this.color = color;
    this.size = size;
    this.usePhysics = usePhysics;
    this.shouldExplodeChildren = shouldExplodeChildren;
    this.hasTail = hasTail;
    this.alpha = 1;
    this.easing = Math.random() * 0.02;
    this.fade = Math.random() * 0.1;
  }

  /**
   * Updates the particle.
   *
   * @return {@code true} if particle is still visible, otherwise {@code false}
   */
  public boolean update() {
    lastPosX = posX;
    lastPosY = posY;
    if (this.usePhysics) { // on way down
      velY += GRAVITY;
      posY += velY;
      this.alpha -= this.fade; // fade out particle
    } else { // on way up
      double distance = (targetY - posY);
      // ease the position
      posY += distance * (0.03 + easing);
      // cap to 1
      alpha = Math.min(distance * distance * 0.00005, 1);
    }
    posX += velX;
    return alpha < 0.005;
  }

  /**
   * Draws this particle with the specified {@link GraphicsContext}.
   *
   * @param context the graphics context object used for drawing the particle
   */
  public void draw(GraphicsContext context) {
    final double x = Math.round(posX);
    final double y = Math.round(posY);
    final double xVel = (x - lastPosX) * -5;
    final double yVel = (y - lastPosY) * -5;
    // set the opacity for all drawing of this particle
    context.setGlobalAlpha(Math.random() * this.alpha);
    // draw particle
    context.setFill(color);
    context.fillOval(x - size, y - size, size + size, size + size);
    // draw the arrow triangle from where we were to where we are now
    if (hasTail) {
      context.setFill(Color.rgb(255, 255, 255, 0.3));
      context.fillPolygon(
          new double[] {posX + 1.5, posX + xVel, posX - 1.5},
          new double[] {posY, posY + yVel, posY},
          3);
    }
  }
}
