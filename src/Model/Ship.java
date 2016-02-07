package Model;


import java.util.ArrayList;


public class Ship {

    private Player owner;
    private Rules.ShipType type;
    private int x;
    private int y;
    private boolean isVertical;

    private ArrayList<Guess> hits = new ArrayList<>();


    public Ship(Rules.ShipType type, Player owner) {
        this.type = type;
        this.owner = owner;
    }

    public boolean isVertical(Game.DataKey key) {

        if (key == null)
            return false;

        if (key.isMasterKey() || key.getPlayer() == this.owner)
            return isVertical;

        return false;
    }

    public void setVertical(Game.DataKey key, boolean vertical) {

        if (key == null)
            return;

        if (key.isMasterKey()) {
            this.isVertical = vertical;
        } else if (key.getPlayer() == this.owner && key.hasPregameAccess()) {
            this.isVertical = vertical;
        }
    }

    public int getX(Game.DataKey key) {

        if (key == null)
            return -1;

        if (key.isMasterKey() || key.getPlayer() == this.owner)
            return this.x;

        if (this.isSunk())
            return this.x;

        return -1;
    }

    public int getY(Game.DataKey key) {

        if (key == null)
            return -1;

        if (key.isMasterKey() || key.getPlayer() == this.owner)
            return this.y;

        if (this.isSunk())
            return this.y;

        return -1;
    }

    public void setX(Game.DataKey key, int x) {

        if (key == null)
            return;

        if (key.isMasterKey()) {
            this.x = x;
            return;
        }

        if (key.getPlayer() == this.owner && key.hasPregameAccess()) {
            this.x = x;
        }
    }

    public void setY(Game.DataKey key, int y) {

        if (key == null)
            return;

        if (key.isMasterKey()) {
            this.y = y;
            return;
        }

        if (key.getPlayer() == this.owner && key.hasPregameAccess()) {
            this.y = y;
        }
    }

    public Rules.ShipType getType(Game.DataKey key) {

        if (key == null)
            return null;

        if (key.isMasterKey())
            return this.type;

        if (key.getPlayer() == this.owner)
            return this.type;

        if (this.isSunk())
            return this.type;

        return null;

    }

    public ArrayList<Guess> getHits(Game.DataKey key) {

        if (key.isMasterKey())
            return hits;

        if (key.getPlayer() == this.owner)
            return new ArrayList<>(hits);

        if (this.isSunk())
            return new ArrayList<>(hits);

        return null;
    }

    public void addHit(Game.DataKey key, Guess hit) {
        if (key.isMasterKey())
            hits.add(hit);
    }

    public boolean isSunk() {
        return this.hits.size() >= this.type.length;
    }

    public boolean checkIfShotHits(Game.DataKey key, Guess g) {

        if (!key.isMasterKey())
            return false;

        int l = type.length;

        for (int i = 0; i < l; ++i) {

            if (isVertical && g.getX() == x && g.getY() == y + i)
                return true;
            else if (!isVertical && g.getX() == x + i && g.getY() == y)
                return true;
        }

        return false;
    }


}
