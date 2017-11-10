package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StickColumn {
	
	private List<Stick> sticks = new ArrayList<>();
	public int totalHeight = 0;
	public boolean inserted = false;
	
	public StickColumn() {
	}
	
	public void setInserted(boolean inserted) {
		this.inserted = inserted;
	}
	
	public void addStick(Stick stick) {
		int insertIndex = 0;
		for (int i = 0; i < sticks.size(); i++) {
			if (sticks.get(i).height > stick.height) {
				insertIndex = i;
				break;
			}
		}
		
		sticks.add(insertIndex, stick);
		totalHeight += stick.height;
	}
	
	public void addSticks(List<Stick> sticks) {
		this.sticks.addAll(sticks);
		Collections.sort(this.sticks, new Comparator<Stick>() {

			@Override
			public int compare(Stick o1, Stick o2) {
				if (o1.height < o2.height) {
					return -1;
				} else if (o1.height > o2.height) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		for (Stick stick : sticks) {
			totalHeight += stick.height;
		}
	}
	
	public void removeStick(Stick stick) {
		if (sticks.remove(stick)) {
			totalHeight -= stick.height;
		} else {
			throw new IllegalArgumentException("Unable to find stick");
		}
	}
	
	public void removeAllSticks() {
		sticks = new ArrayList<>();
		totalHeight = 0;
	}
	
	public Stick removeSmallestStick() {
		Stick smallest = sticks.get(0);
		removeStick(smallest);
		return smallest;
	}
	
	public boolean isEmtpy() {
		return sticks.size() == 0;
	}
	
	@Override
	public String toString() {
		return "TotalH: " + totalHeight + ", " + sticks;
	}
	
	public List<Stick> getSticks() {
		return sticks;
	}
	
	public StickColumn duplicate() {
		StickColumn newCol = new StickColumn();
		for (Stick stick : sticks) {
			newCol.addStick(new Stick(stick.index, stick.height));
		}
		newCol.totalHeight = this.totalHeight;
		
		return newCol;
	}
	
	/**
	 * @param col
	 * @return If there are duplicates returns a list of all non duplicate elements,
	 * 		else returns an empty list.
	 */
	public List<Stick> getNonDuplicates(List<Stick> col) {
		boolean duplicates = false;
		List<Stick> nonDuplicates = new ArrayList<>();
		
		for (Stick stick : col) {
			if (this.sticks.contains(stick)) {
				duplicates = true;
			} else {
				nonDuplicates.add(new Stick(stick.index, stick.height));
			}
		}
		
		if (duplicates) {
			return nonDuplicates;
		} else {
			return null;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		for (Stick stick : sticks) {
			if (!stick.equals(obj)) {
				return false;
			}
		}
		
		return true;
	}
}
