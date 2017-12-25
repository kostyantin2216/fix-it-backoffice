/**
 * 
 */
package com.fixit.bo.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;
import org.springframework.stereotype.Component;

import com.fixit.core.dao.sql.StoredPropertyDao;
import com.fixit.core.data.sql.StoredProperty;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/01 17:16:57 GMT+2
 */
@Component
@ManagedBean
@ViewScoped
public class StoredPropertyBean {
	
	private final StoredPropertyDao mStoredPropDao;
	
	private List<StoredProperty> mStoredProps;
	
	public StoredPropertyBean(StoredPropertyDao storedPropertyDao) {
		mStoredPropDao = storedPropertyDao;
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
