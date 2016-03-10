package uk.ac.glasgow.jagora.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.StockExchange;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;

/**
 * Implements a random trading strategy using limit orders. The trader will
 * place random buy and sell orders within a constrained price range (and up to
 * a maximum quantity) of the current market spread (bestBid, bestOffer). The
 * pseudo code for the trading strategy is as follows:
 * 
 * <pre>
 * buy <- randomChoice
 * if buy:
 *   price <- bestBid + (-0.5 <= random <= 0.5) * priceRange
 *   quantity <- (0 < random < maxTradeQuantity)
 *   stock <- random (s in inventory)
 *   placeLimitBuyOrder(stock, quantity, price)
 * else:
 *   price <- bestOffer + (-0.5 <= random <= 0.5) * priceRange
 *   quantity <- (0 < random < maxTradeQuantity)
 *   stock <- random (s in inventory)
 *   placeLimitSellOrder(stock, quantity, price)
 * </pre>
 * 
 * @author tws
 *
 */

public class RandomTrader implements Trader {
	
	private Trader trader;
	private double priceRange;
	private Integer maxTradeQuantity;
	private Random random;
	
	public RandomTrader(String name, Double cash, Stock stock, Integer quantity,
						Integer maxTradeQuantity, double priceRange, Random random) {
		// delegates functions to a DefaultTrader
		this.trader = new DefaultTrader(name, cash, stock, quantity);
		this.priceRange = priceRange;
		this.maxTradeQuantity = maxTradeQuantity;
		this.random = random;
	}

	@Override
	public String getName() {
		return trader.getName();
	}

	@Override
	public Double getCash() {
		return trader.getCash();
	}

	@Override
	public void sellStock(Stock stock, Integer quantity, Double price) throws TradeException {
		trader.sellStock(stock, quantity, price);
	}

	@Override
	public void buyStock(Stock stock, Integer quantity, Double price) throws TradeException {
		trader.buyStock(stock, quantity, price);
	}

	@Override
	public Integer getInventoryHolding(Stock stock) {
		return trader.getInventoryHolding(stock);
	}

	@Override
	public void speak(StockExchange stockExchange) {
		// Decide to buy or not
		boolean buy = random.nextBoolean();

		// Choose a random stock to buy or sell
		List<Stock> holding = new ArrayList<>(trader.getTradingStocks());
		int stockIndex = holding.size() == 1 ? 0 : random.nextInt(holding.size()-1);
		Stock stock = holding.get(stockIndex);
		
		// Detailed in the algorithm given above
		if (buy){
			double price = stockExchange.getBestBid(stock) + (random.nextDouble() - 0.5 * priceRange);
			int quantity = random.nextInt(maxTradeQuantity);
			stockExchange.placeBuyOrder(new LimitBuyOrder(trader,stock,quantity,price));
		} else {
			double price = stockExchange.getBestOffer(stock) + (random.nextDouble() - 0.5 * priceRange);
			int quantity = random.nextInt(maxTradeQuantity);
			stockExchange.placeSellOrder(new LimitSellOrder(trader,stock,quantity,price));
		}
	}

	@Override
	public Set<Stock> getTradingStocks() {
		return trader.getTradingStocks();
	}

	@Override
	public String toString() {
		return "RandomTrader{" +
				"trader=" + trader +
				'}';
	}
}
