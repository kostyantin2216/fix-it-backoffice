/**
 * 
 */
package com.fixit.bo.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.bson.types.ObjectId;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.util.StringUtils;

import com.fixit.bo.filters.Filter;
import com.fixit.bo.filters.FilterStore;
import com.fixit.bo.filters.OrderDataFilter;
import com.fixit.bo.general.OrderTableType;
import com.fixit.bo.utils.DialogUtils;
import com.fixit.bo.utils.FacesUtils;
import com.fixit.bo.views.OrderView;
import com.fixit.components.maps.MapAreaController;
import com.fixit.components.users.UserFactory;
import com.fixit.core.dao.mongo.OrderDataDao;
import com.fixit.core.dao.mongo.TradesmanDao;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.mongo.CommonUser;
import com.fixit.core.data.mongo.OrderData;
import com.fixit.core.data.mongo.Tradesman;
import com.fixit.core.logging.FILog;
import com.fixit.core.utils.Constants;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/21 23:25:19 GMT+3
 */
@ManagedBean
@ViewScoped
public class OrderBean implements Serializable {
	private static final long serialVersionUID = -1502443699202960075L;

	@ManagedProperty(value = "#{mapAreaController}")
	private transient MapAreaController mMapAreaController;

	@ManagedProperty(value = "#{userFactory}")
	private transient UserFactory mUserFactory;

	@ManagedProperty(value = "#{orderDataDao}")
	private transient OrderDataDao mOrderDao;
	
	@ManagedProperty(value = "#{tradesmanDao}")
	private transient TradesmanDao mTradesmanDao;
	
	private FilterStore<OrderData> mOrderDataStore;
	
	private OrderData mSelectedOrder;
	private OrderView mOrderView;
	
	private OrderDataFilter mTableFilter;

	public void setmMapAreaController(MapAreaController mMapAreaController) {
		this.mMapAreaController = mMapAreaController;
	}

	public void setmUserFactory(UserFactory mUserFactory) {
		this.mUserFactory = mUserFactory;
	}

	public void setmOrderDao(OrderDataDao mOrderDao) {
		this.mOrderDao = mOrderDao;
	}

	public void setmTradesmanDao(TradesmanDao mTradesmanDao) {
		this.mTradesmanDao = mTradesmanDao;
	}

	@PostConstruct
	public void init() {
		// We want 2 different filters instances, one for editing and one for the current values.
		mTableFilter = OrderDataFilter.createDefault(); 
		initStore(OrderDataFilter.createDefault());
		
		final String orderId = FacesUtils.getExternalContextRequestParam(Constants.ARG_ORDER_ID);
		
		if(!StringUtils.isEmpty(orderId) && ObjectId.isValid(orderId)) {
			Optional<OrderData> order = mOrderDataStore.getData()
					.stream()
					.filter(o -> o.get_id().toHexString().equals(orderId))
					.findFirst();
			if(order.isPresent()) {
				FILog.i("setting selectd order: " + order.get());
				mSelectedOrder = order.get();
				onOrderSelected(null);
			} else {
				FILog.i("could not find order with id: " + orderId);
			}
		}
	}
	
	private void initStore(Filter<OrderData> filter) {
		mOrderDataStore = new FilterStore<>(
				mOrderDao.findAll(), 
				filter, 
				(o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt())
		);
	}
	
	public List<OrderData> getOrderData() {
		return mOrderDataStore.getData();
	}
	
	public OrderDataFilter getTableFilter() {
		return mTableFilter;
	}
	
	public void setSelectedOrder(OrderData order) {
		mSelectedOrder = order;
	}
	
	public OrderData getSelectedOrder() {
		return mSelectedOrder;
	}
	
	public OrderView getOrderView() {
		return mOrderView;
	}
	
	public void onTableFilterChanged(SelectEvent event) {
		onTableFilterChanged();
	}
	
	public void onTableFilterChanged() {
		mOrderDataStore.applyFilter(mTableFilter);
	}
	
	public void clearDateRangeFilter(boolean fromDate) {
		if(fromDate) {
			mTableFilter.clearFromDate();
		} else {
			mTableFilter.clearToDate();
		}
		
		onTableFilterChanged();
	}
	
	public void clearUserFilter() {
		mTableFilter.clearUser();
		
		onTableFilterChanged();
	}
	
	public void clearTradesmanFilter() {
		mTableFilter.clearTradesman();
		
		onTableFilterChanged();
	}
	
	public String getRowStyleClass(OrderData orderData) {
		if(mTableFilter.getTableType() == OrderTableType.GENERAL) {
			if(orderData.isOrderComplete()) {
				return "success";
			} 
			if(orderData.isOrderCancelled()) {
				return "warning";
			}
		}
		return null;
	}
	
	public void refreshOrders() {
		initStore(OrderDataFilter.createCopy(mTableFilter));
	}
	
	public void createNewOrder() {
		DialogUtils.showProfessionSelection();
	}
	
