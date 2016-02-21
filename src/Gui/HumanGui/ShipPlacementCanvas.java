package Gui.HumanGui;

import Gui.CanvasBoard;
import Gui.CanvasButton;
import Gui.CanvasDock;
import Gui.CanvasShip;
import Model.Rules;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;



public class ShipPlacementCanvas extends JPanel {

    public static final int SHIP_SELECTION_BOARD_X = 50;
    public static final int SHIP_SELECTION_BOARD_Y = 50;
    public static final int SHIP_SELECTION_BOARD_WIDTH = 400;
    public static final int SHIP_SELECTION_BOARD_HEIGHT = 400;


    public static final int SHIP_SELECTION_DOCK_X = SHIP_SELECTION_BOARD_X+SHIP_SELECTION_BOARD_WIDTH+50;
    public static final int SHIP_SELECTION_DOCK_Y = 50;
    public static final int SHIP_SELECTION_DOCK_WIDTH = 400;
    public static final int SHIP_SELECTION_DOCK_HEIGHT = 400;

    public static final int SHIP_SELECTION_ROTATE_WIDTH = 50;
    public static final int SHIP_SELECTION_ROTATE_HEIGHT = 20;
    public static final int SHIP_SELECTION_ROTATE_X = SHIP_SELECTION_DOCK_X + SHIP_SELECTION_DOCK_WIDTH - SHIP_SELECTION_ROTATE_WIDTH;
    public static final int SHIP_SELECTION_ROTATE_Y = SHIP_SELECTION_DOCK_Y - SHIP_SELECTION_ROTATE_HEIGHT - 10;


    public static final int SHIP_SELECTION_SUBMIT_WIDTH = 50;
    public static final int SHIP_SELECTION_SUBMIT_HEIGHT = 20;
    public static final int SHIP_SELECTION_SUBMIT_X = SHIP_SELECTION_DOCK_X + SHIP_SELECTION_DOCK_WIDTH - SHIP_SELECTION_SUBMIT_WIDTH;
    public static final int SHIP_SELECTION_SUBMIT_Y = SHIP_SELECTION_DOCK_Y + SHIP_SELECTION_DOCK_HEIGHT + 10;

    public static final int SHIP_SELECTION_RANDOM_WIDTH = 60;
    public static final int SHIP_SELECTION_RANDOM_HEIGHT = 20;
    public static final int SHIP_SELECTION_RANDOM_X = SHIP_SELECTION_SUBMIT_X - SHIP_SELECTION_RANDOM_WIDTH - 10;
    public static final int SHIP_SELECTION_RANDOM_Y = SHIP_SELECTION_DOCK_Y + SHIP_SELECTION_DOCK_HEIGHT + 10;

    public CanvasBoard canvasBoard = new CanvasBoard(SHIP_SELECTION_BOARD_X, SHIP_SELECTION_BOARD_Y,
            SHIP_SELECTION_BOARD_WIDTH, SHIP_SELECTION_BOARD_HEIGHT);
    public CanvasDock canvasDock = new CanvasDock(SHIP_SELECTION_DOCK_X, SHIP_SELECTION_DOCK_Y,
            SHIP_SELECTION_DOCK_WIDTH, SHIP_SELECTION_DOCK_HEIGHT);
    public CanvasButton rotateButton = new CanvasButton(SHIP_SELECTION_ROTATE_X, SHIP_SELECTION_ROTATE_Y,
            SHIP_SELECTION_ROTATE_WIDTH, SHIP_SELECTION_ROTATE_HEIGHT, "Rotate", true);
    public CanvasButton submitButton = new CanvasButton(SHIP_SELECTION_SUBMIT_X, SHIP_SELECTION_SUBMIT_Y,
            SHIP_SELECTION_SUBMIT_WIDTH, SHIP_SELECTION_SUBMIT_HEIGHT, "Submit", false);
    public CanvasButton randomButton = new CanvasButton(SHIP_SELECTION_RANDOM_X, SHIP_SELECTION_RANDOM_Y,
            SHIP_SELECTION_RANDOM_WIDTH, SHIP_SELECTION_RANDOM_HEIGHT, "Random", true);

    int width;
    int height;

    public boolean submitted = false;
    public boolean place_random = false;

    public ArrayList<CanvasShip> ships;
    public CanvasShip clickedShip = null;

    public ShipPlacementCanvas(int width, int height) {

        this.width = width;
        this.height = height;

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (submitted)
                    return;

                int x = e.getX();
                int y = e.getY();

                if (rotateButton.isOnTopOf(x, y)) {
                    canvasDock.rotateDockedShips(ships);
                    repaint();
                }


                if (randomButton.isOnTopOf(x, y)) {
                    place_random = true;
                }

                if (submitButton.isOnTopOf(x, y)) {
                    submitted = true;
                }

                for (CanvasShip cs : ships) {

                    if (cs.isOnShip(x, y)) {
                        clickedShip = cs;
                        clickedShip.mouse_x = x - cs.draw_x;
                        clickedShip.mouse_y = y - cs.draw_y;
                        cs.is_placed = false;
                    }
                }

            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (submitted)
                    return;
                int x = e.getX();
                int y = e.getY();

                if (clickedShip != null) {
                    clickedShip.draw_x = x - clickedShip.mouse_x;
                    clickedShip.draw_y = y - clickedShip.mouse_y;

                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (submitted)
                    return;
                int x = e.getX();
                int y = e.getY();


                if (clickedShip != null) {
                    clickedShip.draw_x = x - clickedShip.mouse_x;
                    clickedShip.draw_y = y - clickedShip.mouse_y;

                    if (canvasBoard.shipFitsOnBoard(clickedShip, ships))
                        canvasBoard.snapShipToBoardGrid(clickedShip);
                    else
                        canvasDock.placeInDock(clickedShip);

                    submitButton.enabled = canSubmit();

                    clickedShip = null;
                    repaint();
                }
            }
        });


        ships = new ArrayList<>(Rules.STARTING_SHIP_COUNT);
        int i = 0;
        for (Rules.ShipType shipType : Rules.ShipType.values()) {
            CanvasShip cs = new CanvasShip(i, shipType);
            canvasDock.placeInDock(cs);
            ships.add(cs);
            ++i;
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);
        canvasBoard.draw(g);
        canvasDock.draw(g);

        rotateButton.draw(g);
        submitButton.draw(g);
        randomButton.draw(g);

        for (CanvasShip cs : ships) {
            cs.drawShip(g);
        }

        if (clickedShip != null)
            clickedShip.drawShip(g);


    }

    public void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
    }

    public boolean canSubmit() {

        for (CanvasShip cs : ships)
            if (!cs.is_placed)
                return false;

        return true;
    }

}