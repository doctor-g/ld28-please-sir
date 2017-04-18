package edu.bsu.pvgestwicki.ld28.core;

public enum SystemPriority {

	NORMAL(0);

	private final int value;

	private SystemPriority(int value) {
		this.value = value;
	}

	public int toInt() {
		return value;
	}
}
