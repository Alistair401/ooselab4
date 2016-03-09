package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.TickEvent;

public class DefaultTickEvent<T> implements TickEvent<T> {

	private T event;
	private Long tick;
	
	public DefaultTickEvent(T event, Long tick) {
		this.event = event;
		this.tick = tick;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(TickEvent<T> tickEvent) {
		int result = 0;

		if (event instanceof Comparable && tickEvent.getEvent() instanceof Comparable) {
			result = ((Comparable) event).compareTo(tickEvent.getEvent());
		}

		return result != 0 ? result : Long.compare(getTick(), tickEvent.getTick());
	}

	@Override
	public T getEvent() {
		return event;
	}

	@Override
	public Long getTick() {
		return tick;
	}

	@Override
	public String toString() {
		return "DefaultTickEvent{" +
				"event=" + event +
				", tick=" + tick +
				'}';
	}
}
