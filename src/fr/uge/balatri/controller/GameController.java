package fr.uge.balatri.controller;

import fr.uge.balatri.domain.HandEvaluator;
import fr.uge.balatri.domain.Planet;
import fr.uge.balatri.domain.card.Card;
import fr.uge.balatri.model.GameState;
import fr.uge.balatri.view.View;

import java.util.ArrayList;
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
		view.displayGameOver(gameState.getCumulatedScore(), gameState.isGameWon());
	}

	private void turnLoop() {
		view.displayGameState(gameState);

		var playerAction = view.askTurn(gameState);

		if (playerAction.isPlay()) {
			handlePlay(playerAction.selectedIndices());
		} else {
			handleDiscard(playerAction.selectedIndices());
		}
	}

	private void handlePlay(Set<Integer> selectedIndices) {
		var cardsPlayed = new ArrayList<>(gameState.playHand(selectedIndices));

		var combination = HandEvaluator.evaluate(cardsPlayed);
		var scoreGained = combination.score(gameState.getPlanets(), cardsPlayed);

		gameState.addScore(scoreGained);
		gameState.decrementHandsRemainingInBlind();

		view.displayTurnResult(gameState, combination, Card.computeChips(cardsPlayed), scoreGained);

		if (gameState.isBlindBeaten()) {
			handleBlindBeaten();
		}
	}

	private void handleDiscard(Set<Integer> selectedIndices) {
		if (!gameState.canDiscard()) {
			throw new IllegalStateException("No discards remaining in current blind");
		}

		gameState.discardCards(selectedIndices);
	}
  
	private void handleBlindBeaten() {
		var planet = Planet.random();
		gameState.addPlanet(planet);

		view.displayBlindBeaten(gameState, planet);

		if (!gameState.isGameWon()) {
			gameState.nextBlind();
		}
	}

}
