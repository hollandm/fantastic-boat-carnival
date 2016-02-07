package Gui.HumanGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


public class ShipPlacementGui {

    public static final int SHIP_SELECTION_WINDOW_WIDTH = 950;
    public static final int SHIP_SELECTION_WINDOW_HEIGHT = 500;

    final ShipPlacementCanvas canvas;
    final JFrame frame;

    public ShipPlacementGui() {
        frame = new JFrame("Ship Placement");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        canvas = new ShipPlacementCanvas();
        canvas.setPreferredSize(new Dimension(SHIP_SELECTION_WINDOW_WIDTH, SHIP_SELECTION_WINDOW_HEIGHT));
        frame.getContentPane().add(canvas, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public ArrayList<CanvasShip> getPlacement() {

        while (canvas.submited == false) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        return canvas.ships;


    }

    public void closeWindow() {
        this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        System.out.println("Starting");

        ShipPlacementGui gui = new ShipPlacementGui();
        System.out.println("Made Window");

        ArrayList<CanvasShip> ships = gui.getPlacement();
        System.out.println("Got Placement");

        gui.closeWindow();

    }
}
