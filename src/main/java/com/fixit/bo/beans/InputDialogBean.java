/**
 * 
 */
package com.fixit.bo.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.util.StringUtils;

import com.fixit.bo.utils.FacesUtils;
import com.fixit.core.utils.Constants;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/23 16:18:58 GMT+2
 */
@ManagedBean
@ViewScoped
public class InputDialogBean {
	
	private final static String DEFAULT_TITLE = "Input";
	private final static String DEFAULT_LABEL = "Please provide an input: ";

	private String title;
	private String label;
	private String input;
	
	@PostConstruct
	public void init() {
		title = FacesUtils.getExternalContextRequestParam(Constants.ARG_TITLE);	
		if(StringUtils.isEmpty(title)) {
			title = DEFAULT_TITLE;
		}
		
		label = FacesUtils.getExternalContextRequestParam(Constants.ARG_LABEL);
		if(StringUtils.isEmpty(label)) {
			label = DEFAULT_LABEL;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	public void submitInput() {
		if(!StringUtils.isEmpty(input)) {
			RequestContext.getCurrentInstance().closeDialog(input);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Must provide input."));
		}
	}
	
}
