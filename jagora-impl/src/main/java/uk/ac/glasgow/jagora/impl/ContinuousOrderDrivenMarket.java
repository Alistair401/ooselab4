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

		// This isn't actually an infinite loop
		// It will always exit:
		// * break from loop
		// * continue, after removing an order from book due to fulfillment
		while (true) {
			BuyOrder bestBid = buyBook.getBestOrder();
			SellOrder bestSellOrder = sellBook.getBestOrder();

			// If we've ran out of orders, break
			// If the best orders don't overlap in pricing, break
			if (bestBid == null || bestSellOrder == null || bestBid.getPrice() < bestSellOrder.getPrice())
				break;

			// pick quantity as the maximum possible quantity (the smallest of the quantities of either order)
			int quantity = Math.min(bestBid.getRemainingQuantity(), bestSellOrder.getRemainingQuantity());

			// The price used is the price of the sell order. This doesn't seem to be in the specification anywhere,
			// but we chose to do this as it makes the most sense, and passes the tests
			double price = bestSellOrder.getPrice();

			// If the buying trader can't successfully perform an order, cancel it
			// It seems that this should punish/record a mark against the trader in some way
			// (we discussed this in earlier labs)
			// however, there is no way to do this with the APIs we have been given
			// so we just cancel the order without fulfilling anything
			if (bestBid.getTrader().getCash() < quantity * price) {
				buyBook.cancelOrder(bestBid);
				continue;
			}

			// Same, as above, but for selling trader
			if (bestSellOrder.getTrader().getInventoryHolding(stock) < quantity) {
				sellBook.cancelOrder(bestSellOrder);
				continue;
			}

			// Create a new Trade using our chosen values based on possible quantities/prices
			Trade t = new DefaultTrade(world, bestBid, bestSellOrder, stock, quantity, price);

			try {
				trades.add(t.execute());
			} catch (TradeException e) {
				// Clearing algorithm should never perform a bad trade as it pays attention to price/quantities
				// so if it does we wrap in a runtime exception
				// and throw it upwards, as this is a serious error
				throw new RuntimeException(e);
			}

			// Cancel any orders which have been completely fulfilled
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
		// For null safety, we must not call getPrice() on a null order, and instead just return null
		BuyOrder order = buyBook.getBestOrder();
		return order == null ? null : order.getPrice();
	}

	@Override
	public Double getBestOffer() {
		// For null safety, we must not call getPrice() on a null order, and instead just return null
		SellOrder order = sellBook.getBestOrder();
		return order == null ? null : order.getPrice();
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
