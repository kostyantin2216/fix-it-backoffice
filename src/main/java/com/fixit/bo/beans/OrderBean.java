/**
 * 
 */
package com.fixit.bo.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.fixit.bo.general.OrderDataFilter;
import com.fixit.bo.general.OrderDataStore;
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
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.PlaceAutocompleteType;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/21 23:25:19 GMT+3
 */
@ManagedBean
@ViewScoped
public class OrderBean implements Serializable {
	private static final long serialVersionUID = -1502443699202960075L;

	@ManagedProperty(value = "#{geoApiContext}")
	private transient GeoApiContext mGeoApiContext;

	@ManagedProperty(value = "#{mapAreaController}")
	private transient MapAreaController mMapAreaController;

	@ManagedProperty(value = "#{userFactory}")
	private transient UserFactory mUserFactory;

	@ManagedProperty(value = "#{orderDataDao}")
	private transient OrderDataDao mOrderDao;
	
	@ManagedProperty(value = "#{tradesmanDao}")
	private transient TradesmanDao mTradesmanDao;
	
	private OrderDataStore mOrderDataStore;
	
	private OrderData mSelectedOrder;
	private OrderView mOrderView;
	
	private OrderDataFilter mTableFilter;
	
	public void setmGeoApiContext(GeoApiContext mGeoApiContext) {
		this.mGeoApiContext = mGeoApiContext;
	}

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
		mTableFilter = OrderDataFilter.createDefault();
		mOrderDataStore = new OrderDataStore(mOrderDao.findAll());
		
		final String orderId = FacesContext.getCurrentInstance()
									 .getExternalContext()
									 .getRequestParameterMap()
									 .get(Constants.ARG_ORDER_ID);
		
		if(!StringUtils.isEmpty(orderId) && ObjectId.isValid(orderId)) {
			Optional<OrderData> order = mOrderDataStore.getData()
					.stream()
					.filter(o -> o.get_id().toHexString().equals(orderId))
					.findFirst();
			if(order.isPresent()) {
				FILog.i("setting selectd order: " + order.get());
				mSelectedOrder = order.get();
			} else {
				FILog.i("could not find order with id: " + orderId);
			}
		}
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
	
	public void refreshOrders() {
		mOrderDataStore = new OrderDataStore(mOrderDao.findAll(), mOrderDataStore.getFilter());
	}
	
	public void createNewOrder() {
		FacesUtils.showDialog("dfChooseProfession", 255, 385, false, true);
	}
	
	public void onProfessionForNewOrderChosen(SelectEvent event) {
		int profession = (int) event.getObject();
		mOrderView = OrderView.newOrder(profession);
	}
	
	public void completeOrder(OrderData orderData) {
		Map<String, String> params = new HashMap<>();
		params.put(Constants.ARG_ORDER_ID, orderData.get_id().toHexString());

		FacesUtils.showDialog("dfCompleteOrder", 270, 270, false, true, params);
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
		Map<String, String> params = new HashMap<>();
		params.put(Constants.ARG_PROFESSION, String.valueOf(mOrderView.getProfessionId()));
		
		FacesUtils.showDialog("dfTradesmenTable", 450, 1100, false, true, params);
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
		List<Tradesman> tradesmen = mTradesmanDao.findIn(TradesmanDao.PROP_ID, (Object[]) mSelectedOrder.getTradesmen());
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
	
	public List<String> completeAddress(String query) {
		try {
			AutocompletePrediction[] prediction = PlacesApi.placeAutocomplete(mGeoApiContext, query)
				.types(PlaceAutocompleteType.ADDRESS)
				.components(ComponentFilter.country("ZA"))
				.await();
			
			return Arrays.stream(prediction)
						.map(p -> p.description)
						.collect(Collectors.toList());
		} catch (ApiException | InterruptedException | IOException e) {
			FILog.e("Order address autocomplete", e.getMessage(), e);
		}
		
		return Collections.emptyList();
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

}
