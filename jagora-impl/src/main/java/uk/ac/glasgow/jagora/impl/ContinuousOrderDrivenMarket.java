package uk.ac.glasgow.jagora.impl;


import java.util.ArrayList;
import java.util.List;

import uk.ac.glasgow.jagora.*;

/**
 * Provides the behaviour of a continuous order driven market.
 * @author tws
 *
 */
public class ContinuousOrderDrivenMarket implements Market {
	
	private OrderBook<SellOrder> sellBook;
	private OrderBook<BuyOrder> buyBook;
	private Stock stock;
	private World world;
	
	/**
	 * Constructs a new continuous order driven market for the specified stock,
	 * synchronised to the tick events of the specified world.
	 * 
	 * @param stock
	 * @param world
	 */
	public ContinuousOrderDrivenMarket(Stock stock, World world) {
		this.stock = stock;
		this.world = world;
		sellBook = new DefaultOrderBook<>(world);
		buyBook = new DefaultOrderBook<>(world);
	}

	@Override
	public Stock getStock() {
		return stock;
	}

	@Override
	public List<TickEvent<Trade>> doClearing() {
		List<TickEvent<Trade>> trades = new ArrayList<>();

		while (true) {
			BuyOrder bestBid = buyBook.getBestOrder();
			SellOrder bestSellOrder = sellBook.getBestOrder();

			if (bestBid == null || bestSellOrder == null || bestBid.getPrice() < bestSellOrder.getPrice())
				break;

			Trade t = new DefaultTrade(world, bestBid, bestSellOrder, stock, Math.min(bestBid.getRemainingQuantity(), bestSellOrder.getRemainingQuantity()), bestSellOrder.getPrice());
			System.out.println(t);
			try {
				trades.add(t.execute());
			} catch (TradeException e) {
				throw new RuntimeException(t.toString(), e);
			}

			if (bestBid.getRemainingQuantity() == 0)
				buyBook.cancelOrder(bestBid);

			if (bestSellOrder.getRemainingQuantity() == 0)
				sellBook.cancelOrder(bestSellOrder);
		}

		return trades;
	}

	@Override
	public void placeBuyOrder(BuyOrder buyOrder) {
		buyBook.recordOrder(buyOrder);
	}

	@Override
	public void placeSellOrder(SellOrder sellOrder) {
		sellBook.recordOrder(sellOrder);
	}

	@Override
	public void cancelBuyOrder(BuyOrder buyOrder) {
		buyBook.cancelOrder(buyOrder);
	}

	@Override
	public void cancelSellOrder(SellOrder sellOrder) {
		sellBook.cancelOrder(sellOrder);
	}

	@Override
	public Double getBestBid() {
		return buyBook.getBestOrder().getPrice();
	}

	@Override
	public Double getBestOffer() {
		return sellBook.getBestOrder().getPrice();
	}

	@Override
	public String toString() {
		return "ContinuousOrderDrivenMarket{" +
				"sellBook=" + sellBook +
				", buyBook=" + buyBook +
				", stock=" + stock +
				", world=" + world +
				'}';
	}
}
