package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;

public class LimitSellOrder implements SellOrder {

	private Trader trader;
	private Stock stock;
	private Integer quantity;
	private Double price;
	
	public LimitSellOrder(Trader trader, Stock stock, Integer quantity, Double price) {
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
		return "LimitSellOrder{" +
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
	public void satisfyTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		int tradeQuantity = tradeEvent.getEvent().getQuantity();
		Double tradePrice = tradeEvent.getEvent().getPrice();
		if (price > tradePrice){throw new TradeException("Price too low for sale");}
		if (quantity < tradeQuantity){
			trader.sellStock(stock, quantity,tradePrice);
		} else {trader.sellStock(stock, tradeQuantity, tradePrice);}
		
	}

	@Override
	public void rollBackTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		int tradeQuantity = tradeEvent.getEvent().getQuantity();
		Double tradePrice = tradeEvent.getEvent().getPrice();
		// TODO
		
	}

	@Override
	public int compareTo(SellOrder order) {
		return price.compareTo(order.getPrice());
	}

}
