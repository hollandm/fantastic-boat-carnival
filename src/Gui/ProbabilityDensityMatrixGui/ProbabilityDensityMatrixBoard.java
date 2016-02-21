package Gui.ProbabilityDensityMatrixGui;

import Model.Rules;

import java.awt.*;

public class ProbabilityDensityMatrixBoard extends Gui.CanvasBoard {




    public ProbabilityDensityMatrixBoard(int x, int y, int draw_width, int draw_height) {
        super(x, y, draw_width, draw_height);
    }


    public void drawPDM(Graphics g, int[][] pdm) {

        double maxVal = 0;

        int MAX_RED_VAL = 100;
        int MAX_GREEN_VAL = 100;
        int MAX_BLUE_VAL = 255;

        int MIN_RED_VAL = 0;
        int MIN_GREEN_VAL = 0;
        int MIN_BLUE_VAL = 0;

        for (int x = 0; x < Rules.BOARD_SIZE_X; ++x) {
            for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y) {

                if (pdm[x][y] > maxVal)
                    maxVal = pdm[x][y];

            }
        }


        for (int x = 0; x < Rules.BOARD_SIZE_X; ++x) {
            for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y) {

                double density = pdm[x][y] / maxVal;

                int draw_r = (int)Math.round(density * (MAX_RED_VAL-MIN_RED_VAL) + MIN_RED_VAL);
                int draw_g = (int)Math.round(density * (MAX_GREEN_VAL-MIN_GREEN_VAL) + MIN_GREEN_VAL);
                int draw_b = (int)Math.round(density * (MAX_BLUE_VAL-MIN_BLUE_VAL) + MIN_BLUE_VAL);

                float[] draw_hsv = Color.RGBtoHSB(draw_r, draw_g, draw_b, null);
                Color draw_color = Color.getHSBColor(draw_hsv[0], draw_hsv[1], draw_hsv[2]);

                int draw_x = super.canvas_x + x * super.tile_width;
                int draw_y = super.canvas_y + y * super.tile_height;

                g.setColor(draw_color);
                g.fillRect(draw_x, draw_y, super.tile_width, super.tile_height);

                String label = Integer.toString(pdm[x][y]);
                int labelDrawWidth = g.getFontMetrics().stringWidth(label);
                int center_offset_x = (super.tile_width - labelDrawWidth)/2;
                int center_offset_y = super.tile_height / 2 + 3;

                g.setColor(Color.white);
                g.drawString(label, draw_x + center_offset_x, draw_y + center_offset_y);
            }
        }



    }

}
