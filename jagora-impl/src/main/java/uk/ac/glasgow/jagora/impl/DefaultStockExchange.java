package uk.ac.glasgow.jagora.impl;

import java.util.*;

import uk.ac.glasgow.jagora.BuyOrder;
import uk.ac.glasgow.jagora.Market;
import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.StockExchange;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.World;

public class DefaultStockExchange implements StockExchange {

	private Map<Stock, Market> markets = new HashMap<>();
	private World world;
	private List<TickEvent<Trade>> tradeHistory = new ArrayList<>();
	
	public DefaultStockExchange(World world){
		this.world = world;
	}
	
	@Override
	// Iterates through all stock markets and execute clearing on them
	public void doClearing() {
		for (Market market : markets.values()) {
			// Add all trades from market clearing to the trade history
			tradeHistory.addAll(market.doClearing());
		}
	}

	/**
	 * Gets (or creates if necessary) the market for a given stock
	 * @return market associated with the given stock
	 */
	private Market getOrCreateMarket(Stock stock) {
		return markets.computeIfAbsent(stock, it -> new ContinuousOrderDrivenMarket(it, world));
	}

	@Override
	public void placeBuyOrder(BuyOrder buyOrder) {
		getOrCreateMarket(buyOrder.getStock()).placeBuyOrder(buyOrder);
	}

	@Override
	public void placeSellOrder(SellOrder sellOrder) {
		getOrCreateMarket(sellOrder.getStock()).placeSellOrder(sellOrder);
	}

	@Override
	public void cancelBuyOrder(BuyOrder buyOrder) {
		getOrCreateMarket(buyOrder.getStock()).cancelBuyOrder(buyOrder);
	}

	@Override
	public void cancelSellOrder(SellOrder sellOrder) {
		getOrCreateMarket(sellOrder.getStock()).cancelSellOrder(sellOrder);
	}
	
	@Override
	public Double getBestOffer(Stock stock) {
		return getOrCreateMarket(stock).getBestOffer();
	}

	@Override
	public Double getBestBid(Stock stock) {
		return getOrCreateMarket(stock).getBestBid();
	}
	
	@Override
	public List<TickEvent<Trade>> getTradeHistory(Stock stock) {
		// Return an immutable list of the trade history, so it can't be mucked with by other classes
		return Collections.unmodifiableList(tradeHistory);
	}

	@Override
	public String toString() {
		return "DefaultStockExchange{" +
				"markets=" + markets +
				", world=" + world +
				", tradeHistory=" + tradeHistory +
				'}';
	}
}
