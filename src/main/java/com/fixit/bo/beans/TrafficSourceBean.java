/**
 * 
 */
package com.fixit.bo.beans;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;
import org.springframework.util.StringUtils;

import com.fixit.core.dao.sql.TrafficSourceDao;
import com.fixit.core.data.sql.TrafficSource;
import com.fixit.core.logging.FILog;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/15 22:54:37 GMT+2
 */
@ManagedBean
@ViewScoped
public class TrafficSourceBean {

	@ManagedProperty(value = "#{trafficSourceDao}")
	public transient TrafficSourceDao mTrafficSrcDao;
	
	private List<TrafficSource> mTrafficSources;
	
	private TrafficSource mNewTrafficSource;
	
	public void setmTrafficSrcDao(TrafficSourceDao trafficSourceDao) {
		mTrafficSrcDao = trafficSourceDao;
	}
	
	public List<TrafficSource> getTrafficSources() {
		if(mNewTrafficSource == null) {
			mTrafficSources = mTrafficSrcDao.findAll();
		}
		return mTrafficSources;
	}
	
	public TrafficSource getNewTrafficSource() {
		return mNewTrafficSource;
	}
	
	public void createNewTrafficSource() {
		FILog.i("creating new traffic source");
		mNewTrafficSource = new TrafficSource();
	}
	
	public void saveNewTrafficSource() {
		if(mNewTrafficSource != null) {
			if(StringUtils.isEmpty(mNewTrafficSource.getName())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Name cannot be empty"));
			} else {
				mTrafficSrcDao.save(mNewTrafficSource);
				mTrafficSources.add(mNewTrafficSource);
				mNewTrafficSource = null;
			}
		}
	}
	
	public void onTafficSourceEdit(RowEditEvent event) {
		mTrafficSrcDao.update((TrafficSource) event.getObject()); 
	}
	
	public void deleteSource(TrafficSource ts) {
		mTrafficSrcDao.delete(ts.getId());
		mTrafficSources.remove(ts);
	}
	
	public String getNameForSource(Integer trafficSourceId) {
		TrafficSource ts = mTrafficSrcDao.findById(trafficSourceId);
		if(ts != null) {
			return ts.getName();
		}
		return "NA";
	}
	
}
