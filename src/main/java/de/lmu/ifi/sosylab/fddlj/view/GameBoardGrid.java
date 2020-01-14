package de.lmu.ifi.sosylab.fddlj.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.lmu.ifi.sosylab.fddlj.model.GameFieldImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameBoardGrid extends BorderPane implements PropertyChangeListener{

    private Model model;
    private GameMode gameMode;
    private Controller controller;

    private SwitchButton switchButton;
    private Label numberPlayerOneDisks;
    private Label numberPlayerTwoDisks;

    private char startLetter = 'A';

    public GameBoardGrid(Model model, GameMode gameMode, Controller controller, Stage stage) {
	super();

	this.model = model;
	this.gameMode = gameMode;
	this.controller = controller;

	model.addListener(this);

	initToggleSwitch();
	initGameBoard(stage);
	initDiskCounter();
    }

    private void initGameBoard(Stage stage) {

	GridPane grid = new GridPane();

	grid.setStyle("-fx-background-color: transparent;");
	grid.setGridLinesVisible(false);
	grid.setAlignment(Pos.CENTER);

	for (int column = 1; column < GameFieldImpl.SIZE + 1; column++) {
	    for (int row = 1; row < GameFieldImpl.SIZE + 1; row++) {

		GraphicCell cell = new GraphicCell(column - 1, row - 1, this, model, controller);
		grid.add(cell, column, row);
		switchButton.addListener(cell);
		model.addListener(cell);
	    }
	}

	for (int i = 1; i < GameFieldImpl.SIZE + 1; i++) {
	    Label letter = new Label(String.valueOf((char) (startLetter + i - 1)));
	    letter.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white");

	    Label rowNumber = new Label(String.valueOf(i));
	    rowNumber.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: white");
	    grid.add(letter, i, 0);
	    grid.add(rowNumber, 0, i);

	    GridPane.setHalignment(letter, HPos.CENTER);
	    GridPane.setValignment(rowNumber, VPos.CENTER);
	    GridPane.setMargin(letter, new Insets(5, 0, 20, 0));
	    GridPane.setMargin(rowNumber, new Insets(0, 20, 0, 5));
	}

	setCenter(grid);
    }

    private void initToggleSwitch() {
	BorderPane borderPane = new BorderPane();

	Label title = getLabel("Hints anzeigen?");
	borderPane.setTop(title);
	BorderPane.setAlignment(title, Pos.CENTER);

	HBox hbox = new HBox(15);
	hbox.setAlignment(Pos.CENTER);

	Label no = getLabel("Nein");
	Label yes = getLabel("Ja");
	switchButton = new SwitchButton(100, 15);

	hbox.getChildren().addAll(no, switchButton, yes);
	borderPane.setCenter(hbox);
	BorderPane.setAlignment(hbox, Pos.CENTER);
	BorderPane.setMargin(hbox, new Insets(10, 0, 0, 0));

	setBottom(borderPane);
	BorderPane.setMargin(borderPane, new Insets(30, 0, 0, 0));
    }

    private void initDiskCounter() {
	HBox hbox = new HBox(100);
	hbox.setAlignment(Pos.CENTER);

	if (model instanceof ModelImpl) {
	    ModelImpl mod = (ModelImpl) model;
	    
	    HBox playerOneInfo = new HBox(10);
	    playerOneInfo.setAlignment(Pos.CENTER);

	    playerOneInfo.getChildren().add(buildDiskTriangle(model.getState().getPlayerManagement().getPlayerOne().getColor()));
	    numberPlayerOneDisks = new Label(String.valueOf(mod.getNumberOfDisksPlayerOne()));
	    numberPlayerOneDisks.setFont(Font.font("Boulder", FontWeight.BOLD, 25));
	    numberPlayerOneDisks.setStyle("-fx-text-fill: white;");
	    playerOneInfo.getChildren().add(numberPlayerOneDisks);
	    hbox.getChildren().add(playerOneInfo);

	    HBox playerTwoInfo = new HBox(10);
	    playerTwoInfo.setAlignment(Pos.CENTER);
	    
	    numberPlayerTwoDisks = new Label(String.valueOf(mod.getNumberOfDisksPlayerTwo()));
	    numberPlayerTwoDisks.setFont(Font.font("Boulder", FontWeight.BOLD, 25));
	    playerTwoInfo.getChildren().add(numberPlayerTwoDisks);
	    playerTwoInfo.getChildren().add(buildDiskTriangle(model.getState().getPlayerManagement().getPlayerTwo().getColor()));
	    hbox.getChildren().add(playerTwoInfo);
	}

	setTop(hbox);
	BorderPane.setMargin(hbox, new Insets(0, 0, 30, 0));
    }

    private GridPane buildDiskTriangle(Color color) {
	GridPane grid = new GridPane();

	grid.setStyle("-fx-background-color: transparent;");
	grid.setGridLinesVisible(false);
	grid.setAlignment(Pos.CENTER);

	GraphicDisk diskOne = new GraphicDisk(20, 20, 10, color);
	GraphicDisk diskTwo = new GraphicDisk(20, 20, 10, color);
	GraphicDisk diskThree = new GraphicDisk(20, 20, 10, color);
	GraphicDisk diskFour = new GraphicDisk(20, 20, 10, color);

	grid.add(diskOne, 1, 0);
	grid.add(diskTwo, 0, 1);
	grid.add(diskThree, 1, 1);
	grid.add(diskFour, 2, 1);

	return grid;
    }

    private Label getLabel(String text) {
	Label label = new Label(text);
	label.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
	return label;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	Platform.runLater(() -> {
	    if (model instanceof ModelImpl) {
		ModelImpl mod = (ModelImpl) model;

		numberPlayerOneDisks.setText(String.valueOf(mod.getNumberOfDisksPlayerOne()));

		numberPlayerTwoDisks.setText(String.valueOf(mod.getNumberOfDisksPlayerTwo()));
	    }});

    }
}
