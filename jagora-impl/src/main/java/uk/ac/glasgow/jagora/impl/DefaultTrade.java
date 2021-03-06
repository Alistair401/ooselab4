package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.BuyOrder;
import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.World;

public class DefaultTrade implements Trade {

	private World world;
	private BuyOrder buyOrder;
	private SellOrder sellOrder;
	private Integer quantity;
	private Stock stock;
	private Double price;

	public DefaultTrade(World world, BuyOrder buyOrder, SellOrder sellOffer, Stock stock, Integer quantity, Double price) {
		this.world = world;
		this.buyOrder = buyOrder;
		this.sellOrder = sellOffer;
		this.quantity = quantity;
		this.stock = stock;
		this.price = price;
	}

	@Override
	public Stock getStock() {
		return stock;
	}

	@Override
	public Integer getQuantity() {
		return quantity;
	}

	@Override
	public Double getPrice() {
		return price;
	}

	@Override
	// Executes the trade by calling functions on the buyOrder and sellOrder
	public TickEvent<Trade> execute() throws TradeException {
		TickEvent<Trade> event = world.createTickEvent(this);
		buyOrder.satisfyTrade(event);
		sellOrder.satisfyTrade(event);
		return event;
	}

	@Override
	public String toString() {
		return "DefaultTrade{" +
				"world=" + world +
				", buyOrder=" + buyOrder +
				", sellOrder=" + sellOrder +
				", quantity=" + quantity +
				", stock=" + stock +
				", price=" + price +
				'}';
	}
}
