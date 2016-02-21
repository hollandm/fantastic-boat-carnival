package Model;

import Brains.BaseBrain;
import Brains.HeuristicAI;
import Brains.Human;
import Brains.RandomAI;

import java.util.ArrayList;
import java.util.Hashtable;

public class Game {

    public class DataKey {

        private final Player playerAccess;
        private boolean pregameAccess = true;


        private DataKey(Player player) {
            this.playerAccess = player;
        }

        public boolean hasPregameAccess() {
            return pregameAccess;
        }

        public void revokePregameAccess(DataKey key) {
            if (key.isMasterKey())
                pregameAccess = false;
        }

        public boolean isMasterKey() {
            return this.playerAccess == null;
        }

        public Player getPlayer() {
            return this.playerAccess;
        }
    }


    private DataKey masterKey;

    private Hashtable<Player, BaseBrain> playerBrainMap = new Hashtable<>();

    private ArrayList<BaseBrain> player_brains = new ArrayList<>(2);
    private ArrayList<BaseBrain> spectator_brains = new ArrayList<>();
    private boolean gameRunning = false;

    public void addPlayer(BaseBrain brain) {

        if (player_brains.contains(brain))
            return;
        if (spectator_brains.contains(brain))
            return;
        if (gameRunning)
            return;

        player_brains.add(brain);
    }

    public void addSpectator(BaseBrain brain) {

        if (player_brains.contains(brain))
            return;
        if (spectator_brains.contains(brain))
            return;
        if (gameRunning)
            return;

        spectator_brains.add(brain);
    }

    public Game() {
        masterKey = new DataKey(null);
    }

    public void runGame() {

        if (player_brains.size() != 2)
            return;

        gameRunning = true;


        GameState gameState;
        {
            BaseBrain p1Brain = player_brains.get(0);
            Player p1 = new Player(p1Brain.id, p1Brain.name);
            playerBrainMap.put(p1, p1Brain);
            DataKey p1Key = new DataKey(p1);
            p1Brain.setKey(p1Key);

            BaseBrain p2Brain = player_brains.get(1);
            Player p2 = new Player(p2Brain.id, p2Brain.name);
            playerBrainMap.put(p2, p2Brain);
            DataKey p2Key = new DataKey(p2);
            p2Brain.setKey(p2Key);

            gameState = new GameState(p1, p2);

            do {
                p1Brain.placeShips(gameState);
            } while (!isShipPlacementValid(gameState, masterKey));
            p1Key.revokePregameAccess(masterKey);
            gameState.nextTurn(masterKey);


            do {
                p2Brain.placeShips(gameState);
            } while (!isShipPlacementValid(gameState, masterKey));
            p2Key.revokePregameAccess(masterKey);
            gameState.nextTurn(masterKey);
        }


        do {

            Player currentPlayer = gameState.getCurrentPlayer();
            BaseBrain currentBrain = playerBrainMap.get(currentPlayer);

            Guess guess;
            do {
                guess = currentBrain.makeGuess(gameState);
            } while (!isGuessValid(gameState, guess));

            currentPlayer.addGuess(masterKey, guess);
            fire(gameState, guess);

//            System.out.println(gameState.getBoardString(masterKey, currentPlayer));

            if (isGameOver(gameState))
                break;

            gameState.nextTurn(masterKey);

        } while (true);

        gameState.setWinner(masterKey, gameState.getCurrentPlayer());

    }

    private boolean isGameOver(GameState gameState) {

        for (Player p : gameState.getPlayerArray()) {

            boolean playerHasShips = false;

            shipLoop:
            for (Ship s : p.getShips(masterKey)) {
                if (!s.isSunk()) {
                    playerHasShips = true;
                    break shipLoop;
                }
            }

            if (!playerHasShips) {
                return true;
            }

        }

        return false;
    }

    public static boolean isShipPlacementValid(GameState gameState, Game.DataKey key) {

        if (gameState == null)
            return false;

        if (key == null)
            return true;

        Player player = gameState.getCurrentPlayer();

        //
        if (!key.isMasterKey() && key.getPlayer() != player)
            return true;

        boolean[][] board = new boolean[Rules.BOARD_SIZE_X][Rules.BOARD_SIZE_Y];

        for (Ship s : player.getShips(key)) {

            int length = s.getType(key).length;
            int x = s.getX(key);
            int y = s.getY(key);

            // we need to check x or y based on the vertical orientation of the ship
            if (s.isVertical(key)) {

                // Ship fits entirely on the board
                if (y < 0 || y + length-1 >= Rules.BOARD_SIZE_Y || x < 0 || x >= Rules.BOARD_SIZE_X)
                    return false;

                // Ship does not overlap any other ships
                for (int i = 0; i < length; ++i) {
                    if (board[x][y + i])
                        return false;
                    board[x][y + i] = true;
                }

            } else {

                // Ship fits entirely on the board
                if (x < 0 || x + length-1 >= Rules.BOARD_SIZE_X || y < 0 || y >= Rules.BOARD_SIZE_Y)
                    return false;

                // Ship does not overlap any other ships
                for (int i = 0; i < length; ++i) {
                    if (board[x + i][y])
                        return false;
                    board[x + i][y] = true;
                }

            }
        }

        return true;
    }

    public static boolean isGuessValid(GameState gameState, Guess guess) {

        if (guess == null || gameState == null)
            return false;

        Player player = gameState.getCurrentPlayer();

        // Is it a spot on the board
        if (guess.getX() < 0 || guess.getY() < 0 || guess.getX() >= Rules.BOARD_SIZE_X || guess.getY() >= Rules.BOARD_SIZE_Y)
            return false;

        //Has the player guessed that tile already
        for (Guess pastGuess : player.getGuesses(null))
            if (guess.equals(pastGuess))
                return false;

        return true;
    }

    private void fire(GameState gameState, Guess g) {

        Player target = gameState.getOpposingPlayer();


        for (Ship s : target.getShips(masterKey)) {

            if (s.checkIfShotHits(masterKey, g)) {
                g.setHitShip(masterKey, s);
                s.addHit(masterKey, g);
            }
        }
    }

    public void showTurn() {

    }


    public static void main(String[] args) {

        Game g = new Game();
        BaseBrain b1 = new HeuristicAI();
        BaseBrain b2 = new HeuristicAI();

        g.addPlayer(b1);
        g.addPlayer(b2);

        g.runGame();

    }
}
