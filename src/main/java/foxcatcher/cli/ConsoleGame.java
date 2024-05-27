package foxcatcher.cli;


import foxcatcher.model.FoxCatcherGameModel;
import foxcatcher.model.Position;

import game.console.TwoPhaseMoveGame;

import java.util.Scanner;

public class ConsoleGame {

    public static void main(String[] args) {
        FoxCatcherGameModel state = new FoxCatcherGameModel();
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
