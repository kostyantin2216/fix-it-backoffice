/**
 * 
 */
package com.fixit.bo.beans;

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

import com.fixit.bo.utils.DialogUtils;
import com.fixit.bo.utils.FacesUtils;
import com.fixit.bo.views.DirectOrderView;
import com.fixit.components.maps.MapAreaController;
import com.fixit.components.orders.OrderController;
import com.fixit.components.orders.OrderParams;
import com.fixit.core.dao.mongo.TradesmanDao;
import com.fixit.core.dao.sql.JobReasonDao;
import com.fixit.core.dao.sql.ProfessionDao;
import com.fixit.core.dao.sql.TrafficSourceDao;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.mongo.CommonUser;
import com.fixit.core.data.mongo.OrderData;
import com.fixit.core.data.mongo.Tradesman;
import com.fixit.core.data.sql.DataComment;
import com.fixit.core.logging.FILog;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.Formatter;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/06 20:50:07 GMT+2
 */
@ManagedBean
@ViewScoped
public class DirectOrderBean {

	@ManagedProperty(value = "#{orderController}")
	private transient OrderController mOrderController;

	@ManagedProperty(value = "#{tradesmanDao}")
	private transient TradesmanDao mTradesmanDao;
	
	@ManagedProperty(value = "#{professionDao}")
	private transient ProfessionDao mProfessionDao;

	@ManagedProperty(value = "#{jobReasonDao}")
	private transient JobReasonDao mJobReasonDao;
	
	@ManagedProperty(value = "#{trafficSourceDao}")
	private transient TrafficSourceDao mTrafficSourceDao;

	@ManagedProperty(value = "#{mapAreaController}")
	private transient MapAreaController mMapAreaController;
	
	private DirectOrderView mView;
	
	@PostConstruct
	public void init() {
		final String tradesmanId = FacesContext.getCurrentInstance()
				 .getExternalContext()
				 .getRequestParameterMap()
				 .get(Constants.ARG_TRADESMAN_ID);
		FILog.i("initializing direcrt order bean with tradesman id: " + tradesmanId);
		if(!StringUtils.isEmpty(tradesmanId) && ObjectId.isValid(tradesmanId)) {
			Tradesman tradesman = mTradesmanDao.findById(new ObjectId(tradesmanId));
			if(tradesman != null) {
				final String professionId = FacesContext.getCurrentInstance()
						 .getExternalContext()
						 .getRequestParameterMap()
						 .get(Constants.ARG_PROFESSION);
				
				if(Formatter.isInteger(professionId)) {
					mView = new DirectOrderView(tradesman, Integer.parseInt(professionId));
				} else {
					mView = new DirectOrderView(tradesman);
				}
			} 
		}
		
		if(mView == null) {
			mView = new DirectOrderView();
		}
	}
	
	public void setmOrderController(OrderController mOrderController) {
		this.mOrderController = mOrderController;
	}

	public void setmTradesmanDao(TradesmanDao mTradesmanDao) {
		this.mTradesmanDao = mTradesmanDao;
	}
	
	public void setmProfessionDao(ProfessionDao mProfessionDao) {
		this.mProfessionDao = mProfessionDao;
	}

	public void setmJobReasonDao(JobReasonDao mJobReasonDao) {
		this.mJobReasonDao = mJobReasonDao;
	}
	
	public void setmTrafficSourceDao(TrafficSourceDao mTrafficSourceDao) {
		this.mTrafficSourceDao = mTrafficSourceDao;
	}

	public void setmMapAreaController(MapAreaController mMapAreaController) {
		this.mMapAreaController = mMapAreaController;
	}

	public DirectOrderView getView() {
		return mView;
	}
	
	public void selectTradesman() {		
		DialogUtils.showTradesmenSelectionTable();
	}
	
	public void onTradesmanSelected(SelectEvent event) {
		Tradesman tradesman = (Tradesman) event.getObject();
		mView.setTradesman(tradesman);
	}
	
	public void onUserCreated(SelectEvent event) {
		CommonUser user = (CommonUser) event.getObject();
		mView.setUser(user);
	}
	
	public void onAddressSelected(SelectEvent event) {
		String address = mView.getLocation().getGoogleAddress();

		Optional<JobLocation> jobLocation = mMapAreaController.createLocation(address);
		if(jobLocation.isPresent()) {
			mView.setLocation(jobLocation.get());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid address"));
		}
    }
	
	public boolean sendOrder() {
		String error = mView.validate();
		if(StringUtils.isEmpty(error)) {
			OrderParams op = mView.toParams(mProfessionDao, mJobReasonDao, mTrafficSourceDao);
			Optional<OrderData> orderData = mOrderController.orderTradesmen(op);
			
			if(orderData.isPresent()) {
				String orderComment = mView.getOrderComment();
				if(!StringUtils.isEmpty(orderComment)) {
					String orderId = orderData.get().get_id().toHexString();
					
					DataComment dataComment = new DataComment();
					dataComment.setContent(orderComment);
					
					DataCommentsBean dataCommentsBean = FacesUtils.evalEL("#{dataCommentsBean}", DataCommentsBean.class);
					dataCommentsBean.setEditableComment(dataComment);
					dataCommentsBean.saveEditableComment(Constants.DATA_TYPE_ORDER_DATA, orderId);
				}
			}

			return true;
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(error));
			return false;
		}
	}
	
	public void sendOrderAndCloseDialog() {
		if(sendOrder()) {
			RequestContext.getCurrentInstance().closeDialog(null);
		}
	}
	
}
