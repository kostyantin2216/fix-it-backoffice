/**
 * 
 */
package com.fixit.bo.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.springframework.util.StringUtils;

import com.fixit.bo.utils.FacesUtils;
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
	private transient UserFactory mUserFactory;
	
	private TempUserView tempUserView;
	
	@PostConstruct
	private void init() {
		tempUserView = TempUserView.newUser();
	}
	
	public void setmUserFactory(UserFactory userFactory) {
		this.mUserFactory = userFactory;
	}
	
	public TempUserView getTempUserView() {
		return tempUserView;
	}
	
	public List<CommonUser> completeUser(String query) {
		if(!StringUtils.isEmpty(query)) {			
			query = query.trim();
			if(query.length() > 2 && !query.contains(",")) {
				return mUserFactory.findUsersWithContainingTelepone(query);
			}
		}

		return null;
	}
	
	public void showCreateUserDialog() {		
		FacesUtils.showDialog("dfCreateUser", 190, 400, false, true);
	}
	
	public void createUserAndCloseDialog() {
		TempUser tempUser = tempUserView.toUser();
		CommonUser user = mUserFactory.createOrFindCommonUser(tempUser);
		
        RequestContext.getCurrentInstance().closeDialog(user);
	}
	
}
