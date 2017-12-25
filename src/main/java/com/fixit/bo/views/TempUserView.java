/**
 * 
 */
package com.fixit.bo.views;

import com.fixit.core.data.mongo.TempUser;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/20 21:38:18 GMT+2
 */
public class TempUserView {
	
	public static TempUserView newUser() {
		return new TempUserView();
	}
	
	public static TempUserView forUser(TempUser user) {
		TempUserView view = new TempUserView();
		view.id = user.get_id().toHexString();
		view.name = user.getName();
		view.email = user.getEmail();
		view.telephone = user.getTelephone();
		return view;
	}

	private String id;
	private String name;
	private String email;
	private String telephone;
	
	private TempUserView() { }
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public TempUser toUser() {
		TempUser user = new TempUser();
		user.setName(name);
		user.setTelephone(telephone);
		user.setEmail(email);
		return user;
	}
	
}
