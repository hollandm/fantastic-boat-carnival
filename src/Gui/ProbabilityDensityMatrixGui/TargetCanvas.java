package Gui.ProbabilityDensityMatrixGui;


import Model.Game;
import Model.GameState;
import Model.Guess;
import Model.Ship;

import Gui.CanvasBoard;
import Gui.CanvasGuessMarker;
import Gui.CanvasShip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TargetCanvas extends JPanel {


    public final static boolean SHOW_MY_SHIPS_IN_GUI = false;


    public static final int MY_SHIPS_BOARD_X = 50;
    public static final int MY_SHIPS_BOARD_Y = 50;
    public static final int MY_SHIPS_BOARD_WIDTH = 400;
    public static final int MY_SHIPS_BOARD_HEIGHT = 400;

    public static final int TARGET_BOARD_X = MY_SHIPS_BOARD_X+MY_SHIPS_BOARD_WIDTH+50;
    public static final int TARGET_BOARD_Y = 50;
    public static final int TARGET_BOARD_WIDTH = 400;
    public static final int TARGET_BOARD_HEIGHT = 400;

    public CanvasBoard myBoard = new CanvasBoard(MY_SHIPS_BOARD_X, MY_SHIPS_BOARD_Y,
            MY_SHIPS_BOARD_WIDTH, MY_SHIPS_BOARD_HEIGHT);
    public ProbabilityDensityMatrixBoard targetBoard = new ProbabilityDensityMatrixBoard(TARGET_BOARD_X, TARGET_BOARD_Y,
            TARGET_BOARD_WIDTH, TARGET_BOARD_HEIGHT);


    ArrayList<CanvasShip> playersShipMarkers = new ArrayList<>();
    ArrayList<CanvasShip> targetShipMarkers = new ArrayList<>();
    ArrayList<CanvasGuessMarker> myGuessMarkers = new ArrayList<>();
    ArrayList<CanvasGuessMarker> targetGuessMarkers = new ArrayList<>();
    public CanvasGuessMarker myGuess = null;


    int width;
    int height;
    public boolean done = false;
    int[][] probabilityDensityMartix;


    public TargetCanvas(GameState gameState, Game.DataKey key,  int width, int height) {
        this.width = width;
        this.height = height;

        int i = -1;
        ArrayList<Ship> myShips = gameState.getCurrentPlayer().getShips(key);
        for (Ship modelShip : myShips) {
            CanvasShip canvasShip = new CanvasShip(++i, modelShip.getType(key));
            canvasShip.tile_x = modelShip.getX(key);
            canvasShip.tile_y = modelShip.getY(key);
            canvasShip.is_vertical = modelShip.isVertical(key);

            myBoard.snapShipToBoardGrid(canvasShip);

            playersShipMarkers.add(canvasShip);

        }

        ArrayList<Ship> targetShips = gameState.getOpposingPlayer().getShips(key);
        for (Ship modelShip : targetShips) {
            if (!modelShip.isSunk())
                continue;
            CanvasShip canvasShip = new CanvasShip(++i, modelShip.getType(key));
            canvasShip.tile_x = modelShip.getX(key);
            canvasShip.tile_y = modelShip.getY(key);
            canvasShip.is_vertical = modelShip.isVertical(key);

            targetBoard.snapShipToBoardGrid(canvasShip);

            targetShipMarkers.add(canvasShip);

        }

        ArrayList<Guess> myGuesses = gameState.getCurrentPlayer().getGuesses();
        for (Guess guess : myGuesses) {
            CanvasGuessMarker.guessMarkerType type = guess.getHitShip() == null ?
                    CanvasGuessMarker.guessMarkerType.MISS : CanvasGuessMarker.guessMarkerType.HIT;
            CanvasGuessMarker guessMarker = new CanvasGuessMarker(type, guess.getX(), guess.getY());

            myGuessMarkers.add(guessMarker);
        }

        ArrayList<Guess> targetGuesses = gameState.getOpposingPlayer().getGuesses();
        for (Guess guess : targetGuesses) {
            CanvasGuessMarker.guessMarkerType type = guess.getHitShip() == null ?
                    CanvasGuessMarker.guessMarkerType.MISS : CanvasGuessMarker.guessMarkerType.HIT;
            CanvasGuessMarker guessMarker = new CanvasGuessMarker(type, guess.getX(), guess.getY());

            targetGuessMarkers.add(guessMarker);
        }


        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

                done = true;

            }
        });
    }


    public void placeNewGuess(Guess g) {

        myGuess = new CanvasGuessMarker(CanvasGuessMarker.guessMarkerType.GUESS,
                g.getX(), g.getY());

        repaint();

    }

    public void setProbabilityDensityMartix(int[][] pdm) {
        this.probabilityDensityMartix = pdm;
    }

    public void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);
        myBoard.draw(g);
        targetBoard.drawPDM(g, probabilityDensityMartix);
        targetBoard.draw(g);

        if (SHOW_MY_SHIPS_IN_GUI) {
            for (CanvasShip cs : playersShipMarkers) {
                cs.drawShip(g);
            }
        }

        for (CanvasShip cs : targetShipMarkers) {
            cs.drawShip(g);
        }

        for (CanvasGuessMarker gm : myGuessMarkers)
            gm.drawOnBoard(g, targetBoard);

        for (CanvasGuessMarker gm : targetGuessMarkers)
            gm.drawOnBoard(g, myBoard);

        if (myGuess != null)
            myGuess.drawOnBoard(g, targetBoard);



    }


}
