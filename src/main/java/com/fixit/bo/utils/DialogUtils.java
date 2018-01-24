/**
 * 
 */
package com.fixit.bo.utils;

import java.util.HashMap;
import java.util.Map;

import com.fixit.core.utils.Constants;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/23 15:57:15 GMT+2
 */
public class DialogUtils {
	
	public static void showTradesmenSelectionTable() {
		showTradesmenSelectionTable(-1);
	}
	
	public static void showTradesmenSelectionTable(int professionId) {
		Map<String, String> params;
		if(professionId > 0) {
			params = new HashMap<>();
			params.put(Constants.ARG_PROFESSION, String.valueOf(professionId));
		} else {
			params = null;
		}
		
		FacesUtils.showDialog("dfTradesmenTable", 450, 1100, false, true, params);
	}
	
	public static void showProfessionSelection() {
		FacesUtils.showDialog("dfChooseProfession", 255, 385, false, true);
	}
	
	public static void showOrderCompletion(String orderId) {
		Map<String, String> params = new HashMap<>();
		params.put(Constants.ARG_ORDER_ID, orderId);
		
		FacesUtils.showDialog("dfCompleteOrder", 270, 270, false, true, params);
	}
	
	public static void showUserCreation() {
		FacesUtils.showDialog("dfCreateUser", 190, 400, false, true);
	}
	
	public static void showDirectOrder(String tradesmanId) {
		Map<String, String> params = new HashMap<>();
		params.put(Constants.ARG_TRADESMAN_ID, tradesmanId);
		
		FacesUtils.showDialog("dfDirectOrder", 520, 800, false, false, params);
	}
	
	public static void requestInput(String dialogTitle, String inputLabel) {
		Map<String, String> params = new HashMap<>();
		params.put(Constants.ARG_TITLE, dialogTitle);
		params.put(Constants.ARG_LABEL, inputLabel);
		
		FacesUtils.showDialog("dfInputText", 175, 300, false, false, params);
	}
	
}
