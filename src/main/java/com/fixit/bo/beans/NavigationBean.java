/**
 * 
 */
package com.fixit.bo.beans;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/22 00:27:42 GMT+3
 */
@Component
@ManagedBean
@SessionScoped
public class NavigationBean {
	
	private final static ConcurrentMap<String, Integer> NavigationItemIndexCache = new ConcurrentHashMap<>();
	
	private final static MenuModel MENU_MODEL = NavigationItem.toMenuModel();
	
	private enum NavigationItem {
		TRADESMEN("Tradesmen", "/faces/tradesmen.xhtml", "ui-icon-person"),
		ORDERS("Orders", "/faces/orders.xhtml", "ui-icon-cart"),
		ORDER_REQUESTS("Order Requests", "/faces/orderRequests.xhtml", "ui-icon-notice"),
		MAP_AREAS("Map Areas", "/faces/mapAreas.xhtml", "ui-icon-search"),
		STORED_PROPERTIES("Configurations", "/faces/configurations.xhtml", "ui-icon-wrench");
		
		final String title;
		final String viewId;
		final String icon;
		
		NavigationItem(String title, String viewId, String icon) {
			this.title = title;
			this.viewId = viewId;
			this.icon = icon;
		}
		
		MenuItem toMenuItem() {
			DefaultMenuItem menuItem = new DefaultMenuItem(title);
			menuItem.setUrl(viewId);
			menuItem.setIcon(icon);
			return menuItem;
		}
		
		static MenuModel toMenuModel() {
			DefaultMenuModel menuModel = new DefaultMenuModel();
			
			for(NavigationItem item : values()) {
				menuModel.addElement(item.toMenuItem());
			}
			
			return menuModel;
		}
		
	}
	
	public MenuModel getMenuModel() {
		return MENU_MODEL;
	}
	
	public Integer getCurrentNavigationIndex() {
		String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		
		if(StringUtils.isEmpty(viewId)) {
			throw new IllegalStateException("Could not get viewId from FacesContext::getViewRoot");
		}
		
		Integer index = NavigationItemIndexCache.get(viewId);
		if(index == null) {
			final NavigationItem[] navItems = NavigationItem.values();
			for(int i = 0; i < navItems.length; i++) {
				if(viewId.equals(navItems[i].viewId)) {
					index = navItems[i].ordinal();
					break;
				}
			}
			if(index == null) {
				throw new IllegalStateException("Invalid viewId " + viewId + " from FacesConext::getViewRoot");
			} else {
				NavigationItemIndexCache.putIfAbsent(viewId, index);
			}
		}
		return index;
	}
	
}
