/**
 * 
 */
package com.fixit.bo.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fixit.components.users.UserFactory;
import com.fixit.core.data.mongo.CommonUser;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/19 19:36:34 GMT+2
 */
@Component
public class UserUiConverter implements Converter {
	
	@Autowired
	private UserFactory userFactory;

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if(value != null) {
			value = value.trim();
			if(ObjectId.isValid(value)) {
				return userFactory.tryFindUser(new ObjectId(value));
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object obj) {
		if(obj != null) {
			return ((CommonUser) obj).get_id().toHexString();
		}
		return null;
	}
	
}