	public void onProfessionForNewOrderChosen(SelectEvent event) {
		int profession = (int) event.getObject();
		mOrderView = OrderView.newOrder(profession);
	}
	
	public void cancelOrder() {
		if(mSelectedOrder != null && mOrderView != null) {
			DialogUtils.requestInput("Cancel Reason", "Why is this order being cancelled?");
		}
	}
	
	public void onCancellationReasonProvided(SelectEvent event) {
		String cancelReason = (String) event.getObject();
		if(!StringUtils.isEmpty(cancelReason)) {
			mSelectedOrder.setCancelReason(cancelReason);
			mOrderView.setCancelReason(cancelReason);
			mOrderDao.update(mSelectedOrder);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cannot canel order without a reason."));
		}
	}
	
	public void completeOrder(OrderData orderData) {
		DialogUtils.showOrderCompletion(orderData.get_id().toHexString());
	}
	
	public void completeOrderAndCloseDialog() {
		RequestContext.getCurrentInstance().closeDialog(mSelectedOrder);
	}
	
	public void onCompleteOrderDialogClosed(SelectEvent event) {
		final OrderData order = (OrderData) event.getObject();
		
		mOrderDao.update(order);
		
		Optional<OrderData> oldOrder = mOrderDataStore.getData()
				.stream()
				.filter(o -> o.get_id().equals(order.get_id()))
				.findFirst();
		if(oldOrder.isPresent()) {
			mOrderDataStore.removeOrder(oldOrder.get());
			mOrderDataStore.addOrder(order);
		} else {
			FILog.i("Could not find order before edit");
			refreshOrders();
		}
	}
	
	public void chooseTradesman() {
		DialogUtils.showTradesmenSelectionTable(mOrderView.getProfessionId());
	}
	
	public void onTradesmanChosen(SelectEvent event) {
	    Tradesman tradesman = (Tradesman) event.getObject();
	    mOrderView.addTradesman(tradesman);
	}
	
	public void onUserCreated(SelectEvent event) {
		CommonUser user = (CommonUser) event.getObject();
		mOrderView.setUser(user);
		mOrderView.setUserType(user.getType());
	}

	public void onOrderSelected(SelectEvent event) {
		ObjectId[] tradesmenIds = mSelectedOrder.getTradesmen();
		List<Tradesman> tradesmen;
		if(tradesmenIds != null) {
			tradesmen =  mTradesmanDao.findIn(TradesmanDao.PROP_ID, (Object[]) mSelectedOrder.getTradesmen());
		} else {
			tradesmen = null;
		}
		CommonUser user = mUserFactory.getUserForOrder(mSelectedOrder);
		mOrderView = OrderView.forOrder(mSelectedOrder, user, tradesmen);
	}

	public void onUserSelected(SelectEvent event) {
		mOrderView.setUserType(mOrderView.getUser().getType());
	}

	public void onAddressSelected(SelectEvent event) {
		String address = event.getObject().toString();

		Optional<JobLocation> jobLocation = mMapAreaController.createLocation(address);
		if(jobLocation.isPresent()) {
			mOrderView.setLocation(jobLocation.get());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid address"));
		}
    }
	
	public void unselectOrder() {
		mSelectedOrder = null;
		mOrderView = null;
	}
	
	public void saveOrUpdateOrder() {
		if(mOrderView != null) {
			if(mOrderView.isNewOrder()) { 
				OrderData orderData = mOrderView.toDMO();
				mOrderDao.save(orderData);
				if(orderData.get_id() != null) {
					mOrderDataStore.addOrder(orderData);
					mOrderDataStore.applyFilter(mTableFilter, true);
					mSelectedOrder = orderData;
					mOrderView = OrderView.forOrder(mSelectedOrder, mOrderView.getUser(), mOrderView.getTradesmen());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Saved new order"));
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Couldn't save order"));
				}
			} else if(mSelectedOrder != null) { 
				if(mOrderView.updateDMO(mSelectedOrder)) {
					mOrderDao.update(mSelectedOrder);
					mOrderDataStore.updateOrder(mSelectedOrder);
					mOrderDataStore.applyFilter(mTableFilter, true);
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Inconsistency in order data"));
				}
			}
		}
	}
	
	public void saveOrUpdateAndUnselectedOrder() {
		saveOrUpdateOrder();
		unselectOrder();
	}
	
	public void deleteSelectedOrder() {
		if(mSelectedOrder != null) {
			mOrderDao.delete(mSelectedOrder.get_id());
			mOrderDataStore.removeOrder(mSelectedOrder);
			unselectOrder();
		}
	}
	
	public void pickTradesmanForFilter() {
		DialogUtils.showTradesmenSelectionTable();
	}
	
	public void onTradesmanForFilterSelected(SelectEvent event) {
		Tradesman tradesman = (Tradesman) event.getObject();
		mTableFilter.setTradesman(tradesman);
		onTableFilterChanged();
	}

}
