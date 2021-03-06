package uk.ac.glasgow.jagora.impl;

import java.util.HashMap;
import java.util.Set;

import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.StockExchange;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;

/**
 * Implements a trader with behaviours for satisfying trades, but never speaks
 * on the exchange to place buy or sell orders.
 *
 * @author tws
 */
public class DefaultTrader implements Trader {

	private String name;
	private Double cash;
	private Integer quantity;
	// Stores the trader's stock holdings
	private HashMap<Stock, Integer> holding = new HashMap<>();

	/**
	 * Constructs a new instance of default trader with the specified cash and a
	 * single stock of the specified quantity.
	 *
	 * @param name
	 * @param cash
	 * @param stock
	 * @param quantity
	 */
	public DefaultTrader(String name, Double cash, Stock stock, Integer quantity) {
		this.name = name;
		holding.put(stock, quantity);
		this.cash = cash;
		this.quantity = quantity;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Double getCash() {
		return cash;
	}

	@Override
	// Update stock quantities in the trader's holding and update cash
	public void sellStock(Stock stock, Integer quantity, Double price) throws TradeException {
		if (!holding.containsKey(stock)) {
			throw new TradeException("Trader does not have this stock", this);
		}
		if (holding.get(stock) < quantity) {
			throw new TradeException("Trader does not have enough of this stock", this);
		}
		holding.replace(stock, holding.get(stock) - quantity);
		cash += quantity * price;
	}

	@Override
	// Update stock quantities in the trader's holding and update cash
	public void buyStock(Stock stock, Integer quantity, Double price) throws TradeException {
		if (cash < quantity * price) {
			throw new TradeException("Trader doesn't have enough cash for trade", this);
		}
		if (holding.containsKey(stock)) {
			holding.replace(stock, holding.get(stock) + quantity);
		} else {
			holding.put(stock, quantity);
		}
		cash -= quantity * price;
	}

	@Override
	public Integer getInventoryHolding(Stock stock) {
		if (holding.containsKey(stock)) {
			return holding.get(stock);
		}
		return null;
	}

	@Override
	public void speak(StockExchange stockExchange) {
		// DefaultTrader doesn't do this
		// Implemented in RandomTrader
	}

	@Override
	public String toString() {
		return "DefaultTrader{" +
				"name='" + name + '\'' +
				", cash=" + cash +
				", quantity=" + quantity +
				", holding=" + holding +
				'}';
	}

	@Override
	public Set<Stock> getTradingStocks() {
		return holding.keySet();
	}

}
