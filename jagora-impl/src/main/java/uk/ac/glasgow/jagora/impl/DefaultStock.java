package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.Stock;

public class DefaultStock implements Stock{
	
	private String name;
	
	public DefaultStock(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString(){
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DefaultStock)) return false;

		DefaultStock that = (DefaultStock) o;

		return name.equals(that.name);

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
