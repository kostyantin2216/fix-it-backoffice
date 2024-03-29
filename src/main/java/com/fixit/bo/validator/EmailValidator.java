/**
 * 
 */
package com.fixit.bo.validator;

import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/20 23:25:24 GMT+2
 */
@FacesValidator("fixit.emailValidator")
public class EmailValidator implements Validator, ClientValidator {
 
    private final Pattern pattern;
  
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  
    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }
 
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value == null) {
            return;
        }
        String val = value.toString();
        if(!val.trim().isEmpty()) {
	        if(!pattern.matcher(val).matches()) {
	            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", 
	                        value + " is not a valid email;"));
	        }
        }
    }
 
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    public String getValidatorId() {
        return "fixit.emailValidator";
    }
}
