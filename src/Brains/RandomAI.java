package Brains;

import Model.*;

import java.util.Random;

/**
 * Created by hollanma on 1/25/16.
 */
public class RandomAI extends BaseBrain {

    // TODO: Make these not private
    public String name = "Base Brain";
    Random random = new Random();

    public final String FLAVOR_TEXT_HIT = "Hit";
    public final String FLAVOR_TEXT_MISS = "Miss";
    public final String FLAVOR_TEXT_SUNK = "You sunk my %s!";



    public void placeShips(GameState gameState) {

        Player p = gameState.getCurrentPlayer();

        boolean[][] board = new boolean[Rules.BOARD_SIZE_X][Rules.BOARD_SIZE_Y];
        for (int x = 0; x < Rules.BOARD_SIZE_X; ++x)
            for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y)
                board[x][y] = false;

        for (Ship s : p.getShips(super.getKey())) {

            int length = s.getType(getKey()).length;

            boolean isVertical = random.nextBoolean();
            int x, y;
            placeShipLoop:
            while (true) {
                if (isVertical) {
                    x = random.nextInt(Rules.BOARD_SIZE_X);
                    y = random.nextInt(Rules.BOARD_SIZE_Y -length);

                    for (int i = 0; i < length; ++i) {
                        if (board[x][y+i])
                            continue placeShipLoop;
                    }

                    break placeShipLoop;
                } else {
                    x = random.nextInt(Rules.BOARD_SIZE_X -length);
                    y = random.nextInt(Rules.BOARD_SIZE_Y);

                    for (int i = 0; i < length; ++i) {
                        if (board[x+i][y])
                            continue placeShipLoop;
                    }

                    break placeShipLoop;
                }


            }

            if (isVertical) {
                for (int i = 0; i < length; ++i)
                    board[x][y+i] = true;
            } else {
                for (int i = 0; i < length; ++i)
                    board[x+i][y] = true;
            }

            s.setVertical(super.getKey(), isVertical);
            s.setX(super.getKey(), x);
            s.setY(super.getKey(), y);
        }

    }

    public Guess makeGuess(GameState gameState) {

        int x = random.nextInt(Rules.BOARD_SIZE_X);
        int y = random.nextInt(Rules.BOARD_SIZE_Y);

        return new Guess(x, y);
    }
}
