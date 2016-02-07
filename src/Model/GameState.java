package Model;

import java.util.ArrayList;

public class GameState {

    private static int gameID_counter = 0;
    private final int gameID = ++gameID_counter;

    private boolean p1Turn = true;

    private Player p1, p2;
    private Player winner = null;

    public GameState(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;

    }

    public Player getPlayer1() {
        return this.p1;
    }

    public Player getPlayer2() {
        return this.p2;
    }

    public Player getCurrentPlayer() {
        return p1Turn ? p1 : p2;
    }

    public Player getOpposingPlayer() {
        return p1Turn ? p2 : p1;
    }

    public Player getWinner() {
        return this.winner;
    }

    public void setWinner(Game.DataKey key, Player winner) {
        if (key.isMasterKey()) {
            this.winner = winner;
            System.out.println(winner + " Wins!");
        }
    }

    public void nextTurn(Game.DataKey key) {
        if (key.isMasterKey())
            this.p1Turn = !this.p1Turn;
    }

    public ArrayList<Player> getPlayerArray() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        return players;
    }

    public String getBoardString(Game.DataKey key, Player p) {

        if (key == null)
            return "";

        if (!key.isMasterKey() && key.getPlayer() != p)
            return "";

        Player oponent;
        if (p == p1)
            oponent = p2;
        else if (p == p2)
            oponent = p1;
        else
            return "";

        String symbols = " !@#$%^&&*()";
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWYXZ";


        Guess[][] guessBoard = new Guess[Rules.BOARD_SIZE_X][Rules.BOARD_SIZE_Y];
        for (Guess g : p.getGuesses())
            guessBoard[g.getX()][g.getY()] = g;

        String[][] shipBoard = new String[Rules.BOARD_SIZE_X][Rules.BOARD_SIZE_Y];
        for (int x = 0; x < Rules.BOARD_SIZE_X; ++x)
            for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y)
                shipBoard[x][y] = ".";

        int shipID = 1;
        for (Ship s : p.getShips(key)) {
            int length = s.getType(key).length;
            int x = s.getX(key);
            int y = s.getY(key);
            if (s.isVertical(key)) {
                for (int i = 0; i < length; ++ i)
                    shipBoard[x][y+i] = Integer.toString(shipID);
            } else {
                for (int i = 0; i < length; ++ i)
                    shipBoard[x+i][y] = Integer.toString(shipID);
            }

            for (Guess hit : s.getHits(key))
                shipBoard[hit.getX()][hit.getY()] = Character.toString(symbols.charAt(shipID));

            ++shipID;
        }

        for (Guess g : oponent.getGuesses()) {
            if (g.getHitShip() == null)
                shipBoard[g.getX()][g.getY()] = "M";
        }



        String boardString = this.toString() + " Board\n";

        boardString += " " + alphabet.substring(0, Rules.BOARD_SIZE_X) + "\n";
        for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y) {
            boardString += Integer.toString(y);

            for (int x = 0; x < Rules.BOARD_SIZE_X; ++x) {

                if (guessBoard[x][y] == null)
                    boardString += ".";
                else
                    boardString += guessBoard[x][y].getHitShip() == null ? "H" : "M";

            }

            boardString += "\n";
        }

        boardString += "\n";
        boardString += " " + alphabet.substring(0, Rules.BOARD_SIZE_X) + "\n";
        for (int y = 0; y < Rules.BOARD_SIZE_Y; ++y) {
            boardString += Integer.toString(y);

            for (int x = 0; x < Rules.BOARD_SIZE_X; ++x)
                boardString += shipBoard[x][y];


            boardString += "\n";
        }

        return boardString;

    }
}