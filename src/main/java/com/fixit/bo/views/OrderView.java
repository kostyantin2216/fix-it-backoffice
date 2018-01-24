/**
 * 
 */
package com.fixit.bo.views;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;

import com.fixit.core.data.JobLocation;
import com.fixit.core.data.OrderType;
import com.fixit.core.data.UserType;
import com.fixit.core.data.mongo.CommonUser;
import com.fixit.core.data.mongo.OrderData;
import com.fixit.core.data.mongo.Tradesman;
import com.fixit.core.utils.Formatter;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/15 19:38:37 GMT+2
 */
public class OrderView implements DataModelView<OrderData> {
	
	public static OrderView forOrder(OrderData order, CommonUser user, List<Tradesman> tradesmen) {
		OrderView ov = new OrderView();
		ov.id = order.get_id().toHexString();
		ov.tradesmen = tradesmen;
		ov.user = user;
		ov.professionId = order.getProfessionId();
		ov.location = order.getLocation();
		ov.jobReasons = order.getJobReasons();
		ov.comment = order.getComment();
		ov.userType = order.getUserType();
		ov.orderType = order.getOrderType();
		ov.createdAt = order.getCreatedAt();
		if(order.getOrderType() == null) {
			ov.setOrderType(OrderType.SEARCH);
		}
		ov.amountCharged = order.getAmountCharged();
		ov.commissionPercentage = order.getCommissionPercentage();
		return ov;
	}

	public static OrderView newOrder(int profession) {
		OrderView ov = new OrderView();
		ov.setProfessionId(profession);
		ov.setLocation(new JobLocation());
		ov.setOrderType(OrderType.CUSTOM);
		ov.setCreatedAt(new Date());
		return ov;
	}
	
	private String id;
	private List<Tradesman> tradesmen;
	private CommonUser user;
	private int professionId;
	private int trafficSourceId;
	private JobLocation location;
	private int[] jobReasons;
	private String comment;
	private UserType userType;
	private OrderType orderType;
	private Date createdAt;
	private BigDecimal amountCharged;
	private double commissionPercentage;
	private String cancelReason;
	
	private OrderView() { }
	
	public boolean isNewOrder() {
		return id == null;
	}
	
	public String getId() {
		return id;
	}

	public List<Tradesman> getTradesmen() {
		return tradesmen;
	}

	public void setTradesmen(List<Tradesman> tradesmen) {
		this.tradesmen = tradesmen;
	}

	public CommonUser getUser() {
		return user;
	}

	public void setUser(CommonUser user) {
		this.user = user;
	}

	public int getProfessionId() {
		return professionId;
	}
	
	public void setProfessionId(int professionId) {
		this.professionId = professionId;
	}
	
	public JobLocation getLocation() {
		return location;
	}
	
	public void setLocation(JobLocation location) {
		this.location = location;
	}
	
	public int[] getJobReasons() {
		return jobReasons;
	}
	
	public void setJobReasons(int[] jobReasons) {
		this.jobReasons = jobReasons;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public UserType getUserType() {
		return userType;
	}
	
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}
	
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public BigDecimal getAmountCharged() {
		return amountCharged;
	}

	public void setAmountCharged(BigDecimal amountCharged) {
		this.amountCharged = amountCharged;
	}

	public double getCommissionPercentage() {
		return commissionPercentage;
	}

	public void setCommissionPercentage(double commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}
	
	public int getTrafficSourceId() {
		return trafficSourceId;
	}

	public void setTrafficSourceId(int trafficSourceId) {
		this.trafficSourceId = trafficSourceId;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public BigDecimal calculateEarnings() {
		if(amountCharged != null && amountCharged.signum() > 0 && commissionPercentage > 0) {
			return Formatter.percentage(amountCharged, commissionPercentage);
		}
		return BigDecimal.ZERO;
	}

	public void addTradesman(Tradesman tradesman) {
		if(tradesmen == null) {
			tradesmen = new ArrayList<>();
		}
		if(!tradesmen.contains(tradesman)) {
			tradesmen.add(tradesman);
		}
	}
	
	public void removeTradesman(Tradesman tradesman) {
		if(tradesmen != null) {
			tradesmen.remove(tradesman);
		}
	}
	
	public boolean isCancelled() {
		return !StringUtils.isEmpty(cancelReason);
	}

	@Override
	public OrderData toDMO() {
		OrderData orderData = new OrderData();
		if(!StringUtils.isEmpty(id)) {
			orderData.set_id(new ObjectId(id));
		}
		fillOrderData(orderData);
		return orderData;
	}

	@Override
	public boolean updateDMO(OrderData entity) {
		if(entity.get_id().toHexString().equals(id)) {
			fillOrderData(entity);
			return true;
		}
		return false;
	}
	
	private void fillOrderData(OrderData orderData) {
		if(tradesmen != null) {
			ObjectId[] tradesmenIds = tradesmen.stream()
					.map(Tradesman::get_id)
					.toArray(ObjectId[]::new);
			orderData.setTradesmen(tradesmenIds);
		}
		if(user != null) {
			orderData.setUserId(user.get_id());
		}
		orderData.setProfessionId(professionId);
		orderData.setLocation(location);
		orderData.setJobReasons(jobReasons);
		orderData.setComment(comment);
		orderData.setUserType(userType);
		orderData.setOrderType(orderType);
		orderData.setCreatedAt(createdAt);
		orderData.setAmountCharged(amountCharged);
		orderData.setCommissionPercentage(commissionPercentage);
		orderData.setTrafficSourceId(trafficSourceId);
		orderData.setCancelReason(cancelReason);
	}
	
	public Set<String> validate() {
		Set<String> errors = new HashSet<>();
		if(tradesmen.isEmpty()) {
			errors.add("Missing tradesmen");
		}
		if(user == null) {
			errors.add("Missing user");
		}
		return errors;
	}
	
}
