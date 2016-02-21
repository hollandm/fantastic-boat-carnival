package Brains;
import Gui.CanvasShip;
import Model.*;

import Gui.HumanGui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


public class Human extends RandomAI {


    public static final int SHIP_SELECTION_WINDOW_WIDTH = 950;
    public static final int SHIP_SELECTION_WINDOW_HEIGHT = 500;

    @Override
    public void placeShips(GameState gameState) {

        ShipPlacementCanvas canvas;
        JFrame frame;

        frame = new JFrame("Ship Placement");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        canvas = new ShipPlacementCanvas(SHIP_SELECTION_WINDOW_WIDTH, SHIP_SELECTION_WINDOW_HEIGHT);
        canvas.setPreferredSize(new Dimension(SHIP_SELECTION_WINDOW_WIDTH, SHIP_SELECTION_WINDOW_HEIGHT));
        frame.getContentPane().add(canvas, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        while (canvas.submitted == false && canvas.place_random == false) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }

        if (canvas.place_random) {
//            RandomAI rai = new RandomAI();
//            rai.setKey(super.getKey());
//            rai.placeShips(gameState);
            super.placeShips(gameState);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            return;
        }

        ArrayList<CanvasShip> canvasShips = canvas.ships;


        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        Game.DataKey key = this.getKey();

        ArrayList<Ship> modelShips = gameState.getCurrentPlayer().getShips(key);

        for (CanvasShip cs : canvasShips) {
            for (Ship ms : modelShips) {
                if (cs.getShipType() == ms.getType(key)) {
                    ms.setX(key, cs.getTileX());
                    ms.setY(key, cs.getTileY());
                    ms.setVertical(key, cs.isVertical());
                }
            }
        }
    }

    public Guess makeGuess(GameState gameState) {

        TargetCanvas canvas;
        JFrame frame;

        frame = new JFrame("Target Selection");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        canvas = new TargetCanvas(gameState, getKey(), SHIP_SELECTION_WINDOW_WIDTH, SHIP_SELECTION_WINDOW_HEIGHT);
        canvas.setPreferredSize(new Dimension(SHIP_SELECTION_WINDOW_WIDTH, SHIP_SELECTION_WINDOW_HEIGHT));
        frame.getContentPane().add(canvas, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        while (canvas.submitted == false) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));


        Guess guess = new Guess(canvas.myGuess.tile_x, canvas.myGuess.tile_y);

//        return super.makeGuess(gameState);
        return guess;
    }



}
