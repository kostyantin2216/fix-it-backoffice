/**
 * 
 */
package com.fixit.bo.beans;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.bson.types.ObjectId;
import org.primefaces.event.SelectEvent;
import org.springframework.util.StringUtils;

import com.fixit.bo.filters.FilterStore;
import com.fixit.bo.filters.OrderRequestFilter;
import com.fixit.bo.general.OrderRequestsTableType;
import com.fixit.bo.utils.DialogUtils;
import com.fixit.bo.views.OrderRequestView;
import com.fixit.components.maps.MapAreaController;
import com.fixit.components.orders.OrderRequestController;
import com.fixit.components.users.UserFactory;
import com.fixit.core.dao.mongo.TradesmanDao;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.mongo.CommonUser;
import com.fixit.core.data.mongo.OrderData;
import com.fixit.core.data.mongo.OrderRequest;
import com.fixit.core.data.mongo.Tradesman;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/17 22:51:44 GMT+2
 */
@ManagedBean
@ViewScoped
public class OrderRequestBean {
	
	@ManagedProperty("#{orderRequestController}")
	private transient OrderRequestController mOrderReqController;
	
	@ManagedProperty("#{mapAreaController}")
	private transient MapAreaController mMapAreaController;
	
	@ManagedProperty("#{tradesmanDao}")
	private transient TradesmanDao mTradesmanDao;
	
	@ManagedProperty("#{userFactory}")
	private transient UserFactory mUserFactory;
	
	private OrderRequest mSelectedRequest;
	
	private OrderRequestView mRequestView;

	private FilterStore<OrderRequest> mFilterStore;
	private OrderRequestFilter mFilter;
	
	@PostConstruct
	public void init() {		
		mFilter = OrderRequestFilter.createDefault();
		mFilterStore = new FilterStore<>(
				mOrderReqController.getAllRequests(), 
				OrderRequestFilter.createDefault(), 
				(o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt())
		);
	}
	
	public void setmOrderReqController(OrderRequestController mOrderReqController) {
		this.mOrderReqController = mOrderReqController;
	}
	
	public void setmMapAreaController(MapAreaController mMapAreaController) {
		this.mMapAreaController = mMapAreaController;
	}
	
	public void setmTradesmanDao(TradesmanDao mTradesmanDao) {
		this.mTradesmanDao = mTradesmanDao;
	}
	
	public void setmUserFactory(UserFactory mUserFactory) {
		this.mUserFactory = mUserFactory;
	}
	
	public List<OrderRequest> getOrderRequests() {
		return mFilterStore.getData();
	}
	
	public OrderRequestFilter getFilter() {
		return mFilter;
	}
	
	public OrderRequest getSelectedRequest() {
		return mSelectedRequest;
	}

	public void setSelectedRequest(OrderRequest selectedRequest) {
		mSelectedRequest = selectedRequest;
	}

	public OrderRequestView getView() {
		return mRequestView;
	}
	
	public void updateAndUnselectOrderRequest() {
		updateOrderRequest();
		unselectOrderRequest();
	}
	
	public void updateOrderRequest() {
		if(mSelectedRequest != null && mRequestView != null) {
			if(mRequestView.updateDMO(mSelectedRequest)) {
				mOrderReqController.updateRequest(mSelectedRequest);
			}
		}
	}
	
	public void unselectOrderRequest() {
		mSelectedRequest = null;
		mRequestView = null;
	}
	
	public String getRowStyleClass(OrderRequest request) {
		if(mFilter.getTableType() == OrderRequestsTableType.ALL) {
			if(request.isFulfilled()) {
				return "success";
			} else if(request.isDenied()) {
				return "warning";
			}
		} else if(mFilter.getTableType() == OrderRequestsTableType.PENDING) {
			if(request.isNewRequest()) {
				return "success";
			}
		}
		return null;
	}

	public void onTableFilterChanged(SelectEvent event) {
		onTableFilterChanged();
	}
	
	public void onTableFilterChanged() {
		mFilterStore.applyFilter(mFilter);
	}
	
	public void denyOrderRequest() {
		if(mSelectedRequest != null && mRequestView != null) {
			DialogUtils.requestInput("Deny Reason", "Why is this order being denied?");
		}
	}
	
	public void onDenyReasonGiven(SelectEvent event) {
		String denyReason = (String) event.getObject();
		if(StringUtils.isEmpty(denyReason)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cannot deny order without a reason"));
		} else {
			mRequestView.setReasonDenied(denyReason);
			mOrderReqController.denyRequest(mSelectedRequest, denyReason);
		}
	}
	
	public void fulfillOrderRequest() {
		if(mSelectedRequest != null && mRequestView != null) {
			mRequestView.updateDMO(mSelectedRequest);
			OrderData orderData = mOrderReqController.completeRequest(mSelectedRequest);
			
			String msg;
			if(orderData != null) {
				mRequestView.setFulfilledOrderId(orderData.get_id().toHexString());
				mFilterStore.applyFilter(mFilter, true);
				msg = "Order success";
			} else {
				msg = "Order failed";
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
		}
	}

	public void onRequestSelected(SelectEvent event) {
		ObjectId[] tradesmenIds = mSelectedRequest.getTradesmen();
		List<Tradesman> tradesmen;
		if(tradesmenIds != null) {
			tradesmen =  mTradesmanDao.findIn(TradesmanDao.PROP_ID, (Object[]) mSelectedRequest.getTradesmen());
		} else {
			tradesmen = null;
		}
		CommonUser user = mUserFactory.tryFindUser(mSelectedRequest.getUserId());
		
		if(mSelectedRequest.isNewRequest()) {
			mSelectedRequest.setNewRequest(false);
			mOrderReqController.updateRequest(mSelectedRequest);
		}
		
		mRequestView = OrderRequestView.transform(mSelectedRequest, user, tradesmen);
	}
	
	public void onAddressSelected(SelectEvent event) {
		String address = event.getObject().toString();

		Optional<JobLocation> jobLocation = mMapAreaController.createLocation(address);
		if(jobLocation.isPresent()) {
			mRequestView.setLocation(jobLocation.get());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid address"));
		}
    }
	
	public void onUserCreated(SelectEvent event) {
		CommonUser user = (CommonUser) event.getObject();
		mRequestView.setUser(user);
	}
}
