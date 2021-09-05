
/*
 * Name:Leoul Gezu
 * Date: February 27/2021
 * CSC 202
 * Project 1-Player.java
 * 
 * Cave defines the discrete cave structures that are interconnected through tunnels. 
 * 
 * Document Assistance(who and describe; if no assistance, declare that fact):
 * 	Dr. Mueller pointed out a bug in my getAdjCaveNumber method and helped with the solution
 * 
 */
import java.util.*;

public class Cave {

	private String caveName;
	private int caveNumber;
	private int[] adjCaveArray;
	private CaveContents contents;
	private boolean beenVisited;

	/**
	 * Constructs a Cave object with some name, number and an array of adjacent
	 * caves
	 * 
	 * @param name        - The name assigned to the Cave object
	 * @param number      - The number assigned to the Cave object
	 * @param adjCaveList - The array of caves adjacent to the Cave object
	 */
	public Cave(String name, int number, int[] adjCaveList) {
		caveName = name;
		caveNumber = number;
		adjCaveArray = adjCaveList;
		contents = CaveContents.EMPTY;

	}

	/**
	 * Returns the cave number of the cave reached by traveling through "tunnel" of
	 * the cave the method was called upon
	 * 
	 * @param tunnel - the tunnel leading to the desired adjacent cave
	 * @return adjCaveArray[tunnel-1]- cave number of the adjacent cave
	 */
	public int getAdjCaveNumber(int tunnel) {
		try {
			return adjCaveArray[tunnel - 1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}

	}

	/**
	 * Returns the adjacent cave array of the cave
	 * 
	 * @return-adjCaveArray
	 */
	public int[] getAdjCaveArray() {
		return adjCaveArray;
	}

	/**
	 * Returns the Cave object's name
	 * 
	 * @return-caveName
	 */
	public String getCaveName() {
		while (beenVisited) {
			return caveName;
		}
		return "unknown";
	}

	/**
	 * Returns the Cave object's number
	 * 
	 * @return-caveNumber
	 */
	public int getCaveNumber() {
		return caveNumber;
	}

	/**
	 * Returns the Cave object's contents
	 * 
	 * @return-contents
	 */
	public CaveContents getContents() {
		return contents;
	}

	/**
	 * Returns the number of adjacent caves
	 * 
	 * @return-adjCaveArray.length
	 */
	public int getNumAdjacent() {
		return adjCaveArray.length;
	}

	/**
	 * Marks a cave as visited by setting beenVisited to true
	 */
	public void markAsVisited() {
		beenVisited = true;
	}

	/**
	 * Sets the content of a Cave object
	 * 
	 * @param content
	 */
	public void setContents(CaveContents content) {
		contents = content;
	}

	/**
	 * toString method that returns a clean cave tag
	 */
	public String toString() {
		String caveTag = "Cave number: " + caveNumber + ", Cave name: " + caveName + ", Adj Cave List: "
				+ Arrays.toString(adjCaveArray) + ", Visited: " + beenVisited + ", Contents: " + contents;
		return caveTag;
	}

}
