package Brains;

import Gui.ProbabilityDensityMatrixGui.TargetCanvas;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HeuristicAI extends RandomAI {


    public final static boolean SHOW_GUI = true;
    public static final int PDM_AI_WINDOW_WIDTH = 950;
    public static final int PDM_AI_WINDOW_HEIGHT = 500;

    enum shot {
        HIT, MISS, SUNK
    }

    public Guess makeGuess(GameState gameState) {

        int[][] pdm = makeProbabilityDensityMatrix(gameState);

        Guess g = analyzeProbabilityDensityMatrix(pdm);

        printProbabilityDensityMatrix(pdm, g);

        if (SHOW_GUI) {
            TargetCanvas canvas;
            JFrame frame;

            frame = new JFrame("Ship Placement");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


            canvas = new TargetCanvas(gameState, getKey(), PDM_AI_WINDOW_WIDTH, PDM_AI_WINDOW_HEIGHT);
            canvas.setPreferredSize(new Dimension(PDM_AI_WINDOW_WIDTH, PDM_AI_WINDOW_HEIGHT));
            canvas.placeNewGuess(g);
            canvas.setProbabilityDensityMartix(pdm);
            frame.getContentPane().add(canvas, BorderLayout.CENTER);

            //Display the window.
            frame.pack();
            frame.setVisible(true);

            while (canvas.done == false) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            }

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }

        return g;

    }


    public int[][] makeProbabilityDensityMatrix(GameState gameState) {
        Player opponent = gameState.getOpposingPlayer();
        ArrayList<Rules.ShipType> remainingShips = new ArrayList<>(Arrays.asList(Rules.ShipType.values()));
        for (Ship s : opponent.getShips(null)) {
            if (s.isSunk()) {
                remainingShips.remove(s.getType(null));
            }
        }
        System.out.println("Remaining Types: " + remainingShips.size());

        shot[][] shots = new shot[Rules.BOARD_SIZE_X][Rules.BOARD_SIZE_Y];
        for (Guess g : gameState.getCurrentPlayer().getGuesses()) {
            if (g.getHitShip() == null)
                shots[g.getX()][g.getY()] = shot.MISS;
            else if (g.getHitShip().isSunk())
                shots[g.getX()][g.getY()] = shot.SUNK;
            else
                shots[g.getX()][g.getY()] = shot.HIT;

        }

        //Probability Density Matrix
        int[][] pdm = new int[Rules.BOARD_SIZE_X][Rules.BOARD_SIZE_Y];

        for (int x = 0; x < Rules.BOARD_SIZE_X; ++x) {
            for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y) {

                if (shots[x][y] == shot.MISS || shots[x][y] == shot.SUNK)
                    continue;

                shipLoop:
                for (Rules.ShipType s : remainingShips) {
                    int l = s.length;

                    boolean isObstructedX = x+l > Rules.BOARD_SIZE_X;
                    boolean isObstructedY = y+l > Rules.BOARD_SIZE_Y;

                    int hitsX = 0;
                    int hitsY = 0;

                    for (int i = 0; i < l; ++i) {

                        if (!isObstructedX) {
                            isObstructedX = shots[x + i][y] == shot.MISS || shots[x + i][y] == shot.SUNK;
                            if (shots[x + i][y] == shot.HIT)
                                hitsX += 1;
                        }

                        if (!isObstructedY) {
                            isObstructedY = shots[x][y + i] == shot.MISS || shots[x][y + i] == shot.SUNK;
                            if (shots[x][y + i] == shot.HIT)
                                hitsY += 1;
                        }

                        if (isObstructedX && isObstructedY)
                            continue shipLoop;
                    }

                    for (int i = 0; i < l; ++i) {

                        if (!isObstructedX && shots[x + i][y] != shot.HIT) {
                            pdm[x + i][y] += Math.pow(10, hitsX);
                        }

                        if (!isObstructedY && shots[x][y + i] != shot.HIT) {
                            pdm[x][y + i] += Math.pow(10, hitsY);
                        }

                    }
                }
            }
        }

        return pdm;
    }

    public void printProbabilityDensityMatrix(int[][] pdm, Guess guess) {

        System.out.println();
        System.out.println("Board: ");
        for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y) {
            for (int x = 0; x < Rules.BOARD_SIZE_X; ++x) {


                String board_value = Integer.toString(pdm[x][y]);

                if (x == guess.getX() && y == guess.getY())
                    board_value = "-" + board_value + "-";
                else
                    board_value = " " + board_value + " ";


                while (board_value.length() < 6)
                    board_value = " " + board_value;

                System.out.print(" " + board_value + " ");

            }
            System.out.println();
        }

    }

    public Guess analyzeProbabilityDensityMatrix(int[][] pdm) {
        int max_value = 0;
        ArrayList<Guess> best_guesses = new ArrayList<>();

        for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y) {
            for (int x = 0; x < Rules.BOARD_SIZE_X; ++x) {

                if (pdm[x][y] > max_value) {
                    max_value = pdm[x][y];
                    best_guesses.clear();
                }

                if (pdm[x][y] == max_value)
                    best_guesses.add(new Guess(x,y));

            }
        }

        Random rnd = new Random();
        int i = rnd.nextInt(best_guesses.size());
        return best_guesses.get(i);
    }

}
