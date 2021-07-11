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

    private TilePane tp;
    private int counter;
    private int flags = 10;
    private int bombDetected;
    private Label count;
    private Label flag;
    private Label bombsfound;


    public Minesweeper() {
        this.tp = tp;
        this.counter = counter;
        this.flags = flags;
        this.bombDetected = bombDetected;
        this.count = count;
        this.flag = flag;
        this.bombsfound = bombsfound;
    }

    public void start(Stage stage) {
        BorderPane bp = new BorderPane();
        TilePane tp = createTilePane(stage);
        tp.setPrefSize(500, 500);

        HBox hBox = new HBox();
        count = new Label("Moves: " + counter);
        flag = new Label("Flags: " + flags);
        bombsfound = new Label("Bombs detected: " + bombDetected);
        hBox.getChildren().addAll(count, flag, bombsfound);
        hBox.setSpacing(30);
        bp.setBottom(hBox);

        bp.setCenter(tp);
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }


    public TilePane createTilePane(Stage stage) {
        tp = new TilePane();
        List<Plate> plates = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Plate plate = new Plate();
            plate.setPrefSize(50, 50);
            plate.setOnMouseClicked((event -> {
                onPlateClicked(stage, event, plates, plate);
            }));
            tp.getChildren().add(plate);
            plates.add(plate);
        }
        addBombs(plates);
        addNeighbors(plates);
        addNumbers(plates);
        return tp;
    }

    public void onPlateClicked(Stage stage, MouseEvent event, List<Plate> plates, Plate plate) {
        counter++;
        count.setText("Moves: " + counter);

        if (event.getButton() == MouseButton.PRIMARY) {
            if (!plate.isFlagged()) {
                if (plate.HasBomb()) {
                    for (Plate p : plates) {
                        if (p.HasBomb()) {
                            p.setText("BB");
                        }
                    }
                    lose(stage);
                } else if (plate.getNumOfBombs() == 0) {
                    plate.setText("0");
                    for (Plate pl : plate.getNeighbors()) {
                        pl.setText(String.valueOf(pl.getNumOfBombs()));
                    }
                } else {
                    plate.setText(String.valueOf(plate.getNumOfBombs()));
                }
            }
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            System.out.println(plate.getText());
            System.out.println(plate.isFlagged());
            if (plate.isFlagged()) {
                plate.setText("");
                plate.setUnflagged();
                flags++;
                flag.setText("Flags: " + flags);
                if (plate.HasBomb()) {
                    bombDetected--;
                    bombsfound.setText("Bombs detected: " + bombDetected);
                }
            } else if (plate.getText().isEmpty() && !plate.isFlagged()) {
                if (flags > 0) {
                    plate.setText("F");
                    plate.setFlagged();
                    flags--;
                    flag.setText("Flags: " + flags);
                    if (plate.HasBomb()) {
                        bombDetected++;
                        bombsfound.setText("Bombs detected: " + bombDetected);
                        if (bombDetected == 10) {
                            win(stage);
                        }
                    }
                }
            }
        }
    }


    public void addNumbers(List<Plate> plates) {
        for (int j = 0; j < plates.size(); j++) {
            for (int i = 0; i < plates.get(j).getNeighbors().size(); i++) {
                if (plates.get(j).getNeighbors().get(i).HasBomb()) {
                    plates.get(j).setNumOfBombs((plates.get(j).getNumOfBombs() + 1));
                }
            }
        }
    }

    public void addNeighbors(List<Plate> plates) {
        int[] other = {-11, -10, -9, -1, 1, 9, 10, 11};
        int[] otherleft = {-10, -9, 1, 0, 0, 0, 10, 11};
        int[] otherright = {-11, -10, -1, 0, 0, 0, 9, 10};

        for (int i = 0; i < plates.size(); i++) {

            for (int j = 0; j < 8; j++) {
                int k = i + other[j];
                int kleft = i + otherleft[j];
                int kright = i + otherright[j];
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
        Label message = new Label("YOU WIN :) \nBombs found: " + bombDetected + "/10\nMoves: " + counter);
        message.setFont(new Font(20));
        Button tryAgain = new Button("TRY AGAIN!");
        BorderPane loosebp = new BorderPane();
        loosebp.setTop(message);
        loosebp.setCenter(tryAgain);
        Scene looseScene = new Scene(loosebp, 300, 200);
        winStage.setScene(looseScene);
        winStage.show();

    }

    public void lose(Stage stage) {
        Stage loseStage = new Stage();
        loseStage.setResizable(false);
        loseStage.setTitle("BOMB EXPLODED");
        loseStage.initModality(Modality.APPLICATION_MODAL);
        loseStage.initOwner(stage);
        Label message = new Label("Bomb exploded :( \nBombs found: " + bombDetected + "/10\nMoves: " + counter);
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
            this.start(stage);
        }));
    }
}
