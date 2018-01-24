/**
 * 
 */
package com.fixit.bo.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;

import com.fixit.core.dao.sql.StoredPropertyDao;
import com.fixit.core.data.sql.StoredProperty;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/01 17:16:57 GMT+2
 */
@ManagedBean
@ViewScoped
public class StoredPropertyBean {
	
	@ManagedProperty(value = "#{storedPropertyDao}")
	private transient StoredPropertyDao mStoredPropDao;
	
	private List<StoredProperty> mStoredProps;
	
	public void setmStoredPropDao(StoredPropertyDao mStoredPropDao) {
		this.mStoredPropDao = mStoredPropDao;
	}
	
	public List<StoredProperty> getStoredProperties() {
		if(mStoredProps == null) {
			mStoredProps = mStoredPropDao.findAll();
		}
		return mStoredProps;
	}
	
	public void onStoredPropertyEdit(RowEditEvent event) {
		mStoredPropDao.update((StoredProperty) event.getObject()); 
	}
	
}
