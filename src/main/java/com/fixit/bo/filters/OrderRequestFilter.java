/**
 * 
 */
package com.fixit.bo.filters;

import com.fixit.bo.general.OrderRequestsTableType;
import com.fixit.core.data.mongo.OrderRequest;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/22 23:30:16 GMT+2
 */
public class OrderRequestFilter implements Filter<OrderRequest> {

	public static OrderRequestFilter createDefault() {
		OrderRequestFilter filter = new OrderRequestFilter();
		filter.tableType = OrderRequestsTableType.ALL;
		return filter;
	}
	
	private OrderRequestsTableType tableType;
	
	private OrderRequestFilter() { }

	public OrderRequestsTableType getTableType() {
		return tableType;
	}

	public void setTableType(OrderRequestsTableType tableType) {
		this.tableType = tableType;
	}
	
	public boolean isFiltered(OrderRequest request) {
		return tableType == OrderRequestsTableType.ALL 
				|| (tableType == OrderRequestsTableType.DENIED && request.isDenied())
				|| (tableType == OrderRequestsTableType.FULFILLED && request.isFulfilled())
				|| (tableType == OrderRequestsTableType.PENDING && request.isPending());
	}
	
	public boolean update(Filter<OrderRequest> filter) {
		boolean updated = false;
		
		if(filter != null && filter instanceof OrderRequestFilter) {
			OrderRequestFilter other = (OrderRequestFilter) filter;
			if(this.tableType != other.tableType) {
				updated = true;
				setTableType(other.tableType);
			}
		}
		
		return updated;
	}

	@Override
	public String toString() {
		return "OrderRequestFilter [tableType=" + tableType + "]";
	}
	
}
