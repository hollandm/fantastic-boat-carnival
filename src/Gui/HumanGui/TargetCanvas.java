package Gui.HumanGui;


import Gui.CanvasBoard;
import Gui.CanvasButton;
import Gui.CanvasGuessMarker;
import Gui.CanvasShip;
import Model.Game;
import Model.GameState;
import Model.Guess;
import Model.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TargetCanvas extends JPanel {

    public static final int MY_SHIPS_BOARD_X = 50;
    public static final int MY_SHIPS_BOARD_Y = 50;
    public static final int MY_SHIPS_BOARD_WIDTH = 400;
    public static final int MY_SHIPS_BOARD_HEIGHT = 400;

    public static final int TARGET_BOARD_X = MY_SHIPS_BOARD_X+MY_SHIPS_BOARD_WIDTH+50;
    public static final int TARGET_BOARD_Y = 50;
    public static final int TARGET_BOARD_WIDTH = 400;
    public static final int TARGET_BOARD_HEIGHT = 400;
    
    public static final int TARGET_SUBMIT_WIDTH = 50;
    public static final int TARGET_SUBMIT_HEIGHT = 20;
    public static final int TARGET_SUBMIT_X = TARGET_BOARD_X + TARGET_BOARD_WIDTH - TARGET_SUBMIT_WIDTH;
    public static final int TARGET_SUBMIT_Y = TARGET_BOARD_Y + TARGET_BOARD_HEIGHT + 10;

    public CanvasBoard myBoard = new CanvasBoard(MY_SHIPS_BOARD_X, MY_SHIPS_BOARD_Y,
            MY_SHIPS_BOARD_WIDTH, MY_SHIPS_BOARD_HEIGHT);
    public CanvasBoard targetBoard = new CanvasBoard(TARGET_BOARD_X, TARGET_BOARD_Y,
            TARGET_BOARD_WIDTH, TARGET_BOARD_WIDTH);

    public CanvasButton submitButton = new CanvasButton(TARGET_SUBMIT_X, TARGET_SUBMIT_Y,
            TARGET_SUBMIT_WIDTH, TARGET_SUBMIT_HEIGHT, "Submit", false);


    ArrayList<CanvasShip> playersShipMarkers = new ArrayList<>();
    ArrayList<CanvasShip> targetShipMarkers = new ArrayList<>();
    ArrayList<CanvasGuessMarker> myGuessMarkers = new ArrayList<>();
    ArrayList<CanvasGuessMarker> targetGuessMarkers = new ArrayList<>();
    public CanvasGuessMarker myGuess = null;


    int width;
    int height;
    public boolean submitted = false;

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
                if (submitted)
                    return;

                int x = e.getX();
                int y = e.getY();

                if (targetBoard.isOnBoard(x, y))
                    placeNewGuess(x, y);

                if (submitButton.isOnTopOf(x, y)) {
                    submitted = true;
                }

            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (submitted)
                    return;
                int x = e.getX();
                int y = e.getY();

                if (targetBoard.isOnBoard(x, y))
                    placeNewGuess(x, y);

            }
        });
    }

    public void placeNewGuess(int x, int y) {

        int tile_x = targetBoard.getTileX(x);
        int tile_y = targetBoard.getTileY(y);

        for (CanvasGuessMarker g : myGuessMarkers) {
            if (g.tile_x == tile_x && g.tile_y == tile_y)
                return;
        }

        myGuess = new CanvasGuessMarker(CanvasGuessMarker.guessMarkerType.GUESS,
                tile_x, tile_y);

        submitButton.enabled = true;
        repaint();

    }

    public void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);
        myBoard.draw(g);
        targetBoard.draw(g);

        for (CanvasShip cs : playersShipMarkers) {
            cs.drawShip(g);
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

        submitButton.draw(g);


    }


}
