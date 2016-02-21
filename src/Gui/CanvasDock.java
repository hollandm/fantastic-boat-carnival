package Gui;

import java.awt.*;
import java.util.ArrayList;


public class CanvasDock {

    int draw_x;
    int draw_y;
    int width;
    int height;

    boolean is_dock_vertical = false;

    public CanvasDock(int x, int y, int width, int height) {
        this.draw_x = x;
        this.draw_y = y;
        this.width = width;
        this.height = height;
    }

    public void rotateDockedShips(ArrayList<CanvasShip> ships) {

        is_dock_vertical = !is_dock_vertical;

        for (CanvasShip canvasShip : ships) {
            if (!canvasShip.is_placed) {
                placeInDock(canvasShip);
            }
        }
    }

    public void placeInDock(CanvasShip canvasShip) {
        canvasShip.is_placed = false;
        canvasShip.is_vertical = is_dock_vertical;

        if (is_dock_vertical) {
            canvasShip.draw_x = draw_x + 20 + 70*canvasShip.id;
            canvasShip.draw_y = draw_y + 20;
        } else {
            canvasShip.draw_x = draw_x + 20;
            canvasShip.draw_y = draw_y + 20 + 70*canvasShip.id;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);

        g.drawRect(draw_x, draw_y, width, height);

        g.drawString("Ship Dock", draw_x, draw_y - 20);
    }



}
