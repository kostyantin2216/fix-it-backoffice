/**
 * 
 */
package com.fixit.bo.general;

import java.util.Date;

import com.fixit.core.data.UserType;
import com.fixit.core.data.mongo.CommonUser;
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
		newFilter.userType = oldFilter.userType;
		newFilter.user = oldFilter.user;
		newFilter.fromDate = oldFilter.fromDate;
		newFilter.toDate = oldFilter.toDate;
		newFilter.professionId = oldFilter.professionId;
		return newFilter;
	}

	private OrderTableType tableType;
	private UserType userType;
	private CommonUser user;
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
	
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public CommonUser getUser() {
		return user;
	}

	public void setUser(CommonUser user) {
		this.user = user;
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
	
	public void clearUser() {
		user = null;
	}
	
	public boolean isFiltered(OrderData order) {
		return (tableType == OrderTableType.GENERAL || (order.isOrderComplete() 
					? tableType == OrderTableType.COMPLETED : tableType == OrderTableType.ONGOING))
				&& (userType == null || userType.equals(order.getUserType()))
				&& (user == null || (user.getType() == order.getUserType() && user.get_id().equals(order.getUserId())))
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
		result = prime * result + ((userType == null) ? 0 : userType.hashCode());
		result = prime * result + ((toDate == null) ? 0 : toDate.hashCode());
		result = prime * result + ((user == null) ? 0 : user.getType().hashCode());
		result = prime * result + ((user == null) ? 0 : user.get_id().hashCode());
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
		if (userType != other.userType)
			return false;
		if (toDate == null) {
			if (other.toDate != null)
				return false;
		} else if (!toDate.equals(other.toDate))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
			else
				return true;
		} else if (other.user == null) {
			return false;
		} else {		
			if (!user.getType().equals(other.user.getType()))
				return false;
			if (!user.get_id().equals(other.user.get_id()))
				return false;
		}
		
		
		return true;
	}
	
}
