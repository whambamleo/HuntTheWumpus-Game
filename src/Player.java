/*
 * Name:Leoul Gezu
 * Date: February 27/2021
 * CSC 202
 * Project 1-Player.java
 * 
 * Player defines the "human" player for the Hunt the Wumpus game. 
 * The player gets 3 grenades for every wumpus in the maze.
 * 
 * Document Assistance(who and describe; if no assistance, declare that fact):
 *  I received no assistance in the development of this class
 */

public class Player {
	private static final int NUM_GRENADES_PER_WUMPI = 3;
	private int numGrenades;
	private boolean isAlive;

	/**
	 * Constructs a player with "NUM_GRENADES_PER_WUMPI" grenades per "numWumpi"
	 * 
	 * @param numWumpi - number of wumpi to initialize
	 */
	public Player(int numWumpi) {
		isAlive = true;
		numGrenades = numWumpi * NUM_GRENADES_PER_WUMPI;

	}

	/**
	 * Checks if the player is still alive
	 * 
	 * @return - returns true if player is alive, returns false otherwise
	 */
	public boolean alive() {
		return isAlive;
	}

	/**
	 * Sets the player's isAlive data-field to false (RIP)
	 */
	public void die() {
		isAlive = false;
	}

	/**
	 * Method for returning numGrenades, the number of remaining grenades a player
	 * has
	 * 
	 * @return - remaining numGrenades data-field
	 */
	public int getNumGrenades() {
		return numGrenades;
	}

	/**
	 * Method for throwing a grenade
	 * 
	 * @return - If the player is alive and still has grenades left, throws a
	 *         grenade (numGrenades is reduced by one) and returns true. Otherwise,
	 *         returns false
	 */
	public boolean throwGrenade() {
		while (isAlive) {
			if (numGrenades != 0) {
				numGrenades -= 1;
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * toString method that returns a clean player tag
	 */
	public String toString() {
		String playerTag = "alive: " + isAlive + ", num grenades: " + numGrenades;
		return playerTag;
	}

}
