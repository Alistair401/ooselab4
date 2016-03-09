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
	
	public String toString(){
		//TODO
		return null;
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
		if (price < tradePrice){throw new TradeException("Price too high to buy");}
		if (quantity < tradeQuantity){
			trader.buyStock(stock, quantity,tradePrice);
		} else {trader.buyStock(stock, tradeQuantity, tradePrice);}
	}

	@Override
	public void rollBackTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		// TODO
		
	}

	@Override
	public int compareTo(BuyOrder order) {
		if (price < order.getPrice()){return -1;}
		else if (price > order.getPrice()){return 1;}
		return 0;
	}


}
