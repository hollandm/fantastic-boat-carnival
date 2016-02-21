package Gui;

import Model.Rules;

import java.awt.*;
import java.util.ArrayList;


public class CanvasBoard {

    public int canvas_x;
    public int canvas_y;

    public int draw_width;
    public int draw_height;

    public int tile_width;
    public int tile_height;

    public CanvasBoard(int x, int y, int draw_width, int draw_height) {
        this.canvas_x = x;
        this.canvas_y = y;
        this.draw_width = draw_width;
        this.draw_height = draw_height;

        tile_width = draw_width /Rules.BOARD_SIZE_X;
        tile_height = draw_height /Rules.BOARD_SIZE_Y;
    }

    public boolean isOnBoard(int canvas_x, int canvas_y) {
        return canvas_x > this.canvas_x && canvas_x < this.canvas_x + this.draw_width &&
                canvas_y > this.canvas_y && canvas_y < this.canvas_y + this.draw_height;
    }

    public int getTileX(int canvas_x) {
        return (canvas_x - this.canvas_x) / tile_width;
    }
    public int getTileY(int canvas_y) {
        return (canvas_y - this.canvas_y) / tile_height;
    }

    public int getCanvasX(int tile_x) {
        return this.canvas_x + this.tile_width * tile_x;
    }

    public int getCanvasY(int tile_y) {
        return this.canvas_y + this.tile_height * tile_y;
    }

    public boolean shipFitsOnBoard(CanvasShip newShip, ArrayList<CanvasShip> ships) {

        int x =  Math.round((newShip.draw_x - canvas_x) / (float) tile_width);
        int y = Math.round((newShip.draw_y - canvas_y) / (float) tile_height);
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

        canvasShip.draw_x = canvas_x + tile_width * canvasShip.tile_x;
        canvasShip.draw_y = canvas_y + tile_height * canvasShip.tile_y;

        canvasShip.is_placed = true;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        for (int x = 0; x < Rules.BOARD_SIZE_X+1; ++x) {
            g.drawLine(canvas_x + tile_width * x,
                    canvas_y,
                    canvas_x + tile_width * x,
                    canvas_y + draw_height);

        }

        String X_AXIS = "ABCDEFGHIJKLMN";
        for (int x = 0; x < Rules.BOARD_SIZE_X; ++x) {
            g.drawString(X_AXIS.substring(x,x+1),
                    canvas_x +tile_width*x + 20,
                    canvas_y - 20);
        }

        for (int y = 0; y < Rules.BOARD_SIZE_Y+1; ++y) {
            g.drawLine(canvas_x,
                    canvas_y +tile_width*y,
                    canvas_x + draw_width,
                    canvas_y +tile_height*y);
        }

        String Y_AXIS = "123456789";
        for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y) {
            g.drawString(Y_AXIS.substring(y, y+1),
                    canvas_x -20,
                    canvas_y +tile_height*y+30);
        }
    }
}
