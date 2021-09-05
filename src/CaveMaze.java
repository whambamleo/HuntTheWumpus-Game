
/*
 * Name: Leoul Gezu
 * Date: March 7, 2021
 * CSC 202
 * Project 1-CaveMaze.java
 * 
 * Incomplete class that models a maze of caves for the "Hunt the Wumpus" game.
 * The maze of caves is defined in a file where the first line of the file is 
 * the number of caves and each subsequent line is the cave number, number of
 * adjacent caves followed by the cave number of each adjacent cave, and the 
 * name of the cave. 
 * 
 * One cave will be initialized to store CaveContents.BAT.
 * One cave will be initialized to store CaveContents.PIT.
 * Randomly one to three caves will be initialized to store CaveContents.WUMPUS.
 * 
 * @author Dave Reed
 * @revised by Diane Mueller
 * 
 * Document Assistance(who and describe; if no assistance, declare that fact):
 *	Dr. Mueller assisted me with the wumpusShuffle and wumpusSearch methods
 * 
 */
import java.util.*;
import java.io.*;

public class CaveMaze {
	private Random randGen = new Random();

	private Cave currentCave; // The cave the player is in
	private Cave[] caves; // All of the caves in the maze
	private int numWumpi; // Number of wumpi currently in the caves

	/**
	 * Constructs a CaveMaze from the data found in a file.
	 * 
	 * @param filename-the name of the cave data file
	 * @throws FileNotFoundException - if the filename received as a parameter is
	 *                               not found in the project.
	 * 
	 */
	public CaveMaze(String filename) throws FileNotFoundException {
		Scanner infile = new Scanner(new File(filename));

		int numCaves = infile.nextInt(); // num caves initialized to 20 caves
		this.caves = new Cave[numCaves]; // cave arraylist initialized for 20 caves

		for (int i = 0; i < caves.length; i++) { // a loop that goes through all the caves in the array
			int num = infile.nextInt(); // num is the cave's own number
			int numAdj = infile.nextInt(); // numAdj is the num of caves adj to it
			int[] adj = new int[numAdj]; // adj is an int array with the adj caves
			for (int a = 0; a < adj.length; a++) { // a loop that puts the nums of the adjacent caves into the adj cave

				adj[a] = infile.nextInt();
			}
			String name = infile.nextLine().trim(); // the name of the cave is stored
			this.caves[i] = new Cave(name, num, adj); // a cave object is created
		}

		currentCave = caves[0];
		currentCave.markAsVisited();

		int number = randGen.nextInt(3) + 1;
		numWumpi = number;

		for (int i = 0; i < numWumpi; i++) {
			caveFinder(1, 20).setContents(CaveContents.WUMPUS);

		}

		caveFinder(1, 20).setContents(CaveContents.BATS);
		caveFinder(1, 20).setContents(CaveContents.PIT);

	}

	/**
	 * Returns an empty cave object after searching for it through a section of the
	 * cave array defined by min and max
	 * 
	 * @param min- minimum index allowed
	 * @param max- maximum index allowed
	 * @return- an empty cave
	 */
	private Cave caveFinder(int min, int max) {
		int num = randGen.nextInt(max - min) + min;
		while (caves[num].getContents() != CaveContents.EMPTY) {
			num = randGen.nextInt(max - min) + min;
		}
		return caves[num];
	}

	/**
	 * Moves the player from the current cave along the specified tunnel, marking
	 * the new cave as visited. Also checks if the player accidentally goes into a
	 * cave with bats, wumpi or the pit inside
	 * 
	 * @param tunnel - the number of the tunnel to be traversed (1-number of
	 *               tunnels)
	 * @param player - the player roaming the caves
	 * 
	 * @return the message depending on the tunnel and the result depending on the
	 *         contents of the cave the tunnel leads to.
	 */
	public String move(int tunnel, Player player) {
		if (tunnel < 1 || tunnel > currentCave.getNumAdjacent()) {
			return "There is no tunnel number " + tunnel;
		} else {
			int caveNum = currentCave.getAdjCaveNumber(tunnel);
			currentCave = caves[caveNum];
			if (currentCave.getContents() == CaveContents.WUMPUS) {
				player.die();
				String message = "You've entered a cave with a wumpus…CHOMP CHOMP CHOMP!";
				return message;
			} else if (currentCave.getContents() == CaveContents.PIT) {
				player.die();
				String message = "You've fallen into the bottomless pit!";
				return message;
			} else if (currentCave.getContents() == CaveContents.BATS) {
				String message = "The bats dropped you into another cave!";
				currentCave = caveFinder(0, 20);
				currentCave.markAsVisited();

				return message;
			} else {
				currentCave.markAsVisited();
				String message = "Moving down tunnel " + tunnel + "...";

				return message;

			}

		}
	}

	/**
	 * Attempts to toss a stun grenade into the specified tunnel
	 * 
	 * @param tunnel - the number of the tunnel(1-number of tunnels) leading to cave
	 *               to be bombed
	 * @param player - the player roaming the caves
	 * 
	 * @return the message depending on the result of the tunnel and the cave's
	 *         contents
	 */
	public String toss(int tunnel, Player player) {
		if (player.getNumGrenades() != 0) {
			player.throwGrenade();
			Cave grenadedCave = caves[currentCave.getAdjCaveNumber(tunnel)];

			if (grenadedCave.getContents() == CaveContents.WUMPUS) { // you catch a wumpus
				boolean caughtWumpus = true;
				numWumpi -= 1;
				grenadedCave.setContents(CaveContents.EMPTY);
				String message = wumpusSearch(player, currentCave.getAdjCaveArray(), caughtWumpus);
				return message;

			}

			else if (grenadedCave.getContents() != CaveContents.WUMPUS) { // you don't catch a wumpus
				boolean caughtWumpus = false;
				String message = wumpusSearch(player, currentCave.getAdjCaveArray(), caughtWumpus);
				return message;

			} else {
				return null;
			}

		} else {
			String message = "You have no stun grenades to throw!";
			return message;
		}
	}

