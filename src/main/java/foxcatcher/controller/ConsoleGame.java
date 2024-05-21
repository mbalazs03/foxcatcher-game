package foxcatcher.controller;


import foxcatcher.model.FoxCatcherGameState;
import foxcatcher.model.Position;
import game.console.TwoPhaseMoveGame;

import java.util.Scanner;

public class ConsoleGame {

    public static void main(String[] args) {
        FoxCatcherGameState state = new FoxCatcherGameState();
        TwoPhaseMoveGame<Position> game = new TwoPhaseMoveGame<>(state, ConsoleGame::parseMove);
        game.start();
    }

    public static Position parseMove(String s) {
        s = s.trim();
        if (!s.matches("\\d+\\s+\\d+")) {
            throw new IllegalArgumentException();
        }
        var scanner = new Scanner(s);
        return new Position(scanner.nextInt(), scanner.nextInt());
    }
}
