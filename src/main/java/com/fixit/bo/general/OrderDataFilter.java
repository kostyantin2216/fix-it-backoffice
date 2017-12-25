/**
 * 
 */
package com.fixit.bo.general;

import java.util.Date;

import com.fixit.core.data.mongo.OrderData;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/25 14:32:37 GMT+2
 */
public class OrderDataFilter {
	
	private final static int ALL_PROFESSIONS = -1;
	
	public static OrderDataFilter createDefault() {
		OrderDataFilter odf = new OrderDataFilter();
		odf.tableType = OrderTableType.GENERAL;
		odf.professionId = ALL_PROFESSIONS;
		return odf;
	}
	
	public static OrderDataFilter createCopy(OrderDataFilter oldFilter) {
		OrderDataFilter newFilter = new OrderDataFilter();
		newFilter.tableType = oldFilter.tableType;
		newFilter.fromDate = oldFilter.fromDate;
		newFilter.toDate = oldFilter.toDate;
		newFilter.professionId = oldFilter.professionId;
		return newFilter;
	}

	private OrderTableType tableType;
	private Date fromDate;
	private Date toDate;
	private int professionId;

	private OrderDataFilter() { }
	
	public OrderTableType getTableType() {
		return tableType;
	}
	
	public void setTableType(OrderTableType tableType) {
		this.tableType = tableType;
	}
	
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public Date getToDate() {
		return toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public int getProfessionId() {
		return professionId;
	}

	public void setProfessionId(int professionId) {
		this.professionId = professionId;
	}

	public void clearFromDate() {
		fromDate = null;
	}
	
	public void clearToDate() {
		toDate = null;
	}
	
	public boolean isFiltered(OrderData order) {
		return (tableType == OrderTableType.GENERAL || (order.isOrderComplete() 
					? tableType == OrderTableType.COMPLETED : tableType == OrderTableType.ONGOING))
				&& (fromDate == null || !order.getCreatedAt().before(fromDate)) 
				&& (toDate == null || !order.getCreatedAt().after(toDate))
				&& (professionId == ALL_PROFESSIONS || professionId == order.getProfessionId()); 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromDate == null) ? 0 : fromDate.hashCode());
		result = prime * result + professionId;
		result = prime * result + ((tableType == null) ? 0 : tableType.hashCode());
		result = prime * result + ((toDate == null) ? 0 : toDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDataFilter other = (OrderDataFilter) obj;
		if (fromDate == null) {
			if (other.fromDate != null)
				return false;
		} else if (!fromDate.equals(other.fromDate))
			return false;
		if (professionId != other.professionId)
			return false;
		if (tableType != other.tableType)
			return false;
		if (toDate == null) {
			if (other.toDate != null)
				return false;
		} else if (!toDate.equals(other.toDate))
			return false;
		return true;
	}
	
}
