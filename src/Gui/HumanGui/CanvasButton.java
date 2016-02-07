package Gui.HumanGui;

import java.awt.*;

/**
 * Created by hollanma on 2/6/16.
 */
public class CanvasButton {

    int draw_x;
    int draw_y;

    public int width;
    public int height;

    public String label;

    public boolean enabled;

    public Color button_color = Color.darkGray;;
    public Color text_color = Color.lightGray;;

    public CanvasButton(int x, int y, int width, int height, String label, boolean enabled) {
        this.draw_x = x;
        this.draw_y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.enabled = enabled;
    }

    public boolean isOnTopOf(int x, int y) {
        return (x > draw_x && x < draw_x + width && y > draw_y && y < draw_y + height);
    }

    public void draw(Graphics g) {
        if (!enabled)
            return;

        g.setColor(button_color);
        g.fillRect(draw_x, draw_y, width, height);

        int labelDrawWidth = g.getFontMetrics().stringWidth(label);
        int label_margin_x = (width - labelDrawWidth)/2;
//        g.setColor(text_color);
//        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
//        g.drawString();
        g.setColor(text_color);
        g.drawString(label, draw_x + label_margin_x, draw_y + 14);
    }



}
