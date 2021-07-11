package minesweaper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Minesweeper extends Application {

    private int moves;
    private int flags = 10;
    private int bombsFound;
    private Label movesLabel;
    private Label flagsLabel;
    private Label bombsLabel;


    public void start(Stage stage) {
        BorderPane bp = new BorderPane();
        TilePane tp = createTilePane(stage);
        tp.setPrefSize(500, 500);

        HBox hBox = new HBox();
        movesLabel = new Label("Moves: " + moves);
        flagsLabel = new Label("Flags: " + flags);
        bombsLabel = new Label("Bombs detected: " + bombsFound);
        hBox.getChildren().addAll(movesLabel, flagsLabel, bombsLabel);
        hBox.setSpacing(30);
        bp.setBottom(hBox);

        bp.setCenter(tp);
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }


    public TilePane createTilePane(Stage stage) {
        TilePane tp = new TilePane();
        List<Plate> plates = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Plate plate = new Plate();
            plate.setPrefSize(50, 50);
            plate.setOnMouseClicked(event -> onPlateClicked(stage, event, plates, plate));
            tp.getChildren().add(plate);
            plates.add(plate);
        }
        addBombs(plates);
        addNeighbors(plates);
        addNumbers(plates);
        return tp;
    }

    public void onPlateClicked(Stage stage, MouseEvent event, List<Plate> plates, Plate plate) {
        moves++;
        movesLabel.setText("Moves: " + moves);

        if (event.getButton() == MouseButton.PRIMARY) {
            if (!plate.isFlagged()) {
                if (plate.HasBomb()) {
                    for (Plate p : plates) {
                        if (p.HasBomb()) {
                            p.setText("BOMB");
                        }
                    }
                    lose(stage);
                } else if (plate.getNumOfBombs() == 0) {
                    plate.setText("0");
                    for (Plate pl : plate.getNeighbors()) {
                        if (!pl.isFlagged()) {
                            pl.setText(String.valueOf(pl.getNumOfBombs()));
                        }
                    }
                } else {
                    plate.setText(String.valueOf(plate.getNumOfBombs()));
                }
            }
        }

        if (event.getButton() == MouseButton.SECONDARY) {
            if (plate.isFlagged()) {
                plate.setText("");
                plate.setUnflagged();
                flags++;
                flagsLabel.setText("Flags: " + flags);
                if (plate.HasBomb()) {
                    bombsFound--;
                    bombsLabel.setText("Bombs detected: " + bombsFound);
                }
            } else if (plate.getText().isEmpty() && !plate.isFlagged()) {
                if (flags > 0) {
                    plate.setText("FLAG");
                    plate.setFlagged();
                    flags--;
                    flagsLabel.setText("Flags: " + flags);
                    if (plate.HasBomb()) {
                        bombsFound++;
                        bombsLabel.setText("Bombs detected: " + bombsFound);
                        if (bombsFound == 10) {
                            win(stage);
                        }
                    }
                }
            }
        }
    }


    public void addNumbers(List<Plate> plates) {
        for (Plate plate : plates) {
            for (int i = 0; i < plate.getNeighbors().size(); i++) {
                if (plate.getNeighbors().get(i).HasBomb()) {
                    plate.setNumOfBombs((plate.getNumOfBombs() + 1));
                }
            }
        }
    }

    public void addNeighbors(List<Plate> plates) {
        int[] middlePlates = {-11, -10, -9, -1, 1, 9, 10, 11};
        int[] leftPlates = {-10, -9, 1, 0, 0, 0, 10, 11};
        int[] rightPlates = {-11, -10, -1, 0, 0, 0, 9, 10};

        for (int i = 0; i < plates.size(); i++) {

            for (int j = 0; j < 8; j++) {
                int k = i + middlePlates[j];
                int kleft = i + leftPlates[j];
                int kright = i + rightPlates[j];
                if (k >= 0 && k < plates.size()) {
                    if (i % 10 == 0) {
                        plates.get(i).addNeighbors(plates.get(kleft));
                    } else if ((i + 1) % 10 == 0) {
                        plates.get(i).addNeighbors(plates.get(kright));
                    } else {
                        plates.get(i).addNeighbors(plates.get(k));
                    }
                }
            }
        }
    }

    public void addBombs(List<Plate> plates) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(plates.size());
            if (plates.get(randomIndex).HasBomb()) {
                i = i - 1;
                continue;
            }
            plates.get(randomIndex).setHasBomb(true);
        }
    }

    public void win(Stage stage) {
        final Stage winStage = new Stage();
        winStage.setResizable(false);
        winStage.setTitle("ALL BOMBS FOUND!");
        winStage.initModality(Modality.APPLICATION_MODAL);
        winStage.initOwner(stage);
        Label message = new Label("YOU WIN :) \n\nBombs found: " + bombsFound + "/10\nMoves: " + moves);
        message.setFont(new Font(20));
        Button tryAgain = new Button("TRY AGAIN!");
        BorderPane losebp = new BorderPane();
        losebp.setTop(message);
        losebp.setCenter(tryAgain);
        Scene looseScene = new Scene(losebp, 300, 200);
        winStage.setScene(looseScene);
        winStage.show();
        tryAgain.setOnAction((event -> {
            winStage.close();
            stage.close();
            moves = 0;
            flags = 10;
            bombsFound = 0;
            this.start(stage);
        }));
    }

    public void lose(Stage stage) {
        Stage loseStage = new Stage();
        loseStage.setResizable(false);
        loseStage.setTitle("BOMB EXPLODED");
        loseStage.initModality(Modality.APPLICATION_MODAL);
        loseStage.initOwner(stage);
        Label message = new Label("Bomb exploded :( \n\nBombs found: " + bombsFound + "/10\nMoves: " + moves);
        message.setFont(new Font(20));
        Button tryAgain = new Button("TRY AGAIN!");
        BorderPane losebp = new BorderPane();
        losebp.setTop(message);
        losebp.setCenter(tryAgain);
        Scene loseScene = new Scene(losebp, 300, 200);
        loseStage.setScene(loseScene);
        loseStage.show();
        tryAgain.setOnAction((event -> {
            loseStage.close();
            stage.close();
            moves = 0;
            flags = 10;
            bombsFound = 0;
            this.start(stage);
        }));
    }
}
