/**
 * Simple, text-based interface for playing "Hunt the Wumpus".
 * The user wins the game by finding all of the wumpi before
 * being mauled by a wumpus or falling into the endless pit.
 * 
 * @author Dave Reed
 * revised Diane Mueller
 * 
 * Students, don't change this code.
 */

import java.util.*;
import java.io.FileNotFoundException;

public class WumpusTerminal {
	public static void main(String[] args) throws FileNotFoundException {
		CaveMaze maze = new CaveMaze("caves.txt");
		Player player = new Player(maze.getNumWumpi());   // a random num of wumpi from 1 to 3 in generated in the cavemaze constructor

		System.out.println("HUNT THE WUMPUS");
		System.out.println("Your mission is to explore the maze of caves and");
		System.out.println("capture all the wumpi (without getting yourself mauled).");
		System.out.println("\nTo move to an adjacent cave, enter 'M' and the tunnel number.");
		System.out.println("To toss a stun grenade into a cave, enter 'T' and the tunnel number.");

		Scanner input = new Scanner(System.in);
		while (player.alive() && maze.stillWumpi()) {                          //GAME LOOP: conditions: player is still alive and there are still wumpi in the maze
			System.out.println("\n" + maze.showLocation());

			System.out.print("\nWhat do you want to do? ");

			try {
				String userInput = input.nextLine();
				userInput = userInput.replaceAll("\\s", "");
				if (userInput.length() != 0) {
					char action = userInput.toLowerCase().charAt(0);
					if (action == 'q') {
						System.out.println("Nobody likes a quitter.");
						break;
					}
					if (action == 't') {
						int tunnelNum = Integer.parseInt(userInput.substring(1));
						String response = maze.toss(tunnelNum, player);
						System.out.println(response);
					} else if (action == 'm') {
						int tunnelNum = Integer.parseInt(userInput.substring(1));
						String response = maze.move(tunnelNum, player);
						System.out.println(response);
					} else {
						System.out.println("Unrecognized command -- please try again.");
					}
				} else {
					System.out.println("Not q, m, t");
					System.out.println("Unrecognized command -- please try again.");
				}

			} catch (IndexOutOfBoundsException|NumberFormatException e) {
				System.out.println("Unrecognized command -- please try again.");
			} 
		}
		
		if (!maze.stillWumpi()) {
			System.out.println("\nYou've caught all of the wumpi!");
		}
		
		System.out.println("\nGAME OVER");
	}
}
