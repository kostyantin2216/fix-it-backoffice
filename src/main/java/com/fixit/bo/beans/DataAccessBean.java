/**
 * 
 */
package com.fixit.bo.beans;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fixit.core.dao.mongo.TradesmanDao;
import com.fixit.core.dao.mongo.impl.TradesmanDaoImpl;
import com.fixit.core.dao.sql.JobReasonDao;
import com.fixit.core.dao.sql.ProfessionDao;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/27 23:59:03 GMT+3
 */
@Component
public class DataAccessBean {
	
	@Autowired
	private BeanFactory beanFactory;
	
	public JobReasonDao getJobReasons() {
		return (JobReasonDao) beanFactory.getBean("jobReasonDao");
	}
	
	public ProfessionDao getProfessions() {
		return (ProfessionDao) beanFactory.getBean("professionDao");
	}
	
	public TradesmanDao getTradesmen() {
		return beanFactory.getBean(TradesmanDaoImpl.class);
	}

}
