/**
 * 
 */
package com.fixit.bo.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import com.fixit.bo.general.ConfigurationsViewType;
import com.fixit.bo.general.OrderRequestsTableType;
import com.fixit.bo.general.OrderTableType;
import com.fixit.bo.general.TradesmenTableType;
import com.fixit.core.data.OrderType;
import com.fixit.core.data.UserType;
import com.fixit.core.data.mongo.Tradesman;
import com.fixit.core.data.sql.Profession;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/27 23:54:29 GMT+3
 */
@ManagedBean
@SessionScoped
public class SelectionsBean {
	
	@ManagedProperty("#{dataAccessBean}")
	private DataAccessBean mDataAccessBean;
	
	private Map<Integer, SelectItem> mProfessionSelections;
	private Map<Integer, List<SelectItem>> mJobReasonsForProfessions = new HashMap<>();
	
	@PostConstruct
	public void init() {
		mProfessionSelections = mDataAccessBean.getProfessions().findAll().stream()
				.collect(Collectors.toMap(Profession::getId, (p) -> new SelectItem(p.getId(), p.getName())));
	}
	
	public void setmDataAccessBean(DataAccessBean mDataAccessBean) {
		this.mDataAccessBean = mDataAccessBean;
	}
	
	public List<SelectItem> getProfessionSelections() {
		return new ArrayList<>(mProfessionSelections.values());
	}
	
	public List<SelectItem> getProfessionSelectionsForTradesman(Tradesman tradesman) {
		List<SelectItem> selections = new ArrayList<>();
		for(int profession : tradesman.getProfessions()) {
			selections.add(mProfessionSelections.get(profession));
		}
		return selections;
	}
	
	public List<SelectItem> getTradesmenSelections() {
		return mDataAccessBean.getTradesmen().findAll().stream()
				.map((p) -> new SelectItem(p.get_id(), p.getContactName()))
				.collect(Collectors.toList());
	}
	
	public List<SelectItem> getJobReasonsForProfession(Integer professionId) {
		List<SelectItem> selections = mJobReasonsForProfessions.get(professionId);
		if(selections == null) {
			selections = mDataAccessBean.getJobReasons().findReasonsForProfession(professionId)
					.stream()
					.map(jr -> new SelectItem(jr.getId(), jr.getName()))
					.collect(Collectors.toList());
			mJobReasonsForProfessions.put(professionId, selections);
		}
		return selections;
	}
	
	public List<SelectItem> getTrafficSourceSelections() {
		return mDataAccessBean.getTrafficSources().findAll().stream()
				.map((p) -> new SelectItem(p.getId(), p.getName()))
				.collect(Collectors.toList());
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
	
	public TradesmenTableType[] getTradesmenTableTypes() {
		return TradesmenTableType.values();
	}
	
	public OrderRequestsTableType[] getOrderRequestsTableTypes() {
		return OrderRequestsTableType.values();
	}
	
	public List<SelectItem> getConfigurationsViewTypes() {
		return Arrays.stream(ConfigurationsViewType.values())
				.map((p) -> new SelectItem(p, p.toDisplayName()))
				.collect(Collectors.toList());
	}

}
