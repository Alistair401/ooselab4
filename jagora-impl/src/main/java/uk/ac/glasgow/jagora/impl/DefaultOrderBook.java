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
 *
 * @author tws
 */
public class DefaultOrderBook<O extends Order & Comparable<O>> implements OrderBook<O> {

	private final Queue<TickEvent<O>> backing;
	private World world;

	/**
	 * Constructs a new instance of the order book synchronized to the ticks of the specified world.
	 *
	 * @param world
	 */
	public DefaultOrderBook(World world) {
		// We do not set a custom comparator here.
		// TickEvent is a comparable which compares first by contents,
		// then by time. This allows the PriorityQueue to sort correctly automatically
		this.backing = new PriorityQueue<>();
		this.world = world;
	}

	@Override
	public void recordOrder(O order) {
		backing.add(world.createTickEvent(order));
	}

	@Override
	public void cancelOrder(O order) {
		// Unfortunately, this is an O(n) operation.
		// Given the choice of data structure there doesn't appear to be a faster way
		// If performance issues occur during real world use,
		// a different data structure should be chosen.

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
		// For null safety, we must only call getEvent() if tickEvent is not null
		TickEvent<O> tickEvent = backing.peek();
		return tickEvent == null ? null : tickEvent.getEvent();
	}

	@Override
	public List<TickEvent<O>> getOrdersAsList() {
		List<TickEvent<O>> events = new ArrayList<>(backing);

		// Surprisingly, PriorityQueue's iterator is not ordered.
		// Creating an ArrayList from a collection uses the iterator
		// so we must also sort here to ensure correct order of the returned list
		Collections.sort(events);

		return events;
	}

	@Override
	public String toString() {
		return "DefaultOrderBook{" +
				"backing=" + backing +
				", world=" + world +
				'}';
	}

}
