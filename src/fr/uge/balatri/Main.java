package fr.uge.balatri;

import fr.uge.balatri.controller.GameController;
import fr.uge.balatri.domain.Blind;
import fr.uge.balatri.model.GameState;
import fr.uge.balatri.view.ConsoleView;
import fr.uge.balatri.view.GraphicView;

import java.awt.Color;
import java.util.List;

import com.github.forax.zen.Application;

public class Main {
	public static void main(String[] args) {
		
	    Application.run(new Color(24, 16, 36), context -> {
	        var blinds = List.of(
	            new Blind("Blind 1", 100),
	            new Blind("Blind 2", 150),
	            new Blind("Blind 3", 200)
	        );

	        var gameState = new GameState(blinds);
	        var view = new GraphicView(context);
	        var controller = new GameController(gameState, view);

	        controller.gameLoop();
	      });
		
//		var blinds = List.of(new Blind("Blind 1", 100), new Blind("Blind 2", 150), new Blind("Blind 3", 200));
//
//		var gameState = new GameState(blinds);
//		var view = new ConsoleView();
//		var controller = new GameController(gameState, view);
//
//		controller.gameLoop();
	}

}