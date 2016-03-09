package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;

import java.util.Objects;

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
		if (price < tradePrice){throw new TradeException("Price too low for sale");}
		if (quantity < tradeQuantity){throw new TradeException("SellOrder is not big enough to satisfy trade");}
		else {
			trader.sellStock(stock, tradeQuantity, tradePrice);
			quantity -= tradeQuantity;
		}
		
	}

	@Override
	public void rollBackTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		int tradeQuantity = tradeEvent.getEvent().getQuantity();
		Double tradePrice = tradeEvent.getEvent().getPrice();
		trader.buyStock(stock, tradeQuantity, tradePrice);
		quantity += tradeQuantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LimitSellOrder)) return false;

		LimitSellOrder that = (LimitSellOrder) o;

		return trader == that.trader &&
				Objects.equals(quantity, that.quantity) &&
				Objects.equals(price, that.price) &&
				Objects.equals(stock, that.stock);

	}

	@Override
	public int hashCode() {
		int result = trader != null ? trader.hashCode() : 0;
		result = 31 * result + (stock != null ? stock.hashCode() : 0);
		result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
		result = 31 * result + (price != null ? price.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(SellOrder order) {
		return price.compareTo(order.getPrice());
	}

}
