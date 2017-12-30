/**
 * 
 */
package com.fixit.bo.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;

import com.fixit.bo.maps.model.ParentChildMapModelWrapper;
import com.fixit.core.data.WorkingDay;
import com.fixit.core.data.mongo.Tradesman;
import com.fixit.core.utils.Formatter;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/27 22:49:35 GMT+3
 */
public class TradesmanView implements DataModelView<Tradesman> {
	
	public static TradesmanView forTradesman(Tradesman tradesman, ParentChildMapModelWrapper baseWrapper) {
		TradesmanView tv = new TradesmanView();
		tv.id = tradesman.get_id().toHexString();
		tv.leadId = tradesman.getLeadId();
		tv.professions = tradesman.getProfessions();
		tv.contactName = tradesman.getContactName();
		tv.companyName = tradesman.getCompanyName();
		tv.email = tradesman.getEmail();
		tv.telephone = tradesman.getTelephone();
		tv.logoUrl = tradesman.getLogoUrl();
		tv.featureImageUrl = tradesman.getFeatureImageUrl();
		tv.workingAreas = baseWrapper;
		tv.workingDays = transform(tradesman.getWorkingDays());
		tv.active = tradesman.isActive();
		tv.priority = tradesman.getPriority();
		return tv;
	}
	
	public static TradesmanView newTradesman(ParentChildMapModelWrapper baseWrapper) {
		TradesmanView tv = new TradesmanView();
		tv.workingDays = transform((WorkingDay[]) null);
		tv.workingAreas = baseWrapper;
		return tv;
	}
	
	private static Map<Integer, List<WorkingHours>> transform(WorkingDay[] workingDays) {
		Map<Integer, List<WorkingHours>> workingDaysCopy = Stream
				.of(0, 1, 2, 3, 4, 5, 6)
				.collect(Collectors.toMap(Integer::valueOf, ArrayList::new));
		
		if(workingDays != null) {
			for(WorkingDay workingDay : workingDays) {
				List<WorkingHours> workingHours = new ArrayList<>();
				for(com.fixit.core.data.WorkingHours hours : workingDay.getHours()) {
					workingHours.add(WorkingHours.transform(hours));
				}
				workingDaysCopy.put(workingDay.getDay(), workingHours);
			}
		}
		
		return workingDaysCopy;
	}
	
	private static WorkingDay[] transform(Map<Integer, List<WorkingHours>> workingDays) {		
		return workingDays.entrySet()
				   .stream()
				   .map(v -> new WorkingDay(
						   v.getKey(), 
						   v.getValue()
						    .stream()
						    .map(WorkingHours::transform)
						    .toArray(com.fixit.core.data.WorkingHours[]::new)
					))
				   .toArray(WorkingDay[]::new);
	}
	
	private String id;
	private long leadId;
	private int[] professions;
	private String contactName;
	private String companyName;
	private String email;
	private String telephone;
	private String logoUrl;
	private String featureImageUrl;
	private ParentChildMapModelWrapper workingAreas;
	private Map<Integer, List<WorkingHours>> workingDays;
	private boolean active;
	private int priority;
	
	private TradesmanView() { }
	
	public boolean isNewTradesman() {
		return id == null;
	}

	public String getId() {
		return id;
	}

	public long getLeadId() {
		return leadId;
	}

	public void setLeadId(long leadId) {
		this.leadId = leadId;
	}

	public int[] getProfessions() {
		return professions;
	}

	public void setProfessions(int[] professions) {
		this.professions = professions;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getFeatureImageUrl() {
		return featureImageUrl;
	}

	public void setFeatureImageUrl(String featureImageUrl) {
		this.featureImageUrl = featureImageUrl;
	}
	
	public ParentChildMapModelWrapper getWorkingAreasModel() {
		return workingAreas;
	}

	public void setWorkingAreas(List<String> areaIds) {
		this.workingAreas = new ParentChildMapModelWrapper(workingAreas.getCurrentNode().getRoot(), new HashSet<>(areaIds), workingAreas.getCenter());
	}

	public Map<Integer, List<WorkingHours>> getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(Map<Integer, List<WorkingHours>> workingDays) {
		this.workingDays = workingDays;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void addWorkingHours(Integer day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		Date open = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY, 18);
		Date close = cal.getTime();
		
		this.workingDays.get(day).add(new WorkingHours(open, close));
	}
	
	public void clearWorkingHours(Integer day) {
		this.workingDays.get(day).clear();
	}
	
	public void removeWorkingHours(Integer day, int index) {
		this.workingDays.get(day).remove(index);
	}
	
	@Override
	public boolean updateDMO(Tradesman t) {
		if(id.equals(t.get_id().toHexString())) {
			fillTradesman(t);
		
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.fixit.bo.views.DataModelView#toDMO()
	 */
	@Override
	public Tradesman toDMO() {
		Tradesman tradesman = new Tradesman();
		if(!StringUtils.isEmpty(id)) {
			tradesman.set_id(new ObjectId(id));
		}
		fillTradesman(tradesman);
		return tradesman;
	}
	
	private void fillTradesman(Tradesman t) {
		t.setLeadId(leadId);
		t.setActive(active);
		t.setCompanyName(companyName);
		t.setContactName(contactName);
		t.setEmail(email);
		t.setFeatureImageUrl(featureImageUrl);
		t.setLogoUrl(logoUrl);
		t.setPriority(priority);
		t.setProfessions(professions);
		t.setTelephone(telephone);
		Set<String> areaIds = workingAreas.getSelectedAreas();
		if(areaIds != null) {
			t.setWorkingAreas(areaIds.toArray(new String[areaIds.size()]));
		}
		t.setWorkingDays(transform(workingDays));
	}
	
	public static class WorkingHours {
		private Date open;
		private Date close;
		
		private WorkingHours(Date open, Date close) {
			this.open = open;
			this.close = close;
		}
		
		private static WorkingHours transform(com.fixit.core.data.WorkingHours workingHours) {
			return new WorkingHours(
					Formatter.timeFractionToDateTime(workingHours.getOpen()),
					Formatter.timeFractionToDateTime(workingHours.getClose())
			);
		}
		
		private static com.fixit.core.data.WorkingHours transform(WorkingHours workingHours) {
			return new com.fixit.core.data.WorkingHours(
					Formatter.dateTimeToTimeFraction(workingHours.open), 
					Formatter.dateTimeToTimeFraction(workingHours.close)
			);
		}
		
		public Date getOpen() {
			return open;
		}
		public void setOpen(Date open) {
			this.open = open;
		}
		public Date getClose() {
			return close;
		}
		public void setClose(Date close) {
			this.close = close;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((close == null) ? 0 : close.hashCode());
			result = prime * result + ((open == null) ? 0 : open.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WorkingHours other = (WorkingHours) obj;
			if (close == null) {
				if (other.close != null)
					return false;
			} else if (!close.equals(other.close))
				return false;
			if (open == null) {
				if (other.open != null)
					return false;
			} else if (!open.equals(other.open))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "WorkingHours [open=" + open + ", closed=" + close + "]";
		}
	}

	@Override
	public String toString() {
		return "TradesmanView [id=" + id + ", leadId=" + leadId + ", professions=" + professions
				+ ", contactName=" + contactName + ", companyName=" + companyName + ", email=" + email + ", telephone="
				+ telephone + ", logoUrl=" + logoUrl + ", featureImageUrl=" + featureImageUrl + ", workingAreas="
				+ workingAreas + ", workingDays=" + workingDays + ", active=" + active
				+ ", priority=" + priority + "]";
	}

}
