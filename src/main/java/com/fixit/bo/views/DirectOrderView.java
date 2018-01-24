/**
 * 
 */
package com.fixit.bo.views;

import java.util.List;

import org.springframework.util.StringUtils;

import com.fixit.components.orders.OrderParams;
import com.fixit.core.dao.sql.JobReasonDao;
import com.fixit.core.dao.sql.ProfessionDao;
import com.fixit.core.dao.sql.TrafficSourceDao;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.OrderType;
import com.fixit.core.data.mongo.CommonUser;
import com.fixit.core.data.mongo.Tradesman;
import com.fixit.core.data.sql.JobReason;
import com.fixit.core.data.sql.Profession;
import com.fixit.core.data.sql.TrafficSource;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/06 20:51:10 GMT+2
 */
public class DirectOrderView {

	private Tradesman tradesman;
	private CommonUser user;
	private JobLocation location;
	private String comment;
	private String orderComment;
	private int profession;
	private int trafficSource;
	private int[] jobReasons;
	
	public DirectOrderView() {
		this.trafficSource = -1;
		this.profession = -1;
		this.location = new JobLocation();
	}
	
	public DirectOrderView(Tradesman tradesman) {
		this.trafficSource = -1;
		setTradesman(tradesman);
		this.location = new JobLocation();
	}
	
	public DirectOrderView(Tradesman tradesman, int profession) {
		this.trafficSource = -1;
		this.tradesman = tradesman;
		this.profession = profession;
		this.location = new JobLocation();
	}

	public Tradesman getTradesman() {
		return tradesman;
	}

	public void setTradesman(Tradesman tradesman) {
		this.tradesman = tradesman;
		this.profession = tradesman.getProfessions()[0];
	}

	public CommonUser getUser() {
		return user;
	}

	public void setUser(CommonUser user) {
		this.user = user;
	}

	public JobLocation getLocation() {
		return location;
	}

	public void setLocation(JobLocation location) {
		this.location = location;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOrderComment() {
		return orderComment;
	}

	public void setOrderComment(String orderComment) {
		this.orderComment = orderComment;
	}

	public int getProfession() {
		return profession;
	}

	public void setProfession(int profession) {
		this.profession = profession;
	}

	public int[] getJobReasons() {
		return jobReasons;
	}

	public void setJobReasons(int[] jobReasons) {
		this.jobReasons = jobReasons;
	}
	
	public int getTrafficSource() {
		return trafficSource;
	}

	public void setTrafficSource(int trafficSource) {
		this.trafficSource = trafficSource;
	}

	public String validate() {
		if(tradesman == null) {
			return "Missing Tradesman";
		} else if(location == null || StringUtils.isEmpty(location.getMapAreaId())) {
			return "Missing Address";
		} else if(user == null) {
			return "Missing User";
		} else if(profession < 0) {
			return "Invalid Profession";
		} else {
			return null;
		}
	}
	
	public OrderParams toParams(ProfessionDao professionDao, JobReasonDao jobReasonsDao, TrafficSourceDao trafficSrcDao) {
		Profession profession = professionDao.findById(this.profession);
		List<JobReason> reasons = jobReasonsDao.findIn(JobReasonDao.PROP_ID, this.jobReasons);
		JobReason[] jobReasons = reasons == null ? null : reasons.toArray(new JobReason[reasons.size()]);
		TrafficSource trafficSource = trafficSrcDao.findById(this.trafficSource);
		return new OrderParams.Builder(OrderType.DIRECT)
					.byUser(user)
					.withTradesman(tradesman)
					.forProfession(profession)
					.atLocation(location)
					.forReasons(jobReasons)
					.withComment(comment)
					.fromTrafficSource(trafficSource)
					.build();
	}
	
	
}
