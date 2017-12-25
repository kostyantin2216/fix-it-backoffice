/**
 * 
 */
package com.fixit.bo.beans;

import java.util.List;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fixit.core.dao.sql.ProfessionDao;
import com.fixit.core.data.sql.Profession;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/21 23:38:38 GMT+3
 */
@Component
public class ProfessionBean {

	private final ProfessionDao mProfessionDao;
	
	private int profession = -1;
	
	@Autowired
	public ProfessionBean(ProfessionDao professionDao) {
		mProfessionDao = professionDao;
	}
	
	public List<Profession> getProfessions() {
		return mProfessionDao.findAll();
	}
	
	public String getNameForProfession(int id) {
		return mProfessionDao.findById(id).getName();
	}
	
	public void selectProfessionAndCloseDialog() {
		RequestContext.getCurrentInstance().closeDialog(profession);
		profession = -1;
	}

	public int getProfession() {
		return profession;
	}

	public void setProfession(int profession) {
		this.profession = profession;
	}
		
}
