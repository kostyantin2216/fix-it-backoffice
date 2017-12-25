/**
 * 
 */
package com.fixit.bo.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.fixit.bo.views.TempUserView;
import com.fixit.components.users.UserFactory;
import com.fixit.core.data.mongo.CommonUser;
import com.fixit.core.data.mongo.TempUser;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/20 21:37:13 GMT+2
 */
@ManagedBean
@ViewScoped
public class UserBean {
	
	@ManagedProperty(value = "#{userFactory}")
	private transient UserFactory userFactory;
	
	private TempUserView tempUserView;
	
	@PostConstruct
	private void init() {
		tempUserView = TempUserView.newUser();
	}
	
	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}
	
	public TempUserView getTempUserView() {
		return tempUserView;
	}
	
	public void createUserAndCloseDialog() {
		TempUser tempUser = tempUserView.toUser();
		CommonUser user = userFactory.createOrFindCommonUser(tempUser);
		
        RequestContext.getCurrentInstance().closeDialog(user);
	}
	
}
