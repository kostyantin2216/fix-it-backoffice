/**
 * 
 */
package com.fixit.bo.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;

import com.fixit.core.data.JobLocation;
import com.fixit.core.data.OrderType;
import com.fixit.core.data.mongo.CommonUser;
import com.fixit.core.data.mongo.OrderRequest;
import com.fixit.core.data.mongo.Tradesman;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/18 21:20:35 GMT+2
 */
public class OrderRequestView implements DataModelView<OrderRequest> {
	
	public static OrderRequestView transform(OrderRequest request, CommonUser user, List<Tradesman> tradesmen) {
		OrderRequestView view = new OrderRequestView();
		view.id = request.get_id().toHexString();
		view.tradesmen = tradesmen;
		view.user = user;
		view.professionId = request.getProfessionId();
		view.address = request.getAddress();
		view.location = request.getLocation();
		view.jobReasons = request.getJobReasons();
		view.comment = request.getComment();
		view.trafficSourceId = request.getTrafficSourceId();
		view.orderType = request.getOrderType();
		view.notifyTradesmen = request.isNotifyTradesmen();
		if(request.getFulfilledOrderId() != null) {
			view.fulfilledOrderId = request.getFulfilledOrderId().toHexString();
		}
		view.reasonDenied = request.getReasonDenied();
		view.createdAt = request.getCreatedAt();
		view.newRequest = request.isNewRequest();
		return view;
	}
	
	private String id;
	private List<Tradesman> tradesmen;
	private CommonUser user;
	private int professionId;
	private String address;
	private JobLocation location;
	private int[] jobReasons;
	private String comment;
	private int trafficSourceId;
	private OrderType orderType;
	private boolean notifyTradesmen;
	private String fulfilledOrderId;
	private String reasonDenied;
	private Date createdAt;
	private boolean newRequest;
	
	private OrderRequestView(){ }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public int getTrafficSourceId() {
		return trafficSourceId;
	}

	public void setTrafficSourceId(int trafficSourceId) {
		this.trafficSourceId = trafficSourceId;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public boolean isNotifyTradesmen() {
		return notifyTradesmen;
	}

	public void setNotifyTradesmen(boolean notifyTradesmen) {
		this.notifyTradesmen = notifyTradesmen;
	}

	public String getFulfilledOrderId() {
		return fulfilledOrderId;
	}

	public void setFulfilledOrderId(String fulfilledOrderId) {
		this.fulfilledOrderId = fulfilledOrderId;
	}

	public String getReasonDenied() {
		return reasonDenied;
	}

	public void setReasonDenied(String reasonDenied) {
		this.reasonDenied = reasonDenied;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isNewRequest() {
		return newRequest;
	}

	public void setNewRequest(boolean newRequest) {
		this.newRequest = newRequest;
	}
	
	public boolean isPending() {
		return StringUtils.isEmpty(fulfilledOrderId) && StringUtils.isEmpty(reasonDenied);
	}
	
	public boolean isFulfilled() {
		return !StringUtils.isEmpty(fulfilledOrderId);
	}
	
	public boolean isDenied() {
		return !StringUtils.isEmpty(reasonDenied);
	}
	
	public boolean isEditble() {
		return isPending();
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

	@Override
	public OrderRequest toDMO() {
		OrderRequest request = new OrderRequest();
		request.set_id(new ObjectId(id));
		fillRequest(request);
		return request;
	}

	@Override
	public boolean updateDMO(OrderRequest entity) {
		if(!StringUtils.isEmpty(id) && id.equals(entity.get_id().toHexString())) {
			fillRequest(entity);
			
			return true;
		}
		return false;
	}
	
	
	private void fillRequest(OrderRequest request) {
		if(tradesmen != null) {
			request.setTradesmen(
					tradesmen.stream()
						  	 .map(Tradesman::get_id)
						  	 .toArray(ObjectId[]::new)
			);
		}
		request.setUserId(user.get_id());
		request.setProfessionId(professionId);
		request.setAddress(address);
		request.setLocation(location);
		request.setJobReasons(jobReasons);
		request.setComment(comment);
		request.setTrafficSourceId(trafficSourceId);
		request.setOrderType(orderType);
		request.setNotifyTradesmen(notifyTradesmen);
		if(!StringUtils.isEmpty(fulfilledOrderId)) {
			request.setFulfilledOrderId(new ObjectId(fulfilledOrderId));
		}
		if(!StringUtils.isEmpty(reasonDenied)) {
			request.setReasonDenied(reasonDenied);
		}
		request.setCreatedAt(createdAt);
		request.setNewRequest(newRequest);
	}

}
