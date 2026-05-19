package fr.uge.balatri.controller;

import fr.uge.balatri.domain.combination.Combination;
import fr.uge.balatri.domain.handevaluator.HandEvaluator;
import fr.uge.balatri.domain.planet.Planet;
import fr.uge.balatri.model.GameState;
import fr.uge.balatri.view.View;

import java.util.Objects;
import java.util.Set;

public final class GameController {

    private final GameState gameState;
    private final View view;

    public GameController(GameState gameState, View view) {
        Objects.requireNonNull(gameState);
        Objects.requireNonNull(view);

        this.gameState = gameState;
        this.view = view;
    }

    public void gameLoop() {
        while (!gameState.isGameOver() && !gameState.isGameWon()) {
            turnLoop();
        }

        if (gameState.isGameWon()) {
            view.displayGameWon(gameState.getCumulatedScore());
        } else {
            view.displayGameOver(gameState.getCumulatedScore());
        }
    }

    private void turnLoop() {
        view.displayGameState(gameState);

        var selectedCardIndices = view.askCardSelection();
        var cardsPlayed = gameState.playHand(selectedCardIndices);

        var combination = HandEvaluator.evaluate(cardsPlayed);
        var scoreGained = combination.score(gameState.getPlanets());

        gameState.addScore(scoreGained);
        gameState.decrementHandsRemainingInBlind();

        view.displayTurnResult(combination, scoreGained);

        if (gameState.isBlindBeaten()) {
            handleBlindBeaten();
        }
    }

    private void handleBlindBeaten() {
        var planet = Planet.random();
        gameState.addPlanet(planet);

        view.displayBlindBeaten(planet);

        if (!gameState.isGameWon()) {
            gameState.nextBlind();
        }
    }

}
