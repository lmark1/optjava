package hr.fer.zemris.optjava.dz4.part2;

public class Stick {
	public Stick(int index, int height) {
		this.index = index;
		this.height = height;
	}
	final int index;
	final int height;
	
	@Override
	public String toString() {
		return "H: " + height + " I: " + index;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Stick) {
			return ((Stick)obj).index == this.index;
		} else {
			return super.equals(obj);
		}
	}
}
