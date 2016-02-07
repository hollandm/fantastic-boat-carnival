package Brains;
import Model.*;

import Gui.HumanGui.*;
import java.util.ArrayList;


public class Human extends BaseBrain {

    @Override
    public void placeShips(GameState gameState) {
        ShipPlacementGui gui = new ShipPlacementGui();
        ArrayList<CanvasShip> canvasShips = gui.getPlacement();
        gui.closeWindow();

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

        return super.makeGuess(gameState);
    }



}