	/**
	 * A method that determines how a wumpus startled by a grenade behaves
	 * 
	 * @param playerCave-  cave that the player is in
	 * @param wumpiTunnel- tunnel that leads to the adjacent cave that the wumpus is
	 *                     in
	 * @return
	 */
	private int wumpusShuffle(Cave playerCave, int wumpiTunnel) {
		ArrayList<Cave> wumpusOptions = new ArrayList<Cave>();
		// System.out.println(wumpusOptions);
		for (int i = 0; i < caves[currentCave.getAdjCaveNumber(wumpiTunnel)].getAdjCaveArray().length; i++) {
			if (caves[caves[currentCave.getAdjCaveNumber(wumpiTunnel)].getAdjCaveArray()[i]]
					.getContents() == CaveContents.EMPTY) {
				wumpusOptions.add(caves[caves[currentCave.getAdjCaveNumber(wumpiTunnel)].getAdjCaveArray()[i]]);

			}

		}
		int index = randGen.nextInt(wumpusOptions.size());
		// System.out.println(index);
		wumpusOptions.get(index).setContents(CaveContents.WUMPUS);
		// System.out.println(wumpusOptions);
		return wumpusOptions.get(index).getCaveNumber();

	}

	/**
	 * A method that determines the consequences of a grenade throw by calling
	 * wumpusShuffle after performing a search for the wumpus and randomly deciding
	 * where they shuffle to
	 * 
	 * @param player
	 * @param currentCaveAdjCaveArray - the adjacent array where the cave that the
	 *                                grenade was thrown in is
	 * @param caughtWumpus-           a boolean that informs the method if a wumpus
	 *                                was stunned by the thrown grenade or not
	 * @return- the results depending on the method's deliberations
	 */
	private String wumpusSearch(Player player, int[] currentCaveAdjCaveArray, boolean caughtWumpus) {

		if (caughtWumpus) {
			for (int i = 0; i < currentCave.getAdjCaveArray().length; i++) {
				if (caves[currentCave.getAdjCaveArray()[i]].getContents() == CaveContents.WUMPUS) {
					int randomCaveNum = wumpusShuffle(currentCave, i + 1);
					if (randomCaveNum == currentCave.getCaveNumber()) {
						player.die();
						String message = "You caught a wumpus!" + "\n"
								+ "A startled wumpus charges into your cave...CHOMP CHOMP CHOMP";
						return message;
					}
				}
			}
			String message = "You caught a wumpus!";
			return message;
		} else {
			for (int i = 0; i < currentCave.getAdjCaveArray().length; i++) {
				// System.out.println(i);
				if (caves[currentCave.getAdjCaveArray()[i]].getContents() == CaveContents.WUMPUS) {
					int randomCaveNum = wumpusShuffle(currentCave, i + 1);
					if (randomCaveNum == currentCave.getCaveNumber()) {
						player.die();
						String message = "Missed, dagnabit!" + "\n"
								+ "A startled wumpus charges into your cave...CHOMP CHOMP CHOMP";
						return message;
					}
				}
			}
			String message = "Missed, dagnabit!";
			return message;
		}
	}

	/**
	 * Displays the current cave name and the names of adjacent caves. Caves that
	 * have not yet been visited are displayed as "unknown".
	 * 
	 * @return the message giving the current location and clues from the adjacent
	 *         caves
	 */
	public String showLocation() {
		String message = "You are currently in " + this.currentCave.getCaveName();

		ArrayList<String> clues = new ArrayList<String>();
		for (int i = 1; i <= currentCave.getNumAdjacent(); i++) {
			int caveIndex = currentCave.getAdjCaveNumber(i);
			Cave adjCave = caves[caveIndex];
			message += "\n    (" + i + ") " + adjCave.getCaveName();
		}

		// figure out how to only print warning once even for multiple wumpi
		boolean wumpusClue = true;
		for (int i = 0; i < this.currentCave.getNumAdjacent(); i++) {
			if (caves[currentCave.getAdjCaveArray()[i]].getContents() == CaveContents.BATS) {
				clues.add("You hear the flapping of wings close by.");
			} else if (caves[currentCave.getAdjCaveArray()[i]].getContents() == CaveContents.PIT) {
				clues.add("You feel a draft coming from one of the tunnels.");
			} else if (caves[currentCave.getAdjCaveArray()[i]].getContents() == CaveContents.WUMPUS) {
				if (wumpusClue == true) {
					clues.add("You smell an awful stench coming from somewhere nearby.");
					wumpusClue = false;
				}

			}
		}
		String clueString = "\n";
		Collections.shuffle(clues);
		for (int i = 0; i < clues.size(); i++) {
			clueString += clues.get(i) + "\n";
		}
//		System.out.print(cavesAsString()); // INTERNAL WUMPI MONITOR
		return message + clueString;
	}

	/**
	 * Reports the number of wumpi remaining in the maze
	 * 
	 * @return the number of wumpi remaining in the maze
	 */
	public int getNumWumpi() {
		return numWumpi;
	}

	/**
	 * Reports whether there are any wumpi remaining.
	 * 
	 * @return true if there is still a wumpus in some cave
	 */
	public boolean stillWumpi() {
		return (numWumpi != 0);
	}

	// returns a string with one cave's information per line
	// Prof Mueller used this to help in debugging her program!
	private String cavesAsString() {
		String caveList = "";
		for (int i = 0; i < caves.length; i++) {
			caveList = caveList + caves[i] + "\n";

		}
		return caveList;
	}

}