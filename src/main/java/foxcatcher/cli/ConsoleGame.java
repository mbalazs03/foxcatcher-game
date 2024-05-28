package foxcatcher.cli;


import foxcatcher.model.FoxCatcherGameModel;
import foxcatcher.model.Position;

import game.console.TwoPhaseMoveGame;

import java.util.Scanner;

/**
 * The ConsoleGame class serves as the entry point for running the FoxCatcher game in a console environment.
 * It initializes the game model and starts the game, allowing users to input moves via the console.
 */
public class ConsoleGame {
    public static void main(String[] args) {
        FoxCatcherGameModel state = new FoxCatcherGameModel();
        TwoPhaseMoveGame<Position> game = new TwoPhaseMoveGame<>(state, ConsoleGame::parseMove);
        game.start();
    }

    /**
     * Parses a move input string and converts it into a Position object.
     * The input string is expected to contain two integers separated by whitespace.
     *
     * @param s The input string representing the move.
     * @return A {@link Position} object representing the parsed move.
     * @throws IllegalArgumentException if the input string does not match the expected format.
     */
    public static Position parseMove(String s) {
        s = s.trim();
        if (!s.matches("\\d+\\s+\\d+")) {
            throw new IllegalArgumentException();
        }
        var scanner = new Scanner(s);
        return new Position(scanner.nextInt(), scanner.nextInt());
    }
}
