/**
 * 
 */
package com.fixit.bo.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fixit.bo.general.OrderTableType;
import com.fixit.core.data.OrderType;
import com.fixit.core.data.UserType;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/27 23:54:29 GMT+3
 */
@Component
@ManagedBean
@SessionScoped
public class SelectionsBean {
	
	@Autowired
	private DataAccessBean dataAccessBean;
	
	private List<SelectItem> mProfessionSelections;
	private Map<Integer, List<SelectItem>> mJobReasonsForProfessions = new HashMap<>();
	
	public List<SelectItem> getProfessionSelections() {
		if(mProfessionSelections == null) {		
			mProfessionSelections = dataAccessBean.getProfessions().findAll().stream()
					.map((p) -> new SelectItem(p.getId(), p.getName()))
					.collect(Collectors.toList());
		}
		return mProfessionSelections;
	}
	
	public List<SelectItem> getTradesmenSelections() {
		return dataAccessBean.getTradesmen().findAll().stream()
				.map((p) -> new SelectItem(p.get_id(), p.getContactName()))
				.collect(Collectors.toList());
	}
	
	public List<SelectItem> getJobReasonsForProfession(Integer professionId) {
		List<SelectItem> selections = mJobReasonsForProfessions.get(professionId);
		if(selections == null) {
			selections = dataAccessBean.getJobReasons().findReasonsForProfession(professionId)
					.stream()
					.map(jr -> new SelectItem(jr.getId(), jr.getName()))
					.collect(Collectors.toList());
			mJobReasonsForProfessions.put(professionId, selections);
		}
		return selections;
	}
	
	public UserType[] getUserTypes() {
		return UserType.values();
	}
	
	public OrderType[] getOrderTypes() {
		return OrderType.values();
	}
	
	public OrderTableType[] getOrderTableTypes() {
		return OrderTableType.values();
	}

}
