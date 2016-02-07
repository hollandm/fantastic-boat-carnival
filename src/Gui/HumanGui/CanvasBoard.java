package Gui.HumanGui;

import Model.Rules;

import java.awt.*;
import java.util.ArrayList;


public class CanvasBoard {

//    public static final int SHIP_SELECTION_BOARD_WIDTH = 400;
//    public static final int SHIP_SELECTION_BOARD_HEIGHT = 400;

//    public static final int SHIP_SELECTION_TILE_WIDTH = SHIP_SELECTION_BOARD_WIDTH/Rules.BOARD_SIZE_X;
//    public static final int SHIP_SELECTION_TILE_HEIGHT = SHIP_SELECTION_BOARD_HEIGHT/Rules.BOARD_SIZE_Y;

    int draw_x;
    int draw_y;

    int width;
    int height;

    int tile_width;
    int tile_height;

    public CanvasBoard(int x, int y, int width, int height) {
        this.draw_x = x;
        this.draw_y = y;
        this.width = width;
        this.height = height;

        tile_width = width/Rules.BOARD_SIZE_X;
        tile_height = height/Rules.BOARD_SIZE_Y;
    }

    public boolean shipFitsOnBoard(CanvasShip newShip, ArrayList<CanvasShip> ships) {

        int x =  Math.round((newShip.draw_x - draw_x) / (float) tile_width);
        int y = Math.round((newShip.draw_y - draw_y) / (float) tile_height);
        int l = newShip.shipType.length;
        boolean v = newShip.is_vertical;


        System.out.println("X: " + x + ", Y: " + y + ", V: " + v);

        if (v) {
            if (x < 0 || x >= Rules.BOARD_SIZE_X || y < 0 || y + l > Rules.BOARD_SIZE_Y)
                return false;
        } else {
            if (x < 0 || x + l > Rules.BOARD_SIZE_X || y < 0 || y >= Rules.BOARD_SIZE_Y)
                return false;
        }

        boolean[][] board = new boolean[Rules.BOARD_SIZE_X][Rules.BOARD_SIZE_Y];

        for (CanvasShip cs : ships) {
            if (!cs.is_placed)
                continue;

            if (cs.is_vertical) {
                for (int i = 0; i < l; ++i) {
                    board[cs.tile_x][cs.tile_y+i] = true;
                }
            } else {
                for (int i = 0; i < l; ++i) {
                    board[cs.tile_x + i][cs.tile_y] = true;
                }
            }
        }

        if (newShip.is_vertical) {
            for (int i = 0; i < l; ++i) {
                if (board[x][y+i])
                    return false;
            }
        } else {
            for (int i = 0; i < l; ++i) {
                if (board[x + i][y])
                    return false;
            }
        }


        newShip.tile_x = x;
        newShip.tile_y = y;
        return true;
    }


    public void snapShipToBoardGrid(CanvasShip canvasShip) {

        canvasShip.draw_x = draw_x + tile_width * canvasShip.tile_x;
        canvasShip.draw_y = draw_y + tile_height * canvasShip.tile_y;

        canvasShip.is_placed = true;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        for (int x = 0; x < Rules.BOARD_SIZE_X+1; ++x) {
            g.drawLine(draw_x + tile_width * x,
                    draw_y,
                    draw_x + tile_width * x,
                    draw_y + height);

        }

        String X_AXIS = "ABCDEFGHIJKLMN";
        for (int x = 0; x < Rules.BOARD_SIZE_X; ++x) {
            g.drawString(X_AXIS.substring(x,x+1),
                    draw_x+tile_width*x + 20,
                    draw_y - 20);
        }

        for (int y = 0; y < Rules.BOARD_SIZE_Y+1; ++y) {
            g.drawLine(draw_x,
                    draw_y +tile_width*y,
                    draw_x +width,
                    draw_y +tile_height*y);
        }

        String Y_AXIS = "123456789";
        for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y) {
            g.drawString(Y_AXIS.substring(y, y+1),
                    draw_x-20,
                    draw_y+tile_height*y+30);
        }
    }
}
