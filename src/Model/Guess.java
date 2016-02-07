package Model;

public class Guess {

    private final int x, y;
    private Ship hit = null;


    public Guess(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setHitShip(Game.DataKey key, Ship hit) {
        if (key.isMasterKey())
            this.hit = hit;
    }

    public Ship getHitShip() {
        return this.hit;
    }

    public boolean equals(Guess guess) {
        return this.x == guess.x && this.y == guess.y;
    }
}
