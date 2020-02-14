package nl.quintor.solitaire.game;

import nl.quintor.solitaire.game.moves.ex.MoveException;
import nl.quintor.solitaire.models.deck.Deck;
import nl.quintor.solitaire.models.deck.DeckType;
import nl.quintor.solitaire.models.state.GameState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;

/**
 * Library class for GameState initiation and status checks that are called from {@link nl.quintor.solitaire.Main}.
 * The class is not instantiable, all constructors are private and all methods are static.
 */
public class GameStateController {
    private GameStateController(){}

    /**
     * Creates and initializes a new GameState object. The newly created GameState is populated with shuffled cards. The
     * stack pile and column maps are filled with headers and Deck objects. The column decks have an appropriate number
     * of invisible cards set.
     *
     * @return a new GameState object, ready to go
     */
    public static GameState init(){
        GameState initialGameState = new GameState();
        Deck baseDeck = Deck.createDefaultDeck();
        Collections.shuffle(baseDeck);
        String[] headers = new String[]{"SA", "SB", "SC", "SD"};
        Arrays.stream(headers)
            .forEach(header -> initialGameState.getStackPiles()
                .put(header, new Deck(DeckType.STACK)));

        String[] columnHeaders = new String[]{"A", "B", "C", "D", "E", "F", "G"};
        for(int i = 0; i < columnHeaders.length; i++){
            Deck deckColumn = new Deck(DeckType.COLUMN);
            deckColumn.setInvisibleCards(i);

            for(int j = 0; j < i +1; j++){
                deckColumn.add(baseDeck.remove(0));
            }
            initialGameState.getColumns().put(columnHeaders[i], deckColumn);
        }
        initialGameState.setStartTime(LocalDateTime.now());
        initialGameState.getStock().add(baseDeck.remove(0));
        initialGameState.getWaste().addAll(baseDeck);
        return initialGameState;
    }

    /**
     * Applies a score penalty to the provided GameState object based on the amount of time passed.
     * The following formula is applied : "duration of game in seconds" / 10 * -2
     *
     * @param gameState GameState object that the score penalty is applied to
     */
    public static void applyTimePenalty(GameState gameState){
        // TODO: Write implementation

        int gameTimeSeconds = Math.toIntExact(Duration.between(gameState.getStartTime(), gameState.getEndTime()).toSeconds());
        int points = (int) gameState.getTimeScore() - (gameTimeSeconds / 10 * 2);
        gameState.setTimeScore(points);
    }

    /**
     * Applies a score bonus to the provided GameState object based on the amount of time passed. Assumes the game is won.
     * When the duration of the game is more than 30 seconds then apply : 700000 / "duration of game in seconds"
     *
     * @param gameState GameState object that the score penalty is applied to
     */
    public static void applyBonusScore(GameState gameState){
        // TODO: Write implementation
        int bonusPoints = 0;
        // Calculate the difference in time between the start and end using the Duration instance. Convert to int using the Math class.
        int gameTimeSeconds = Math.toIntExact(Duration.between(gameState.getStartTime(), gameState.getEndTime()).toSeconds());

        if(gameTimeSeconds > 30) {
            bonusPoints = 700000 / gameTimeSeconds;
        }

        gameState.setTimeScore(bonusPoints);
    }

    /**
     * Detects if the game has been won, and if so, sets the gameWon flag in the GameState object.
     * The game is considered won if there are no invisible cards left in the GameState object's columns and the stock
     * is empty.
     *
     * @param gameState GameState object of which it is determined if the game has been won
     */
    public static void detectGameWin(GameState gameState){
        boolean checkInvisCards = gameState.getColumns().values().stream().anyMatch(d -> (d.getInvisibleCards() > 0));
        if(!checkInvisCards && gameState.getStock().isEmpty()){gameState.setGameWon(true);}
    }
}
