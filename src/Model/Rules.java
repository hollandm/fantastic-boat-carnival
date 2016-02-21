package Model;

import java.util.ArrayList;


public class Rules {

    public enum ShipType {
        CARRIER(5), BATTLESHIP(4), CRUISER(3), SUBMARINE(3), DESTROYER(2);


        public final int length;

        ShipType(int length) {
            this.length = length;
        }
    }

    public final static int STARTING_SHIP_COUNT = ShipType.values().length;
    public final static int BOARD_SIZE_X = 8;
    public final static int BOARD_SIZE_Y = 8;
}
