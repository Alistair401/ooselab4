package uk.ac.glasgow.jagora.impl;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import uk.ac.glasgow.jagora.Order;
import uk.ac.glasgow.jagora.OrderBook;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.World;

/**
 * Provides the default implementation of an order book for sorting buy and sell orders.
 * @author tws
 */
public class DefaultOrderBook<O extends Order & Comparable<O>> implements OrderBook<O> {

	private final Queue<TickEvent<O>> backing;
	private World world;
	
	/**
	 * Constructs a new instance of the order book synchronized to the ticks of the specified world.
	 * @param world
	 */
	public DefaultOrderBook(World world) {
		this.backing = new PriorityQueue<>();
		this.world = world;
	}

	@Override
	public void recordOrder(O order) {
		backing.add(world.createTickEvent(order));
	}

	@Override
	public void cancelOrder(O order) {
		Iterator<TickEvent<O>> i = backing.iterator();

		while (i.hasNext()) {
			TickEvent<O> next = i.next();
			if (next.getEvent().equals(order)) {
				i.remove();
				return;
			}
		}

		throw new RuntimeException("Failed to remove " + order + " as it is not in the order book");
	}

	@Override
	public O getBestOrder() {
		TickEvent<O> tickEvent = backing.peek();
		return tickEvent == null ? null : tickEvent.getEvent();
	}

	@Override
	public List<TickEvent<O>> getOrdersAsList() {
		return new ArrayList<>(backing);
	}

	@Override
	public String toString() {
		return "DefaultOrderBook{" +
				"backing=" + backing +
				", world=" + world +
				'}';
	}

}
