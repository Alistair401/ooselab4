package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.Stock;

public class DefaultStock implements Stock {

	private String name;

	public DefaultStock(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		// Stocks are considered to be equal when they are the same object,
		// or all fields match
		return (this == o) || (o instanceof DefaultStock && name.equals(((DefaultStock) o).name));
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
