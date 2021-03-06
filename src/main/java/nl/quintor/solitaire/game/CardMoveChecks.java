package nl.quintor.solitaire.game;

import nl.quintor.solitaire.game.moves.Help;
import nl.quintor.solitaire.game.moves.ex.MoveException;
import nl.quintor.solitaire.models.card.Card;
import nl.quintor.solitaire.models.card.Rank;
import nl.quintor.solitaire.models.card.Suit;
import nl.quintor.solitaire.models.deck.Deck;
import nl.quintor.solitaire.models.deck.DeckType;

/**
 * Library class for card move legality checks. The class is not instantiable, all constructors are private and all methods are
 * static. The class contains several private helper methods. All methods throw {@link MoveException}s, which can
 * contain a message that is fed to the {@link nl.quintor.solitaire.ui.UI}-implementation as error messages to be
 * shown to the user.
 */
public class CardMoveChecks {
    private CardMoveChecks(){}
    private final static String helpInstructions = new Help().toString();

    /**
     * Verifies that the player input for a CardMove is syntactically legal. Legal input consists of three parts:
     * the move command "M", the source location and the destination location.
     * The source location has to be the stock header, a stack header or a column coordinate.
     * The destination location has to be the stock header, a stack header or a column header (the column row is not
     * relevant because cards can only be added at the end of a column). The method verifies the syntax using regular
     * expressions.
     *
     * @param input the user input, split on the space character, cast to uppercase
     * @throws MoveException on syntax error
     */
    public static void checkPlayerInput(String[] input) throws MoveException{
        // TODO: Write implementation
    }

    /**
     * Verifies that a card move is possible given the source deck, the source card index and the destination deck.
     * Assumes that the {@link #checkPlayerInput(String[])} checks have passed.
     * {@link Deck} objects have a {@link DeckType} that is used in this method. The rank and suit of the actual cards
     * are not taken into consideration here.
     *
     * @param sourceDeck deck that the card(s) originate from
     * @param sourceCardIndex index of the (first) card
     * @param destinationDeck deck that the card(s) will be transferred to
     * @throws MoveException on illegal move
     */
    public static void deckLevelChecks(Deck sourceDeck, int sourceCardIndex, Deck destinationDeck) throws MoveException {
        // TODO: Write implementation
        int invisibleCards = sourceDeck.getInvisibleCards();
        if(sourceCardIndex < invisibleCards){
            throw new MoveException("You can't move an invisible card");
        }
        if(sourceDeck == destinationDeck){
            throw new MoveException("Move source and destination can't be the same");
        }
        if(sourceDeck.size() == 0){
            throw new MoveException("You can't move a card from an empty deck");
        }
        if(destinationDeck.getDeckType() == DeckType.STOCK){
            throw new MoveException("You can't move cards to the stock");
        }
        if(destinationDeck.getDeckType() == DeckType.STACK && sourceCardIndex != (sourceDeck.size() -1)) {
            throw new MoveException("You can't move more than 1 card at a time to a Stack Pile");
        }
    }

    /**
     * Verifies that a card move is possible given the rank and suit of the card or first card to be moved. Assumes the
     * {@link #checkPlayerInput(String[])} and {@link #deckLevelChecks(Deck, int, Deck)} checks have passed. The checks
     * for moves to a stack pile or to a column are quite different, so the method calls one of two helper methods,
     * {@link #checkStackMove(Card, Card)} and {@link #checkColumnMove(Card, Card)}.
     *
     * @param targetDeck deck that the card(s) will be transferred to
     * @param cardToAdd (first) card
     * @throws MoveException on illegal move
     */
    public static void cardLevelChecks(Deck targetDeck, Card cardToAdd) throws MoveException {
        // TODO: Write implementation
        int deckSize = targetDeck.size();
        Card targetCard;

        if (deckSize > 0) {
            targetCard = targetDeck.get(deckSize - 1);
        }
        else {
            targetCard = null;
        }

        if (targetDeck.getDeckType() != DeckType.STACK && targetDeck.getDeckType() != DeckType.COLUMN) {
            throw new MoveException("Target deck is neither Stack nor Column.");
        }
        if(targetDeck.getDeckType() == DeckType.STACK) {
            checkStackMove(targetCard, cardToAdd);
        }
        if(targetDeck.getDeckType() == DeckType.COLUMN) {
            checkColumnMove(targetCard, cardToAdd);
        }
    }

    // Helper methods

    /**
     * Verifies that the proposed move is legal given that the targetCard is the top of a stack pile.
     *
     * @param targetCard top card of a stack or null if the stack is empty
     * @param cardToAdd card to add to the stack
     * @throws MoveException on illegal move
     */
    static void checkStackMove(Card targetCard, Card cardToAdd) throws MoveException {
        // TODO: Write implementation
        if (targetCard == null) {
            if(cardToAdd.getRank() != Rank.ACE) {
                throw new MoveException("An Ace has to be the first card of a Stack pile");
            }
        }
        else {
            if (targetCard.getRank().ordinal() != cardToAdd.getRank().ordinal() - 1) {
                if(targetCard.getRank() != Rank.ACE || cardToAdd.getRank() != Rank.TWO) {
                    throw new MoveException("Stack piles can only hold cards increasing in rank from Ace to King");
                }
            }
            if (targetCard.getSuit() != cardToAdd.getSuit()) {
                throw new MoveException("Stack piles can only contain same-suit cards");
            }
        }
    }

    /**
     * Verifies that the proposed move is legal given that the targetCard is the last card of a column.
     *
     * @param targetCard last card of a column or null if the column is empty
     * @param cardToAdd card to add to the column
     * @throws MoveException on illegal move
     */
    static void checkColumnMove(Card targetCard, Card cardToAdd) throws MoveException {
        /// TODO: Write implementation
        if (targetCard == null) {
            if (cardToAdd.getRank() != Rank.KING) {
                throw new MoveException("A King has to be the first card in a Column");
            }
        }
        else {
            if (!opposingColor(targetCard, cardToAdd)) {
                throw new MoveException("Column cards have to alternate colors");
            }
            if (targetCard.getRank().ordinal() != cardToAdd.getRank().ordinal() + 1) {
                throw new MoveException("Columns hold alternating-color cards of decreasing rank from King to Ace");
            }
        }
    }

    /**
     * Helper method to determine if the provided cards are of opposing color (red versus black).
     *
     * @param card1 first card
     * @param card2 second card
     * @return true if the cards are of different colors
     */
    static boolean opposingColor(Card card1, Card card2){
        // TODO: Write implementation
        String c1Suit = card1.getSuit().toString();
        String c2Suit = card2.getSuit().toString();
        if(c1Suit.equals(c2Suit))
            return false;

        if((c1Suit.equals("CLUBS") || c1Suit.equals("SPADES")) && (c2Suit.equals("CLUBS") || c2Suit.equals("SPADES")))
            return false;

        return (!c1Suit.equals("DIAMONDS") && !c1Suit.equals("HEARTS")) || (!c2Suit.equals("DIAMONDS") && !c2Suit.equals("HEARTS"));
    }

    /**
     * Helper method to determine if the card's suit is colored red (Diamonds or Hearts).
     *
     *
     * @param card card to be tested for red color
     * @return true if card is either of suit Diamonds or Hearts
     * @throws RuntimeException exception when Joker card is checked with message 'Method redSuit() should not be used with Jokers'
     */
    static boolean redSuit(Card card){
        if (card.getSuit() == Suit.JOKER) throw new RuntimeException("Method redSuit() should not be used with Jokers");
        return card.getSuit() == Suit.DIAMONDS || card.getSuit() == Suit.HEARTS;
    }
}
