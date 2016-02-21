package Model;

import java.util.ArrayList;

public class Player {

    private int id;
    private String name;

    private ArrayList<Ship> ships;
    private ArrayList<Guess> guesses;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;

        this.ships = new ArrayList<>(Rules.STARTING_SHIP_COUNT);
        this.ships.add(new Ship(Rules.ShipType.CARRIER, this));
        this.ships.add(new Ship(Rules.ShipType.BATTLESHIP, this));
        this.ships.add(new Ship(Rules.ShipType.CRUISER, this));
        this.ships.add(new Ship(Rules.ShipType.SUBMARINE, this));
        this.ships.add(new Ship(Rules.ShipType.DESTROYER, this));

        this.guesses = new ArrayList<>();
    }


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Ship> getShips(Game.DataKey key) {

        if (key == null)
//            return null;
            return new ArrayList<>(this.ships);

        if (key.isMasterKey())
            return this.ships;

//        if (key.getPlayer() == this)
        return new ArrayList<>(this.ships);

//        return null;
    }

    public ArrayList<Guess> getGuesses() {return getGuesses(null);}
    public ArrayList<Guess> getGuesses(Game.DataKey key) {

        if (key != null && key.isMasterKey())
            return guesses;


        return new ArrayList<>(this.guesses);
    }

    public void addGuess(Game.DataKey key, Guess g) {
        if (key.isMasterKey())
            this.guesses.add(g);
    }

    public String toString() {
        return this.name + " " + this.id;
    }



}
