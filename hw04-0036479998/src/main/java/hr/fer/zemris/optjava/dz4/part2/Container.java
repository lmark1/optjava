package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Container {

	public List<StickColumn> columnList;
	public int columnCount;
	public int fitness;
	public int maximumHeight;
	public int[] positionTrack;
		
	/**
	 * Genereta a random container based on the given seed
	 * 
	 * @param seed
	 */
	public Container(List<Stick> seed, int maximumHeight, Random random) {
		this.maximumHeight = maximumHeight;
		this.columnList = new ArrayList<>();
		positionTrack = new int[seed.size()];
		
		// Initialize stick columns
		int k = 0;
		for (Stick stick : seed) {
			StickColumn column = new StickColumn();
			column.addStick(stick);
			columnList.add(column);
			k++;
		}
		
		// Find maximum index
		int maxColumnIndex = columnList.size();
				
		// Randomly scramble stick columns
		for (int i = 0; i < columnList.size(); i++) {
			
			// Try to stack with random colum index
			int randomIndex = random.nextInt(maxColumnIndex);
			
			// Extract columns
			StickColumn randomCol = columnList.get(randomIndex);
			StickColumn currentCol = columnList.get(i);
			
			// If they are already empty do not switch
			if (randomCol.isEmtpy() || currentCol.isEmtpy()) {
				continue;
			}
			
			// If total column heights is valid then combine
			if (randomCol.totalHeight + currentCol.totalHeight <= maximumHeight) {
				currentCol.addSticks(randomCol.getSticks()); // Add all sticks to 1. column
				randomCol.removeAllSticks(); // Remove all sticks from 2. column
			}
		}
		
		// Clear column list from all empty columns
		k = 0;
		while (k < columnList.size()) {
			if (columnList.get(k).isEmtpy()) {
				columnList.remove(k);
			} else {
				k++;
			}
		}
		
		this.columnCount = columnList.size();
		this.fitness = - columnList.size();
		//initializePositionTracker();
	}
	
	public Container() { }

	public Container(List<StickColumn> colList) {
		this.columnList = colList;
		this.fitness = -colList.size();
		this.columnCount = colList.size();
		
		int maxIndex = -1;
		for (StickColumn stickColumn : colList) {
			for (Stick stick : stickColumn.getSticks()) {
				if (stick.index > maxIndex) {
					maxIndex = stick.index;
				}
			}
		}
		
		positionTrack = new int[maxIndex];
		//initializePositionTracker();
	}
	
	private void initializePositionTracker() {
		int pos = 0;
		for (StickColumn stickColumn : columnList) {
			for (Stick stick : stickColumn.getSticks()) {
				positionTrack[stick.index] = pos;
			}
			
			pos++;
		}
	}

	/**
	 * @return Duplicate column
	 */
	public Container duplicate() {
		Container dupe = new Container();
		dupe.columnList = new ArrayList<>();
		for (StickColumn stickCol : this.columnList) {
			dupe.columnList.add(stickCol.duplicate());
		}
		dupe.fitness = this.fitness;
		dupe.columnCount = this.columnCount;
		
		return dupe;
	}
		
	@Override
	public String toString() {
		return "Fitness: " + fitness + ", " + columnList.toString();
	}
	
}
