/**
 * 
 */
package com.fixit.bo.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.UIColumn;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.map.LatLng;
import org.springframework.util.StringUtils;

import com.fixit.bo.maps.model.ParentChildMapModelWrapper;
import com.fixit.bo.views.TradesmanView;
import com.fixit.components.users.UserLeadFactory;
import com.fixit.core.dao.mongo.MapAreaDao;
import com.fixit.core.dao.mongo.TradesmanDao;
import com.fixit.core.dao.sql.ProfessionDao;
import com.fixit.core.data.UserLead;
import com.fixit.core.data.mongo.MapArea;
import com.fixit.core.data.mongo.Tradesman;
import com.fixit.core.logging.FILog;
import com.fixit.core.structure.TreeNode;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.Formatter;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/21 22:40:14 GMT+3
 */
@ManagedBean
@ViewScoped
public class TradesmenBean implements Serializable {
	private static final long serialVersionUID = 5150572857353029617L;

	@ManagedProperty(value = "#{tradesmanDao}")
	private transient TradesmanDao mTradesmanDao;

	@ManagedProperty(value = "#{mapAreaDao}")
	private transient MapAreaDao mMapAreaDao;
	
	@ManagedProperty(value = "#{professionDao}")
	private transient ProfessionDao mProfessionDao;
	
	@ManagedProperty(value = "#{userLeadFactory}")
	private transient UserLeadFactory mUserLeadFactory;
	
	private Tradesman mSelectedTradesman;
	private TradesmanView mTradesmanView;
	private List<UserLead> mUserLeads;
	
	private List<Tradesman> mTradesmen;
	private List<Tradesman> mFilteredTradesmen;
	

	@PostConstruct
	public void init() {
		String profession = FacesContext.getCurrentInstance()
										.getExternalContext()
										.getRequestParameterMap()
										.get(Constants.ARG_PROFESSION);
		
		if(StringUtils.isEmpty(profession) || !Formatter.isNumeric(profession)) {
			mTradesmen = mTradesmanDao.findAll();
		} else {
			mTradesmen = mTradesmanDao.findTradesmenForProfession(Integer.parseInt(profession));
		}
		
	}

	public void setmTradesmanDao(TradesmanDao mTradesmanDao) {
		this.mTradesmanDao = mTradesmanDao;
	}

	public void setmMapAreaDao(MapAreaDao mMapAreaDao) {
		this.mMapAreaDao = mMapAreaDao;
	}

	public void setmProfessionDao(ProfessionDao mProfessionDao) {
		this.mProfessionDao = mProfessionDao;
	}

	public void setmUserLeadFactory(UserLeadFactory mUserLeadFactory) {
		this.mUserLeadFactory = mUserLeadFactory;
	}

	public List<Tradesman> getTradesmen() {
		return mTradesmen;
	}
	
	public void setFilteredTradesmen(List<Tradesman> filteredTradesmen) {
		mFilteredTradesmen = filteredTradesmen;
	}
	
	public List<Tradesman> getFilteredTradesmen() {
		return mFilteredTradesmen;
	}
	
	public Tradesman getSelectedTradesman() {
		return mSelectedTradesman;
	}
	
	public void setSelectedTradesman(Tradesman mSelectedTradesman) {
		this.mSelectedTradesman = mSelectedTradesman;
	}

	public TradesmanView getTradesmanView() {
		return mTradesmanView;
	}
	
	public List<UserLead> getUserLeads() {
		return mUserLeads;
	}
	
	private ParentChildMapModelWrapper createBaseMapModelWrapper(String[] workingAreas) {
		TreeNode<MapArea> mapAreaNode = mMapAreaDao.getNode(MapAreaDao.ID_JOBURG_PROVINCE);
		return new ParentChildMapModelWrapper(mapAreaNode, new HashSet<>(Arrays.asList(workingAreas)), new LatLng(-26.1715046, 27.9699835));
	}
	
	public String getProfessionNames(Tradesman tradesman) {
		StringBuilder names = new StringBuilder();
		
		int[] professions = tradesman.getProfessions();
		for(int i = 0; i < professions.length; i++) {
			if(names.length() != 0) {
				names.append(",");
			}
			names.append(mProfessionDao.getNameForProfession(professions[i]));
		}
		
		return names.toString();
	}
	
	public void refreshTradesmen() {
		init();
	}
	
	public void createTradesman() {
		mTradesmanView = TradesmanView.newTradesman(createBaseMapModelWrapper(new String[0]));
		mUserLeads = Collections.emptyList();
	}
	
	public void onTradesmanSelected(SelectEvent event) {
		mTradesmanView = TradesmanView.forTradesman(mSelectedTradesman, createBaseMapModelWrapper(mSelectedTradesman.getWorkingAreas()));
		mUserLeads = mUserLeadFactory.getLeadsForTradesman(mSelectedTradesman.get_id());
	}
	
	public void unselectTrademan() {
		FILog.i("removing view: " + mTradesmanView);
		mSelectedTradesman = null;
		mTradesmanView = null;
		mUserLeads = null;
	}
	
	public void saveOrUpdateTradesman() {
		if(mTradesmanView != null) {
			if(mTradesmanView.isNewTradesman()) { 
				Tradesman tradesman = mTradesmanView.toDMO();
				mTradesmanDao.save(tradesman);
				if(tradesman.get_id() != null) {
					mTradesmen.add(tradesman);
					mSelectedTradesman = tradesman;
					mTradesmanView = TradesmanView.forTradesman(tradesman, createBaseMapModelWrapper(mSelectedTradesman.getWorkingAreas()));
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Saved new tradesman"));
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Couldn't save tradesman"));
				}
			} else if(mSelectedTradesman != null) { 
				if(mTradesmanView.updateDMO(mSelectedTradesman)) {
					mTradesmanDao.update(mSelectedTradesman);
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Inconsistency in tradesman data"));
				}
			}
		}
	}
	
	public void saveOrUpdateAndUnselectedTradesman() {
		saveOrUpdateTradesman();
		unselectTrademan();
	}
	
	public void deleteSelectedTradesman() {
		if(mSelectedTradesman != null) {
			mTradesmanDao.delete(mSelectedTradesman.get_id());
			mTradesmen.remove(mSelectedTradesman);
			unselectTrademan();
		}
	}
	
	public void selectTradesmanFromDialog(Tradesman tradsesman) {
        RequestContext.getCurrentInstance().closeDialog(tradsesman);
    }
	
	public String exportProfessionsColumn(UIColumn uiCol) {
		String result = "na";
		/*UIComponent uiRepeat = uiCol.getChildren().get(0);
		if(uiRepeat instanceof UIRepeat) {
			StringBuilder sb = new StringBuilder();
			for(UIComponent component : uiRepeat.getChildren()) {
				if(component instanceof HtmlOutputText) {
					HtmlOutputText outputText = (HtmlOutputText) component;
					if(sb.length() != 0) {
						sb.append(",");
					}
					sb.append(outputText.getValue());
				}
			}
			result = sb.toString();
		} else {
			result = "error";
		}*/
		return result;
	}
	
}
