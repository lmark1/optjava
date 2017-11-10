package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz4.model.ICrossover;

public class ContainerCrossover implements ICrossover<Container>{

	private int maxHeight;
	
	public ContainerCrossover(int maxHeight) {
		this.maxHeight = maxHeight;
	}
	
	@Override
	public Container[] cross(Container parent1, Container parent2,
			Random random) {
		
		List<StickColumn> sc1 = parent1.duplicate().columnList;
		List<StickColumn> sc2 = parent2.duplicate().columnList;
		
		// Get first parent cutting points
		int cp11 = random.nextInt(sc1.size());
		int cp12 = cp11 + random.nextInt(sc1.size() - cp11);
		
		// Get second parent cutting points
		int cp21 = random.nextInt(sc2.size());
		int cp22 = cp21 + random.nextInt(sc2.size() - cp21);
		
		// Extract crossover columns
		List<StickColumn> crossCol1 = new ArrayList<>();
		for(int i = cp11; i <= cp12; i++) {
			StickColumn addSC = sc1.get(i).duplicate();
			addSC.setInserted(true);
			crossCol1.add(addSC);
		}
		//System.out.println("Cross1: " + crossCol1);
		
		List<StickColumn> crossCol2 = new ArrayList<>();
		for (int i = cp21; i <= cp22; i++) {
			StickColumn addSC = sc2.get(i).duplicate();
			addSC.setInserted(true);
			crossCol2.add(addSC);
		}
		//System.out.println("Cross2: " + crossCol2);
		
		// Insert crossofer collumns into other lists
		// 1st cross column is inserted in 1. crossover point (cp21) of the second list
		// 2nd cross column is inserted in 2. crossover point (cp12) of first list
		sc2.addAll(cp21, crossCol1);
		sc1.addAll(cp12, crossCol2);
		
		// Check for duplicates in the inserted elements. Remove columns with duplicate
		// elements. Save non duplicates
		List<Stick> nonDupes1 = getNonDuplicates(sc1, crossCol2);
		//System.out.println("Extras1: " + nonDupes1);
		
		List<Stick> nonDupes2 = getNonDuplicates(sc2, crossCol1);
		//System.out.println("Extras2: "+ nonDupes2);
		
		// Clear empty column
		clearEmptyColumns(sc1);
		clearEmptyColumns(sc2);
		
		// Add all non duplicates
		addAllNonDuplicates(nonDupes2, sc2);
		addAllNonDuplicates(nonDupes1, sc1);
		
		Container c1 = new Container(sc1);
		Container c2 = new Container(sc2);
		
		return new Container[] {c1, c2};
	} 
	
	private void addAllNonDuplicates(List<Stick> nonDupes, List<StickColumn> sc) {
		// Add all the extras back to their parents
		for (Stick nd1 : nonDupes) {
			boolean added = false;
			
			// Try to find a place to add it...
			for (int i = 0; i < sc.size(); i++) {
				if (nd1.height + sc.get(i).totalHeight <= maxHeight) {
					sc.get(i).addStick(nd1);
					added = true;
					break;
				}
			}
			
			// ... if no place is found add a new column
			if (!added) {
				StickColumn newCol = new StickColumn();
				newCol.addStick(nd1);
				sc.add(newCol);
			}
		}
	}
	
	private void clearEmptyColumns(List<StickColumn> sc) {
		int k = 0;
		while (k < sc.size()) {
			if (sc.get(k).isEmtpy()) {
				sc.remove(k);
			} else {
				k++;
			}
		}
	}
	
	private List<Stick> getNonDuplicates(List<StickColumn> sc, List<StickColumn> crossCol) {
		List<Stick> nonDupes = new ArrayList<>();
		for (int i = 0; i < sc.size(); i++) {
			if (sc.get(i).inserted) {
				continue;
			}
			
			for (StickColumn cCol : crossCol) {
				List<Stick> nd = cCol.getNonDuplicates(sc.get(i).getSticks());
				if (nd == null) {
					continue;
					
				} else {
					// If duplicates are found add them to list
					// Remove the column
					nonDupes.addAll(nd);
					sc.get(i).removeAllSticks();
				}
			}
		}
		
		return nonDupes;
	}
}
