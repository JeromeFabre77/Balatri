package fr.uge.balatri.controller;

import fr.uge.balatri.domain.HandEvaluator;
import fr.uge.balatri.domain.Planet;
import fr.uge.balatri.domain.card.Card;
import fr.uge.balatri.model.GameState;
import fr.uge.balatri.view.View;

import java.util.ArrayList;
import java.util.Objects;

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

		if (view.askAction(gameState)) {
			handlePlay();
		} else {
			handleDiscard();
		}
	}

	private void handlePlay() {
		var selectedCardIndices = view.askCardSelection();
		var cardsPlayed = new ArrayList<>(gameState.playHand(selectedCardIndices));

		var combination = HandEvaluator.evaluate(cardsPlayed);
		var scoreGained = combination.score(gameState.getPlanets(), cardsPlayed);

		gameState.addScore(scoreGained);
		gameState.decrementHandsRemainingInBlind();

		view.displayTurnResult(combination, Card.computeChips(cardsPlayed), scoreGained);

		if (gameState.isBlindBeaten()) {
			handleBlindBeaten();
		}
	}

	private void handleDiscard() {
		if (!gameState.canDiscard()) {
			throw new IllegalStateException("No discards remaining in current blind");
		}

		var selectedCardIndices = view.askDiscardSelection();
		gameState.discardCards(selectedCardIndices);
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
