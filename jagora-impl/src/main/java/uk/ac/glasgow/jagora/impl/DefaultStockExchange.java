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

	private Map<Stock, Market> markets = new HashMap<Stock, Market>() {
		@Override
		public synchronized Market get(Object s) {
			Market m = super.get(s);

			if (m == null) {
				m = new ContinuousOrderDrivenMarket((Stock) s, world);
				super.put((Stock) s, m);
			}

			return m;
		}
	};
	private World world;
	private List<TickEvent<Trade>> tradeHistory = new ArrayList<>();
	
	public DefaultStockExchange(World world){
		this.world = world;
	}
	
	@Override
	// Iterates through all stock markets and execute clearing on them
	public void doClearing() {
		for (Map.Entry<Stock, Market> stockMarketEntry : markets.entrySet()) {
			tradeHistory.addAll(stockMarketEntry.getValue().doClearing());
		}
	}

	@Override
	public void placeBuyOrder(BuyOrder buyOrder) {
		markets.get(buyOrder.getStock()).placeBuyOrder(buyOrder);
	}

	@Override
	public void placeSellOrder(SellOrder sellOrder) {
		markets.get(sellOrder.getStock()).placeSellOrder(sellOrder);
	}

	@Override
	public void cancelBuyOrder(BuyOrder buyOrder) {
		markets.get(buyOrder.getStock()).cancelBuyOrder(buyOrder);
	}

	@Override
	public void cancelSellOrder(SellOrder sellOrder) {
		markets.get(sellOrder.getStock()).cancelSellOrder(sellOrder);
	}
	
	@Override
	public Double getBestOffer(Stock stock) {
		return markets.get(stock).getBestOffer();
	}

	@Override
	public Double getBestBid(Stock stock) {
		return markets.get(stock).getBestBid();
	}
	
	@Override
	public List<TickEvent<Trade>> getTradeHistory(Stock stock) {
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
