/**
 * 
 */
package com.fixit.bo.beans.datatable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.fixit.core.dao.mongo.impl.TradesmanDaoImpl;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/27 19:53:26 GMT+3
 */
@ManagedBean
@ViewScoped
public class DataTableBean {
	
	private String title;

	private DataTableView<?, ?> dataTableView;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@PostConstruct
	public void init() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(!fc.isPostback()) {
			String viewId = fc.getViewRoot().getViewId();
			
			switch (viewId) {
			case "/faces/views/tradesmen.xhtml":
				title = "Tradesmen";
				dataTableView = new TradesmanTableView(applicationContext.getBean(TradesmanDaoImpl.class));
				break;
	
			default:
				break;
			}
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public DataTableView<?, ?> getView() {
		return dataTableView;
	}
	
}
