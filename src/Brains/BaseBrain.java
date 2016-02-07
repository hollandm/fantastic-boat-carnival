package Brains;

import Model.*;

public abstract class BaseBrain {

    private static int brainID_Counter = 0;
    public final int id = ++brainID_Counter;
    private Game.DataKey key = null;

    // TODO: Make these not private
    public String name = "Base Brain";

    public final String FLAVOR_TEXT_HIT = "Hit";
    public final String FLAVOR_TEXT_MISS = "Miss";
    public final String FLAVOR_TEXT_SUNK = "You sunk my %s!";

    public void setKey(Game.DataKey key) {
        if (this.key == null)
            this.key = key;
    }
    protected Game.DataKey getKey() {
        return this.key;
    }


    public void placeShips(GameState gameState) {

        Player p = gameState.getCurrentPlayer();

        int i = 0;
        for (Ship s : p.getShips(key)) {
            s.setVertical(key, false);
            s.setX(key, 0);
            s.setY(key, ++i);
        }

    }

    public Guess makeGuess(GameState gameState) {

        int index = gameState.getCurrentPlayer().getGuesses().size();

        int x = index % Rules.BOARD_SIZE_X;

        int y = index / Rules.BOARD_SIZE_Y;

        return new Guess(x, y);
    }

}
