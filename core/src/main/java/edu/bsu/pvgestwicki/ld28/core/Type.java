package edu.bsu.pvgestwicki.ld28.core;

public final class Type {

	public static final int BUMBLE = 1 << 0;
	public static final int ORPHAN = 1 << 1;
	public static final int GRUEL = 1 << 2;

	public static final int BUMBLE_ORPHAN_COLLISION = BUMBLE | ORPHAN;
	public static final int GRUEL_ORPHAN_COLLISION = GRUEL | ORPHAN;

	private Type() {
	}
}
