/**
 * 
 */
package com.fixit.bo.general;

import java.util.List;
import java.util.stream.Collectors;

import com.fixit.core.data.mongo.OrderData;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/25 13:31:56 GMT+2
 */
public class OrderDataStore {

	private final List<OrderData> mOriginalData;
	private List<OrderData> mFilteredData;
	
	private OrderDataFilter mFilter;
	
	public OrderDataStore(List<OrderData> data, OrderDataFilter filter) {
		mOriginalData = data;
		applyFilter(filter);
	}
	
	public OrderDataStore(List<OrderData> data) {
		this(data, OrderDataFilter.createDefault());
	}
	
	public List<OrderData> getData() {
		return mFilteredData;
	}
	
	public OrderDataFilter getFilter() {
		return mFilter;
	}
	
	public void applyFilter(OrderDataFilter filter) {
		if(mFilter == null || !mFilter.equals(filter)) {
			mFilter = OrderDataFilter.createCopy(filter);
			mFilteredData = mOriginalData.stream()
										 .filter(o -> mFilter.isFiltered(o))
										 .collect(Collectors.toList());
		}
	}
	
	public void addOrder(OrderData order) {
		mOriginalData.add(order);
		if(mFilter.isFiltered(order)) {
			mFilteredData.add(order);
		}
	}
	
	public void removeOrder(OrderData order) {
		mOriginalData.remove(order);
		mFilteredData.remove(order);
	}
	
	public void updateOrder(OrderData order) {
		if(mFilter.isFiltered(order)) {
			if(!mFilteredData.contains(order)) {
				mFilteredData.add(order);
			}
		} else {
			mFilteredData.remove(order);
		}
	}
	
}
