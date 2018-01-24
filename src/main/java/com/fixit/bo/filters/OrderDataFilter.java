/**
 * 
 */
package com.fixit.bo.filters;

import java.util.Arrays;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.fixit.bo.general.OrderTableType;
import com.fixit.core.data.UserType;
import com.fixit.core.data.mongo.CommonUser;
import com.fixit.core.data.mongo.OrderData;
import com.fixit.core.data.mongo.Tradesman;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/25 14:32:37 GMT+2
 */
public class OrderDataFilter implements Filter<OrderData> {
	
	private final static int ALL_PROFESSIONS = -1;
	private final static int ALL_TRAFFIC_SOURCES = -1;
	
	public static OrderDataFilter createDefault() {
		OrderDataFilter odf = new OrderDataFilter();
		odf.tableType = OrderTableType.GENERAL;
		odf.professionId = ALL_PROFESSIONS;
		odf.trafficSourceId = ALL_TRAFFIC_SOURCES;
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
		newFilter.trafficSourceId = oldFilter.trafficSourceId;
		return newFilter;
	}

	private OrderTableType tableType;
	private UserType userType;
	private CommonUser user;
	private Tradesman tradesman;
	private Date fromDate;
	private Date toDate;
	private int professionId;
	private int trafficSourceId;

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
	
	public Tradesman getTradesman() {
		return tradesman;
	}

	public void setTradesman(Tradesman tradesman) {
		this.tradesman = tradesman;
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

	public int getTrafficSourceId() {
		return trafficSourceId;
	}

	public void setTrafficSourceId(int trafficSourceId) {
		this.trafficSourceId = trafficSourceId;
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
	
	public void clearTradesman() {
		tradesman = null;
	}
	
	public boolean isFiltered(OrderData order) {
		return (tableType == OrderTableType.GENERAL || (StringUtils.isEmpty(order.getCancelReason()) 
						? (order.isOrderComplete() ? tableType == OrderTableType.COMPLETED : tableType == OrderTableType.ONGOING) 
						: tableType == OrderTableType.CANCELLED))
				&& (userType == null || userType.equals(order.getUserType()))
				&& (user == null || (user.getType() == order.getUserType() && user.get_id().equals(order.getUserId())))
				&& (tradesman == null || (
						Arrays.stream(order.getTradesmen()))
							  .anyMatch(id -> id.equals(tradesman.get_id()))
				   )
				&& (fromDate == null || !order.getCreatedAt().before(fromDate)) 
				&& (toDate == null || !order.getCreatedAt().after(toDate))
				&& (professionId == ALL_PROFESSIONS || professionId == order.getProfessionId())
				&& (trafficSourceId == ALL_TRAFFIC_SOURCES || trafficSourceId == order.getTrafficSourceId()); 
	}

	@Override
	public boolean update(Filter<OrderData> filter) {
		boolean updated = false;
		
		if(filter != null && filter instanceof OrderDataFilter) {
			OrderDataFilter other = (OrderDataFilter) filter;
			if (other.fromDate != null ? !other.fromDate.equals(fromDate) : fromDate != null) {
				updated = true;
				setFromDate(other.fromDate);
			} 
			
			if (other.toDate != null ? !other.toDate.equals(toDate) : toDate != null) {
				updated = true;
				setToDate(other.toDate);
			} 
			
			if((user == null && other.user != null) 
					|| (user != null && other.user == null) 
					|| (user != null && other.user != null 
						&& (!user.getType().equals(other.user.getType()) 
								|| !user.get_id().equals(other.user.get_id())))) {
				updated = true;
				setUser(other.user);
			}
			
			if((tradesman == null && other.tradesman != null) 
					|| (tradesman != null && other.tradesman == null) 
					|| (tradesman != null && other.tradesman != null 
						&& (!tradesman.get_id().equals(other.tradesman.get_id())))) {
				updated = true;
				setTradesman(other.tradesman);
			}
			
			if (professionId != other.professionId) {
				updated = true;
				setProfessionId(other.professionId);
			}
			
			if (trafficSourceId != other.trafficSourceId) {
				updated = true;
				setTrafficSourceId(other.trafficSourceId);
			}
			
			if (tableType != other.tableType) {
				updated = true;
				setTableType(other.tableType);
			}
			
			if (userType != other.userType) {
				updated = true;
				setUserType(other.userType);
			}
		}
		
		return updated;
	}
	
}
