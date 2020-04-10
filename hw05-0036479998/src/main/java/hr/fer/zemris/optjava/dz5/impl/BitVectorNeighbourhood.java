package hr.fer.zemris.optjava.dz5.impl;

import java.util.Random;

import hr.fer.zemris.optjava.dz5.model.INeighbourhood;

public class BitVectorNeighbourhood implements INeighbourhood<BitVectorSolution>{

	private double flipChance;
	private final Random random = new Random();
	
	public BitVectorNeighbourhood(double flipChance) {
		this.flipChance = flipChance;
	}
	
	@Override
	public BitVectorSolution randomNeighbour(BitVectorSolution root) {
		BitVectorSolution neighbour = root.duplicate();
		
		return neighbour.randomize(random, flipChance);
	}

}
