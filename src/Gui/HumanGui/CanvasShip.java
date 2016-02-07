package Gui.HumanGui;

import Model.Rules;

import java.awt.*;

/**
 * Created by hollanma on 2/5/16.
 */
public class CanvasShip {


    public static final int SHIP_TILE_WIDTH = ShipPlacementCanvas.SHIP_SELECTION_BOARD_WIDTH/Rules.BOARD_SIZE_X;
    public static final int SHIP_TILE_HEIGHT = ShipPlacementCanvas.SHIP_SELECTION_BOARD_HEIGHT/Rules.BOARD_SIZE_Y;

    int id = 0;

    Rules.ShipType shipType;

    int draw_x;
    int draw_y;

    int tile_x;
    int tile_y;

    int mouse_x;
    int mouse_y;

    boolean is_placed;
    boolean is_vertical = false;


    public CanvasShip(int id, Rules.ShipType shipType) {
        this.id = id;
        this.shipType = shipType;
    }

    public int getTileX() {
        return this.tile_x;
    }

    public int getTileY() {
        return this.tile_y;
    }

    public Rules.ShipType getShipType() {
        return this.shipType;
    }

    public boolean isVertical() {
        return this.is_vertical;
    }

    public boolean isOnShip(int x, int y) {

        if (is_vertical) {
            if (x >= draw_x && x < draw_x + SHIP_TILE_WIDTH && y >= draw_y && y < draw_y + SHIP_TILE_WIDTH * shipType.length)
                return true;
        } else {
            if (x >= draw_x && x < draw_x + SHIP_TILE_WIDTH * shipType.length && y >= draw_y && y < draw_y + SHIP_TILE_WIDTH)
                return true;
        }

        return false;
    }

    public void drawShip(Graphics g) {
        g.setColor(Color.GRAY);
        if (is_vertical)
            g.fillRect(draw_x, draw_y, SHIP_TILE_WIDTH, SHIP_TILE_HEIGHT * shipType.length);
        else
            g.fillRect(draw_x, draw_y, SHIP_TILE_WIDTH * shipType.length, SHIP_TILE_HEIGHT);

        g.setColor(Color.lightGray);
        if (is_vertical)
            g.drawRect(draw_x, draw_y, SHIP_TILE_WIDTH, SHIP_TILE_HEIGHT * shipType.length);
        else
            g.drawRect(draw_x, draw_y, SHIP_TILE_WIDTH * shipType.length, SHIP_TILE_HEIGHT);

        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < shipType.length*2; ++i) {
            if (is_vertical)
                g.fillOval(draw_x, draw_y, SHIP_TILE_WIDTH, SHIP_TILE_HEIGHT * i/2);
            else
                g.fillOval(draw_x, draw_y, SHIP_TILE_WIDTH * i/2, SHIP_TILE_HEIGHT);
        }


    }

}
