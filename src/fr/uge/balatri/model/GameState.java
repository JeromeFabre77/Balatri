package fr.uge.balatri.model;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fr.uge.balatri.domain.blind.Blind;
import fr.uge.balatri.domain.card.Card;
import fr.uge.balatri.domain.deck.Deck;
import fr.uge.balatri.domain.deck.Discard;
import fr.uge.balatri.domain.hand.PlayerHand;
import fr.uge.balatri.domain.planet.Planet;

public final class GameState {

    public static final int MAX_HAND_PER_BLIND = 4;

    private final Deck deck;
    private final Discard discard;
    private final PlayerHand hand;
    private int handsRemainingInBlind;

    private final List<Blind> blinds;
    private int currentBlindIndex;

    private int cumulatedScore;

    private final Map<Planet, Integer> planets;

    public GameState(List<Blind> blinds) {
        Objects.requireNonNull(blinds);
        if (blinds.isEmpty()) {
            throw new IllegalArgumentException("Blinds list cannot be empty");
        }

        this.deck = new Deck();
        this.discard = new Discard();
        this.hand = new PlayerHand(deck.draw(PlayerHand.MAX_HAND_SIZE));
        this.handsRemainingInBlind = MAX_HAND_PER_BLIND;

        this.blinds = List.copyOf(blinds);
        this.currentBlindIndex = 0;

        this.cumulatedScore = 0;

        this.planets = new EnumMap<>(Planet.class);
    }

    public int getDeckSize() {
        return deck.size();
    }

    public int getDiscardSize() {
        return discard.size();
    }

    public List<Card> getHandCards() {
        return hand.get();
    }

    public int getHandsRemainingInBlind() {
        return handsRemainingInBlind;
    }

    public int getCumulatedScore() {
        return cumulatedScore;
    }

    public Map<Planet, Integer> getPlanets() {
        return Map.copyOf(planets);
    }

    public Blind currentBlind() {
        return blinds.get(currentBlindIndex);
    }

    public int totalBlinds() {
        return blinds.size();
    }

    public int getCurrentBlindIndex() {
        return currentBlindIndex;
    }

    public boolean isBlindBeaten() {
        return cumulatedScore >= currentBlind().score();
    }

    public boolean isGameOver() {
        return !isBlindBeaten() && handsRemainingInBlind == 0;
    }

    public boolean isGameWon() {
        return isBlindBeaten() && currentBlindIndex == blinds.size() - 1;
    }

    public List<Card> playHand(Set<Integer> selectedIndices) {
        Objects.requireNonNull(selectedIndices);

        var selectedCards = hand.selectCards(selectedIndices);

        discard.addMany(selectedCards);

        refillHand();

        return selectedCards;
    }

    public void addScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score must be non-negative");
        }

        cumulatedScore += score;
    }

    public void decrementHandsRemainingInBlind() {
        if (handsRemainingInBlind <= 0) {
            throw new IllegalStateException("No hands remaining in current blind");
        }

        handsRemainingInBlind--;
    }

    public void addPlanet(Planet planet) {
        Objects.requireNonNull(planet);

        planets.merge(planet, 1, Integer::sum);
    }

    public void nextBlind() {
        if (!isBlindBeaten()) {
            throw new IllegalStateException("Current blind is not beaten yet");
        }

        if (currentBlindIndex >= blinds.size() - 1) {
            throw new IllegalStateException("No more blinds available");
        }

        discard.addMany(hand.get());
        hand.clear();

        deck.refillFrom(discard.drawAll());

        hand.refill(deck.draw(PlayerHand.MAX_HAND_SIZE));

        currentBlindIndex++;
        cumulatedScore = 0;
        handsRemainingInBlind = MAX_HAND_PER_BLIND;
    }

    private void refillHand() {
        var missing = hand.missingCards();

        if (missing > 0) {
            if (!deck.hasEnoughCards(missing)) {
                deck.refillFrom(discard.drawAll());
            }

            hand.refill(deck.draw(missing));
        }
    }
}
