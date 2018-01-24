/**
 * 
 */
package com.fixit.bo.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import com.fixit.core.logging.FILog;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/22 00:29:53 GMT+3
 */
public class FacesUtils {
	
	final static String DIALOG_VIEW_PATH = "/faces/dialogs/";

	public static void printRequest() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance()
					.getExternalContext()
					.getRequest();
		String msg = new StringBuilder().append("\n")
				.append("Path Info: ").append(req.getPathInfo()).append("\n")
				.append("Remote Address: ").append(req.getRemoteAddr()).append("\n")
				.append("Context Path: ").append(req.getContextPath()).append("\n")
				.append("Query String: ").append(req.getQueryString()).append("\n")
				.append("Request URI: ").append(req.getRequestURI()).append("\n")
				.toString();
		System.out.println(msg);
	}
	
	public static String getViewId() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewId();
	}
	
	public static String getRequestURI() {
		return ((HttpServletRequest) FacesContext.getCurrentInstance()
					.getExternalContext()
					.getRequest()).getRequestURI();
	}
	
	public static void printComponentTree(List<UIComponent> components) {
		printRecursive(components, 0);
	}
	
	private static void printRecursive(List<UIComponent> components, int level) {
		if(components != null && components.size() > 0) {
			if(level == 0) {
				FILog.i("UIComponent tree");
			}
			for(UIComponent component : components) {
				String prefix = "|";
				while(prefix.length() - 1 != level) {
					prefix += "-";
				}
				
				FILog.i(prefix + " " + component.getId() + " of type " + component.getClass().getName());
				
				List<UIComponent> children = component.getChildren();
				if(children != null && children.size() > 0) {
					printRecursive(children, ++level);
				}
			}
		}
	}
	
	public static <T> T evalEL(String el, Class<T> cls) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().evaluateExpressionGet(context, el, cls);
	}
	
	public static Map<String, Object> createDialogOptions(int height, int width, boolean resizeable, boolean modal) {
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("width", width);
		options.put("height", height);
		options.put("contentWidth", width);
		options.put("contentHeight", height);
		options.put("resizable", resizeable);
        options.put("modal", modal);
        return options;
	}
	
	public static Map<String, List<String>> transformDialogParams(Map<String, String> params) {
        return params.entrySet()
        					  .stream()
        					  .collect(
        							  Collectors.toMap(
        									  e -> e.getKey(), 
        									  e -> Arrays.asList(e.getValue())
        							  )
        					  );
	}
	
	protected static void showDialog(String view, int height, int width, boolean resizable, boolean modal) {
		showDialog(view, height, width, resizable, modal, null);
	}
	
	protected static void showDialog(String view, int height, int width, boolean resizeable, boolean modal, Map<String, String> params) {
		Map<String,Object> options = createDialogOptions(height, width, resizeable, modal);
        
        Map<String, List<String>> paramsMap = null;
        if(params != null) {
        	paramsMap = transformDialogParams(params);
        }
        
		RequestContext.getCurrentInstance().openDialog(DIALOG_VIEW_PATH + view, options, paramsMap);
	}
	
	public static String getExternalContextRequestParam(String key) {
		return FacesContext.getCurrentInstance()
		 .getExternalContext()
		 .getRequestParameterMap()
		 .get(key);
	}
	
}
