package fr.uge.balatri;

import fr.uge.balatri.controller.GameController;
import fr.uge.balatri.domain.blind.Blind;
import fr.uge.balatri.model.GameState;
import fr.uge.balatri.view.ConsoleView;
 
import java.util.List;

public class Main {
	public static void main(String[] args) {
		var blinds = List.of(
				new Blind("Blind 1", 300),
				new Blind("Blind 2", 450),
				new Blind("Blind 3", 600)
		);

		var gameState = new GameState(blinds);
		var view = new ConsoleView();
		var controller = new GameController(gameState, view);

		controller.gameLoop();
	}
	
}