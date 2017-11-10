package hr.fer.zemris.optjava.dz3.impl;

import hr.fer.zemris.optjava.dz3.model.ITempSchedule;

/**
 * Implementation of the temperature schedule for the simulated annealing
 * algorithm.
 * 
 * @author lmark
 *
 */
public class GeometricTempSchedule implements ITempSchedule{

	private double alpha;
	private double tInitial;
	private double tCurrent;
	private int innerLimit;
	private int outerLimit;
	
	private int currentInner = 0;
	private int currentOuter = 0;
	
	public GeometricTempSchedule(
			double alpha, 
			double tInitial,
			int innerLimit,
			int outerLimit) {
		
		this.alpha = alpha;
		this.tInitial = tInitial;
		this.innerLimit = innerLimit;
		this.outerLimit = outerLimit;
	}
	
	@Override
	public double getNextTermperature() {
		return tCurrent;
	}

	@Override
	public int getInnerLoopCounter() {
		currentInner++;
		
		if (currentInner > innerLimit) {
			return -1;
		}
		
		tCurrent = Math.pow(alpha, currentInner) * tInitial;
		return currentInner;
	}

	@Override
	public int getOuterLoopCounter() {
		currentOuter++;
		currentInner = 0;
		
		if (currentOuter > outerLimit) {
			return -1;
		}
		
		return currentOuter;
	}

}
