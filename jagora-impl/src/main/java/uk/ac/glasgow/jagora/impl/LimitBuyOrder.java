package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.BuyOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;

public class LimitBuyOrder implements BuyOrder {
	
	private Trader trader;
	private Stock stock;
	private Integer quantity;
	private Double price;

	public LimitBuyOrder(Trader trader, Stock stock, Integer quantity, Double price) {
		this.trader = trader;
		this.stock = stock;
		this.quantity = quantity;
		this.price = price;
	}

	@Override
	public Double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "LimitBuyOrder{" +
				"trader=" + trader +
				", stock=" + stock +
				", quantity=" + quantity +
				", price=" + price +
				'}';
	}

	@Override
	public Trader getTrader() {
		return trader;
	}

	@Override
	public Stock getStock() {
		return stock;
	}

	@Override
	public Integer getRemainingQuantity() {
		return quantity;
	}

	@Override
	public synchronized void satisfyTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		int tradeQuantity = tradeEvent.getEvent().getQuantity();
		Double tradePrice = tradeEvent.getEvent().getPrice();
		if (price < tradePrice){throw new TradeException("Price too high to for BuyOrder");}
		if (quantity < tradeQuantity){throw new TradeException("BuyOrder not big enough to satisfy trade");}
		else {trader.buyStock(stock, tradeQuantity, tradePrice);}
	}

	@Override
	public void rollBackTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		int tradeQauntity = tradeEvent.getEvent().getQuantity();
		Double tradePrice = tradeEvent.getEvent().getPrice();
		trader.sellStock(stock,tradeQauntity, tradePrice);
	}

	@Override
	public int compareTo(BuyOrder order) {
		return price.compareTo(order.getPrice());
	}


}
