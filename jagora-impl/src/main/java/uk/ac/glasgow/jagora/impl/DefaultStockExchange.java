package uk.ac.glasgow.jagora.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void doClearing() {
		//TODO
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
		//TODO
		return null;
	}

	@Override
	public Double getBestBid(Stock stock) {
		//TODO
		return null;
	}
	
	@Override
	public List<TickEvent<Trade>> getTradeHistory(Stock stock) {
		//TODO
		return null;
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
