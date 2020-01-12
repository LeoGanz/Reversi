package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.Disk;
import de.lmu.ifi.sosylab.fddlj.model.GameFieldImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameBoard extends Pane {

  private final Canvas canvas;

  private static final char START_LETTER = 'A';
  private static final int START_ROW = GameFieldImpl.SIZE;

  private static final float SPACING = 0f;
  private static final int PADDING_FOR_LETTERS = 30;
  private static final int DISK_BORDER_WIDTH = 2;
  private static final int OFFSET_SHADOW_X = 10;
  private static final int OFFSET_SHADOW_Y = 10;

  private static final Color SHADOW_COLOR = new Color(0, 0, 0, 1);
  private Font letterFont = new Font("Arial", 40);

  private int cellWidth;
  private int cellHeight;
  private float borderWidth;

  private Model model;
  private GameBoardMouseListener listener;

  /** @param model */
  public GameBoard(Model model, GameMode gameMode, Controller controller) {

    listener = new GameBoardMouseListener(gameMode, model, this, controller);
    canvas = new Canvas(getWidth(), getHeight());
    getChildren().add(canvas);
    widthProperty().addListener(e -> canvas.setWidth(getWidth()));
    heightProperty().addListener(e -> canvas.setHeight(getHeight()));

    addEventFilter(MouseEvent.MOUSE_MOVED, e -> listener.handleMouseMoved(e));
    addEventFilter(MouseEvent.MOUSE_CLICKED, e -> listener.handleMouseClicked(e));
    setOnScroll((ScrollEvent event) -> listener.handleScrollEvent(event));

    this.model = model;
  }

  @Override
  protected void layoutChildren() {
    super.layoutChildren();

    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.clearRect(0, 0, getWidth(), getHeight());

    drawGameField(gc);
    drawPlayers(gc);
    showPossibleMoves(gc);
    drawBoundaries(gc);
  }

  private void drawPlayers(GraphicsContext g) {

    Map<Cell, Player> allCellsOccupied = model.getState().getField().getCellsOccupiedWithDisks();
    for (Cell cell : allCellsOccupied.keySet()) {
      int startX = (int) (cell.getColumn() * (cellWidth + SPACING)) + getWidthOffsetForGameField();
      int startY = (int) (cell.getRow() * (cellHeight + SPACING)) + getHeightOffsetForGameField();

      Optional<Disk> optional = model.getState().getField().get(cell);
      if (optional.isPresent()) {
        int padding = ((cellWidth + cellHeight) / 2) / 6;
        drawDisk(optional.get().getPlayer(), g, padding, startX, startY, cellWidth, cellHeight);
      }
    }
  }

  /**
   * Draw a pawn on the current selected cell.
   *
   * @param player The player that owns the cell.
   * @param g The {@link GraphicsContext} object that allows to draw on the board.
   * @param padding Used to determine the gap-size between the cell and its border
   * @param x The coordinate marking the left point of the cell.
   * @param y The coordinate marking the upper point of the cell.
   * @param cellWidth The width of the cell.
   * @param cellHeight The height of the cell.
   */
  private void drawDisk(
      Player player, GraphicsContext g, int padding, int x, int y, int cellWidth, int cellHeight) {

    g.setFill(player.getColor());
    g.fillOval(x + padding, y + padding, cellWidth - 2 * padding, cellHeight - 2 * padding);

    g.setStroke(Color.BLACK);
    g.setLineWidth(DISK_BORDER_WIDTH);
    g.strokeOval(x + padding, y + padding, cellWidth - 2 * padding, cellHeight - 2 * padding);

    g.applyEffect(
        new DropShadow(cellWidth - 2 * padding, OFFSET_SHADOW_X, OFFSET_SHADOW_Y, SHADOW_COLOR));
  }

  /**
   * Draws the the entire game field with white and black cells and the pawn on a cell if available.
   *
   * @param g the <code>GraphicsContext</code> object used for drawing
   */
  private void drawGameField(GraphicsContext g) {
    for (int i = 0; i < GameFieldImpl.SIZE; i++) {
      for (int z = 0; z < GameFieldImpl.SIZE; z++) {
        // Rectangle represents one cell
        int startX = (int) (z * (cellWidth + SPACING)) + getWidthOffsetForGameField();
        int startY = (int) (i * (cellHeight + SPACING)) + getHeightOffsetForGameField();

        // Select the cells color
        if (i % 2 == 0 && z % 2 == 0) { // Used for even row and column
          g.setFill(Color.WHITE);
        } else if (i % 2 == 0 && z % 2 != 0) { // Used for even row and uneven column
          g.setFill(Color.DARKGRAY);
        } else if (i % 2 != 0 && z % 2 == 0) { // Used for uneven row and even columns
          g.setFill(Color.DARKGRAY);
        } else if (i % 2 != 0 && z % 2 != 0) { // used for uneven row and columns
          g.setFill(Color.WHITE);
        } else {
          g.setFill(Color.CYAN);
        }

        // Draw the cell
        g.fillRect(startX, startY, cellWidth, cellHeight);

        // Draw a border around the cell
        g.setStroke(Color.BLACK);
        g.setLineWidth(borderWidth);
        g.strokeRect(startX, startY, cellWidth, cellHeight);
      }

      g.setStroke(Color.WHITE);
      g.setFont(letterFont);

      String letter = "" + ((char) (START_LETTER + i));
      String row = "" + ((START_ROW) - i);
      int x =
          (int)
              ((i * (cellWidth + SPACING))
                  + getWidthOffsetForGameField()
                  + cellWidth / 2
                  - new Text(letter).getLayoutBounds().getWidth() / 2);
      int y =
          (int)
              ((i * (cellHeight + SPACING))
                  + getHeightOffsetForGameField()
                  + cellHeight / 2
                  + letterFont.getSize() / 4);
      g.strokeText(letter, x, getHeightOffsetForGameField() - PADDING_FOR_LETTERS);
      g.strokeText(row, getWidthOffsetForGameField() - 2 * PADDING_FOR_LETTERS, y);
    }
  }

  /**
   * Indicate the cells the user can possibly move to.
   *
   * @param g The <code>GraphicsContext</code> object used for drawing
   */
  private void showPossibleMoves(GraphicsContext g) {
    Set<Cell> cells =
        model.getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer());
    for (Cell cell : cells) {
      indicatePossibleMove(cell, g);
    }
  }

  /**
   * Draws a circle on the given cell to indicate the user can move the currently selected pawn to
   * this cell.
   *
   * @param cell the cell to which a move is possible
   * @param g The <code>GraphicsContext</code> object used for drawing
   */
  private void indicatePossibleMove(Cell cell, GraphicsContext g) {
    g.setFill(new Color(20, 220, 20, 100));
    int startX =
        (int) (cell.getColumn() * (cellWidth + SPACING))
            + getWidthOffsetForGameField()
            + cellWidth / 4;
    int startY =
        (int) (((GameFieldImpl.SIZE - 1) - cell.getRow()) * (cellHeight + SPACING))
            + getHeightOffsetForGameField()
            + cellHeight / 4;

    g.fillOval(startX, startY, cellWidth / 2, cellHeight / 2);
  }

  /**
   * Draws a border around the entire game field.
   *
   * @param g The <code>GraphicsContext</code> object used for drawing
   */
  private void drawBoundaries(GraphicsContext g) {
    g.setFill(Color.BLACK);
    g.setLineWidth(borderWidth);
    int width = getGameFieldWidth();
    int height = getGameFieldHeight();

    g.strokeRect(
        getWidthOffsetForGameField(),
        getHeightOffsetForGameField(),
        (int) (width + (borderWidth / 2)),
        (int) (height + (borderWidth / 2)));
  }

  /**
   * Calculates an offset for the game field in order to center the game field horizontally.
   *
   * @return the calculated width offset for the game field.
   */
  int getWidthOffsetForGameField() {
    return (int)
        (((getWidth() / 2) - (GameFieldImpl.SIZE / 2) * cellWidth)
            - ((GameFieldImpl.SIZE) / 2 + 1) * SPACING);
  }

  /**
   * Calculates an offset for the game field in order to center the game field vertically.
   *
   * @return the calculated height offset for the game field.
   */
  int getHeightOffsetForGameField() {
    return (int)
            (((getHeight() / 2) - (GameFieldImpl.SIZE / 2) * cellHeight)
                - ((GameFieldImpl.SIZE) / 2 + 1) * SPACING)
        + PADDING_FOR_LETTERS / 2;
  }

  /**
   * Returns the width of the game field in pixels.
   *
   * @return the game field's width in pixels
   */
  int getGameFieldWidth() {
    return (int) (GameFieldImpl.SIZE * (cellWidth + SPACING) + SPACING);
  }

  /**
   * Returns the height of the game field in pixels.
   *
   * @return the game field's height in pixels
   */
  int getGameFieldHeight() {
    return (int) (GameFieldImpl.SIZE * (cellHeight + SPACING) + SPACING);
  }

  int getCellWidth() {
    return cellWidth;
  }

  void setCellWidth(int cellWidth) {
    this.cellWidth = cellWidth;
  }

  int getCellHeight() {
    return cellHeight;
  }

  void setCellHeight(int cellHeight) {
    this.cellHeight = cellHeight;
  }

  float getSpacing() {
    return SPACING;
  }
}
