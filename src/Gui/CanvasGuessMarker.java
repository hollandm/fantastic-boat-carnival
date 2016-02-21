package Gui;


import Gui.CanvasBoard;

import java.awt.*;

public class CanvasGuessMarker {

    public final static Color GUESS_MARKER_COLOR = Color.yellow;
    public final static Color HIT_MARKER_COLOR = Color.red;
    public final static Color MISS_MARKER_COLOR = Color.white;

    public enum guessMarkerType {
        GUESS(GUESS_MARKER_COLOR), HIT(HIT_MARKER_COLOR), MISS(MISS_MARKER_COLOR);

        public Color draw_color;

        guessMarkerType(Color draw_color) {
            this.draw_color = draw_color;
        }

    }

    public int tile_x, tile_y;
    guessMarkerType type;

    public CanvasGuessMarker(guessMarkerType type, int x, int y) {
        this.type = type;
        this.tile_x = x;
        this.tile_y = y;
    }

    public void drawOnBoard(Graphics g, CanvasBoard canvasBoard) {

        g.setColor(type.draw_color);

        g.drawOval(canvasBoard.canvas_x + this.tile_x * canvasBoard.tile_width,
                canvasBoard.canvas_y + this.tile_y * canvasBoard.tile_height,
                canvasBoard.tile_width, canvasBoard.tile_height);

    }


}
