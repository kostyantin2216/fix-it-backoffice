/**
 * 
 */
package com.fixit.bo.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.fixit.bo.general.ConfigurationsViewType;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/15 21:13:56 GMT+2
 */
@ManagedBean
@SessionScoped
public class ConfigurationsBean {

	private ConfigurationsViewType mViewType;
	
	@PostConstruct
	private void init() {
		mViewType = ConfigurationsViewType.STORED_PROPERTIES;
	}
	
	public void setViewType(ConfigurationsViewType viewType) {
		mViewType = viewType;
	}
	
	public ConfigurationsViewType getViewType() {
		return mViewType;
	}
	
}
